package com.igrium.options_editor.builder;

import java.util.LinkedList;
import java.util.List;

import com.igrium.options_editor.options.OptionHolder;

public class OptionHolderBuilder {
    private String name;
    private final List<CategoryBuilder> categories = new LinkedList<>();
    
    public OptionHolderBuilder name(String name) {
        this.name = name;
        return this;
    }

    public OptionHolderBuilder category(CategoryBuilder category) {
        categories.add(category);
        return this;
    }

    public OptionHolder build() {
        if (name == null) throw new IllegalStateException("Option holder name must be set.");
        var list = categories.stream().map(c -> new OptionHolder.OptionCategoryEntry(c.name, c.build())).toList();
        OptionHolder holder = new OptionHolder(list);
        holder.setName(name);
        holder.index();
        return holder;
    }
}
