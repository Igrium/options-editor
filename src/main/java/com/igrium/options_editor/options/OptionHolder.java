package com.igrium.options_editor.options;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;

public class OptionHolder {

    public static record OptionEntry<T>(String name, Option<T> option) {}

    public static record OptionCategory(String name, List<OptionEntry<?>> options) {
        public OptionCategory(String name) {
            this(name, new ArrayList<>());
        }

        public <T> void addOption(String name, OptionType<T> type, T value) {
            Option<T> option = new Option<T>(type, value);
            OptionEntry<T> entry = new OptionEntry<>(name, option);
            options.add(entry);
        }
    }

    public final List<OptionCategory> categories = new ArrayList<>();
    
    public NbtList writeNbt() {
        NbtList list = new NbtList();
        for (OptionCategory category : categories) {
            list.add(IO.writeCategory(category));
        }
        return list;
    }

    public OptionHolder readNbt(NbtList list) {
        categories.clear();
        for (NbtElement element : list) {
            categories.add(IO.readCategory((NbtCompound) element));
        }
        return this;
    }

    public static class IO {
        public static <T> NbtCompound writeOptionEntry(OptionEntry<T> entry) {
            NbtCompound compound = new NbtCompound();
            compound.putString("name", entry.name());

            Option<T> option = entry.option();

            Identifier typeId = OptionType.REGISTRY.getId(option.getType());
            if (typeId == null) {
                throw new RuntimeException("Unregistered option type: " + option.getType());
            }
            compound.putString("type", typeId.toString());

            T val = option.getValue();
            compound.put("val", option.getType().toNbt(val));

            return compound;
        }

        public static OptionEntry<?> readOptionEntry(NbtCompound compound) {
            String name = compound.getString("name");

            Identifier typeId = new Identifier(compound.getString("type"));
            OptionType<?> type = OptionType.REGISTRY.get(typeId);
            if (type == null) {
                throw new RuntimeException("Unknown option type: " + typeId);
            }
            
            return readOptionValue(null, name, compound.get("val"));
        }

        private static <T> OptionEntry<T> readOptionValue(OptionType<T> type, String name, NbtElement element) {
            T val = type.fromNbt(element);
            Option<T> option = new Option<T>(type, val);
            return new OptionEntry<>(name, option);
        }

        public static NbtCompound writeCategory(OptionCategory category) {
            NbtCompound compound = new NbtCompound();
            compound.putString("name", category.name());

            NbtList options = new NbtList();
            for (OptionEntry<?> entry : category.options()) {
                options.add(writeOptionEntry(entry));
            }

            compound.put("options", options);
            return compound;
        }

        public static OptionCategory readCategory(NbtCompound compound) {
            String name = compound.getString("name");
            NbtList options = compound.getList("options", NbtElement.COMPOUND_TYPE);

            List<OptionEntry<?>> list = new ArrayList<>(options.size());
            for (NbtElement element : options) {
                OptionEntry<?> option = readOptionEntry((NbtCompound) element);
                list.add(option);
            }

            return new OptionCategory(name, list);
        }
    }
}
