package org.fenci.fencingfplus2.features.module.modules.movement;


import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;

public class ReverseStep extends Module {
    public static final Setting<Double> speed = new Setting<>("Speed", 1.0, 0.1, 5.0);
    public static final Setting<Double> height = new Setting<>("Height", 2.0, 1.0, 6.0);

    public ReverseStep() {
        super("ReverseStep", "Teleports you down blocks", Category.Movement);
    }

    @Override
    public void onUpdate() {
        if (mc.player.onGround && !mc.player.isOnLadder() && !mc.player.isInWater() && !mc.player.isInLava()) {
            for (double y = 0.0; y <= height.getValue() + 0.5; y += 0.1) {
                if (!mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty()) {
                    mc.player.motionY -= speed.getValue();
                }
            }
        }
    }
}