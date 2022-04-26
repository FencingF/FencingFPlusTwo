package org.fenci.fencingfplus2.features.module.modules.movement;

import net.minecraft.client.settings.KeyBinding;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.Globals;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", "Automatically sprints for you", Category.Movement);
    }

    public static final Setting<Mode> mode = new Setting<>("Mode", Mode.Legit);

    @Override
    public String getDisplayInfo() {
        return mode.getValue().name();
    }

    @Override
    public void onUpdate() {
        if (!Globals.mc.player.isSprinting()) {
            switch (mode.getValue()) {
                case Rage:
                case Always: {
                    Globals.mc.player.setSprinting(Globals.mc.gameSettings.keyBindForward.isKeyDown() || mode.getValue() == Mode.Always);
                    break;
                }

                case Legit: {
                    if (Globals.mc.player.isSneaking() || Globals.mc.player.getFoodStats().getFoodLevel() <= 6 || Globals.mc.player.collidedHorizontally || Globals.mc.player.isHandActive()) {
                        break;
                    }

                    KeyBinding.setKeyBindState(Globals.mc.gameSettings.keyBindSprint.getKeyCode(), true);
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
