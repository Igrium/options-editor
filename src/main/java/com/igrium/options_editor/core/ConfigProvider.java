package com.igrium.options_editor.core;

import java.io.IOException;

import net.minecraft.network.PacketByteBuf;

/**
 * Server-side interface between OptionsEditor and a given config file.
 */
public interface ConfigProvider {
    
    /**
     * Load the config from wherever it may be stored and serialize it to this buffer.
     * @param buf Buffer to serialize to.
     */
    public void load(PacketByteBuf buf) throws IOException;

    /**
     * Deserialize and save the config contained in this buffer.
     * @param buf Buffer to read from.
     */
    public void save(PacketByteBuf buf) throws IOException;
}
