package com.igrium.options_editor.options;

import net.minecraft.network.PacketByteBuf;

public final class OptionTypes {

    public static final OptionType<Boolean> BOOLEAN = OptionType.register("options-editor:bool", new BoolOptionType());

    // For classloader
    public static void registerDefaults() {
        
    }

    private static class BoolOptionType implements OptionType<Boolean> {

        @Override
        public Boolean read(PacketByteBuf buf) {
            return buf.readBoolean();
        }

        @Override
        public void write(Boolean val, PacketByteBuf buf) {
            buf.writeBoolean(val);
        }
        
    }
}
