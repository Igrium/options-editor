package com.igrium.options_editor;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;

import com.igrium.options_editor.net.OpenConfigAcknowledgeC2SPacket;
import com.igrium.options_editor.net.OpenConfigS2CPacket;
import com.igrium.options_editor.net.UpdateConfigC2SPacket;
import com.igrium.options_editor.options.OptionHolder;
import com.igrium.options_editor.options.OptionProvider;
import com.igrium.options_editor.util.timer.Timers;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class ClientConfigInterface {
    public static final int RESPONSE_TIMEOUT = 10;

    private final MinecraftServer server;

    private static Map<MinecraftServer, ClientConfigInterface> instances = Collections.synchronizedMap(new WeakHashMap<>());
    
    public static ClientConfigInterface getInstance(MinecraftServer server) {
        return instances.computeIfAbsent(server, s -> new ClientConfigInterface(s));
    }

    public static enum ScreenOpenedResponse { SUCCESS, FAILED, NO_RESPONSE }

    private static record OpenedScreen(int screenId, OptionProvider provider) {};
    private static record QueuedScreen(OpenedScreen screen, CompletableFuture<ScreenOpenedResponse> callback) {};

    private final Map<ServerPlayerEntity, QueuedScreen> queuedScreens = new WeakHashMap<>();
    private final Map<ServerPlayerEntity, OpenedScreen> activeScreens = new WeakHashMap<>();

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

    public CompletableFuture<ScreenOpenedResponse> openOptionScreen(ServerPlayerEntity player, OptionProvider provider) {
        queuedScreens.remove(player);
        activeScreens.remove(player);

        int screenId = nextId;
        OptionHolder current = provider.obtain(server);

        CompletableFuture<ScreenOpenedResponse> callback = new CompletableFuture<>();

        ServerPlayNetworking.send(player, new OpenConfigS2CPacket(current, screenId));
        OpenedScreen screen = new OpenedScreen(screenId, provider);

        queuedScreens.put(player, new QueuedScreen(screen, callback));

        Timers.timeout(server, RESPONSE_TIMEOUT).thenRun(() -> {
            QueuedScreen queued = queuedScreens.remove(player);
            if (queued != null && queued.screen.screenId == screenId) {
                queued.callback().complete(ScreenOpenedResponse.NO_RESPONSE);
            }
        });

        return callback;
    }

    protected void onRecieveAcknowledge(OpenConfigAcknowledgeC2SPacket packet, ServerPlayerEntity player) {
        QueuedScreen queued = queuedScreens.get(player);
        if (queued == null || packet.screenId != queued.screen().screenId()) {
            throw new IllegalStateException("Improper screen ID.");
        }
        activeScreens.put(player, queued.screen());

        queued.callback.complete(packet.success ? ScreenOpenedResponse.SUCCESS : ScreenOpenedResponse.FAILED);
        queuedScreens.remove(player);
    }

    protected void onRecieveUpdate(UpdateConfigC2SPacket packet, ServerPlayerEntity player) {
        if (!mayEditConfig(player)) {
            throw new IllegalArgumentException("Player may not update config.");
        }

        OpenedScreen screen = activeScreens.get(player);
        if (screen == null || packet.screenId != screen.screenId()) {
            throw new IllegalStateException("Improper screen ID.");
        }

        if (packet.contents.isPresent()) {
            screen.provider.apply(packet.contents.get(), server);
            OptionsEditor.LOGGER.info("{} updated config provider: '{}'", player.getName().getString(),
                    OptionProvider.REGISTRY.getId(screen.provider));
        }

        activeScreens.remove(player);
    }

    public static void initListeners() {
        ServerPlayNetworking.registerGlobalReceiver(OpenConfigAcknowledgeC2SPacket.TYPE, (packet, player, res) -> {
            getInstance(player.getServer()).onRecieveAcknowledge(packet, player);
        });
        
        ServerPlayNetworking.registerGlobalReceiver(UpdateConfigC2SPacket.TYPE, (packet, player, res) -> {
            getInstance(player.getServer()).onRecieveUpdate(packet, player);
        });
    }

    public static boolean mayEditConfig(ServerPlayerEntity player) {
        return player.hasPermissionLevel(3);
    }
}
