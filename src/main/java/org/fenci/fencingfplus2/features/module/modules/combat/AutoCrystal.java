package org.fenci.fencingfplus2.features.module.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.fenci.fencingfplus2.events.network.PacketEvent;
import org.fenci.fencingfplus2.events.player.UpdateWalkingPlayerEvent;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.mixin.mixins.misc.ICPacketUseEntityMixin;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.MathUtil;
import org.fenci.fencingfplus2.util.client.Timer;
import org.fenci.fencingfplus2.util.math.CrystalUtil;
import org.fenci.fencingfplus2.util.player.InventoryUtil;
import org.fenci.fencingfplus2.util.player.PlayerUtil;
import org.fenci.fencingfplus2.util.render.RenderUtil;
import org.fenci.fencingfplus2.util.world.HoleUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.fenci.fencingfplus2.util.render.RenderUtil.generateBB;

public class AutoCrystal extends Module {

    public static AutoCrystal INSTANCE;

    public AutoCrystal() {
        super("AutoCrystal", "Automatically places and breaks crystals.", Category.Combat);
        INSTANCE = this;
    }

    //TODO: Multithreading, Motion-based/Ping based predictions

    //placing
    public static final Setting<Place> place = new Setting<>("Place", Place.Normal);
    public static final Setting<Float> placeRange = new Setting<>("PlaceRange", 4f, 0, 10);
    public static final Setting<Float> playerRange = new Setting<>("PlayerRange", 8f, 0, 30);
    public static final Setting<Integer> playerOverrideHealth = new Setting<>("PlayerOverrideHealth", 4, 1, 36);
    //public static final Setting<Float> placeWallRange = new Setting<>("PlaceWallRange", 5f, 0, 6);
    public static final Setting<Boolean> raytrace = new Setting<>("Raytrace", false);
    public static final Setting<Integer> minDamage = new Setting<>("MinDamage", 1, 0, 36);
    public static final Setting<Integer> maxLocalDamage = new Setting<>("MaxLocalDamage", 20, 0, 36);
    public static final Setting<Boolean> ignoreSelfDmg = new Setting<>("IgnoreSelfDmg", false);
    public static final Setting<Boolean> placeSwing = new Setting<>("PlaceSwing", true);
    public static final Setting<Float> placeDelay = new Setting<>("PlaceDelay", 0f, 0, 20);

    //breaking
    public static final Setting<Break> breakCrystal = new Setting<>("Break", Break.All);
    public static final Setting<Float> breakRange = new Setting<>("BreakRange", 5f, 0, 10);
    public static final Setting<Float> breakWallRange = new Setting<>("BreakWallRange", 5f, 0, 6);
    public static final Setting<Boolean> packetExplode = new Setting<>("PacketExplode", true);
    public static final Setting<Boolean> fastCrystal = new Setting<>("NoAntiCheat", false);
    public static final Setting<Boolean> antiStuck = new Setting<>("AntiStuck", true);
    public static final Setting<Integer> stuckAttempts = new Setting<>("StuckAttempts", 4, 1, 20);
    public static final Setting<BreakSwing> breakSwing = new Setting<>("BreakSwing", BreakSwing.Normal);
    public static final Setting<Integer> breakAttempts = new Setting<>("BreakAttempts", 1, 1, 5);
    public static final Setting<Float> breakSpeed = new Setting<>("BreakSpeed", 20f, 0, 20);
    public static final Setting<Weakness> antiWeakness = new Setting<>("AntiWeakness", Weakness.Normal);
    //public static final Setting<Float> antiWeaknessSpeed = new Setting<>("AntiWeaknessSpeed", 0f, 0, 10);
    public static final Setting<Boolean> threaded = new Setting<>("Threded", false);
    //public static final Setting<Integer> threads = new Setting<>("Threads", 2, 1, 5);

    //placing and breaking
    public static final Setting<Boolean> noDesync = new Setting<>("NoDesync", true);
    public static final Setting<Boolean> swordPause = new Setting<>("SwordPause", false);
    public static final Setting<Boolean> gapPause = new Setting<>("GapPause", false);
    //public static final Setting<Boolean> armor = new Setting<>("Armor", true);
    public static final Setting<Integer> popHealth = new Setting<>("PopHealth", 5, 1, 36);
    public static final Setting<Boolean> overrideIfPopable = new Setting<>("OverrideIfPopable", true);
    public static final Setting<Integer> minArmor = new Setting<>("MinArmorDamage", 20, 0, 100);
    public static final Setting<Boolean> antiSuicide = new Setting<>("AntiSuicide", true);
    public static final Setting<Integer> antiSuicideFactor = new Setting<>("AntiSuicideFactor", 3, 0, 20);
    public static final Setting<Boolean> predict = new Setting<>("Predict", true);
    public static final Setting<Boolean> rotate = new Setting<>("Rotate", true);
    public static final Setting<Logic> logic = new Setting<>("Logic", Logic.PlaceBreak);

    //misc
    public static final Setting<Switch> switchMode = new Setting<>("Switch", Switch.Normal);
    public static final Setting<Boolean> strongSwap = new Setting<>("StrongSwap", false);
    public static final Setting<Boolean> onePoint13 = new Setting<>("1.13+", false);

    //rendering
    public static final Setting<Boolean> render = new Setting<>("Render", true);
    public static final Setting<ExtraRender> extraRender = new Setting<>("ExtraRender", ExtraRender.Damage);
    public static final Setting<Integer> red = new Setting<>("Red", 57, 0, 255);
    public static final Setting<Integer> green = new Setting<>("Green", 236, 0, 255);
    public static final Setting<Integer> blue = new Setting<>("Blue", 255, 0, 255);
    public static final Setting<Integer> alpha = new Setting<>("Alpha", 50, 0, 255);

    boolean isInHole;
    boolean isRotating;
    private float pitch = 0.0f;
    private float yaw = 0.0f;
    private int hitTicks;
    private int placeTicks;
    public EntityPlayer target = null;

    private BlockPos renderPosition;

    private final ConcurrentHashMap<EntityEnderCrystal, Integer> attackedCrystals = new ConcurrentHashMap<>();
    private final List<BlockPos> placedCrystals = new ArrayList<>();
    private final List<Long> crystalsPerSecond = new ArrayList<>();
    //private final Map<BlockPos, EntityPlayer> damagesForPlayer = new HashMap<>(); //entity is player we are checking blockpos is the pos with the max damage for that player

    private final Timer clearTimer = new Timer();
    private final Timer cpsTimer = new Timer();

    //Timer weaknessTimer = new Timer();

    @Override
    public String getDisplayInfo() {
        if (target != null) {
            return target.getName();
        }
        return null;
    }

    @Override
    public void onEnable() {
        mc.playerController.syncCurrentPlayItem();
        crystalsPerSecond.clear();
    }

    @Override
    public void onDisable() {
        mc.playerController.syncCurrentPlayItem();
        crystalsPerSecond.clear();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        try {
            if (crystalsPerSecond.isEmpty()) return;
            crystalsPerSecond.removeIf(crystal -> cpsTimer.getTimePassed() > crystal + 1000);
        } catch (ConcurrentModificationException ignored) {}
    }

    @Override
    public void onUpdate() {
        if (clearTimer.hasReached(5L)){
            attackedCrystals.clear();
            placedCrystals.clear();
            clearTimer.reset();
        }
    }

    @SubscribeEvent
    public void onPlayerWalkingEvent(UpdateWalkingPlayerEvent event) {
        try {
            doAutoCrystal();
            hitTicks++;
            placeTicks++;
        } catch (NullPointerException ignored) {}
    }

    public void doAutoCrystal() {
        if (swordPause.getValue() && mc.player.getHeldItemMainhand().getItem() instanceof ItemSword || gapPause.getValue() && mc.player.getHeldItemMainhand().getItem().equals(Items.GOLDEN_APPLE)) return;
        target = getFinalTarget();
        //this.getAllDamages();
        if (target != null) {
            if (threaded.getValue()) {
                Threaded threaded = new Threaded();
                threaded.start();
            }
            if (logic.getValue().equals(Logic.PlaceBreak)) {
                if (!place.getValue().equals(Place.Off) && placeTicks > placeDelay.getValue()) placeCrystal();
                if (!breakCrystal.getValue().equals(Break.Off) && hitTicks > breakSpeed.getValue()) breakCrystal();
            } else {
                if (!breakCrystal.getValue().equals(Break.Off) && hitTicks > breakSpeed.getValue()) breakCrystal();
                if (!place.getValue().equals(Place.Off) && placeTicks > placeDelay.getValue()) placeCrystal();
            }
        }
    }

    // calculateDamageForPlayer returns damage and then getAllDamages adds each of those damages + the player to a hasmap then we compare which player has the highest damage

    public void placeCrystal() {
        BlockPos targetPosition;
        boolean hasSilentSwapped = false;

        targetPosition = calculateBlockForPlayer();

        if (targetPosition == null) {
            renderPosition = null;
            return;
        }

        int crystalSlot = InventoryUtil.getHotbarSlot(Items.END_CRYSTAL);
        int oldSlot = mc.player.inventory.currentItem;

        BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);

        BlockPos currentPos = pos.down();
        EnumFacing currentFace = EnumFacing.UP;

        Vec3d vec = new Vec3d(currentPos).add(0.5, 0.5, 0.5).add(new Vec3d(currentFace.getDirectionVec()).scale(0.5));

        float f = (float) (vec.x - (double) pos.getX());
        float f1 = (float) (vec.y - (double) pos.getY());
        float f2 = (float) (vec.z - (double) pos.getZ());

        if (!switchMode.getValue().equals(Switch.Off)) {
            if (switchMode.getValue().equals(Switch.Normal) && !mc.player.getHeldItemMainhand().getItem().equals(Items.END_CRYSTAL) && getHand().equals(EnumHand.MAIN_HAND)) {
                InventoryUtil.switchTo(Items.END_CRYSTAL);
                if (strongSwap.getValue()) mc.player.connection.sendPacket(new CPacketHeldItemChange(crystalSlot));
            } else if (switchMode.getValue().equals(Switch.Silent)) {
                InventoryUtil.switchToSlot(crystalSlot, true);
                hasSilentSwapped = true;
            }
        }

        if (mc.player.getHeldItem(getHand()).getItem().equals(Items.END_CRYSTAL) || hasSilentSwapped) {
            renderPosition = targetPosition;
            if (rotate.getValue()) rotateToPos(targetPosition);
            if (place.getValue().equals(Place.Normal)) {
                RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ), new Vec3d((double) targetPosition.getX() + 0.5, (double) targetPosition.getY() - 0.5, (double) targetPosition.getZ() + 0.5));
                EnumFacing facing = result == null || result.sideHit == null ? EnumFacing.UP : result.sideHit;
                Vec3d hitVec = result == null || result.hitVec == null ? new Vec3d(0, 0, 0) : result.hitVec;
                mc.playerController.processRightClickBlock(mc.player, mc.world, targetPosition, facing, hitVec, hasSilentSwapped ? EnumHand.MAIN_HAND : getHand());
            }
            if (place.getValue().equals(Place.Vanilla)) {
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(currentPos, currentFace, hasSilentSwapped ? EnumHand.MAIN_HAND : getHand(), f, f1, f2));
            }
            if (place.getValue().equals(Place.Packet)) {
                CrystalUtil.placeCrystalOnBlock(targetPosition, hasSilentSwapped ? EnumHand.MAIN_HAND : getHand());
                placedCrystals.add(targetPosition);
            }

            if (placeSwing.getValue()) mc.player.swingArm(getHand());

            placedCrystals.add(targetPosition);

            if (switchMode.getValue().equals(Switch.Silent) && hasSilentSwapped && (!mc.player.getHeldItem(getHand()).getItem().equals(Items.END_CRYSTAL))) {
                mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
            }
        } else {
            renderPosition = null;
        }

        placeTicks = 0;
    }

    public void breakCrystal() {
        if (target == null) return;

        boolean silentWeakness = false;

        int oldSlot = mc.player.inventory.currentItem;

        EntityEnderCrystal targetCrystal = null;
        double maxDamage = 0;

        for (Entity entity : mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal)) continue;
            EntityEnderCrystal crystal = (EntityEnderCrystal) entity;

            if (crystal.isDead) continue;
            if (attackedCrystals.containsKey(crystal) && attackedCrystals.get(crystal) > stuckAttempts.getValue() && antiStuck.getValue())
                continue;

            if (mc.player.canEntityBeSeen(crystal)) {
                if (mc.player.getDistanceSq(crystal) > MathUtil.square(breakRange.getValue())) continue;
            } else {
                if (mc.player.getDistanceSq(crystal) > MathUtil.square(breakWallRange.getValue())) continue;
            }

            double targetDamage = CrystalUtil.calculateDamage(crystal, target);
            double selfDamage = ignoreSelfDmg.getValue() ? 0 : CrystalUtil.calculateDamage(crystal, mc.player);

            if (!breakCrystal.getValue().equals(Break.All)) {
                if (targetDamage < minArmor.getValue() && targetDamage < target.getHealth() + target.getAbsorptionAmount()) continue;

                if (selfDamage > maxLocalDamage.getValue()) continue;

                if ((mc.player.getHealth() + mc.player.getAbsorptionAmount()) - antiSuicideFactor.getValue() - selfDamage <= 0 && antiSuicide.getValue()) continue;
            }

            if (breakCrystal.getValue().equals(Break.All)) {
                targetCrystal = crystal;
            } else {
                if (targetDamage > maxDamage) {
                    maxDamage = targetDamage;
                    targetCrystal = crystal;
                }
            }
        }

        if (targetCrystal == null) return;

        if (rotate.getValue()) rotateTo(targetCrystal);

        if (shouldAntiWeakness()) {
            if (antiWeakness.getValue().equals(Weakness.Normal)) {
                InventoryUtil.switchToSlot(InventoryUtil.findToolsInHotbar());
            } else {
                InventoryUtil.switchToSlot(InventoryUtil.findToolsInHotbar(), true);
                silentWeakness = true;
            }
        }

        for (int i = 0; i < breakAttempts.getValue(); i++) {
            if (fastCrystal.getValue()) targetCrystal.setDead();
            if (packetExplode.getValue()) {
                mc.player.connection.sendPacket(new CPacketUseEntity(targetCrystal));
            } else {
                mc.playerController.attackEntity(mc.player, targetCrystal);
            }
            if (!breakSwing.getValue().equals(BreakSwing.Off)) {
                if (breakSwing.getValue().equals(BreakSwing.Silent)) {
                    mc.player.connection.sendPacket(new CPacketAnimation(getHand()));
                } else {
                    mc.player.swingArm(getHand());
                }
            }
        }

        if (silentWeakness) {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
        }

        crystalsPerSecond.add(cpsTimer.getTimePassed());
        newAttackedCrystal(targetCrystal);
        hitTicks = 0;
    }

    public boolean shouldAntiWeakness() {
        return mc.player.isPotionActive(MobEffects.WEAKNESS) && !(mc.player.isPotionActive(MobEffects.STRENGTH) && Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.STRENGTH)).getAmplifier() == 2) && !antiWeakness.getValue().equals(Weakness.Off);
    }

    public void newAttackedCrystal(EntityEnderCrystal crystal) {
        if (attackedCrystals.containsKey(crystal)) {
            int value = attackedCrystals.get(crystal);
            attackedCrystals.put(crystal, value + 1);
        } else {
            attackedCrystals.put(crystal, 1);
        }
    }

    /**
    This was not working properly, so I am temporarily commenting it out
     */

//    public Map<BlockPos, EntityPlayer> getTargetAndPos() {
//        getAllDamages(); // this should add all the damages before it can decide if its null
//        if (damagesForPlayer.isEmpty()) return null;
//        Map<BlockPos, EntityPlayer> targetAndPos = new HashMap<>();
//        BlockPos finalPos = null;
//        for (BlockPos pos : damagesForPlayer.keySet()) {
//            if (finalPos != null) {
//                if (CrystalUtil.calculateDamage(pos, damagesForPlayer.get(pos)) < CrystalUtil.calculateDamage(finalPos, damagesForPlayer.get(pos))) continue;
//            }
//            finalPos = pos;
//        }
//        targetAndPos.put(finalPos, damagesForPlayer.get(finalPos)); //Fuck Idk if this works //TODO: Check on this please its really late and I can't think
//        return targetAndPos;
//    }
//
//    public void getAllDamages() { //bro its like 2 am right now im exhausted im just ganna do this the easy way and just fix it later also this has no return statement cause its just adding stuff to a hashset that I made at the top
//        if (getPossibleTargets().isEmpty()) return;
//        for (EntityPlayer player : getPossibleTargets()) {
//            damagesForPlayer.put(calculateBlockForPlayer(player), player);
//        }
//    }

    public BlockPos calculateBlockForPlayer() {
        BlockPos finalPos = null;
        if (getFinalTarget() == null) return null;
        for (BlockPos pos : CrystalUtil.possiblePlacePositions(placeRange.getValue(), onePoint13.getValue(), true)) {

            if (CrystalUtil.calculateDamage(pos, mc.player) > maxLocalDamage.getValue() && !ignoreSelfDmg.getValue()) continue;

            if (!CrystalUtil.canSeePos(pos) && raytrace.getValue()) continue;

            if (antiSuicide.getValue() && CrystalUtil.calculateDamage(pos, mc.player) > (mc.player.getHealth() + mc.player.getAbsorptionAmount()) - antiSuicideFactor.getValue() && !ignoreSelfDmg.getDefaultValue()) continue;

            if (CrystalUtil.calculateDamage(pos, getFinalTarget()) < getMinDamage(getFinalTarget())) continue;

            if (finalPos != null) {
                if (CrystalUtil.calculateDamage(pos, getFinalTarget()) < CrystalUtil.calculateDamage(finalPos, getFinalTarget())) {
                    continue;
                }
            }
            finalPos = pos;
        }
        return finalPos;
    }

    public EntityPlayer getFinalTarget() {
        EntityPlayer finalPlayer = null;
        for (EntityPlayer player : getPossibleTargets()) {
            //if (!areAllPlayersInHoles() && HoleUtil.isInHole(player)) continue; //TODO: Fix the thing that makes it so it doesn't return the correct player if they are in holes

//            if (finalPlayer != null && PlayerUtil.getPlayerHealth(player) <= playerOverrideHealth.getValue()) {
//                if (player.getHealth() > finalPlayer.getHealth()) continue;
//            } else if (finalPlayer != null && PlayerUtil.getPlayerHealth(player) >= playerOverrideHealth.getValue()) {
//            }
            if (finalPlayer != null) {
                if (player.getDistance(player) > player.getDistanceSq(finalPlayer)) continue;
            }

            finalPlayer = player;
        }
        return finalPlayer;
    }
//
    public Set<EntityPlayer> getPossibleTargets() { //TODO: Get most exposed player
        Set<EntityPlayer> possiblePlayers = new HashSet<>();
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player.getDistanceSq(mc.player) > MathUtil.square(playerRange.getValue())) continue;

            if (getFencing().friendManager.isFriend(player.getUniqueID())) continue;

            if (player.getHealth() <= 0) continue;

            if (player.equals(mc.player)) continue;

            possiblePlayers.add(player);
        }
        return possiblePlayers;
    }

//    public Map<EntityPlayer, Integer> getExposedAmount() {
//        Map<EntityPlayer, Integer> exposedAmount = new HashMap<>();
//        Map<EntityPlayer, BlockPos> protectiveBlocks = new HashMap<>();
//        for (EntityPlayer player : getPossibleTargets()) {
//            BlockPos playerPos = player.getPosition();
//            for (EnumFacing facing : EnumFacing.values()) {
//                if (facing.equals(EnumFacing.UP)) continue;
//                BlockPos neighbor = playerPos.offset(facing);
//                if (BlockUtil.getBlockResistance(neighbor) != BlockUtil.BlockResistance.RESISTANT) continue;
//                protectiveBlocks.put(player, neighbor);
//            }
//        }
//    }

    public boolean areAllPlayersInHoles() {
        Set<EntityPlayer> playersInHoles = new HashSet<>();
        for (EntityPlayer player : getPossibleTargets()) {
            if (!HoleUtil.isInHole(player)) continue;
            playersInHoles.add(player);
        }
        isInHole = !playersInHoles.isEmpty();
        return isInHole;
    }

    public int getMinDamage(EntityPlayer player) { //Apparently this crashes sometimes I don't know why though
        int minumumDamage;
        if (isPlayerPopable(player) && overrideIfPopable.getValue()) {
            minumumDamage = 1;
        } else {
            minumumDamage = minDamage.getValue();
        }
        return minumumDamage;
    }

    public boolean isPlayerPopable(EntityPlayer player) {
        return PlayerUtil.getPlayerHealth(player) <= popHealth.getValue();
    }

    public EnumHand getHand() {
        if (switchMode.getValue().equals(Switch.Silent)) {
            return EnumHand.MAIN_HAND;
        } else if (mc.player.getHeldItemOffhand().getItem().equals(Items.END_CRYSTAL)){
            return EnumHand.OFF_HAND;
        } else {
            return EnumHand.MAIN_HAND;
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event) {
        if (!fullNullCheck()) return;

        if (event.getPacket() instanceof SPacketSpawnObject && predict.getValue()){
            final SPacketSpawnObject packet = event.getPacket();
            final BlockPos position = new BlockPos(packet.getX(), packet.getY() - 1, packet.getZ());
            if (packet.getType() == 51 && placedCrystals.contains(position)){
                CPacketUseEntity packetUseEntity = new CPacketUseEntity();
                ((ICPacketUseEntityMixin) packetUseEntity).setEntityIdAccessor(packet.getEntityID());
                ((ICPacketUseEntityMixin) packetUseEntity).setActionAccessor(CPacketUseEntity.Action.ATTACK);

                mc.player.connection.sendPacket(packetUseEntity);
                if (!breakSwing.getValue().equals(BreakSwing.Off)){
                    if (breakSwing.getValue().equals(BreakSwing.Silent)){
                        mc.player.connection.sendPacket(new CPacketAnimation(getHand()));
                    } else {
                        mc.player.swingArm(getHand());
                    }
                }
            }
        }

        if (event.getPacket() instanceof SPacketSoundEffect && noDesync.getValue()) {
            SPacketSoundEffect packet = event.getPacket();
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (Entity entity : mc.world.loadedEntityList) {
                    if (entity instanceof EntityEnderCrystal) {
                        if (entity.getDistanceSq(packet.getX(), packet.getY(), packet.getZ()) <= MathUtil.square(6.0)) {
                            entity.setDead();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onRender3D() {
        if (fullNullCheck() && renderPosition != null && render.getValue()) {
            RenderUtil.drawBox(generateBB(renderPosition.getX(), renderPosition.getY(), renderPosition.getZ()), red.getValue() / 255f, green.getValue() / 255f, blue.getValue() / 255f, alpha.getValue() / 255f);
            if (extraRender.getValue().equals(ExtraRender.CPS)) {
                RenderUtil.drawText(renderPosition, String.valueOf(crystalsPerSecond.size()));
            } else if (extraRender.getValue().equals(ExtraRender.Damage) && target != null) {
                double damageAmount = CrystalUtil.calculateDamage(renderPosition, target);
                RenderUtil.drawText(renderPosition, String.valueOf(Math.floor(damageAmount)));
            }
        }
    }

    //rotations
    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (rotate.getValue() && event.getPacket() instanceof CPacketPlayer && isRotating) {
            CPacketPlayer packet = event.getPacket();
            packet.yaw = this.yaw;
            packet.pitch = this.pitch;
            isRotating = false;
        }
    }

    public void rotateTo(Entity entity) {
        if (rotate.getValue()) {
            float[] angle = MathUtil.calcAngle(AutoCrystal.mc.player.getPositionEyes(mc.getRenderPartialTicks()), entity.getPositionVector());
            this.yaw = angle[0];
            this.pitch = angle[1];
            this.isRotating = true;
        }
    }

    public void rotateToPos(BlockPos pos) {
        if (rotate.getValue()) {
            float[] angle = MathUtil.calcAngle(AutoCrystal.mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((float) pos.getX() + 0.5f, (float) pos.getY() - 0.5f, (float) pos.getZ() + 0.5f));
            this.yaw = angle[0];
            this.pitch = angle[1];
            this.isRotating = true;
        }
    }

    //enums for the enum settings

    public enum BreakSwing {
       Normal, Silent, Off
    }

    public enum Switch {
        Normal, Silent, Off
    }

    public enum Break {
        All, OnlyOwn, Off //redo smart mode later
    }

    public enum Logic {
        PlaceBreak, BreakPlace
    }

    public enum Weakness {
        Normal, Silent, Off
    }

    public enum Place {
        Packet, Normal, Vanilla, Off
    }

    public enum ExtraRender {
        CPS, Damage, Off
    }

    public static final class Threaded extends Thread {

        @Override
        public void run() {
            //AutoCrystal.INSTANCE.finalPos = AutoCrystal.INSTANCE.getPlacePos();
        }
    }
}
