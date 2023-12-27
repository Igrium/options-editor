package com.igrium.options_editor.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntFunction;

import net.minecraft.network.PacketByteBuf;

public final class NetUtils {
    public static <T> void writeArray(PacketByteBuf buf, T[] array, BiConsumer<PacketByteBuf, T> writer) {
        buf.writeShort(array.length);
        for (T val : array) {
            writer.accept(buf, val);
        }
    }

    public static <T> T[] readArray(PacketByteBuf buf, IntFunction<T[]> factory, Function<PacketByteBuf, T> reader) {
        int length = buf.readShort();
        T[] array = factory.apply(length);
        
        for (int i = 0; i < length; i++) {
            array[i] = reader.apply(buf);
        }
        return array;
    }

    public static <T> void writeCollection(PacketByteBuf buf, Collection<T> collection, BiConsumer<PacketByteBuf, T> writer) {
        buf.writeShort(collection.size());
        for (T val : collection) {
            writer.accept(buf, val);
        }
    }

    public static <T> List<T> readList(PacketByteBuf buf, Function<PacketByteBuf, T> reader) {
        int length = buf.readShort();
        List<T> list = new ArrayList<>(length);

        for (int i = 0; i < length; i++) {
            list.add(reader.apply(buf));
        }
        return list;
    }

    public static void writeStringArray(PacketByteBuf buf, String[] array) {
        writeArray(buf, array, PacketByteBuf::writeString);
    }

    public static String[] readStringArray(PacketByteBuf buf) {
        return readArray(buf, String[]::new, PacketByteBuf::readString);
    }
}
