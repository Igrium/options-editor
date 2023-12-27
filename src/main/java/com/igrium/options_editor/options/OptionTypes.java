package com.igrium.options_editor.options;

import net.minecraft.nbt.AbstractNbtNumber;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtByteArray;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtLong;
import net.minecraft.nbt.NbtLongArray;
import net.minecraft.nbt.NbtShort;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;

public class OptionTypes {
    public static final OptionType<Boolean> BOOLEAN = OptionType.register(new BooleanType(), new Identifier("options-editor:boolean"));
    public static final OptionType<Byte> BYTE = OptionType.register(new ByteType(), new Identifier("options-editor:byte"));
    public static final OptionType<Short> SHORT = OptionType.register(new ShortType(), new Identifier("options-editor:short"));
    public static final OptionType<Integer> INTEGER = OptionType.register(new IntType(), new Identifier("options-editor:integer"));
    public static final OptionType<Long> LONG = OptionType.register(new LongType(), new Identifier("options-editor:long"));
    public static final OptionType<Float> FLOAT = OptionType.register(new FloatType(), new Identifier("options-editor:float"));
    public static final OptionType<Double> DOUBLE = OptionType.register(new DoubleType(), new Identifier("options-editor:double"));

    public static final OptionType<String> STRING = OptionType.register(new StringType(), new Identifier("options-editor:string"));
    public static final OptionType<byte[]> BYTE_ARRAY = OptionType.register(new ByteArrayType(), new Identifier("options-editor:byte_array"));
    public static final OptionType<int[]> INT_ARRAY = OptionType.register(new IntArrayType(), new Identifier("options-editor:int_array"));
    public static final OptionType<long[]> LONG_ARRAY = OptionType.register(new LongArrayType(), new Identifier("options-editor:long_array"));

    // Empty method for classloader.
    public static void registerDefaults() {

    }

    private static class BooleanType implements OptionType<Boolean> {
        @Override
        public java.lang.Boolean fromNbt(NbtElement nbt) {
            return ((AbstractNbtNumber) nbt).byteValue() != 0;
        }

        @Override
        public NbtElement toNbt(java.lang.Boolean val) {
            return NbtByte.of(val);
        }
    }

    private static class ByteType implements OptionType<Byte> {

        @Override
        public Byte fromNbt(NbtElement nbt) {
            return ((AbstractNbtNumber) nbt).byteValue();
        }

        @Override
        public NbtElement toNbt(Byte val) {
            return NbtByte.of(val);
        }
        
    }
    
    private static class ShortType implements OptionType<Short> {

        @Override
        public Short fromNbt(NbtElement nbt) {
            return ((AbstractNbtNumber) nbt).shortValue();
        }

        @Override
        public NbtElement toNbt(Short val) {
            return NbtShort.of(val);
        }
        
    }

    private static class IntType implements OptionType<Integer> {

        @Override
        public Integer fromNbt(NbtElement nbt) {
            return ((AbstractNbtNumber) nbt).intValue();
        }

        @Override
        public NbtElement toNbt(Integer val) {
            return NbtInt.of(val);
        }
        
    }

    private static class LongType implements OptionType<Long> {

        @Override
        public Long fromNbt(NbtElement nbt) {
            return ((AbstractNbtNumber) nbt).longValue();
        }

        @Override
        public NbtElement toNbt(Long val) {
            return NbtLong.of(val);
        }
        
    }
    
    private static class FloatType implements OptionType<Float> {

        @Override
        public Float fromNbt(NbtElement nbt) {
            return ((AbstractNbtNumber) nbt).floatValue();
        }

        @Override
        public NbtElement toNbt(Float val) {
            return NbtFloat.of(val);
        }
        
    }

    private static class DoubleType implements OptionType<Double> {

        @Override
        public Double fromNbt(NbtElement nbt) {
            return ((AbstractNbtNumber) nbt).doubleValue();
        }

        @Override
        public NbtElement toNbt(Double val) {
            return NbtDouble.of(val);
        }
        
    }

    private static class StringType implements OptionType<String> {

        @Override
        public String fromNbt(NbtElement nbt) {
            return nbt.asString();
        }

        @Override
        public NbtElement toNbt(String val) {
            return NbtString.of(val);
        }
        
    }

    private static class ByteArrayType implements OptionType<byte[]> {

        @Override
        public byte[] fromNbt(NbtElement nbt) {
            return ((NbtByteArray) nbt).getByteArray().clone();
        }

        @Override
        public NbtElement toNbt(byte[] val) {
            return new NbtByteArray(val.clone());
        }
        
    }

    private static class IntArrayType implements OptionType<int[]> {

        @Override
        public int[] fromNbt(NbtElement nbt) {
            return ((NbtIntArray) nbt).getIntArray().clone();
        }

        @Override
        public NbtElement toNbt(int[] val) {
            return new NbtIntArray(val.clone());
        }
        
    }

    private static class LongArrayType implements OptionType<long[]> {

        @Override
        public long[] fromNbt(NbtElement nbt) {
            return ((NbtLongArray) nbt).getLongArray().clone();
        }

        @Override
        public NbtElement toNbt(long[] val) {
            return new NbtLongArray(val.clone());
        }}
}
