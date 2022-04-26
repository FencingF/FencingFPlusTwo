package org.fenci.fencingfplus2.features.module.modules.render;

import net.minecraft.entity.player.EntityPlayer;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;

public class Animations extends Module {
    public Animations() {
        super("Animations", "Allows for custom animations", Category.Render);
    }

    public static final Setting<Boolean> crouch = new Setting<>("Crouch", true);
    public static final Setting<Boolean> noLimbSwing = new Setting<>("NoLimbSwing", true);


    @Override
    public void onUpdate() {
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player.equals(mc.player)) continue;
            player.setSneaking(crouch.getValue());
        }
    }

    @Override
    public void onRender3D() {
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player.equals(mc.player)) continue;
            if (noLimbSwing.getValue()) {
                player.limbSwing = 0;
                player.limbSwingAmount = 0;
                player.prevLimbSwingAmount = 0;
            }
        }
    }
}
