package org.fenci.fencingfplus2.features.module.modules.client;

import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.gui.ClickGUIScreen;
import org.lwjgl.input.Keyboard;

public class HUDEditor extends Module {

    public static HUDEditor INSTANCE;

    public HUDEditor() {
        super("HUDEditor", "Edit HUD", Category.Client, Keyboard.KEY_PERIOD);
        INSTANCE = this;
    }

    @Override
    protected void onEnable() {
        if (!fullNullCheck()) {
            this.setToggled(false);
            return;
        }
        mc.displayGuiScreen(ClickGUIScreen.getEDITORINSTANCE());
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
