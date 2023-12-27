package com.igrium.options_editor.options;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import net.minecraft.network.PacketByteBuf;

/**
 * Holds deserialized option values in a standardized way.
 */
public class OptionHolder {
    public static final class OptionCategoryEntry {

        public OptionCategoryEntry(String name, OptionCategory category) {
            setName(name);
            setCategory(category);
        }

        private String name = "";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = Objects.requireNonNull(name);
        }

        private OptionCategory category;

        public OptionCategory getCategory() {
            return category;
        }

        public void setCategory(OptionCategory category) {
            this.category = Objects.requireNonNull(category);
        }
    }

    private final List<OptionCategoryEntry> categories = new ArrayList<>();

    public List<OptionCategoryEntry> getCategories() {
        return categories;
    }

    public Stream<OptionCategory> getCategoryStream() {
        return categories.stream().map(v -> v.getCategory());
    }

    public void writeBuffer(PacketByteBuf buf) {
        buf.writeShort(categories.size());
        for (OptionCategoryEntry entry : categories) {
            buf.writeString(entry.getName());
            entry.getCategory().writeBuffer(buf);
        }
    }

    public void readBuffer(PacketByteBuf buf) {
        categories.clear();
        int size = buf.readShort();

        for (int i = 0; i < size; i++) {
            String name = buf.readString();
            OptionCategory cat = new OptionCategory();
            cat.readBuffer(buf);

            categories.add(new OptionCategoryEntry(name, cat));
        }
    }
}
