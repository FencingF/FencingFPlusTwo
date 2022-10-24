package org.fenci.fencingfplus2.setting;

import net.minecraftforge.common.MinecraftForge;
import org.fenci.fencingfplus2.events.client.OptionChangeEvent;

import java.util.function.Supplier;

public class Setting<T> {
    private final String name;

    private final T defaultValue;
    private final Supplier<Boolean> visibility;
    private final Number min;
    private final Number max;
    private T value;

    public Setting(String name, T value) {
        this(name, value, null, null, null);
    }

    public Setting(String name, T value, Supplier<Boolean> visibility) {
        this(name, value, null, null, visibility);
    }

    public Setting(String name, T value, Number min, Number max) {
        this(name, value, min, max, null);
    }

    public Setting(String name, T value, Number min, Number max, Supplier<Boolean> visibility) {
        this.name = name;
        this.defaultValue = value;
        this.value = value;
        this.min = min;
        this.max = max;
        this.visibility = visibility;
    }

    public static int current(Enum clazz) {
        for (int i = 0; i < clazz.getClass().getEnumConstants().length; ++i) {
            Enum e = ((Enum[]) clazz.getClass().getEnumConstants())[i];
            if (e.name().equalsIgnoreCase(clazz.name())) {
                return i;
            }
        }

        return -1;
    }

    public static Enum increase(Enum clazz) {
        int index = current(clazz);

        for (int i = 0; i < clazz.getClass().getEnumConstants().length; ++i) {
            Enum e = ((Enum[]) clazz.getClass().getEnumConstants())[i];
            if (i == index + 1) {
                return e;
            }
        }

        return ((Enum[]) clazz.getClass().getEnumConstants())[0];
    }

    public String getName() {
        return name;
    }


    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        MinecraftForge.EVENT_BUS.post(new OptionChangeEvent(this));
    }

    public Number getMin() {
        return min;
    }

    public Number getMax() {
        return max;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public boolean isBoolean() {
        return (getValue() instanceof Boolean);
    }

    public boolean isEnum() {
        return (getValue() instanceof Enum);
    }

    public boolean isNumber() {
        return (getValue() instanceof Number);
    }
}