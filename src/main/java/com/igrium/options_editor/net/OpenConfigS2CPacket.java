package com.igrium.options_editor.net;

import com.igrium.options_editor.options.OptionHolder;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class OpenConfigS2CPacket implements FabricPacket {

    public static final PacketType<OpenConfigS2CPacket> TYPE = PacketType
            .create(new Identifier("options-editor:open_config"), OpenConfigS2CPacket::new);
    
    public final OptionHolder contents;
    public final int screenId;

    public OpenConfigS2CPacket(OptionHolder contents, int screenId) {
        this.contents = contents;
        this.screenId = screenId;
    }

    public OpenConfigS2CPacket(PacketByteBuf buf) {
        screenId = buf.readShort();
        contents = OptionHolder.fromBuffer(buf);
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeShort(screenId);
        contents.writeBuffer(buf);
    }
    
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

}
