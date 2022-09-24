package org.fenci.fencingfplus2.features.module.modules.movement;

import net.minecraft.client.settings.KeyBinding;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;

public class Sprint extends Module {
    public static final Setting<Mode> mode = new Setting<>("Mode", Mode.Legit);

    public Sprint() {
        super("Sprint", "Automatically sprints for you", Category.Movement);
    }

    @Override
    public String getDisplayInfo() {
        return mode.getValue().name();
    }

    @Override
    public void onUpdate() {
        if (!mc.player.isSprinting()) {
            switch (mode.getValue()) {
                case Rage:
                case Always: {
                    mc.player.setSprinting(mc.gameSettings.keyBindForward.isKeyDown() || mode.getValue() == Mode.Always);
                    break;
                }

                case Legit: {
                    if (mc.player.isSneaking() || mc.player.getFoodStats().getFoodLevel() <= 6 || mc.player.collidedHorizontally || mc.player.isHandActive()) {
                        break;
                    }

                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
                    break;
                }
            }
        }
    }

    public enum Mode {
        /**
         * If to always sprint
         */
        Rage,

        /**
         * If to sprint in all directions, yet non-strict
         */
        Always,

        /**
         * More checks to check if you are able to sprint. Eg hunger above 6, not colliding with a wall, not eating, etc
         */
        Legit
    }
}
