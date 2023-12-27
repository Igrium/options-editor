package com.igrium.options_editor.options;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import com.igrium.options_editor.options.OptionCategory.OptionEntry;
import com.mojang.logging.LogUtils;

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

    private String name = "";

    private Map<String, Option<?>> index = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
    }

    private final List<OptionCategoryEntry> categories;

    public OptionHolder() {
        categories = new ArrayList<>();
    }

    public OptionHolder(Collection<? extends OptionCategoryEntry> categories) {
        this.categories = new ArrayList<>(categories);
    }

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

    public void index() throws IllegalStateException {
        index.clear();
        for (OptionCategoryEntry cat : categories) {
            for (OptionEntry<?> entry : cat.category.getOptions()) {
                if (index.put(entry.id(), entry.option()) != null) {
                    throw new IllegalStateException("Duplicate option ID: " + entry.id());
                }
            }
        }
    }

    public Option<?> get(String id) {
        Option<?> option = index.get(id);
        if (option != null) return option;

        LogUtils.getLogger().warn("Option '%s' is not present in the index. Searching manually...".formatted(id));

        for (OptionCategoryEntry cat : categories) {
            for (OptionEntry<?> entry : cat.category.getOptions()) {
                if (entry.id().equals(id)) {
                    return entry.option();
                }
            }
        }

        return null;
    }

    public <T> T getValue(String id, Class<T> type) {
        Option<?> option = get(id);
        return option != null ? type.cast(option.value()) : null;
    }
}
