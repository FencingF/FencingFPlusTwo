package org.fenci.fencingfplus2.features.module.modules.misc;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.client.OptionChangeEvent;
import org.fenci.fencingfplus2.events.player.UpdateWalkingPlayerEvent;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FakePlayer extends Module {


    public static final Setting<Boolean> pops = new Setting<>("Totem Pop", true);
    public static final Setting<Boolean> move = new Setting<>("Move", false);
    public static final Setting<Float> moveSpeed = new Setting<>("MoveSpeed", 1f, 0.1f, 10f);
    public static final Setting<Boolean> record = new Setting<>("Record", false);
    public static final Setting<Boolean> playRecording = new Setting<>("Play Recording", false);
    public static final Setting<Boolean> resetRecording = new Setting<>("Reset Recording", false);
    public static FakePlayer INSTANCE;
    public EntityOtherPlayerMP fakePlayer;
    BlockPos startPos;

    public FakePlayer() {
        super("FakePlayer", "Makes a clientside player", Category.Misc);
        INSTANCE = this;
    }

    Timer delayTimer = new Timer();
    public List<Location> playerLocations = new ArrayList<>();

    @Override
    public void onEnable() {
        if (mc.player == null || mc.player.isDead) {
            this.toggle(true);
            return;
        }

        GameProfile profile = new GameProfile(UUID.fromString("87f6fc57-7032-4977-997e-53a075ef8000"), "TheInfInventor");
        fakePlayer = new EntityOtherPlayerMP(FakePlayer.mc.world, profile);
        fakePlayer.copyLocationAndAnglesFrom(FakePlayer.mc.player);
        fakePlayer.rotationYawHead = FakePlayer.mc.player.rotationYawHead;
        fakePlayer.inventory.copyInventory(FakePlayer.mc.player.inventory);
        startPos = mc.player.getPosition();
        mc.world.addEntityToWorld(-1234, fakePlayer);
    }

    List<BlockPos> positions = new ArrayList<>();

    @Override
    public void onUpdate() {
        if (fakePlayer.isDead) fakePlayer.respawnPlayer();
        if (pops.getValue()) {
            fakePlayer.setHeldItem(EnumHand.OFF_HAND, new ItemStack(Items.TOTEM_OF_UNDYING));
            if (fakePlayer.getHealth() <= 0) {
                fakePop(fakePlayer);
                fakePlayer.setHealth(20);
            }

        }
        if (!move.getValue()) return;

        if (resetRecording.getValue()) {
            positions.clear();
            resetRecording.setValue(false);
        }
    }

    @Override
    public void onDisable() {
        if (mc.world != null) {
            mc.world.removeEntityFromWorld(-1234);
        }
    }

    private void fakePop(Entity entity) {
        mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.TOTEM, 30);
        mc.world.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ITEM_TOTEM_USE, entity.getSoundCategory(), 1.0F, 1.0F, false);
        fakePlayer.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 900, 1));
        fakePlayer.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 100, 1));
    }

    @SubscribeEvent
    public void onUpdateWalking(UpdateWalkingPlayerEvent event) {
        if (record.getValue()) {
            playerLocations.add(new Location(mc.player.getPosition(), mc.player.limbSwing, mc.player.limbSwingAmount, mc.player.prevLimbSwingAmount, mc.player.rotationYawHead, mc.player.prevRotationYawHead));
        }
        if (playRecording.getValue() && !playerLocations.isEmpty()) {
            for (Location location : playerLocations) {
                fakePlayer.setPosition(location.getPosition().getX(), location.getPosition().getY(), location.getPosition().getZ());
                fakePlayer.limbSwing = location.getLimbSwing();
                fakePlayer.limbSwingAmount = location.getLimbSwingAmount();
                fakePlayer.prevLimbSwingAmount = location.getPrevLimbSwingAmount();
                fakePlayer.rotationYawHead = location.getRotationYawHead();
                fakePlayer.prevRotationYawHead = location.getPrevRotationYawHead();
            }
        }
    }

    @SubscribeEvent
    public void onOption(OptionChangeEvent event) {
        if (event.getOption().equals(record) && record.getValue()) {
            playRecording.setValue(false);
        }
        if (event.getOption().equals(playRecording) && playRecording.getValue()) {
            record.setValue(false);
        }
    }

    public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        float doubleSize = 12.0F;
        double size = entity.getDistance(posX, posY, posZ) / (double) doubleSize;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        double value = (1.0D - size) * blockDensity;
        float damage = (float) ((int) ((value * value + value) / 2.0D * 7.0D * (double) doubleSize + 1.0D));
        double finalDamage = 1.0D;

        if (entity instanceof EntityLivingBase) {
            finalDamage = getBlastReduction((EntityLivingBase) entity, getMultipliedDamage(damage), new Explosion(mc.world, null, posX, posY, posZ, 6.0F, false, true));
        }

        return (float) finalDamage;
    }

    public static float getBlastReduction(final EntityLivingBase entity, final float damageI, final Explosion explosion) {
        float damage = damageI;
        if (entity instanceof EntityPlayer) {
            final EntityPlayer ep = (EntityPlayer) entity;
            final DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float) ep.getTotalArmorValue(), (float) ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            int k = 0;
            try {
                k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            } catch (Exception ignored) {
            }
            final float f = MathHelper.clamp((float) k, 0.0f, 20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.isPotionActive(MobEffects.RESISTANCE)) {
                damage -= damage / 4.0f;
            }
            damage = Math.max(damage, 0.0f);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }

    private static float getMultipliedDamage(float damage) {
        return damage * (mc.world.getDifficulty().getId() == 0 ? 0.0F : (mc.world.getDifficulty().getId() == 2 ? 1.0F : (mc.world.getDifficulty().getId() == 1 ? 0.5F : 1.5F)));
    }

    public static class Location {
        private final BlockPos position;
        private final float limbSwing;
        private final float limbSwingAmount;
        private final float prevLimbSwingAmount;
        private final float rotationYawHead;
        private final float prevRotationYawHead;

        public Location(BlockPos position, float limbSwing, float limbSwingAmount, float prevLimbSwingAmount, float rotationYawHead, float prevRotationYawHead) {
            this.position = position;
            this.limbSwing = limbSwing;
            this.limbSwingAmount = limbSwingAmount;
            this.prevLimbSwingAmount = prevLimbSwingAmount;
            this.rotationYawHead = rotationYawHead;
            this.prevRotationYawHead = prevRotationYawHead;
        }

        public BlockPos getPosition() {
            return position;
        }

        public float getLimbSwing() {
            return limbSwing;
        }

        public float getLimbSwingAmount() {
            return limbSwingAmount;
        }

        public float getPrevLimbSwingAmount() {
            return prevLimbSwingAmount;
        }

        public float getRotationYawHead() {
            return rotationYawHead;
        }

        public float getPrevRotationYawHead() {
            return prevRotationYawHead;
        }
    }
}
