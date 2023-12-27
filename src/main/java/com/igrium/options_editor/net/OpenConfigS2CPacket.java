package com.igrium.options_editor.net;

import com.igrium.options_editor.options.OptionHolder;
import com.igrium.options_editor.options.OptionProvider;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class OpenConfigS2CPacket implements FabricPacket {

    public static final PacketType<OpenConfigS2CPacket> TYPE = PacketType
            .create(new Identifier("options-editor:open_config"), OpenConfigS2CPacket::new);

    public final OptionProvider provider;
    public final OptionHolder contents;
    public final int screenId;

    public OpenConfigS2CPacket(OptionProvider provider, OptionHolder contents, int screenId) {
        this.provider = provider;
        this.contents = contents;
        this.screenId = screenId;
    }

    public OpenConfigS2CPacket(PacketByteBuf buf) {
        screenId = buf.readInt();

        Identifier providerId = buf.readIdentifier();
        provider = OptionProvider.REGISTRY.get(providerId);
        if (provider == null) {
            throw new RuntimeException("Unknown option provider: " + providerId);
        }
        
        NbtCompound nbt = buf.readNbt();
        NbtList cats = nbt.getList("cats", NbtElement.COMPOUND_TYPE);
        contents = new OptionHolder().readNbt(cats);
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(screenId);

        Identifier providerId = OptionProvider.REGISTRY.getId(provider);
        if (providerId == null) {
            throw new RuntimeException("Unregistered option provider: " + providerId);
        }
        buf.writeIdentifier(providerId);

        NbtCompound nbt = new NbtCompound();
        nbt.put("cats", contents.writeNbt());
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

}
