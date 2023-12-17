package com.igrium.options_editor;

import com.igrium.options_editor.net.OpenConfigAcknowledgeC2SPacket;
import com.igrium.options_editor.net.OpenConfigS2CPacket;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ConfigScreensNet {
    public static void registerListeners() {
        ClientPlayNetworking.registerGlobalReceiver(OpenConfigS2CPacket.TYPE, (packet, player, res) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            client.inGameHud.getChatHud().addMessage(Text.literal("Requested to show " + packet.provider));
            res.sendPacket(new OpenConfigAcknowledgeC2SPacket(packet.screenId, false));
        });
    }
}
