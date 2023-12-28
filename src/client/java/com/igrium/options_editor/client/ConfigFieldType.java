package com.igrium.options_editor.client;

import java.util.function.Consumer;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.igrium.options_editor.options.Option;
import com.igrium.options_editor.options.OptionType;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;

public interface ConfigFieldType<T> {

    /**
     * Build the field that controls this option type.
     * 
     * @param configBuilder The config builder to use.
     * @param option        Option to populate the field with.
     * @return The built field.
     */
    public AbstractConfigListEntry<?> getField(ConfigBuilder configBuilder, Option<T> option, String name, String id, Consumer<T> saveConsumer);

    static final BiMap<OptionType<?>, ConfigFieldType<?>> REGISTRY = HashBiMap.create();

    /**
     * Get an unmodifiable view of the registry.
     * @return Registry view.
     */
    public static BiMap<OptionType<?>, ConfigFieldType<?>> getRegistry() {
        return Maps.unmodifiableBiMap(REGISTRY);
    }

    /**
     * Register a config field type.
     * @param <T> Type of option.
     * @param <F> Type of field.
     * @param optionType The option type to register on.
     * @param fieldType The field type to register.
     * @return <code>fieldType</code>
     */
    public static <T, F extends ConfigFieldType<T>> F register(OptionType<T> optionType, F fieldType) {
        REGISTRY.put(optionType, fieldType);
        return fieldType;
    }

    @SuppressWarnings("unchecked")
    public static <T> ConfigFieldType<T> get(OptionType<T> optionType) {
        return (ConfigFieldType<T>) REGISTRY.get(optionType);
    }

    @SuppressWarnings("unchecked")
    public static <T> OptionType<T> getKey(ConfigFieldType<T> fieldType) {
        return (OptionType<T>) REGISTRY.inverse().get(fieldType);
    }
}
