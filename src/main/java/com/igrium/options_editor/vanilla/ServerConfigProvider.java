package com.igrium.options_editor.vanilla;

import com.igrium.options_editor.OptionsEditor;
import com.igrium.options_editor.core.ConfigProvider;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;

public class ServerConfigProvider implements ConfigProvider<SimpleServerProperties> {

    @Override
    public Class<SimpleServerProperties> configClass() {
        return SimpleServerProperties.class;
    }

    @Override
    public void serialize(PacketByteBuf buf, SimpleServerProperties config) {
        // config.serialize(buf);
    }

    @Override
    public SimpleServerProperties deserialize(PacketByteBuf buf) {
        SimpleServerProperties props = new SimpleServerProperties();
        // props.deserialize(buf);
        return props;
    }

    @Override
    public SimpleServerProperties load(MinecraftServer server) {
        if (server instanceof DedicatedServer dedicated) {
            SimpleServerProperties props = new SimpleServerProperties();
            props.load(dedicated.getProperties());
            return props;
        } else {
            OptionsEditor.LOGGER.error("Cannot load properties of integrated server.");
            return new SimpleServerProperties();
        }
    }

    @Override
    public void apply(MinecraftServer server, SimpleServerProperties config) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'apply'");
    }
}
