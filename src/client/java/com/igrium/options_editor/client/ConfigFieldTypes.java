package com.igrium.options_editor.client;

import java.util.function.Consumer;

import com.igrium.options_editor.options.Option;
import com.igrium.options_editor.options.OptionType;
import com.igrium.options_editor.options.OptionTypes;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.AbstractFieldBuilder;
import net.minecraft.text.Text;

public class ConfigFieldTypes {

    public static final ConfigFieldType<Boolean> BOOLEAN = registerSimple(OptionTypes.BOOLEAN, ConfigEntryBuilder::startBooleanToggle);
    public static final ConfigFieldType<Integer> INTEGER = registerSimple(OptionTypes.INTEGER, ConfigEntryBuilder::startIntField);
    public static final ConfigFieldType<Long> LONG = registerSimple(OptionTypes.LONG, ConfigEntryBuilder::startLongField);
    public static final ConfigFieldType<Float> FLOAT = registerSimple(OptionTypes.FLOAT, ConfigEntryBuilder::startFloatField);
    public static final ConfigFieldType<Double> DOUBLE = registerSimple(OptionTypes.DOUBLE, ConfigEntryBuilder::startDoubleField);
    

    // Classloader bullshit
    public static void registerDefaults() {

    }

    private static interface BuilderFactory<T> {
        AbstractFieldBuilder<T, ?, ?> create(ConfigEntryBuilder configBuilder, Text name, T value);
    }

    private static class SimpleFieldType<T> implements ConfigFieldType<T> {
        private final BuilderFactory<T> factory;

        public SimpleFieldType(BuilderFactory<T> factory) {
            this.factory = factory;
        }

        @Override
        public AbstractConfigListEntry<?> getField(ConfigBuilder configBuilder, Option<T> option, String name,
                String id, Consumer<T> saveConsumer) {
            AbstractFieldBuilder<T, ?, ?> builder = factory.create(configBuilder.entryBuilder(), Text.literal(name), option.getValue());
            builder.setTooltip(Text.literal(id));
            builder.setSaveConsumer(saveConsumer);
            return builder.build();

        }
    }

    private static <T> ConfigFieldType<T> registerSimple(OptionType<T> type, BuilderFactory<T> builderFactory) {
        return ConfigFieldType.register(type, new SimpleFieldType<>(builderFactory));
    }


}
