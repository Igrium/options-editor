package com.igrium.options_editor.core;

import com.igrium.options_editor.vanilla.ServerConfigProvider;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public final class ConfigProviders {
    public static final SimpleRegistry<ConfigProvider<?>> REGISTRY = FabricRegistryBuilder
            .<ConfigProvider<?>>createSimple(
                    RegistryKey.ofRegistry(new Identifier("options-editor:configs")))
            .buildAndRegister();
    
    public static <T, P extends ConfigProvider<T>> P register(P provider, Identifier id) {
        Registry.register(REGISTRY, id, provider);
        return provider;
    }

    public static final ServerConfigProvider VANILLA = register(new ServerConfigProvider(), new Identifier("minecraft:server"));

    // Empty function for classloader.
    public static void registerDefaults() {

    }
}
