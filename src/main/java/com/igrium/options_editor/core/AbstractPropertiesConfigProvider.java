package com.igrium.options_editor.core;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;

import net.minecraft.network.PacketByteBuf;

/**
 * A config provider based on a Java Properties object.
 */
public abstract class AbstractPropertiesConfigProvider implements ConfigProvider {
    /**
     * Load the properties from wherever they may be stored in memory.
     * @return Properties.
     */
    public abstract Properties load();

    /**
     * Save a modified version of the properties.
     * @param properties Modified properties. May be partial.
     */
    public abstract void save(Properties properties);

    @Override
    public void load(PacketByteBuf buf) throws IOException {
        Properties prop = load();
        StringWriter writer = new StringWriter();

        prop.store(writer, "");
        buf.writeString(writer.toString());
    }

    @Override
    public void save(PacketByteBuf buf) throws IOException {
        String str = buf.readString();
        StringReader reader = new StringReader(str);

        Properties prop = new Properties();
        prop.load(reader);

        save(prop);
    }
}
