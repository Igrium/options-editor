package com.igrium.options_editor.vanilla;

import com.igrium.options_editor.builder.CategoryBuilder;
import com.igrium.options_editor.builder.OptionBuilder;
import com.igrium.options_editor.builder.OptionHolderBuilder;
import com.igrium.options_editor.options.OptionHolder;
import com.igrium.options_editor.options.OptionProvider;
import com.igrium.options_editor.options.OptionTypes;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;

public class GamerulesOptionProvider implements OptionProvider {

    @Override
    public OptionHolder obtain(MinecraftServer server) {
        GameRules gamerules = server.getGameRules();

        return new OptionHolderBuilder().name("Game Rules").category(
            new CategoryBuilder().name("gamerules").option(
                OptionBuilder.create(OptionTypes.BOOLEAN)
                        .name("Mob Griefing")
                        .id("mobGriefing")
                        .value(gamerules.getBoolean(GameRules.DO_MOB_GRIEFING))
            ).option(
                OptionBuilder.create(OptionTypes.BOOLEAN)
                        .name("Keep Inventory")
                        .id("keepInventory")
                        .value(gamerules.getBoolean(GameRules.KEEP_INVENTORY))
            )
        ).build();
    }

    @Override
    public void apply(OptionHolder holder, MinecraftServer server) {
        GameRules gameRules = server.getGameRules();
        gameRules.get(GameRules.DO_MOB_GRIEFING).set(holder.getValue("mobGriefing", Boolean.class), server);
        gameRules.get(GameRules.KEEP_INVENTORY).set(holder.getValue("keepInventory", Boolean.class), server);
    }
    
}
