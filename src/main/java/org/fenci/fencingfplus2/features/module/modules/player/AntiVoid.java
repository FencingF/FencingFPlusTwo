package org.fenci.fencingfplus2.features.module.modules.player;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.world.BlockUtil;

public class AntiVoid extends Module {
    public AntiVoid() {
        super("AntiVoid", "Doesn't let you die in the void no matter what.", Category.Player);
    }

    public static final Setting<ReturnMode> returnMode = new Setting<>("Mode", ReturnMode.Move);

    @Override
    public String getDisplayInfo() {
        return returnMode.getValue().name();
    }

    @Override
    public void onUpdate() {
        if (mc.player.posY < 1 && BlockUtil.getBlock(new BlockPos(mc.player.posX, 0, mc.player.posZ)).equals(Blocks.AIR)) {
            mc.player.motionY = 0;
            if (returnMode.getValue().equals(ReturnMode.None)) {
                if (mc.player.moveForward > 0) {
                    mc.player.motionY = 0.15;
                }
            }
            if (returnMode.getValue().equals(ReturnMode.Move)) {
                mc.player.motionY = 0.15;
            } else if (returnMode.getValue().equals(ReturnMode.Jump)) {
                mc.player.jump();
            }
        }
    }

    public enum ReturnMode {
        Move, Jump, None
    }
}
