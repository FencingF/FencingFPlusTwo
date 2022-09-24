package org.fenci.fencingfplus2.features.module.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.network.PacketEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.MathUtil;
import org.fenci.fencingfplus2.util.player.InventoryUtil;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Aura extends Module {
    public static final Setting<Boolean> weaponsCheck = new Setting<>("WeaponsCheck", true);
    public static final Setting<Boolean> delay = new Setting<>("Delay", true);
    public static final Setting<Boolean> players = new Setting<>("Players", true);
    public static final Setting<Boolean> hostileMobs = new Setting<>("HostileMobs", true);
    public static final Setting<Boolean> passiveMobs = new Setting<>("PassiveMobs", true);
    public static final Setting<Boolean> rotate = new Setting<>("Rotate", true);
    public static final Setting<Switch> switchSetting = new Setting<>("Switch", Switch.Normal);
    public static final Setting<Integer> range = new Setting<>("Range", 4, 1, 10);
    public static final Setting<OnDisableSwitch> onDisableSwitch = new Setting<>("OnDisableSwitch", OnDisableSwitch.None);
    boolean isRotating;
    private Item disableItem;
    private float pitch = 0.0f;
    private float yaw = 0.0f;

    public Aura() {
        super("Aura", "Automatically hits people with your sword", Category.Combat);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.player.isDead) return;

        List<Entity> targets = mc.world.loadedEntityList.stream()
                .filter(entity -> entity != mc.player)
                .filter(entity -> mc.player.getDistance(entity) <= range.getValue())
                .filter(entity -> !entity.isDead)
                .filter(this::attackCheck)
                .sorted(Comparator.comparing(s -> mc.player.getDistance(s)))
                .collect(Collectors.toList());

        targets.forEach(this::attack);
    }

    @Override
    public void onEnable() {
        if (switchSetting.getValue().equals(Switch.Normal)) {
            InventoryUtil.switchTo(Items.WOODEN_SWORD);
            InventoryUtil.switchTo(Items.STONE_SWORD);
            InventoryUtil.switchTo(Items.GOLDEN_SWORD);
            InventoryUtil.switchTo(Items.IRON_SWORD);
            InventoryUtil.switchTo(Items.DIAMOND_SWORD);
        }
    }

    @Override
    public void onDisable() {
        InventoryUtil.switchTo(getDisableItem());
        this.isRotating = false;
    }

    public void attack(Entity entity) {
        if (!getFencing().friendManager.isFriend(entity.getUniqueID())) {
            rotateTo(entity);
            if (weaponsCheck.getValue() && !switchSetting.getValue().equals(Switch.Silent)) {
                if ((mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD) || (mc.player.getHeldItemMainhand().getItem() == Items.IRON_SWORD) || (mc.player.getHeldItemMainhand().getItem() == Items.GOLDEN_SWORD) || (mc.player.getHeldItemMainhand().getItem() == Items.STONE_SWORD) || (mc.player.getHeldItemMainhand().getItem() == Items.WOODEN_SWORD) && !switchSetting.getValue().equals(Switch.Silent)) {
                    if (delay.getValue()) {
                        if (mc.player.getCooledAttackStrength(0) >= 1) {
                            mc.playerController.attackEntity(mc.player, entity);
                            mc.player.swingArm(EnumHand.MAIN_HAND);
                        }
                    }
                    if (!delay.getValue()) {
                        mc.playerController.attackEntity(mc.player, entity);
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                    }
                }
            } else {
                if (delay.getValue()) {
                    if (mc.player.getCooledAttackStrength(0) >= 1) {
                        mc.playerController.attackEntity(mc.player, entity);
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                    }
                }
                if (!delay.getValue()) {
                    mc.playerController.attackEntity(mc.player, entity);
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                }
            }
            /*
            if(switchSetting.getValue().equals(Switch.Silent)){
                mc.player.connection.sendPacket(new CPacketHeldItemChange(findSword()));
                for (int i = 0; i < packets.getValue(); i++) {
                    if(delay.getValue()) {
                        if(timer.hasReached(8000, true))
                        mc.playerController.attackEntity(mc.player, entity);
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                    }
                }
                mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
            }
             */

            //lol idk why I tried to do that
        }

    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (rotate.getValue() && event.getPacket() instanceof CPacketPlayer && isRotating) {
            CPacketPlayer packet = event.getPacket();
            packet.yaw = this.yaw;
            packet.pitch = this.pitch;
            isRotating = false;
        }
    }

    private boolean attackCheck(Entity entity) {

        if (players.getValue() && entity instanceof EntityPlayer) {
            if (((EntityPlayer) entity).getHealth() > 0) {
                return true;
            }
        }

        if (passiveMobs.getValue() && entity instanceof EntityAnimal) {
            return !(entity instanceof EntityTameable);
        }
        return hostileMobs.getValue() && entity instanceof EntityMob;
    }

    public Item getDisableItem() {
        if (onDisableSwitch.getValue() == OnDisableSwitch.Crystals) {
            disableItem = Items.END_CRYSTAL;
        }
        if (onDisableSwitch.getValue() == OnDisableSwitch.Gapples) {
            disableItem = Items.GOLDEN_APPLE;
        }
        if (onDisableSwitch.getValue() == OnDisableSwitch.None) {
            disableItem = mc.player.getHeldItemMainhand().getItem();
        }
        return disableItem;
    }

    public void rotateTo(Entity entity) {
        if (rotate.getValue()) {
            float[] angle = MathUtil.calcAngle(Aura.mc.player.getPositionEyes(mc.getRenderPartialTicks()), entity.getPositionVector());
            this.yaw = angle[0];
            this.pitch = angle[1];
            isRotating = true;
        }
    }

    public enum Switch {
        Normal, Silent, Off
    }

    public enum OnDisableSwitch {
        Crystals, Gapples, None
    }
}
