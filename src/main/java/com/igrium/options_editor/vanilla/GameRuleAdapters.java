package com.igrium.options_editor.vanilla;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.igrium.options_editor.options.OptionType;
import com.igrium.options_editor.options.OptionTypes;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules.BooleanRule;
import net.minecraft.world.GameRules.IntRule;
import net.minecraft.world.GameRules.Rule;

public class GameRuleAdapters {
    public static interface GameRuleAdapter<T, R extends Rule<?>> {
        T read(R rule);
        void write(T val, R rule, MinecraftServer server);

        public OptionType<T> getType();

    }

    private static final BiMap<Class<? extends Rule<?>>, GameRuleAdapter<?, ?>> REGISTRY = HashBiMap.create();

    public static <T extends Rule<T>, A extends GameRuleAdapter<?, T>> A register(Class<T> clazz, A rule) {
        REGISTRY.put(clazz, rule);
        return rule;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Rule<?>> GameRuleAdapter<?, T> get(Class<T> clazz) {
        return (GameRuleAdapter<?, T>) REGISTRY.get(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Rule<T>> Class<T> getKey(GameRuleAdapter<?, T> adapter) {
        return (Class<T>) REGISTRY.inverse().get(adapter);
    }

    public static final GameRuleAdapter<Boolean, BooleanRule> BOOLEAN = register(BooleanRule.class, new BooleanRuleAdapter());
    public static final GameRuleAdapter<Integer, IntRule> INTEGER = register(IntRule.class, new IntRuleAdapter());

    private static class BooleanRuleAdapter implements GameRuleAdapter<Boolean, BooleanRule> {

        @Override
        public Boolean read(BooleanRule rule) {
            return rule.get();
        }

        @Override
        public void write(Boolean val, BooleanRule rule, MinecraftServer server) {
            rule.set(val, server);
        }

        @Override
        public OptionType<Boolean> getType() {
            return OptionTypes.BOOLEAN;
        }
        
    }

    private static class IntRuleAdapter implements GameRuleAdapter<Integer, IntRule> {
        @Override
        public Integer read(IntRule rule) {
            return rule.get();
        }

        @Override
        public void write(Integer val, IntRule rule, MinecraftServer server) {
            rule.set(val, server);
        }

        @Override
        public OptionType<Integer> getType() {
            return OptionTypes.INTEGER;
        }
    }

}
