package com.igrium.options_editor.core;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;

/**
 * Server-side interface between OptionsEditor and a given config file.
 */
public interface ConfigProvider<T> {

    /**
     * Get the class of the config object in use.
     * @return
     */
    public Class<T> configClass();

    public void serialize(PacketByteBuf buf, T config);

    public T deserialize(PacketByteBuf buf);

    public T load(MinecraftServer server);

    public void apply(MinecraftServer server, T config);
        
    // /**
    //  * Load the config from wherever it may be stored and serialize it to this buffer.
    //  * @param buf Buffer to serialize to.
    //  */
    // public void load(PacketByteBuf buf) throws IOException;

    // /**
    //  * Deserialize and save the config contained in this buffer.
    //  * @param buf Buffer to read from.
    //  */
    // public void save(PacketByteBuf buf) throws IOException;
}
