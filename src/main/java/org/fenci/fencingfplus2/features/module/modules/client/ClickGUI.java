package org.fenci.fencingfplus2.features.module.modules.client;

import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.gui.click.ClickGUIScreen;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.Globals;
import org.lwjgl.input.Keyboard;

public class ClickGUI extends Module {

    //old color 245, 34, 34
    //purple color 108, 0, 255
    //light blue 57, 236, 255
    public static final Setting<Integer> getred = new Setting<>("Red", 57, 0, 255);
    public static final Setting<Integer> getgreen = new Setting<>("Green", 236, 0, 255);
    public static final Setting<Integer> getblue = new Setting<>("Blue", 255, 0, 255);
    public static final Setting<Boolean> rainbow = new Setting<>("Rainbow", false);
    //public static final Setting<Integer> rainbowSpeed = new Setting<>("RainbowSpeed", 3, 0, 10);
    public static final Setting<Float> saturation = new Setting<>("Rainbow Saturation", 255f, 1, 255);
    public static final Setting<Float> brightness = new Setting<>("Rainbow Brightness", 255f, 0, 255);
    public static ClickGUI INSTANCE;

    public ClickGUI() {
        super("ClickGUI", "Shows this screen", Category.Client, Keyboard.KEY_K);
        INSTANCE = this;
    }

    @Override
    protected void onEnable() {
        if (!fullNullCheck()) {
            toggle(true);
            return;
        }

        Globals.mc.displayGuiScreen(ClickGUIScreen.getInstance());
    }

    @Override
    protected void onDisable() {
        if (fullNullCheck()) {
            Globals.mc.displayGuiScreen(null);
        }
    }

    @Override
    public void onUpdate() {
        if (Globals.mc.currentScreen == null) {
            toggle(true);
        }
    }
}
