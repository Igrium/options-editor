package com.igrium.options_editor.vanilla;

import com.igrium.options_editor.OptionsEditor;
import com.igrium.options_editor.options.OptionHolder;
import com.igrium.options_editor.options.OptionProvider;
import com.igrium.options_editor.options.OptionTypes;
import com.igrium.options_editor.options.OptionHolder.OptionCategory;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.dedicated.ServerPropertiesHandler;

public class ServerOptionProvider implements OptionProvider {

    @Override
    public OptionHolder load(MinecraftServer server) {
        if (!(server instanceof MinecraftDedicatedServer)) {
            OptionsEditor.LOGGER.error("Can't load options for integrated server.");
            return new OptionHolder();
        }
        MinecraftDedicatedServer dedicated = (MinecraftDedicatedServer) server;

        OptionHolder holder = new OptionHolder();
        OptionCategory category = new OptionCategory("general");
        holder.categories.add(category);

        ServerPropertiesHandler properties = dedicated.getProperties();

        category.addOption("onlineMode", OptionTypes.BOOLEAN, properties.onlineMode);
        category.addOption("preventProxyConnections", OptionTypes.BOOLEAN, properties.preventProxyConnections);
        category.addOption("spawnAnimals", OptionTypes.BOOLEAN, null);


        return holder;
    }

    @Override
    public void apply(MinecraftServer server, OptionHolder holder) {
        // TODO Auto-generated method stub
    }
    
}
