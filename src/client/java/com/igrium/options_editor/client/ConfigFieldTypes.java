package com.igrium.options_editor.client;

import java.util.function.Consumer;

import com.igrium.options_editor.options.Option;
import com.igrium.options_editor.options.OptionType;
import com.igrium.options_editor.options.OptionTypes;
import com.igrium.options_editor.options.OptionTypes.IntSlider;
import com.igrium.options_editor.options.OptionTypes.LongSlider;

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
    public static final ConfigFieldType<String> STRING = registerSimple(OptionTypes.STRING, ConfigEntryBuilder::startStrField);

    public static final ConfigFieldType<IntSlider> INT_SLIDER = ConfigFieldType.register(OptionTypes.INT_SLIDER, new IntSliderFieldType());

    private static final class IntSliderFieldType implements ConfigFieldType<IntSlider> {

        @Override
        public AbstractConfigListEntry<?> getField(ConfigBuilder configBuilder, Option<IntSlider> option, String name,
                String id, Consumer<IntSlider> saveConsumer) {
            IntSlider val = option.getValue();
            return configBuilder.entryBuilder()
                    .startIntSlider(Text.literal(name), val.value(), val.min(), val.max())
                    .setTooltip(Text.literal(id))
                    .setSaveConsumer(i -> saveConsumer.accept(new IntSlider(i, val.min(), val.max())))
                    .build();
        }

    }

    public static final ConfigFieldType<LongSlider> LONG_SLIDER = ConfigFieldType.register(OptionTypes.LONG_SLIDER, new LongSliderFieldType());

    private static final class LongSliderFieldType implements ConfigFieldType<LongSlider> {

        @Override
        public AbstractConfigListEntry<?> getField(ConfigBuilder configBuilder, Option<LongSlider> option, String name,
                String id, Consumer<LongSlider> saveConsumer) {
            LongSlider val = option.getValue();
            return configBuilder.entryBuilder()
                    .startLongSlider(Text.literal(name), val.value(), val.min(), val.max())
                    .setTooltip(Text.literal(id))
                    .setSaveConsumer(i -> saveConsumer.accept(new LongSlider(i, val.min(), val.max())))
                    .build();
        }

    }

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
