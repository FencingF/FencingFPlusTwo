package org.fenci.fencingfplus2.features.module.modules.movement;

import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.Globals;

public class ReverseStep extends Module {
    public ReverseStep() {
        super("ReverseStep", "Tps you down blocks", Category.Movement);
    }

    public static final Setting<Double> speed = new Setting<>("Speed", 1.0, 0.1, 5.0);
    public static final Setting<Double> height = new Setting<>("Height", 2.0, 1.0, 6.0);

    @Override
    public void onUpdate() {
        if (Globals.mc.player.onGround && !Globals.mc.player.isOnLadder() && !Globals.mc.player.isInWater() && !Globals.mc.player.isInLava()) {
            for (double y = 0.0; y <= height.getValue() + 0.5; y += 0.1) {
                if (!Globals.mc.world.getCollisionBoxes(Globals.mc.player, Globals.mc.player.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty()) {
                    Globals.mc.player.motionY -= speed.getValue();
                }
            }
        }
    }
}