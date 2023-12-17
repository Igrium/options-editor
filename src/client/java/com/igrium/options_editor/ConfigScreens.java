package com.igrium.options_editor;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.igrium.options_editor.core.ConfigProvider;

import net.minecraft.client.gui.screen.Screen;

public class ConfigScreens {
    public static interface ConfigScreenFactory<T> {
        public Screen create(ConfigProvider<T> provider, T config, Consumer<T> callback);
    }

    private static final BiMap<ConfigProvider<?>, ConfigScreenFactory<?>> REGISTRY = HashBiMap.create();

    public static <T> void register(ConfigProvider<T> provider, ConfigScreenFactory<T> factory) {
        REGISTRY.put(provider, factory);
    }

    @SuppressWarnings("unchecked")
    public static <T> ConfigScreenFactory<T> getFactory(ConfigProvider<T> provider) {
        return (ConfigScreenFactory<T>) REGISTRY.get(provider);
    }

    public static Map<ConfigProvider<?>, ConfigScreenFactory<?>> registryMap() {
        return Collections.unmodifiableMap(REGISTRY);
    }
}
