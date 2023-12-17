package com.igrium.options_editor.vanilla;

import java.util.function.BiConsumer;
import java.util.function.Function;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.dedicated.ServerPropertiesHandler;
import net.minecraft.text.Text;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameMode;

/**
 * A simple container of server & level properties. All values may be null.
 */
public class SimpleServerProperties {
    public Boolean onlineMode;
    public Boolean preventProxyConnections;
    public Boolean spawnAnimals;
    public Boolean spawnNpcs;
    public Boolean pvp;
    public Boolean allowFlight;
    public String motd;
    public Boolean forceGameMode;
    public Boolean enforceWhitelist;
    public Difficulty difficulty;
    public GameMode gameMode;
    public String levelName;
    // public Boolean enableQuery;
    // public Integer queryPort;
    // public Integer enableRcon;
    // public Integer rconPort;
    public Boolean hardcore;
    public Boolean allowNether;
    public Boolean spawnMonsters;
    // public Boolean useNativeTransport;
    public Boolean enableCommandBlock;
    public Integer spawnProtection;
    public Integer functionPermissionLevel;
    public Long maxTickTime;
    public Integer maxChainedNeighborUpdates;
    public Integer rateLimit;
    public Integer viewDistance;
    public Integer simulationDistance;
    public Integer maxPlayers;
    // public Boolean broadcastRconToOps;
    public Boolean broadcastConsoleToOps;
    public Integer maxWorldSize;
    public Boolean syncChunkWrites;
    // public Boolean enableJmxMonitoring;
    public Boolean enableStatus;
    public Boolean hideOnlinePlayers;
    public Integer entityBroadcastRangePercentage;
    public String textFilteringConfig;

    public String resourcePack;
    public String resourcePackPrompt;
    public String resourcePackSha1;
    public Boolean requireResourcePack;

    public Integer playerIdleTimeout;
    public Boolean whiteList;
    public Boolean enforceSecureProfile;
    public Boolean logIps;

    public void serialize(PacketByteBuf buffer) {
        writeNullable(buffer, onlineMode);
        writeNullable(buffer, preventProxyConnections);
        writeNullable(buffer, spawnAnimals);
        writeNullable(buffer, spawnNpcs);
        writeNullable(buffer, pvp);
        writeNullable(buffer, allowFlight);
        writeNullable(buffer, motd);
        writeNullable(buffer, forceGameMode);
        writeNullable(buffer, difficulty);
        writeNullable(buffer, levelName);
        writeNullable(buffer, hardcore);
        writeNullable(buffer, allowNether);
        writeNullable(buffer, spawnMonsters);
        writeNullable(buffer, enableCommandBlock);
        writeNullable(buffer, spawnProtection);
        writeNullable(buffer, functionPermissionLevel);
        writeNullable(buffer, maxTickTime);
        writeNullable(buffer, maxChainedNeighborUpdates);
        writeNullable(buffer, rateLimit);
        writeNullable(buffer, viewDistance);
        writeNullable(buffer, simulationDistance);
        writeNullable(buffer, maxPlayers);
        writeNullable(buffer, broadcastConsoleToOps);
        writeNullable(buffer, maxWorldSize);
        writeNullable(buffer, enableStatus);
        writeNullable(buffer, hideOnlinePlayers);
        writeNullable(buffer, entityBroadcastRangePercentage);
        writeNullable(buffer, textFilteringConfig);
        writeNullable(buffer, resourcePack);
        writeNullable(buffer, resourcePackPrompt);
        writeNullable(buffer, resourcePackSha1);
        writeNullable(buffer, requireResourcePack);
        writeNullable(buffer, playerIdleTimeout);
        writeNullable(buffer, whiteList);
        writeNullable(buffer, enforceSecureProfile);
        writeNullable(buffer, logIps);
    }

    public void deserialize(PacketByteBuf buffer) {
        onlineMode              = readNullableBoolean(buffer);
        preventProxyConnections = readNullableBoolean(buffer);
        spawnAnimals            = readNullableBoolean(buffer);
        spawnNpcs               = readNullableBoolean(buffer);
        pvp                     = readNullableBoolean(buffer);
        allowFlight             = readNullableBoolean(buffer);
        motd                    = readNullableString(buffer);
        forceGameMode           = readNullableBoolean(buffer);
        difficulty              = readNullableEnum(buffer, Difficulty.class);
        levelName               = readNullableString(buffer);
        hardcore                = readNullableBoolean(buffer);
        allowNether             = readNullableBoolean(buffer);
        spawnMonsters           = readNullableBoolean(buffer);
        enableCommandBlock      = readNullableBoolean(buffer);
        spawnProtection         = readNullableInteger(buffer);
        functionPermissionLevel = readNullableInteger(buffer);
        maxTickTime             = readNullableLong(buffer);
        maxChainedNeighborUpdates = readNullableInteger(buffer);
        rateLimit               = readNullableInteger(buffer);
        viewDistance            = readNullableInteger(buffer);
        simulationDistance      = readNullableInteger(buffer);
        maxPlayers              = readNullableInteger(buffer);
        broadcastConsoleToOps   = readNullableBoolean(buffer);
        maxWorldSize            = readNullableInteger(buffer);
        enableStatus            = readNullableBoolean(buffer);
        hideOnlinePlayers       = readNullableBoolean(buffer);
        entityBroadcastRangePercentage = readNullableInteger(buffer);
        textFilteringConfig     = readNullableString(buffer);
        resourcePack            = readNullableString(buffer);
        resourcePackPrompt      = readNullableString(buffer);
        resourcePackSha1        = readNullableString(buffer);
        requireResourcePack     = readNullableBoolean(buffer);
        playerIdleTimeout       = readNullableInteger(buffer);
        enforceSecureProfile    = readNullableBoolean(buffer);
        logIps                  = readNullableBoolean(buffer);
    }

    public void load(ServerPropertiesHandler handler) {
        onlineMode = handler.onlineMode;
        preventProxyConnections = handler.preventProxyConnections;
        spawnAnimals = handler.spawnAnimals;
        spawnNpcs = handler.spawnNpcs;
        pvp = handler.pvp;
        allowFlight = handler.allowFlight;
        motd = handler.motd;
        forceGameMode = handler.forceGameMode;
        enforceWhitelist = handler.enforceWhitelist;
        difficulty = handler.difficulty;
        gameMode = handler.gameMode;
        levelName = handler.levelName;
        hardcore = handler.hardcore;
        allowNether = handler.allowNether;
        spawnMonsters = handler.spawnMonsters;
        enableCommandBlock = handler.enableCommandBlock;
        spawnProtection = handler.spawnProtection;
        functionPermissionLevel = handler.functionPermissionLevel;
        maxTickTime = handler.maxTickTime;
        maxChainedNeighborUpdates = handler.maxChainedNeighborUpdates;
        rateLimit = handler.rateLimit;
        viewDistance = handler.viewDistance;
        simulationDistance = handler.simulationDistance;
        maxPlayers = handler.maxPlayers;
        maxWorldSize = handler.maxWorldSize;
        syncChunkWrites = handler.syncChunkWrites;
        enableStatus = handler.enableStatus;
        hideOnlinePlayers = handler.hideOnlinePlayers;
        entityBroadcastRangePercentage = handler.entityBroadcastRangePercentage;
        textFilteringConfig = handler.textFilteringConfig;
        handler.serverResourcePackProperties.ifPresent(props -> {
            resourcePack = props.url();
            resourcePackPrompt = Text.Serializer.toJson(props.prompt());
            resourcePackSha1 = props.hash();
            requireResourcePack = props.isRequired();
        });
        playerIdleTimeout = handler.playerIdleTimeout.get();
        whiteList = handler.whiteList.get();
        logIps = handler.logIps;
    }

    private static <T> void writeNullable(PacketByteBuf buffer, T val, BiConsumer<PacketByteBuf, T> writer) {
        if (val != null) {
            buffer.writeBoolean(true);
            writer.accept(buffer, val);
        } else {
            buffer.writeBoolean(false);
        }
    }

    private static void writeNullable(PacketByteBuf buffer, Boolean val) {
        writeNullable(buffer, val, (buf, b) -> buf.writeBoolean(b));
    }

    private static void writeNullable(PacketByteBuf buffer, Integer val) {
        writeNullable(buffer, val, (buf, i) -> buf.writeInt(val));
    }

    private static void writeNullable(PacketByteBuf buffer, Long val) {
        writeNullable(buffer, val, (buf, l) -> buf.writeLong(l));
    }

    private static void writeNullable(PacketByteBuf buffer, String val) {
        writeNullable(buffer, val, (buf, s) -> buf.writeString(s));
    }

    private static void writeNullable(PacketByteBuf buffer, Enum<?> enumVal) {
        writeNullable(buffer, enumVal, (buf, v) -> buf.writeEnumConstant(v));
    }

    private static <T> T readNullable(PacketByteBuf buffer, Function<PacketByteBuf, T> reader) {
        if (buffer.readBoolean()) {
            return reader.apply(buffer);
        } else {
            return null;
        }
    }

    private static Boolean readNullableBoolean(PacketByteBuf buffer) {
        return readNullable(buffer, buf -> buf.readBoolean());
    }

    private static Integer readNullableInteger(PacketByteBuf buffer) {
        return readNullable(buffer, buf -> buf.readInt());
    }

    private static Long readNullableLong(PacketByteBuf buffer) {
        return readNullable(buffer, buf -> buf.readLong());
    }

    private static String readNullableString(PacketByteBuf buffer) {
        return readNullable(buffer, buf -> buf.readString());
    }

    public static <T extends Enum<T>> T readNullableEnum(PacketByteBuf buffer, Class<T> enumClass) {
        return readNullable(buffer, buf -> buf.readEnumConstant(enumClass));
    }
}
