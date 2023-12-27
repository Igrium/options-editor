package com.igrium.options_editor.builder;

import java.util.LinkedList;
import java.util.List;

import com.igrium.options_editor.options.OptionCategory;

public class CategoryBuilder {
    protected String name;
    private List<OptionBuilder<?>> options = new LinkedList<>();

    public CategoryBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CategoryBuilder option(OptionBuilder<?> option) {
        options.add(option);
        return this;
    }

    public OptionCategory build() {
        if (name == null) throw new IllegalStateException("Category name must be set.");

        var list = options.stream().map(val -> new OptionCategory.OptionEntry<>(val.id, val.name, val.build())).toList();
        return new OptionCategory(list);
    }
}
