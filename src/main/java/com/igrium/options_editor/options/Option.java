package com.igrium.options_editor.options;

import java.util.Objects;

import net.minecraft.network.PacketByteBuf;

// public record Option<T>(OptionType<T> type, T value) {

//     /**
//      * Write this option's value to a buffer. Does <em>not</em> save option type ID.
//      * @param buf Buffer to write to.
//      * @see OptionType#writeOption(Option, PacketByteBuf)
//      */
//     public void write(PacketByteBuf buf) {
//         type.write(value, buf);
//     }
// }

public class Option<T> {
    private final OptionType<T> type;
    private T value;

    public Option(OptionType<T> type, T value) {
        this.type = type;
        this.value = value;
    }

    public OptionType<T> getType() {
        return type;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void write(PacketByteBuf buf) {
        type.write(value, buf);
    }

    public Option<T> copy() {
        return new Option<>(this.type, this.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Option other) {
            return (this.type.equals(other.type) && this.value.equals(other.value));
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }
    
}