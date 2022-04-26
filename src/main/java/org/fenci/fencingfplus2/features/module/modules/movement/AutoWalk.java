package org.fenci.fencingfplus2.features.module.modules.movement;

import net.minecraft.client.settings.KeyBinding;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;

public class AutoWalk extends Module {

    public static AutoWalk INSTANCE; //LOL

    public AutoWalk() {
        super("AutoWalk", "Automatically walks", Category.Movement);
        INSTANCE = this;
    }

    public static final Setting<Boolean> sprint = new Setting<>("Sprint", true);

    @Override
    public void onUpdate() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
        if (sprint.getValue()) KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
    }

    @Override
    public void onDisable() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
    }
}
