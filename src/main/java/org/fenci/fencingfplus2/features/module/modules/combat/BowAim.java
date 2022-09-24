package org.fenci.fencingfplus2.features.module.modules.combat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import org.fenci.fencingfplus2.features.module.Module;

import java.util.Objects;

import static org.fenci.fencingfplus2.util.player.PlayerUtil.findClosestTarget;

public class BowAim extends Module {
    public BowAim() {
        super("BowAim", "Automatically aims at people", Category.Combat);
    }

    public static float[] rotateToPlayer(EntityPlayer entity) {
        final double xDelta = entity.posX - entity.lastTickPosX;
        final double zDelta = entity.posZ - entity.lastTickPosZ;
        double d = mc.player.getDistance(entity);
        d -= d % 0.8;
        double xMulti;
        double zMulti;
        final boolean sprint = entity.isSprinting();
        xMulti = d / 0.8 * xDelta * (sprint ? 1.25 : 1.0);
        zMulti = d / 0.8 * zDelta * (sprint ? 1.25 : 1.0);
        final double x = entity.posX + xMulti - mc.player.posX;
        final double z = entity.posZ + zMulti - mc.player.posZ;
        final double y = mc.player.posY + mc.player.getEyeHeight() - (entity.posY + entity.getEyeHeight());
        final double dist = mc.player.getDistance(entity);
        final float yaw = (float) Math.toDegrees(Math.atan2(z, x)) - 90.0f;
        final float pitch = (float) Math.toDegrees(Math.atan2(y, dist));
        return new float[]{yaw, pitch};
    }

    @Override
    public void onUpdate() {
        if (mc.player.getHeldItemMainhand().getItem() == Items.BOW && mc.player.isHandActive() && findClosestTarget() != null) {
            mc.player.rotationYaw = rotateToPlayer(Objects.requireNonNull(findClosestTarget()))[0];
            mc.player.rotationPitch = rotateToPlayer(Objects.requireNonNull(findClosestTarget()))[1];
        }
    }
}
