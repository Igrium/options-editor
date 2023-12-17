package com.igrium.options_editor.options;

public class Option<T> {

    private final OptionType<T> type;
    private T value;

    public Option(OptionType<T> type, T value) {
        this.type = type;
        this.value = value;
    }

    public OptionType<T> getType() {
        return type;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
    

}
