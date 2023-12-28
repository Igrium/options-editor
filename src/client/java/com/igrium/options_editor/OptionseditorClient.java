package com.igrium.options_editor;


import java.util.Map;
import java.util.WeakHashMap;

import com.igrium.options_editor.client.ClientOptionHolder;
import com.igrium.options_editor.client.events.ScreenRemovedCallback;
import com.igrium.options_editor.net.OpenConfigAcknowledgeC2SPacket;
import com.igrium.options_editor.net.OpenConfigS2CPacket;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;

public class OptionsEditorClient implements ClientModInitializer {

    private static OptionsEditorClient instance;

    public static OptionsEditorClient getInstance() {
        return instance;
    }

    // Cloth Config doesn't give me the ability to make a subclass so I store a reference to the options here.
    private static Map<Screen, ClientOptionHolder> screenOptions = new WeakHashMap<>();

    @Override
    public void onInitializeClient() {
        instance = this;
        ClientPlayNetworking.registerGlobalReceiver(OpenConfigS2CPacket.TYPE, this::onOpenConfig);
        ScreenRemovedCallback.EVENT.register(this::onScreenRemoved);
    }

    public void onOpenConfig(OpenConfigS2CPacket packet, ClientPlayerEntity player, PacketSender res) {
        try {
            ClientOptionHolder clientHolder = new ClientOptionHolder(packet.contents, packet.screenId);
            this.displayScreen(clientHolder);

            res.sendPacket(new OpenConfigAcknowledgeC2SPacket(packet.screenId, true));
        } catch (Exception e) {
            res.sendPacket(new OpenConfigAcknowledgeC2SPacket(packet.screenId, false));
            OptionsEditor.LOGGER.error("Error opening config screen.", e);
        }
    }

    public void displayScreen(ClientOptionHolder holder) {
        Screen clientScreen = holder.buildScreen(null);
        MinecraftClient.getInstance().setScreen(clientScreen);
    }

    private void onScreenRemoved(MinecraftClient client, Screen screen) {
        ClientOptionHolder options = screenOptions.get(screen);
        if (options != null && !options.isRemoved()) {
            options.onScreenRemoved();
            options.setRemoved();
        }
    }
}