package org.fenci.fencingfplus2.features.module.modules.render;

import net.minecraft.entity.player.EntityPlayer;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;

public class PlayerTrails extends Module {
    public PlayerTrails() {
        super("PlayerTrails", "Cool render idk", Category.Render);
    }

    public static final Setting<Boolean> self = new Setting<>("Self", false);

    @Override
    public void onRender3D() {
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player.equals(mc.player) && !self.getValue()) continue;

        }
    }
}
