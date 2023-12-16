package com.igrium.options_editor.vanilla;

import java.io.IOException;

import com.igrium.options_editor.OptionsEditor;
import com.igrium.options_editor.core.ConfigProvider;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;

public class ServerConfigProvider implements ConfigProvider {

    private final MinecraftServer server;

    public ServerConfigProvider(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void load(PacketByteBuf buf) throws IOException {
        SimpleServerProperties properties = new SimpleServerProperties();
        if (server instanceof MinecraftDedicatedServer dedicated) {
            properties.load(dedicated.getProperties());
        } else {
            OptionsEditor.LOGGER.warn("Unable to obtain the properties of a listen server.");
        }
    }

    @Override
    public void save(PacketByteBuf buf) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }
    
}
