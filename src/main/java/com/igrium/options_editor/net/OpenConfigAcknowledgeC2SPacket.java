package com.igrium.options_editor.net;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class OpenConfigAcknowledgeC2SPacket implements FabricPacket {

    public static final PacketType<OpenConfigAcknowledgeC2SPacket> TYPE = PacketType
            .create(new Identifier("options-editor:open_config_res"), OpenConfigAcknowledgeC2SPacket::parse);
    
    public final int screenId;
    public final boolean success;

    public OpenConfigAcknowledgeC2SPacket(int screenId, boolean success) {
        this.screenId = screenId;
        this.success = success;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(screenId);
        buf.writeBoolean(success);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    public static OpenConfigAcknowledgeC2SPacket parse(PacketByteBuf buf) {
        return new OpenConfigAcknowledgeC2SPacket(buf.readInt(), buf.readBoolean());
    }
    
}
