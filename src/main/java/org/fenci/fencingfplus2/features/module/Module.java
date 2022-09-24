package org.fenci.fencingfplus2.features.module;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.events.client.ModuleToggleEvent;
import org.fenci.fencingfplus2.setting.Configurable;
import org.fenci.fencingfplus2.setting.Keybind;
import org.fenci.fencingfplus2.setting.Setting;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class Module extends Configurable {
    private final String name;
    private final String description;
    private final Category category;
    private final Keybind keybind = new Keybind("Keybind", Keyboard.KEY_NONE);
    private final Setting<Boolean> drawn = new Setting<>("Drawn", true);
    private boolean toggled = false;

    public Module(String name, String description, Category category) {
        this(name, description, category, Keyboard.KEY_NONE);
    }

    public Module(String name, String description, Category category, int code) {
        this.name = name;
        this.description = description;
        this.category = category;

        keybind.setValue(code);

        settings.add(keybind);
        settings.add(drawn);
    }

    public String getDisplayInfo() {
        return null;
    }

    public String getFullDisplay() {
        String display = getName();
        if (getDisplayInfo() != null) {
            display += (" " + ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + getDisplayInfo() + ChatFormatting.GRAY + "]");
        }
        return display;
    }

    public void onUpdate() {

    }

    public void onRender2D() {

    }

    public void onRender3D() {

    }

    protected void onEnable() {

    }

    protected void onDisable() {

    }

    public boolean isOn() {
        return this.toggled;
    }

    public boolean isOff() {
        return !this.toggled;
    }

    public void toggle(boolean silent) {
        toggled = !toggled;
        if (toggled) {
            MinecraftForge.EVENT_BUS.register(this);
            onEnable();
        } else {
            MinecraftForge.EVENT_BUS.unregister(this);
            onDisable();
        }
        if (!silent) {
            MinecraftForge.EVENT_BUS.post(new ModuleToggleEvent(this));
        }
    }

    public boolean isDrawn() {
        return drawn.getValue();
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        if (toggled && !this.isOn()) {
            this.toggle(false);
        }
        if (!toggled && this.isOn()) {
            this.toggle(false);
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public int getKeybind() {
        return keybind.getValue();
    }

    public void setKeybind(int code) {
        keybind.setValue(code);
    }

    public enum Category {
        Combat, Movement, Player, Misc, Render, Chat, Client, Exploit;

        public static int amountPerCategory(Category category) {
            List<Module> categoryModules = new ArrayList<>();
            for (Module module : FencingFPlus2.INSTANCE.moduleManager.getModules()) {
                if (module.getCategory().equals(category)) {
                    categoryModules.add(module);
                }
            }
            return categoryModules.size();
        }

        public static Category getCategoryFromString(String id) {
            Category finalCategory = null;
            for (Category category : Category.values()) {
                if (category.toString().equalsIgnoreCase(id)) {
                    finalCategory = category;
                    break;
                }
            }
            return finalCategory;
        }
    }

}
