package com.igrium.options_editor.options;

import net.minecraft.network.PacketByteBuf;

public record Option<T>(OptionType<T> type, T value) {

    /**
     * Write this option's value to a buffer. Does <em>not</em> save option type ID.
     * @param buf Buffer to write to.
     * @see OptionType#writeOption(Option, PacketByteBuf)
     */
    public void write(PacketByteBuf buf) {
        type.write(value, buf);
    }
}
