package com.igrium.options_editor.options;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public interface OptionType<T> {
    
    public T fromNbt(NbtElement nbt);

    public NbtElement toNbt(T val);

    public static final SimpleRegistry<OptionType<?>> REGISTRY = FabricRegistryBuilder
            .<OptionType<?>>createSimple(RegistryKey.ofRegistry(new Identifier("options-editor:option_type")))
            .buildAndRegister();
    
    public static <T> OptionType<T> register(OptionType<T> type, Identifier id) {
        return Registry.register(REGISTRY, id, type);
    }
}
