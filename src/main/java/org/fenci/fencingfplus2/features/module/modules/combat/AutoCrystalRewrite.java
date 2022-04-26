package org.fenci.fencingfplus2.features.module.modules.combat;

import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;

public class AutoCrystalRewrite extends Module {
    public AutoCrystalRewrite() {
        super("AutoCrystalRewrite", "Trying to make better autocrystal", Category.Combat);
    }
//
//    //placing
//    public static final Setting<Boolean> place = new Setting<>("Place", true);
//    public static final Setting<Float> placeRange = new Setting<>("PlaceRange", 4f, 1, 6);
//    public static final Setting<Double> playerRange = new Setting<>("PlayerRange", 10d, 1, 20);
//    public static final Setting<Float> maxLocalDamage = new Setting<>("MaxLocalDamage", 10f, 1, 26);
//    public static final Setting<Float> minDamage = new Setting<>("MinDamage", 7f, 1, 26);
//    public static final Setting<Boolean> antiSuicide = new Setting<>("AntiSuicide", true);
//    public static final Setting<Boolean> ignoreDamageIfPopable = new Setting<>("IgnoreSelfDamageIfPopable", true);
//    public static final Setting<Boolean> armorBreaker = new Setting<>("ArmorBreaker", true);
//    public static final Setting<Float> armorPercentage = new Setting<>("Armor %", 10f, 1, 100);
//    public static final Setting<Boolean> placeSwing = new Setting<>("PlaceSwing", true);
//    public static final Setting<Boolean> onepoint13 = new Setting<>("1.13+", false);
//    public static final Setting<Boolean> entityCheck = new Setting<>("EntityCheck", true);
//    public static final Setting<Float> placeDelay = new Setting<>("PlaceDelay", 1f, 0, 20);
//
//    //breaking
//    public static final Setting<Break> breakMode = new Setting<>("BreakMode", Break.OnlyOwn);
//    public static final Setting<Float> breakRange = new Setting<>("BreakRange", 4f, 1, 6);
//    public static final Setting<Boolean> packetExplode = new Setting<>("PacketExplode", true);
//    public static final Setting<Float> breakAttempts = new Setting<>("BreakAttempts", 1f, 1, 6);
//    public static final Setting<Boolean> antiStuck = new Setting<>("AntiStuck", true);
//    public static final Setting<Float> stuckAttempts = new Setting<>("StuckAttempts", 3f, 1, 6);
//    public static final Setting<Boolean> antiDesync = new Setting<>("NoDesync", true);
//    public static final Setting<Boolean> predict = new Setting<>("Predict", true);
//    public static final Setting<Boolean> predictChorus = new Setting<>("PredictChorus", true);
//    public static final Setting<BreakSwing> breakSwing = new Setting<>("BreakSwing", BreakSwing.Mainhand);
//    public static final Setting<Weakness> antiWeakness = new Setting<>("AntiWeakness", Weakness.Normal);
//    public static final Setting<Float> breakDelay = new Setting<>("BreakDelay", 1f, 0, 20);
//
//    //other
//    public static final Setting<Boolean> rotate = new Setting<>("Rotate", true);
//    public static final Setting<Switch> swap = new Setting<>("Switch", Switch.Normal);
//    public static final Setting<Logic> logic = new Setting<>("Logic", Logic.PlaceBreak);
//
//
//    //rendering
//    public static final Setting<Boolean> render = new Setting<>("Render", true);
//    //public static final Setting<Boolean> renderDamage = new Setting<>("RenderDamage", true);
//    public static final Setting<Integer> red = new Setting<>("Red", 70, 0, 255);
//    public static final Setting<Integer> green = new Setting<>("Green", 240, 0, 255);
//    public static final Setting<Integer> blue = new Setting<>("Blue", 40, 0, 255);
//    public static final Setting<Integer> alpha = new Setting<>("Alpha", 50, 0, 255);
//
//    boolean isFriendlyChorus;
//    boolean isRotating;
//    float yaw = 0.0f;
//    float pitch = 0.0f;
//    private int hitTicks;
//    private int placeTicks;
//
//    EntityPlayer targetPlayer;
//    BlockPos placePos;
//
//    private final Set<BlockPos> placedCrystals = new HashSet<>();
//    private final ConcurrentHashMap<EntityEnderCrystal, Integer> attackedCrystals = new ConcurrentHashMap<>();
//
//    @Override
//    public void onEnable() {
//        hitTicks = 0;
//        placeTicks = 0;
//        targetPlayer = null;
//        placePos = null;
//    }
//
//    @Override
//    public void onUpdate() {
//        ClientMessage.sendMessage(targetPlayer.getName());
//    }
//
//    @SubscribeEvent
//    public void onWalkingPlayer(UpdateWalkingPlayerEvent event) {
//        doAutoCrystal();
//        placedCrystals.removeIf(pos -> mc.player.getDistanceSq(pos) > MathUtil.square(breakRange.getValue()) || !isCrystalOnBlock(pos));
//        placeTicks++;
//        hitTicks++;
//    }
//
//    public void doAutoCrystal() {
//        targetPlayer = getTargetPlayer();
//        if (targetPlayer != null) {
//            if (logic.getValue().equals(Logic.PlaceBreak)) {
//                if (!place.getValue() && placeTicks > placeDelay.getValue()) placeCrystal();
//                if (!breakMode.getValue().equals(Break.Off) && hitTicks > breakDelay.getValue()) breakCrystal();
//            } else {
//                if (!breakMode.getValue().equals(Break.Off) && hitTicks > breakDelay.getValue()) breakCrystal();
//                if (!place.getValue() && placeTicks > placeDelay.getValue()) placeCrystal();
//            }
//        }
//    }
//
//    public void placeCrystal() {
//        if (!place.getValue()) return;
//        placePos = getPlacePos();
//        boolean hasSilentSwaped = false;
//        if (swap.getValue().equals(Switch.Normal)) {
//            InventoryUtil.switchTo(Items.END_CRYSTAL);
//        }
//        if (swap.getValue().equals(Switch.NoGap) && !mc.player.getHeldItemMainhand().getItem().equals(Items.GOLDEN_APPLE)) {
//            InventoryUtil.switchTo(Items.END_CRYSTAL);
//        }
//        if (swap.getValue().equals(Switch.Silent)) {
//            InventoryUtil.switchToSlot(InventoryUtil.getHotbarSlot(Items.END_CRYSTAL), true);
//            hasSilentSwaped = true;
//        }
//
//        if (mc.player.getHeldItem(getPlaceHand()).getItem().equals(Items.END_CRYSTAL) || hasSilentSwaped) {
//            if (rotate.getValue()) rotateToPos(placePos);
//            CrystalUtil.placeCrystalOnBlock(placePos, hasSilentSwaped ? EnumHand.MAIN_HAND : getPlaceHand());
//            if (placeSwing.getValue()) mc.player.swingArm(EnumHand.MAIN_HAND);
//            if (breakMode.getValue().equals(Break.OnlyOwn)) placedCrystals.add(placePos);
//        }
//
//        placeTicks = 0;
//    }
//
//    public EnumHand getPlaceHand() {
//        if (swap.getValue().equals(Switch.Silent)) {
//            return EnumHand.MAIN_HAND;
//        } else if (mc.player.getHeldItemOffhand().getItem().equals(Items.END_CRYSTAL)){
//            return EnumHand.OFF_HAND;
//        } else {
//            return EnumHand.MAIN_HAND;
//        }
//    }
//
//    public BlockPos getPlacePos() {
//
//        BlockPos finalPos = null;
//        boolean canEzpop;
//
//        if (targetPlayer == null) return null;
//
//        for (BlockPos pos : CrystalUtil.possiblePlacePositions(placeRange.getValue(), onepoint13.getValue(), entityCheck.getValue())) {
//            canEzpop = isEasilyPopable(targetPlayer, true);
//
//            if (mc.player.getDistanceSq(pos) > MathUtil.square(placeRange.getValue())) continue;
//
//            if (CrystalUtil.calculateDamage(pos, mc.player) > PlayerUtil.getPlayerHealth() && antiSuicide.getValue()) continue;
//
//            if (CrystalUtil.calculateDamage(pos, targetPlayer) < minDamage.getValue()) continue;
//
//            if (CrystalUtil.calculateDamage(pos, mc.player) > maxLocalDamage.getValue() || (!canEzpop && !ignoreDamageIfPopable.getValue())) continue; //this line might have an error im not sure I can't use my brain right now
//
//            if (finalPos != null) {
//                if (CrystalUtil.calculateDamage(pos, targetPlayer) < CrystalUtil.calculateDamage(finalPos, targetPlayer)) {
//                    continue;
//                }
//            }
//            finalPos = pos;
//        }
//        return finalPos;
//    }
//
//    public void breakCrystal() {
//        if (breakMode.getValue().equals(Break.OnlyOwn) && placedCrystals.isEmpty() || breakMode.getValue().equals(Break.Off)) return;
//
//        boolean silentWeakness = false;
//        EntityEnderCrystal targetCrystal = null;
//        int oldSlot = mc.player.inventory.currentItem;
//
//        for (Entity entity : mc.world.playerEntities) {
//
//            if (!(entity instanceof EntityEnderCrystal)) continue;
//
//            if (entity.getDistanceSq(mc.player) > MathUtil.square(breakRange.getValue())) continue;
//
//            if (entity.isDead) continue;
//
//            if (attackedCrystals.containsKey(entity) && attackedCrystals.get(entity) > stuckAttempts.getValue() && antiStuck.getValue()) continue;
//
//            if (breakMode.getValue().equals(Break.OnlyOwn) && !isCrystalInList(entity)) continue;
//
//            targetCrystal = (EntityEnderCrystal) entity;
//        }
//
//        if (targetCrystal == null) return;
//
//        if (rotate.getValue()) rotateTo(targetCrystal);
//
//        for (int i = 0; i < breakAttempts.getValue(); i++) {
//            if (shouldWeakness()) {
//                if (antiWeakness.getValue().equals(Weakness.Normal)) {
//                    InventoryUtil.switchToSlot(InventoryUtil.findToolsInHotbar(), antiWeakness.getValue().equals(Weakness.Silent));
//                    if (antiWeakness.getValue().equals(Weakness.Silent)) silentWeakness = true;
//                }
//            }
//            if (packetExplode.getValue()) {
//                mc.player.connection.sendPacket(new CPacketUseEntity(targetCrystal, getPlaceHand()));
//            } else {
//                mc.playerController.attackEntity(mc.player, targetCrystal);
//            }
//
//            if (silentWeakness) {
//                mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
//            } else if (shouldWeakness()) {
//                mc.player.inventory.currentItem = oldSlot;
//            }
//        }
//
//        if (breakSwing.getValue().equals(BreakSwing.Mainhand)) {
//            mc.player.swingArm(EnumHand.MAIN_HAND);
//        } else if (breakSwing.getValue().equals(BreakSwing.Offhand)) {
//            mc.player.swingArm(EnumHand.OFF_HAND);
//        }
//
//        addAttackedCrystal(targetCrystal);
//
//        hitTicks = 0;
//    }
//
//    public boolean shouldWeakness() {
//        return mc.player.isPotionActive(MobEffects.WEAKNESS) && !(mc.player.isPotionActive(MobEffects.STRENGTH) && Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.STRENGTH)).getAmplifier() == 2) && !antiWeakness.getValue().equals(Weakness.Off);
//    }
//
//    public void addAttackedCrystal(EntityEnderCrystal crystal) {
//        if (attackedCrystals.containsKey(crystal)) {
//            int value = attackedCrystals.get(crystal);
//            attackedCrystals.put(crystal, value + 1);
//        } else {
//            attackedCrystals.put(crystal, 1);
//        }
//    }
//
//    public EntityPlayer getTargetPlayer() {
//
//        EntityPlayer bestPlayer = null;
//
//        if (mc.world.playerEntities.size() == 0) return null;
//
//        for (EntityPlayer player : mc.world.playerEntities) {
//            if (getFencing().friendManager.isFriend(player.getUniqueID())) continue;
//
//            if (player.getDistanceSq(mc.player) > MathUtil.square(playerRange.getValue())) continue;
//
//            if (player.isDead || player.getHealth() <= 0) continue;
//
//            if (player.equals(mc.player)) continue;
//
//            if (isEasilyPopable(player, false)) {
//                if (bestPlayer != null) {
//                    if (bestPlayer.getHealth() > player.getHealth()) continue;
//                }
//                bestPlayer = player;
//            } else if (isArmorLow(player) && armorBreaker.getValue()) {
//                bestPlayer = player;
//            } else if (bestPlayer != null){
//                if (mc.player.getDistance(player) > mc.player.getDistance(bestPlayer)) continue;
//                bestPlayer = player;
//            }
//        }
//        return bestPlayer;
//    }
//
//    public boolean isArmorLow(EntityPlayer player) {
//        for (ItemStack stack : player.getArmorInventoryList()) {
//            float itemDamage = ((float) stack.getMaxDamage() - (float) stack.getItemDamage()) / (float) stack.getMaxDamage();
//            if (itemDamage <= armorPercentage.getValue()) return true;
//        }
//        return false;
//    }
//
//    public boolean isEasilyPopable(EntityPlayer player, boolean onehundredpercentsure) {
//        for (BlockPos pos : CrystalUtil.possiblePlacePositions(placeRange.getValue(), onepoint13.getValue(), entityCheck.getValue())) {
//            if (CrystalUtil.calculateDamage(pos, player) <= player.getHealth() + player.getAbsorptionAmount()) {
//                return true;
//            }
//            if (CrystalUtil.calculateDamage(pos, player) <= player.getHealth() + player.getAbsorptionAmount() + 6 && !onehundredpercentsure) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean isCrystalOnBlock(BlockPos pos) {
//        BlockPos pos1 = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
//        for (Entity entity : mc.world.playerEntities) {
//            if (entity instanceof EntityEnderCrystal && entity.getPosition() == pos1) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean isCrystalInList(Entity entity) {
//        return placedCrystals.contains(new BlockPos(entity.posX, entity.posY - 1, entity.posZ));
//    }
//
//    @SubscribeEvent
//    public void onPacket(PacketEvent.Receive event) {
//        if (!fullNullCheck()) return;
//        if (event.getPacket() instanceof SPacketSpawnObject && predict.getValue()) {} //TODO: Finish this
//        if (event.getPacket() instanceof SPacketSoundEffect && predictChorus.getValue()) {
//            SPacketSoundEffect packet = event.getPacket();
//            if (packet.getSound().equals(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT) && isFriendlyChorus) {
//                //placePos = getPlacePos(true, new BlockPos(packet.getX(), packet.getY(), packet.getZ())); // I don't know if this will work but the idea is override the og pos to get the new chorus pos
//            }
//            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE && antiDesync.getValue()) {
//                for (Entity entity : mc.world.loadedEntityList) {
//                    if (entity instanceof EntityEnderCrystal) {
//                        if (entity.getDistanceSq(packet.getX(), packet.getY(), packet.getZ()) <= MathUtil.square(6.0f)) {
//                            entity.setDead();
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    @SubscribeEvent
//    public void onRender(RenderWorldLastEvent event) {
//        if (render.getValue() && fullNullCheck() && placePos != null) {
//            RenderUtil.drawBox(RenderUtil.generateBB(placePos.getX(), placePos.getY(), placePos.getZ()), red.getValue() / 255f, green.getValue() / 255f, blue.getValue() / 255f, alpha.getValue() / 255f);
//        }
//    }
//
//    @SubscribeEvent //added this to be able to differentiate friends and enemies, and the player from eating
//    public void onEat(EatEvent event) {
//        if (!event.getStack().getItem().equals(Items.CHORUS_FRUIT)) return;
//        if (event.getPlayer().equals(mc.player) || getFencing().friendManager.isFriend(event.getPlayer().getUniqueID())) {
//            isFriendlyChorus = true;
//        }
//    }
//
//    @SubscribeEvent
//    public void onPacketSend(PacketEvent.Send event) {
//        if (rotate.getValue() && event.getPacket() instanceof CPacketPlayer && isRotating) {
//            CPacketPlayer packet = event.getPacket();
//            packet.yaw = this.yaw;
//            packet.pitch = this.pitch;
//            isRotating = false;
//        }
//    }
//
//    public void rotateTo(Entity entity) {
//        if (rotate.getValue()) {
//            float[] angle = MathUtil.calcAngle(AutoCrystal.mc.player.getPositionEyes(mc.getRenderPartialTicks()), entity.getPositionVector());
//            this.yaw = angle[0];
//            this.pitch = angle[1];
//            this.isRotating = true;
//        }
//    }
//
//    public void rotateToPos(BlockPos pos) {
//        if (rotate.getValue()) {
//            float[] angle = MathUtil.calcAngle(AutoCrystal.mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((float) pos.getX() + 0.5f, (float) pos.getY() - 0.5f, (float) pos.getZ() + 0.5f));
//            this.yaw = angle[0];
//            this.pitch = angle[1];
//            this.isRotating = true;
//        }
//    }
//
//    public enum Break {
//        All, OnlyOwn, Off
//    }
//
//    public enum Switch {
//        Normal, NoGap, Silent, Off
//    }
//
//    public enum BreakSwing {
//        Mainhand, Offhand, Off
//    }
//
//    public enum Weakness {
//        Normal, Silent, Off
//    }
//
//    public enum Logic {
//        PlaceBreak, BreakPlace
//    }
}
