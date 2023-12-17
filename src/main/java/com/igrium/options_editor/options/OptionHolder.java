package com.igrium.options_editor.options;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class OptionHolder {
    private final List<Option<?>> options = new ArrayList<>();

    public List<Option<?>> getOptions() {
        return options;
    }

    public void serialize(PacketByteBuf buffer) {
        List<OptionType<?>> types = new ArrayList<>();
        for (Option<?> option : options) {
            if (!types.contains(option.getType())) {
                types.add(option.getType());
            }
        }

        buffer.writeInt(types.size());
        for (OptionType<?> type : types) {
            Identifier id = OptionType.REGISTRY.getId(type);
            if (id == null) {
                throw new IllegalStateException("Unregistered option type: " + type);
            }
            buffer.writeIdentifier(id);
        }

        buffer.writeInt(options.size());
        for (Option<?> option : options) {
            int typeIndex = types.indexOf(option.getType());
            if (typeIndex < 0) {
                throw new IllegalStateException("Tried to write an option without declaring its type.");
            }

            buffer.writeByte(typeIndex);
            writeOption(option, buffer);
        }
    }

    public void deserialize(PacketByteBuf buffer) {
        options.clear();

        int numTypes = buffer.readInt();
        List<OptionType<?>> types = new ArrayList<>(numTypes);
        
        for (int i = 0; i < numTypes; i++) {
            Identifier typeId = buffer.readIdentifier();
            OptionType<?> type = OptionType.REGISTRY.get(typeId);

            if (type == null) {
                throw new IllegalStateException("Unknown option type: " + typeId);
            }
            
            types.add(type);
        }

        int numOptions = buffer.readInt();
        for (int i = 0; i < numOptions; i++) {
            int typeIndex = buffer.readByte();
            OptionType<?> type = types.get(typeIndex);
            options.add(readOption(type, buffer));
        }
    }

    private <T> void writeOption(Option<T> option, PacketByteBuf buf) {
        option.getType().serialize(buf, option.getValue());
    }

    private <T> Option<T> readOption(OptionType<T> type, PacketByteBuf buf) {
        T val = type.deserialize(buf);
        return new Option<>(type, val);
    }

}
