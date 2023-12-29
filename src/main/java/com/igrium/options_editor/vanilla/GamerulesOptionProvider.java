package com.igrium.options_editor.vanilla;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.igrium.options_editor.OptionsEditor;
import com.igrium.options_editor.builder.CategoryBuilder;
import com.igrium.options_editor.builder.OptionBuilder;
import com.igrium.options_editor.builder.OptionHolderBuilder;
import com.igrium.options_editor.mixin.GameRulesAccessor;
import com.igrium.options_editor.options.Option;
import com.igrium.options_editor.options.OptionCategory;
import com.igrium.options_editor.options.OptionHolder;
import com.igrium.options_editor.options.OptionProvider;
import com.igrium.options_editor.vanilla.GameRuleAdapters.GameRuleAdapter;
import com.mojang.logging.LogUtils;

import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.Rule;

public class GamerulesOptionProvider implements OptionProvider {

    public static class RuleAdapter<T, R extends GameRules.Rule<R>> {

    }

    @Override
    public OptionHolder obtain(MinecraftServer server) {
        GameRules gamerules = server.getGameRules();

        OptionHolderBuilder builder = new OptionHolderBuilder().name("Game Rules");

        Map<GameRules.Category, CategoryBuilder> categories = new HashMap<>();

        for (var entry : ((GameRulesAccessor) gamerules).getRules().entrySet()) {
            CategoryBuilder category = categories.computeIfAbsent(entry.getKey().getCategory(), cat -> {

                CategoryBuilder catBuilder = new CategoryBuilder().name(Text.translatable(cat.getCategory()).getString());
                builder.category(catBuilder);
                return catBuilder;
            });

            OptionBuilder<?> opt = writeRule(entry.getValue());
            if (opt == null)
                continue;
            category.option(opt
                    .name(Text.translatable(entry.getKey().getTranslationKey()).getString())
                    .id(entry.getKey().getName()));
        }

        return builder.build();
    }

    private static <R extends Rule<?>> OptionBuilder<?> writeRule(R rule) {
        GameRuleAdapter<?, R> adapter = GameRuleAdapters.get(getGenericClass(rule));
        if (adapter == null) {
            OptionsEditor.LOGGER.warn("No gamerule adapter found for " + rule.getClass());
            return null;
        }
        
        return writeRuleAdapter(adapter, rule);
    }
    
    private static <T, R extends Rule<?>> OptionBuilder<T> writeRuleAdapter(GameRuleAdapter<T, R> adapter, R rule) {
        return OptionBuilder.create(adapter.getType()).value(adapter.read(rule));
    }

    @Override
    public void apply(OptionHolder holder, MinecraftServer server) {
        GameRules gameRules = server.getGameRules();

        Map<String, Rule<?>> index = ((GameRulesAccessor) gameRules).getRules().entrySet()
                .stream().collect(Collectors.toMap(val -> val.getKey().getName(), val -> val.getValue()));

        for (OptionCategory category : makeIterable(holder::getCategoryStream)) {
            for (var entry : category.getOptions()) {
                Rule<?> rule = index.get(entry.id());
                if (rule == null) {
                    OptionsEditor.LOGGER.warn("Invalid gamerule: " + entry.name());
                    continue;
                }
                applyRule(entry.option(), rule, server);
            }
        }
        
    }

    private static <R extends Rule<?>> void applyRule(Option<?> option, R rule, MinecraftServer server) {;
        GameRuleAdapter<?, R> adapter = GameRuleAdapters.get(getGenericClass(rule));
        if (adapter == null) {
            LogUtils.getLogger().error(
                    "Somehow, we ended up in applyRule for a rule that does not have an adapter. This should not happen.");
            return;
        }
        applyRuleAdapter(option, rule, adapter, server);
    }

    private static <T, R extends Rule<?>> void applyRuleAdapter(Option<?> option, R rule, GameRuleAdapter<T, R> adapter, MinecraftServer server) {
        T val = adapter.getType().getType().cast(option.getValue());
        adapter.write(val, rule, server);
    }

    private static <T> Iterable<T> makeIterable(Supplier<Stream<T>> stream) {
        return () -> stream.get().iterator();
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<? extends T> getGenericClass(T obj) {
        return (Class<? extends T>) obj.getClass();
    }
}
