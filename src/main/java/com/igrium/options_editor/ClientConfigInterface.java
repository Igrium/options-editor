package com.igrium.options_editor;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;

import com.igrium.options_editor.core.ConfigProvider;
import com.igrium.options_editor.net.OpenConfigAcknowledgeC2SPacket;
import com.igrium.options_editor.net.OpenConfigS2CPacket;
import com.igrium.options_editor.util.timer.Timers;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class ClientConfigInterface {
    public static final int RESPONSE_TIMEOUT = 60;

    private final MinecraftServer server;

    private static Map<MinecraftServer, ClientConfigInterface> instances = Collections.synchronizedMap(new WeakHashMap<>());
    
    public static ClientConfigInterface getInstance(MinecraftServer server) {
        return instances.computeIfAbsent(server, s -> new ClientConfigInterface(s));
    }

    public static enum ScreenOpenedResponse { SUCCESS, FAILED, NO_RESPONSE }

    private static record QueuedScreen(int screenId, CompletableFuture<ScreenOpenedResponse> callback) {};

    private final Map<ServerPlayerEntity, QueuedScreen> queuedScreens = new WeakHashMap<>();
    private final Map<ServerPlayerEntity, Integer> activeScreens = new WeakHashMap<>();

    public ClientConfigInterface(MinecraftServer server) {
        this.server = server;
    }

    private int nextId;

    public int getNextId() {
        return nextId;
    }

    public boolean mayUpdateConfig(ServerPlayerEntity player) {
        return true;
    }

    /**
     * Open the config screen on a client.
     * 
     * @param player Client to open on.
     * @param config Config screen to open.
     * @return A future that completes when the client responds with whether the
     *         screen could be opened.
     */
    public <T> CompletableFuture<ScreenOpenedResponse> openConfigScreen(ServerPlayerEntity player, ConfigProvider<T> config) {
        queuedScreens.remove(player);
        activeScreens.remove(player);

        int screenId = nextId;
        T current = config.load(server);

        CompletableFuture<ScreenOpenedResponse> callback = new CompletableFuture<>();

        ServerPlayNetworking.send(player, new OpenConfigS2CPacket<T>(config, current, screenId));
        queuedScreens.put(player, new QueuedScreen(screenId, callback));

        Timers.timeout(server, RESPONSE_TIMEOUT).thenRun(() -> {
            QueuedScreen queued = queuedScreens.remove(player);
            if (queued != null && queued.screenId() == screenId) {
                queued.callback().complete(ScreenOpenedResponse.NO_RESPONSE);
            }
        });

        return callback;
    }

    protected void onRecieveAcknowledge(OpenConfigAcknowledgeC2SPacket packet, ServerPlayerEntity player) {
        QueuedScreen queued = queuedScreens.get(player);
        if (queued == null || packet.screenId != queued.screenId()) {
            throw new IllegalStateException("Improper screen ID.");
            // player.networkHandler.disconnect(Text.literal("Improper screen ID."));
            // return;
        }
        activeScreens.put(player, queued.screenId());

        queued.callback.complete(packet.success ? ScreenOpenedResponse.SUCCESS : ScreenOpenedResponse.FAILED);
        queuedScreens.remove(player);
    }

    public static void initListeners() {
        ServerPlayNetworking.registerGlobalReceiver(OpenConfigAcknowledgeC2SPacket.TYPE, (packet, player, res) -> {
            getInstance(player.getServer()).onRecieveAcknowledge(packet, player);
        });
    }
}
