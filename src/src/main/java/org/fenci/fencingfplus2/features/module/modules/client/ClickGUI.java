package org.fenci.fencingfplus2.features.module.modules.client;

import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.gui.ClickGUIScreen;
import org.fenci.fencingfplus2.setting.Setting;
import org.lwjgl.input.Keyboard;

public class ClickGUI extends Module {

    //old color 245, 34, 34
    //purple color 108, 0, 255
    //light blue 57, 236, 255

    //nice color i found 96, 194, 255
    public static final Setting<Integer> getred = new Setting<>("OutlineRed", 96, 0, 255);
    public static final Setting<Integer> getgreen = new Setting<>("OutlineGreen", 194, 0, 255);
    public static final Setting<Integer> getblue = new Setting<>("OutlineBlue", 255, 0, 255);
    public static final Setting<Integer> getAlpha = new Setting<>("OutlineAlpha", 255, 0, 255);
    public static final Setting<Integer> insidered = new Setting<>("InsideRed", 96, 0, 255);
    public static final Setting<Integer> insidegreen = new Setting<>("InsideGreen", 194, 0, 255);
    public static final Setting<Integer> insideblue = new Setting<>("InsideBlue", 255, 0, 255);
    public static final Setting<Integer> insideAlpha = new Setting<>("InsideAlpha", 255, 0, 255);
    public static final Setting<Integer> backgroundred = new Setting<>("BackgroundRed", 45, 0, 255);
    public static final Setting<Integer> backgroundgreen = new Setting<>("BackgroundGreen", 45, 0, 255);
    public static final Setting<Integer> backgroundblue = new Setting<>("BackgroundBlue", 45, 0, 255);
    public static final Setting<Integer> backgroundalpha = new Setting<>("BackgroundAlpha", 255, 0, 255);
    public static final Setting<Boolean> brackets = new Setting<>("Brackets", true);
    public static final Setting<Boolean> bracketsKey = new Setting<>("BracketKey", true);
    public static final Setting<Boolean> moduleCount = new Setting<>("ModuleCount", true);
    public static final Setting<Float> lineWidth = new Setting<>("LineWidth", 3.7f, 0.1f, 10f);
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
            this.setToggled(false);
            return;
        }

        mc.displayGuiScreen(ClickGUIScreen.getGUIINSTANCE());
    }

    @Override
    protected void onDisable() {
        if (fullNullCheck()) {
            mc.displayGuiScreen(null);
        }
    }

    @Override
    public void onUpdate() {
        if (mc.currentScreen == null) {
            this.setToggled(false);
        }
    }
}
