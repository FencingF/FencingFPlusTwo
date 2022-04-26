package org.fenci.fencingfplus2.features.module.modules.player;

import net.minecraft.util.EnumHand;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.Timer;

public class AntiAFK extends Module {
    public AntiAFK() {
        super("AntiAFK", "Tries to prevent you from getting afk kicked", Category.Player);
    }

    public static final Setting<Float> timeout = new Setting<>("Delay", 20f, 0, 60);
    public static final Setting<Boolean> stats = new Setting<>("/Stats", true);
    public static final Setting<Boolean> swing = new Setting<>("Swing", false);
    public static final Setting<Boolean> rotate = new Setting<>("Rotate", false);
    public static final Setting<Boolean> afkMessage = new Setting<>("Message", false);

    Timer delay = new Timer();

    @Override
    public String getDisplayInfo() {
        return String.valueOf(delay.getTimePassed());
    }

    @Override
    public void onUpdate() {
        if (delay.hasReached(timeout.getValue().longValue() * 1000)) {
            if (stats.getValue()) {
                mc.player.sendChatMessage("/stats");
            }
            if (swing.getValue()) {
                mc.player.swingArm(EnumHand.MAIN_HAND);
            }
            if (rotate.getValue()) {
                mc.player.rotationYaw += 90;
            }
            if (afkMessage.getValue()) {
                mc.player.sendChatMessage("I am currently afk thanks to FencingF+2!");
            }
            delay.reset();
        }
    }
}
