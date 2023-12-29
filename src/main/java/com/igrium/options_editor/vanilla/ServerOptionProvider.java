package com.igrium.options_editor.vanilla;

import com.igrium.options_editor.OptionsEditor;
import com.igrium.options_editor.builder.CategoryBuilder;
import com.igrium.options_editor.builder.OptionBuilder;
import com.igrium.options_editor.builder.OptionHolderBuilder;
import com.igrium.options_editor.options.OptionHolder;
import com.igrium.options_editor.options.OptionProvider;
import com.igrium.options_editor.options.OptionType;
import com.igrium.options_editor.options.OptionTypes;
import com.igrium.options_editor.options.OptionTypes.EnumValues;
import com.igrium.options_editor.options.OptionTypes.IntSlider;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.ServerPropertiesHandler;

// TODO: write cache system for properties that require restart.
public class ServerOptionProvider implements OptionProvider {

    private static OptionBuilder<Boolean> createBool(String name, String id, Boolean value) {
        return createSimple(OptionTypes.BOOLEAN, name, id, value);
    }

    private static OptionBuilder<Integer> createInt(String name, String id, Integer value) {
        return createSimple(OptionTypes.INTEGER, name, id, value);
    }

    private static <T> OptionBuilder<T> createSimple(OptionType<T> type, String name, String id, T value) {
        return OptionBuilder.create(type)
                .name(name)
                .id(id)
                .value(value);
    }

    @Override
    public OptionHolder obtain(MinecraftServer server) {
        if (!(server instanceof DedicatedServer)) {
            OptionsEditor.LOGGER.error("Can't get properties of integrated server.");
            return new OptionHolder();
        }

        DedicatedServer dedicated = (DedicatedServer) server;
        ServerPropertiesHandler properties = dedicated.getProperties();

        OptionHolderBuilder builder = new OptionHolderBuilder().name("Server Properties");
        CategoryBuilder cat = new CategoryBuilder().name("Server Options");

        cat.option(createBool("Spawn Animals", "spawn-animals", properties.spawnAnimals));
        cat.option(createBool("Spawn NPCs", "spawn-npcs", properties.spawnNpcs));
        cat.option(createBool("Spawn Monsters", "spawnMonsters", properties.spawnMonsters));
        cat.option(createBool("PVP", "pvp", properties.pvp));
        cat.option(createBool("Allow Flight", "allow-flight", properties.allowFlight));
        cat.option(OptionBuilder.create(OptionTypes.STRING)
                .name("MOTD")
                .id("motd")
                .value(properties.motd));

        cat.option(createBool("Force Gamemode", "force-gamemode", properties.forceGameMode));
        cat.option(createBool("Enforce Whitelist", "enforce-whitelist", properties.enforceWhitelist));
        cat.option(OptionBuilder.create(OptionTypes.ENUM)
                .name("Difficulty")
                .id("difficulty")
                .value(EnumValues.ofEnum(properties.difficulty)));

        cat.option(OptionBuilder.create(OptionTypes.ENUM)
                .name("Gamemode")
                .id("gamemode")
                .value(EnumValues.ofEnum(properties.gameMode)));

        cat.option(createBool("Announce Player Achievements", "announce-player-achievements",
                properties.announcePlayerAchievements));
        cat.option(createBool("Hardcore", "hardcore", properties.hardcore));
        cat.option(createBool("Allow Nether", "allow-nether", properties.allowNether));
        // cat.option(createBool("Spawn Monsters", "spawnMonsters",
        // properties.spawnMonsters)); // Moved to top
        cat.option(createBool("Enable Command Block", "enable-command-block", properties.enableCommandBlock));
        cat.option(createInt("Spawn Protection", "spawn-protection", properties.spawnProtection));
        cat.option(createInt("OP Permission Level", "op-permission-level", properties.opPermissionLevel));
        cat.option(createInt("Function Permission Level", "function-permission-level",
                properties.functionPermissionLevel));
        cat.option(createSimple(OptionTypes.LONG, "Max Tick Time", "maxTickTime", properties.maxTickTime));
        cat.option(createInt("Max Chained Neighbor Updates", "max-chained-neighbor-updates",
                properties.maxChainedNeighborUpdates));
        cat.option(createInt("Rate Limit", "rateLimit", properties.rateLimit));
        cat.option(createInt("View Distance", "view-distance", properties.viewDistance));
        cat.option(createInt("Simulation Distance", "simulation-distance", properties.simulationDistance));
        cat.option(createInt("Max Players", "max-players", properties.maxPlayers));
        cat.option(createBool("Hide Online Players", "hide-online-players", properties.hideOnlinePlayers));
        cat.option(OptionBuilder.create(OptionTypes.INT_SLIDER)
                .name("Entity Broadcast Range Percentage")
                .id("entity-broadcast-range-percentage")
                .value(new IntSlider(properties.entityBroadcastRangePercentage, 10, 1000)));
        cat.option(createSimple(OptionTypes.STRING,
                "Text Filtering Confdig", "text-filtering-config", properties.textFilteringConfig));
        cat.option(createInt("Player Idle Timeout", "player-idle-timeout", properties.playerIdleTimeout.get()));

        builder.category(cat);
        return builder.build();
        // throw new UnsupportedOperationException("Unimplemented method 'obtain'");
    }

    @Override
    public void apply(OptionHolder holder, MinecraftServer server) {
        
    }
    
}
