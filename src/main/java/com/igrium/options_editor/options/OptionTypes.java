package com.igrium.options_editor.options;

import java.util.Arrays;

import com.igrium.options_editor.util.NetUtils;

import net.minecraft.network.PacketByteBuf;

public final class OptionTypes {

    public static final OptionType<Boolean> BOOLEAN = OptionType.register("options-editor:bool", new BoolOptionType());
    public static final OptionType<Integer> INTEGER = OptionType.register("options-editor:int", new IntOptionType());
    public static final OptionType<Long> LONG = OptionType.register("options-editor:long", new LongOptionType());
    public static final OptionType<Float> FLOAT = OptionType.register("options-editor:float", new FloatOptionType());
    public static final OptionType<Double> DOUBLE = OptionType.register("options-editor:double", new DoubleOptionType());
    public static final OptionType<IntSlider> INT_SLIDER = OptionType.register("options-editor:int_slider", new IntSliderOptionType());
    public static final OptionType<LongSlider> LONG_SLIDER = OptionType.register("options-editor:long_slider", new LongSliderOptionType());
    public static final OptionType<EnumValues> ENUM = OptionType.register("options-editor:enum", new EnumOptionType());
    public static final OptionType<String> STRING = OptionType.register("options-editor:string", new StringOptionType());


    // For classloader
    public static void registerDefaults() {
        
    }

    private static class BoolOptionType implements OptionType<Boolean> {
        @Override
        public Boolean read(PacketByteBuf buf) {
            return buf.readBoolean();
        }

        @Override
        public void write(Boolean val, PacketByteBuf buf) {
            buf.writeBoolean(val);
        }
        
        @Override
        public Class<? extends Boolean> getType() {
            return Boolean.class;
        }
    }

    private static class IntOptionType implements OptionType<Integer> {
        @Override
        public Integer read(PacketByteBuf buf) {
            return buf.readInt();
        }

        @Override
        public void write(Integer val, PacketByteBuf buf) {
            buf.writeInt(val);
        }

        @Override
        public Class<? extends Integer> getType() {
            return Integer.class;
        }
    }

    private static class LongOptionType implements OptionType<Long> {
        @Override
        public Long read(PacketByteBuf buf) {
            return buf.readLong();
        }

        @Override
        public void write(Long val, PacketByteBuf buf) {
            buf.writeLong(val);
        }

        @Override
        public Class<? extends Long> getType() {
            return Long.class;
        }
    }

    private static class FloatOptionType implements OptionType<Float> {
        @Override
        public Float read(PacketByteBuf buf) {
            return buf.readFloat();
        }

        @Override
        public void write(Float val, PacketByteBuf buf) {
            buf.writeFloat(val);
        }

        @Override
        public Class<? extends Float> getType() {
            return Float.class;
        }
    }

    private static class DoubleOptionType implements OptionType<Double> {
        @Override
        public Double read(PacketByteBuf buf) {
            return buf.readDouble();
        }

        @Override
        public void write(Double val, PacketByteBuf buf) {
            buf.writeDouble(val);
        }

        @Override
        public Class<? extends Double> getType() {
            return Double.class;
        }
    }

    public static record IntSlider(int value, int min, int max) {}

    private static class IntSliderOptionType implements OptionType<IntSlider> {
        @Override
        public IntSlider read(PacketByteBuf buf) {
            int value = buf.readInt();
            int min = buf.readInt();
            int max = buf.readInt();
            return new IntSlider(value, min, max);
        }

        @Override
        public void write(IntSlider val, PacketByteBuf buf) {
            buf.writeInt(val.value);
            buf.writeInt(val.min);
            buf.writeInt(val.max);
        }

        @Override
        public Class<? extends IntSlider> getType() {
            return IntSlider.class;
        }
    }

    public static record LongSlider(long value, long min, long max) {}

    private static class LongSliderOptionType implements OptionType<LongSlider> {
        @Override
        public LongSlider read(PacketByteBuf buf) {
            long value = buf.readLong();
            long min = buf.readLong();
            long max = buf.readLong();
            return new LongSlider(value, min, max);
        }

        @Override
        public void write(LongSlider val, PacketByteBuf buf) {
            buf.writeLong(val.value);
            buf.writeLong(val.min);
            buf.writeLong(val.max);
        }

        @Override
        public Class<? extends LongSlider> getType() {
            return LongSlider.class;
        }
    }

    public static record EnumValues(String[] values, int ordinal) {
        public <T extends Enum<T>> T toEnum(Class<T> enumClass) {
            return enumClass.getEnumConstants()[ordinal];
        }

        public static EnumValues ofEnum(Enum<?> val) {
            Class<? extends Enum<?>> clazz = val.getDeclaringClass();
            String[] values = Arrays.stream(clazz.getEnumConstants()).map(Enum::name).toArray(String[]::new);
            int ordinal = val.ordinal();
            return new EnumValues(values, ordinal);
        }
    }

    private static class EnumOptionType implements OptionType<EnumValues> {
        @Override
        public void write(EnumValues val, PacketByteBuf buf) {
            buf.writeShort(val.ordinal);
            NetUtils.writeStringArray(buf, val.values);
        }

        @Override
        public EnumValues read(PacketByteBuf buf) {
            int ordinal = buf.readShort();
            String[] values = NetUtils.readStringArray(buf);
            return new EnumValues(values, ordinal);
        }

        @Override
        public Class<? extends EnumValues> getType() {
            return EnumValues.class;
        }
    }
    
    private static class StringOptionType implements OptionType<String> {
        @Override
        public void write(String val, PacketByteBuf buf) {
            buf.writeString(val);
        }

        @Override
        public String read(PacketByteBuf buf) {
            return buf.readString();
        }

        @Override
        public Class<? extends String> getType() {
            return String.class;
        }
    }
}
