package com.igrium.options_editor.options;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


import net.minecraft.network.PacketByteBuf;

/**
 * A category of options.
 */
public class OptionCategory {
    public static record OptionEntry<T>(String name, Option<T> option) {

    }

    private final List<OptionEntry<?>> options = new ArrayList<>();

    /**
     * Get a modifiable list of all options in this category, kept with their respective names.
     * @return Option list.
     */
    public List<OptionEntry<?>> getOptions() {
        return options;
    }

    public Stream<Option<?>> getOptionStream() {
        return options.stream().map(v -> v.option());
    }

    /**
     * Write this category to a buffer.
     * @param buf Buffer to write to.
     */
    public void writeBuffer(PacketByteBuf buf) {
        buf.writeShort(options.size());
        for (OptionEntry<?> entry : options) {
            buf.writeString(entry.name());
            OptionType.writeOption(entry.option(), buf);
        }
    }

    /**
     * Read a buffer and override all current values with its values.
     * @param buf Buffer to read from.
     */
    public void readBuffer(PacketByteBuf buf) {
        options.clear();
        int length = buf.readShort();

        for (int i = 0; i < length; i++) {
            String name = buf.readString();
            Option<?> option = OptionType.readOption(buf);
            options.add(new OptionEntry<>(name, option));
        }
    }
}
