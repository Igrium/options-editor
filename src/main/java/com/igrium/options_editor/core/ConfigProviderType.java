package com.igrium.options_editor.core;

import com.igrium.options_editor.vanilla.ServerConfigProvider;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

public class ConfigProviderType<T extends ConfigProvider> {

    public static final SimpleRegistry<ConfigProviderType<?>> REGISTRY = FabricRegistryBuilder
            .<ConfigProviderType<?>>createSimple(
                    RegistryKey.ofRegistry(new Identifier("options-editor:config-provider")))
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();
    
    public static final ConfigProviderType<ServerConfigProvider> SERVER = register(new Identifier("minecraft:server"),
            new ConfigProviderType<>(ServerConfigProvider::new));

    public static <T extends ConfigProvider> ConfigProviderType<T> register(Identifier id, ConfigProviderType<T> type) {
        Registry.register(REGISTRY, id, type);
        return type;
    }

    public static interface ConfigProviderFactory<T extends ConfigProvider> {
        T create(MinecraftServer server);
    }

    private final ConfigProviderFactory<T> factory;

    public ConfigProviderType(ConfigProviderFactory<T> factory) {
        this.factory = factory;
    }

    public T create(MinecraftServer server) {
        return factory.create(server);
    }
}
