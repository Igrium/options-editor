package com.igrium.options_editor.options;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public interface OptionType<T> {
    public T read(PacketByteBuf buf);
    public void write(T val, PacketByteBuf buf);

    default Option<T> create(T val) {
        return new Option<>(this, val);
    }

    public static final Registry<OptionType<?>> REGISTRY = FabricRegistryBuilder
            .<OptionType<?>>createSimple(RegistryKey.ofRegistry(new Identifier("options-editor", "option-types")))
            .buildAndRegister();
    
    public static <T extends OptionType<?>> T register(Identifier id, T entry) {
        return Registry.register(REGISTRY, id, entry);
    }

    public static <T extends OptionType<?>> T register(String id, T entry) {
        return register(new Identifier(id), entry);
    }

    public static void writeOption(Option<?> option, PacketByteBuf buf) {
        Identifier id = REGISTRY.getId(option.getType());
        if (id == null) {
            throw new IllegalStateException("Option has not been registered: " + option);
        }
        buf.writeIdentifier(id);
        option.write(buf);
    }

    public static <T> Option<T> readOption(PacketByteBuf buf, OptionType<T> type) {
        return new Option<T>(type, type.read(buf));
    }

    public static Option<?> readOption(PacketByteBuf buf) {
        Identifier id = buf.readIdentifier();
        OptionType<?> type = REGISTRY.get(id);

        if (type == null) {
            throw new IllegalStateException("Unknown option type: " + id);
        }
        
        return readOption(buf, type);
    }
}
