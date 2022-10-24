package org.fenci.fencingfplus2.features.module.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.network.PacketEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;

public class Criticals extends Module {
    public static final Setting<Mode> mode = new Setting<>("Mode", Mode.Strict);
    public static final Setting<Boolean> entityCheck = new Setting<>("EntityCheck", true);

    public Criticals() {
        super("Criticals", "Scores critical hits", Category.Combat);
    }

    @Override
    public String getDisplayInfo() {
        return mode.getValue().name();
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketUseEntity) {
            if (mc.player.isInLava() || mc.player.isInWater() || !mc.player.onGround || mc.player.isOnLadder()) {
                return;
            }

            CPacketUseEntity packet = event.getPacket();
            if (packet.getAction() != CPacketUseEntity.Action.ATTACK) {
                return;
            }

            Entity entity = packet.getEntityFromWorld(mc.world);
            if (entity == null || entity == mc.player || (entityCheck.getValue() && !(entity instanceof EntityLivingBase))) {
                return;
            }

            if (mode.getValue() == Mode.Jump) {
                mc.player.jump();
            } else if (mode.getValue() == Mode.MiniJump) {
                mc.player.motionY = 0.2;
            }

            for (double offset : mode.getValue().positions) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + offset, mc.player.posZ, false));
            }

            // return to original position
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
        }
    }

    public enum Mode {
        /**
         * Normal, packet critical hit
         */
        Packet(0.3),

        /**
         * Jumps upwards to crit
         */
        Jump(0.98),

        /**
         * Jumps up slightly to crit
         */
        MiniJump(0.18),

        /**
         * Bypasses NCP-Updated
         */
        Strict(0.062602401692772, 0.0726023996066094);

        private final double[] positions;

        Mode(double... positions) {
            this.positions = positions;
        }
    }
}
