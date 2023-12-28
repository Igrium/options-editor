package com.igrium.options_editor.net;

import java.util.Optional;

import com.igrium.options_editor.options.OptionHolder;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class UpdateConfigC2SPacket implements FabricPacket {

    public static PacketType<UpdateConfigC2SPacket> TYPE = PacketType
            .create(new Identifier("options-editor:update_config"), UpdateConfigC2SPacket::new);

    public final int screenId;
    public final Optional<OptionHolder> contents;

    public UpdateConfigC2SPacket(int screenId, Optional<OptionHolder> contents) {
        this.screenId = screenId;
        this.contents = contents;
    }

    public UpdateConfigC2SPacket(PacketByteBuf buf) {
        screenId = buf.readInt();
        contents = buf.readOptional(OptionHolder::fromBuffer);
        contents.ifPresent(OptionHolder::index);
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(screenId);
        buf.writeOptional(contents, (b, v) -> v.writeBuffer(b));
    }

    @Override
    public PacketType<? extends UpdateConfigC2SPacket> getType() {
        return TYPE;
    }
    
}
