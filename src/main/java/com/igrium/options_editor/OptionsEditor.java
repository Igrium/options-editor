package com.igrium.options_editor;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.network.ServerPlayerEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.igrium.options_editor.cmd.ConfigCommand;
import com.igrium.options_editor.options.OptionProviders;
import com.igrium.options_editor.options.OptionTypes;

public class OptionsEditor implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("options-editor");

    @Override
    public void onInitialize() {
        OptionProviders.registerDefaults();
        OptionTypes.registerDefaults();

        CommandRegistrationCallback.EVENT.register(ConfigCommand::register);
        ClientConfigInterface.initListeners();
    }
    
    /**
     * Check if a given client has this mod installed.
     * @param player Client to check.
     * @return If the mod is installed.
     */
    public static boolean clientHasMod(ServerPlayerEntity player) {
        return true;
    }
}