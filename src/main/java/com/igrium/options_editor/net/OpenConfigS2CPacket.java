package com.igrium.options_editor.net;

import com.igrium.options_editor.core.ConfigProvider;
import com.igrium.options_editor.core.ConfigProviders;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class OpenConfigS2CPacket<T> implements FabricPacket {

    public static final PacketType<OpenConfigS2CPacket<?>> TYPE = PacketType
            .create(new Identifier("options-editor:open_sconfig"), OpenConfigS2CPacket::parse);

    public final ConfigProvider<T> provider;
    public final T configContents;
    public final int screenId;

    public OpenConfigS2CPacket(ConfigProvider<T> provider, T configContents, int screenId) {
        this.provider = provider;
        this.configContents = configContents;
        this.screenId = screenId;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(screenId);

        Identifier id = ConfigProviders.REGISTRY.getId(provider);
        if (id == null) {
            throw new IllegalStateException("Config provider is not registered!");
        }
        buf.writeIdentifier(id);

        provider.serialize(buf, configContents);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    
    public static OpenConfigS2CPacket<?> parse(PacketByteBuf buf) {
        int screenId = buf.readInt();
        Identifier id = buf.readIdentifier();
        ConfigProvider<?> provider = ConfigProviders.REGISTRY.get(id);
        if (provider == null) {
            throw new IllegalStateException("Unknown config provider: " + id);
        }
        return parseBuffer(screenId, provider, buf);
    }
    
    private static <T> OpenConfigS2CPacket<T> parseBuffer(int screenId, ConfigProvider<T> provider, PacketByteBuf buf) {
        T contents = provider.deserialize(buf);
        return new OpenConfigS2CPacket<>(provider, contents, screenId);
    }
}
