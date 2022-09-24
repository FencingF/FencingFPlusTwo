package org.fenci.fencingfplus2.features.module.modules.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.fenci.fencingfplus2.events.network.PacketEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.ClientMessage;
import org.fenci.fencingfplus2.util.client.MathUtil;

import java.util.HashSet;
import java.util.Set;

public class Animations extends Module {

    public static final Setting<Boolean> crouch = new Setting<>("Crouch", true);
    public static final Setting<Boolean> noLimbSwing = new Setting<>("NoLimbSwing", true);
    public static final Setting<Boolean> noCrystalRotation = new Setting<>("NoCrystalRotation", false);
    public static final Setting<Boolean> realTime = new Setting<>("RealTime", false);
    public static final Setting<BigEars> mau5Ears = new Setting<>("DeadMau5Ears", BigEars.Self);
    public static Animations INSTANCE;

    public Animations() {
        super("Animations", "Allows for custom animations", Category.Render);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (crouch.getValue()) {
            for (EntityPlayer player : mc.world.playerEntities) {
                if (player.equals(mc.player)) continue;
                player.setSneaking(true);
            }
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
        if (noCrystalRotation.getValue()) {
            for (Entity entity : mc.world.loadedEntityList) {
                if (!(entity instanceof EntityEnderCrystal)) continue;
                EntityEnderCrystal crystal = (EntityEnderCrystal) entity;
                crystal.rotationPitch = 0;
                crystal.rotationYaw = 0;
                crystal.prevRotationPitch = 0;
                crystal.prevRotationYaw = 0;
                crystal.innerRotation = 0;
            }
        }
        if (realTime.getValue() && fullNullCheck()) {
            mc.world.setWorldTime((long) ((MathUtil.getRealLifeTimeInSeconds() / 3.6) - 6000)); //43200 is the amount of seconds in 12 hours
            //ClientMessage.sendOverwriteClientMessage(String.valueOf((MathUtil.getRealLifeTimeInSeconds() / 3.6) - 6000));
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketTimeUpdate && realTime.getValue() && fullNullCheck()) {
            event.setCanceled(true);
        }
    }

    public Set<EntityPlayer> getPlayersToGiveEars() {
        Set<EntityPlayer> playersThatNeedEars = new HashSet<>();
        for (EntityPlayer player : mc.world.playerEntities) {
            if (mau5Ears.getValue().equals(BigEars.Off)) continue;
            if (!player.equals(mc.player) && mau5Ears.getValue().equals(BigEars.Self)) continue;
            if (!getFencing().friendManager.isFriend(player.entityUniqueID) && mau5Ears.getValue().equals(BigEars.Friends))
                continue;
            playersThatNeedEars.add(player);
        }
        return playersThatNeedEars;
    }

    public enum BigEars {
        All, Self, Friends, Off
    }
}
