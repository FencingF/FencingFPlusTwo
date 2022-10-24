package org.fenci.fencingfplus2.features.module.modules.movement;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.network.PacketEvent;
import org.fenci.fencingfplus2.events.player.PushEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;

public class Velocity extends Module {

    public static final Setting<Float> horizontal = new Setting<>("Horizontal % ", 0.0f, 0.0f, 100.0f);
    public static final Setting<Float> vertical = new Setting<>("Vertical % ", 0.0f, 0.0f, 100.0f);
    public static final Setting<Boolean> blocks = new Setting<>("Blocks", true);
    public static final Setting<Boolean> entities = new Setting<>("Entities", true);
    public static final Setting<Boolean> water = new Setting<>("Water", true);
    public static final Setting<Boolean> ice = new Setting<>("Ice", true);
    public static final Setting<Boolean> fishingRods = new Setting<>("FishingRods", true);

    public Velocity() {
        super("Velocity", "Allows you to change your knockback", Category.Movement);
    }

    @Override
    public String getDisplayInfo() {
        return "H: " + horizontal.getValue() + ", V: " + vertical.getValue();
    }

    @Override
    public void onUpdate() {
        if (ice.getValue()) {
            Blocks.ICE.setDefaultSlipperiness(0.6f);
            Blocks.FROSTED_ICE.setDefaultSlipperiness(0.6f);
            Blocks.PACKED_ICE.setDefaultSlipperiness(0.6f);
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketExplosion) {
            if (horizontal.getValue() == 0.0f && vertical.getValue() == 0.0f) {
                event.setCanceled(true);
                return;
            }

            SPacketExplosion packet = event.getPacket();

            packet.motionX *= horizontal.getValue() / 100f;
            packet.motionY *= vertical.getValue() / 100f;
            packet.motionZ *= horizontal.getValue() / 100f;

        } else if (event.getPacket() instanceof SPacketEntityVelocity) {
            SPacketEntityVelocity packet = event.getPacket();
            if (packet.getEntityID() != mc.player.entityId) return;
            if (horizontal.getValue() == 0.0f && vertical.getValue() == 0.0f) {
                event.setCanceled(true);
                return;
            }

            packet.motionX *= horizontal.getValue() / 100f;
            packet.motionY *= vertical.getValue() / 100f;
            packet.motionZ *= horizontal.getValue() / 100f;
        }
        if (event.getPacket() instanceof SPacketEntityStatus && fishingRods.getValue()) {
            SPacketEntityStatus packet = event.getPacket();
            Entity entity = packet.getEntity(mc.world);
            if (!(entity instanceof EntityFishHook)) return;
            EntityFishHook entityFishHook = (EntityFishHook) entity;
            if (packet.getOpCode() == 31 && entity.equals(mc.player) && entityFishHook.caughtEntity.equals(mc.player)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPush(PushEvent event) {
        if (blocks.getValue() && event.getType() == 0) {
            event.setCanceled(true);
        }
        if (entities.getValue() && event.getType() == 1) {
            event.setCanceled(true);
        }
        if (water.getValue() && event.getType() == 2) {
            event.setCanceled(true);
        }
    }

    @Override
    public void onDisable() {
        if (ice.getValue()) {
            Blocks.ICE.setDefaultSlipperiness(0.98f);
            Blocks.FROSTED_ICE.setDefaultSlipperiness(0.98f);
            Blocks.PACKED_ICE.setDefaultSlipperiness(0.98f);
        }
    }
}
