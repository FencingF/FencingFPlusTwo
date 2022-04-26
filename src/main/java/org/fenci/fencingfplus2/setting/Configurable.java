package org.fenci.fencingfplus2.setting;

import org.fenci.fencingfplus2.util.Globals;

import java.util.ArrayList;
import java.util.Arrays;

public class Configurable implements Globals {
    protected final ArrayList<Setting> settings = new ArrayList<>();

    public void register() {
        Arrays.stream(getClass().getDeclaredFields())
                .filter((field) -> Setting.class.isAssignableFrom(field.getType()))
                .forEach((field) -> {
                    field.setAccessible(true);

                    try {
                        settings.add((Setting) field.get(this));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
    }

    public Setting getSetting(String name) {
        for (Setting setting : settings) {
            if (setting.getName().equalsIgnoreCase(name)) {
                return setting;
            }
        }
        return null;
    }

    public ArrayList<Setting> getSettings() {
        return settings;
    }
}
