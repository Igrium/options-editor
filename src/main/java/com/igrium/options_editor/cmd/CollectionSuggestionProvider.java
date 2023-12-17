package com.igrium.options_editor.cmd;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.command.CommandSource;

public class CollectionSuggestionProvider<T, S extends CommandSource> implements SuggestionProvider<S> {
    private final Collection<T> collection;
    private final Function<T, String> mappingFunction;

    public Collection<T> getCollection() {
        return collection;
    }

    public CollectionSuggestionProvider(Collection<T> collection, Function<T, String> mappingFunction) {
        this.collection = collection;
        this.mappingFunction = mappingFunction;
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<S> context,
            SuggestionsBuilder builder) throws CommandSyntaxException {
        for (T val : collection) {
            builder.suggest(mappingFunction.apply(val));
        }
        return builder.buildFuture();
    }

    /**
     * Create a collection-backed suggestion provider that uses an object's native <code>toString</code> method.
     */
    public static <T, S extends CommandSource> CollectionSuggestionProvider<T, S> toStringProvider(Collection<T> collection) {
        return new CollectionSuggestionProvider<>(collection, Object::toString);
    }
}
