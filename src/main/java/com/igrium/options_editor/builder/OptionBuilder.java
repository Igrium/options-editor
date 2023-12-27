package com.igrium.options_editor.builder;

import com.igrium.options_editor.options.Option;
import com.igrium.options_editor.options.OptionType;

public class OptionBuilder<T> {
    private OptionType<T> type;
    protected String name;
    private T value;

    public OptionBuilder<T> type(OptionType<T> type) {
        this.type = type;
        return this;
    }

    public OptionBuilder<T> name(String name) {
        this.name = name;
        return this;
    }

    public OptionBuilder<T> value(T value) {
        this.value = value;
        return this;
    }

    public static <T> OptionBuilder<T> create(OptionType<T> type) {
        return new OptionBuilder<T>().type(type);
    }

    public Option<T> build() {
        if (type == null) throw new IllegalStateException("Option type must be set.");
        if (name == null) throw new IllegalStateException("Option name must be set.");
        if (value == null) throw new IllegalStateException("Default value must be set.");

        return new Option<>(type, value);
    }
}
