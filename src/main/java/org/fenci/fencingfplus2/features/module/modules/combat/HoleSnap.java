package org.fenci.fencingfplus2.features.module.modules.combat;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.ClientMessage;
import org.fenci.fencingfplus2.util.world.HoleUtil;

public class HoleSnap extends Module {
    public HoleSnap() {
        super("HoleSnap", "Teleports you into holes", Category.Combat);
    }

    public static final Setting<Float> range = new Setting<>("Range", 2f, 1, 10);

    @Override
    public void onEnable() {
        BlockPos nearestHole = HoleUtil.getClosestHole(mc.player, range.getValue());
        if (nearestHole != null) {
            mc.player.setVelocity(0.0D, 0.0D, 0.0D);
            mc.player.setPosition(Math.floor(nearestHole.getX()) + 0.5, nearestHole.getY(), Math.floor(nearestHole.getZ()) + 0.5);
            mc.player.connection.sendPacket(new CPacketPlayer.Position(Math.floor(nearestHole.getX()) + 0.5, nearestHole.getY(), Math.floor(nearestHole.getZ()) + 0.5, true));
            this.toggle(true);
        } else {
            ClientMessage.sendModuleMessage("HoleSnap", "Could not find hole, disabling.");
            this.toggle(true);
        }
    }
}
