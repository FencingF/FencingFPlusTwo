package org.fenci.fencingfplus2.features.module.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.network.PacketEvent;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.Globals;

public class Criticals extends Module {
    public Criticals() {
        super("Criticals", "Scores critical hits", Category.Combat);
    }

    public static final Setting<Mode> mode = new Setting<>("Mode", Mode.Strict);
    public static final Setting<Boolean> entityCheck = new Setting<>("EntityCheck", true);

    @Override
    public String getDisplayInfo() {
        return mode.getValue().name();
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketUseEntity) {
            if (Globals.mc.player.isInLava() || Globals.mc.player.isInWater() || !Globals.mc.player.onGround || Globals.mc.player.isOnLadder()) {
                return;
            }

            CPacketUseEntity packet = event.getPacket();
            if (packet.getAction() != CPacketUseEntity.Action.ATTACK) {
                return;
            }

            Entity entity = packet.getEntityFromWorld(Globals.mc.world);
            if (entity == null || entity == Globals.mc.player || (entityCheck.getValue() && !(entity instanceof EntityLivingBase))) {
                return;
            }

            if (mode.getValue() == Mode.Jump) {
                Globals.mc.player.jump();
            } else if (mode.getValue() == Mode.MiniJump) {
                Globals.mc.player.motionY = 0.2;
            }

            for (double offset : mode.getValue().positions) {
                Globals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Globals.mc.player.posX, Globals.mc.player.posY + offset, Globals.mc.player.posZ, false));
            }

            // return to original position
            Globals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Globals.mc.player.posX, Globals.mc.player.posY, Globals.mc.player.posZ, false));
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
