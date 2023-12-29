package com.igrium.options_editor.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.GameRules;

@Mixin(GameRules.class)
public interface GameRulesAccessor {
    @Accessor("rules")
    public Map<GameRules.Key<?>, GameRules.Rule<?>> getRules();
}
