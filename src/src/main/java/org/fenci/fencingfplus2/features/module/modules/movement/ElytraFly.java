package org.fenci.fencingfplus2.features.module.modules.movement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.fenci.fencingfplus2.events.network.PacketEvent;
import org.fenci.fencingfplus2.events.player.ElytraEvent;
import org.fenci.fencingfplus2.events.player.MoveEvent;
import org.fenci.fencingfplus2.events.player.UpdateWalkingPlayerEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.mixin.mixins.network.ICPacketPlayer;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.Timer;
import org.fenci.fencingfplus2.util.player.PlayerUtil;
import org.lwjgl.input.Keyboard;

import java.util.Objects;
import java.util.Random;

public class ElytraFly extends Module {

    private final static Setting<Mode> mode = new Setting<>("Mode", Mode.Control);
    private final static Setting<Float> packetDelay = new Setting<>("Limit", 1F, 0.1F, 5F);
    private final static Setting<Float> staticDelay = new Setting<>("Delay", 5F, 0.1F, 20F);
    private final static Setting<Float> timeout = new Setting<>("Timeout", 0.5F, 0.1F, 1F);
    private final static Setting<Boolean> stopMotion = new Setting<>("StopMotion", true);
    private final static Setting<Boolean> freeze = new Setting<>("Freeze", false);
    private final static Setting<Boolean> cruiseControl = new Setting<>("CruiseControl", false);
    private final static Setting<Double> minUpSpeed = new Setting<>("MinUpSpeed", 0.5D, 0.1D, 5D);
    private final static Setting<Boolean> autoSwitch = new Setting<>("AutoSwitch", false);
    private final static Setting<Float> factor = new Setting<>("Factor", 1.5F, 0.1F, 50F);
    private final static Setting<Integer> minSpeed = new Setting<>("MinSpeed", 20, 0.1, 50);
    private final static Setting<Float> upFactor = new Setting<>("UpFactor", 1.0f, 0, 10);
    private final static Setting<Float> downFactor = new Setting<>("DownFactor", 1.0F, 0F, 10.0F);
    private final static Setting<Boolean> forceHeight = new Setting<>("ForceHeight", false);
    private final static Setting<Integer> manualHeight = new Setting<>("Height", 121, 1, 256);
    private final static Setting<Boolean> groundSafety = new Setting<>("GroundSafety", false);
    private final static Setting<Float> triggerHeight = new Setting<>("TriggerHeight", 0.3F, 0.05F, 1F);
    // Normal/Boost/Glide settings
    private final static Setting<Float> speed = new Setting<>("Speed", 1.0F, 0.1F, 10F);
    private final static Setting<Float> sneakDownSpeed = new Setting<>("DownSpeed", 1.0F, 0.1F, 10F);
    private final static Setting<Boolean> instantFly = new Setting<>("InstantFly", true);
    private final static Setting<Boolean> boostTimer = new Setting<>("Timer", true);
    private final static Setting<Boolean> speedLimit = new Setting<>("SpeedLimit", true);
    private final static Setting<Float> maxSpeed = new Setting<>("MaxSpeed", 2.5F, 0.1F, 10F);
    private final static Setting<Boolean> noDrag = new Setting<>("NoDrag", false);
    // Packet settings
    private final static Setting<Boolean> accelerate = new Setting<>("Accelerate", true);
    private final static Setting<Float> acceleration = new Setting<>("Acceleration", 1.0F, 0.1F, 5F);
    private final static Setting<StrictMode> strict = new Setting<>("Strict", StrictMode.None);
    private final static Setting<Boolean> antiKick = new Setting<>("AntiKick", true);
    private final static Setting<Boolean> infDurability = new Setting<>("InfDurability", true);
    public static ElytraFly INSTANCE;
    private static boolean hasElytra = false;
    private final Random random = new Random();
    private final Timer instantFlyTimer = new Timer();
    private final Timer staticTimer = new Timer();
    private final Timer rocketTimer = new Timer();
    private final Timer strictTimer = new Timer();
    public double tempSpeed;
    private boolean rSpeed;
    private double curSpeed;
    private double height;
    private boolean isJumping = false;
    private boolean hasTouchedGround = false;

    public ElytraFly() {
        super("ElytraFly", "Allows you to fly easier with elytra", Category.Movement);
        INSTANCE = this;
    }

    public static boolean isHasElytra() {
        return hasElytra;
    }

    @Override
    public void onEnable() {
        rSpeed = false;
        curSpeed = 0.0D;
        if (mc.player != null) {
            height = mc.player.posY;
            if (!mc.player.isCreative()) mc.player.capabilities.allowFlying = false;
            mc.player.capabilities.isFlying = false;
        }
        isJumping = false;
        hasElytra = false;
    }

    @Override
    public void onDisable() {
        if (mc.player != null) {
            if (!mc.player.isCreative()) mc.player.capabilities.allowFlying = false;
            mc.player.capabilities.isFlying = false;
        }
        getFencing().tickManager.reset();
        hasElytra = false;
    }

    @Override
    public void onUpdate() {

        if (mc.player.onGround) {
            hasTouchedGround = true;
        }

        if (!cruiseControl.getValue()) {
            height = mc.player.posY;
        }

        for (ItemStack is : mc.player.getArmorInventoryList()) {
            if (is.getItem() instanceof ItemElytra) {
                hasElytra = true;
                break;
            } else {
                hasElytra = false;
            }
        }

        if (strictTimer.hasReached(1500) && !strictTimer.hasReached(2000)) {
            strictTimer.reset();
        }

        if (!mc.player.isElytraFlying() && mode.getValue() != Mode.Packet) {
            if (hasTouchedGround && boostTimer.getValue() != boostTimer.getValue() && !mc.player.onGround) {
                getFencing().tickManager.setTicks(0.3f);
            }
            if (!mc.player.onGround && instantFly.getValue() && mc.player.motionY < 0D) {
                if (!instantFlyTimer.hasReached(1000 * timeout.getValue().longValue()))
                    return;
                instantFlyTimer.reset();
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                hasTouchedGround = false;
                strictTimer.reset();
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (fullNullCheck() && mode.getValue().equals(Mode.Firework) && mc.player.isElytraFlying()) {
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                height += upFactor.getValue() * 0.5;
            } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                height -= downFactor.getValue() * 0.5;
            }

            if (forceHeight.getValue()) {
                height = manualHeight.getValue();
            }

            Vec3d motionVector = new Vec3d(mc.player.motionX, mc.player.motionY, mc.player.motionZ);
            double bps = motionVector.length() * 20;

            double horizSpeed = Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
            double horizPct = MathHelper.clamp(horizSpeed / 1.7, 0.0, 1.0);
            double heightPct = 1 - Math.sqrt(horizPct);
            double minAngle = 0.6;

            if (horizPct >= 0.5 || mc.player.posY > height + 1) {
                double pitch = -((45 - minAngle) * heightPct + minAngle);

                double diff = (height + 1 - mc.player.posY) * 2;
                double heightDiffPct = MathHelper.clamp(Math.abs(diff), 0.0, 1.0);
                double pDist = -Math.toDegrees(Math.atan2(Math.abs(diff), horizSpeed * 30.0)) * Math.signum(diff);

                double adjustment = (pDist - pitch) * heightDiffPct;

                mc.player.rotationPitch = (float) pitch;
                mc.player.rotationPitch += (float) adjustment;
                mc.player.prevRotationPitch = mc.player.rotationPitch;
            }

            if (rocketTimer.hasReached(1000 * factor.getValue().longValue())) {
                double heightDiff = height - mc.player.posY;
                boolean shouldBoost = (heightDiff > 0.25 && heightDiff < 1.0) || bps < minSpeed.getValue();

                if (groundSafety.getValue()) {
                    Block bottomBlock = mc.world.getBlockState(new BlockPos(mc.player).down()).getBlock();
                    if (bottomBlock != Blocks.AIR && !(bottomBlock instanceof BlockLiquid)) {
                        if (mc.player.getEntityBoundingBox().minY - Math.floor(mc.player.getEntityBoundingBox().minY) > triggerHeight.getValue()) {
                            shouldBoost = true;
                        }
                    }
                }

                if (autoSwitch.getValue() && shouldBoost && mc.player.getHeldItemMainhand().getItem() != Items.FIREWORKS) {
                    for (int l = 0; l < 9; ++l) {
                        if (mc.player.inventory.getStackInSlot(l).getItem() == Items.FIREWORKS) {
                            mc.player.inventory.currentItem = l;
                            mc.playerController.updateController();
                            break;
                        }
                    }
                }

                if (mc.player.getHeldItemMainhand().getItem() == Items.FIREWORKS && shouldBoost) {
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    rocketTimer.reset();
                }
            }
        }
    }

    private boolean checkIfBlockInBB(int minY) {
        for (int iX = MathHelper.floor(mc.player.getEntityBoundingBox().minX); iX < MathHelper.ceil(mc.player.getEntityBoundingBox().maxX); iX++) {
            for (int iZ = MathHelper.floor(mc.player.getEntityBoundingBox().minZ); iZ < MathHelper.ceil(mc.player.getEntityBoundingBox().maxZ); iZ++) {
                IBlockState state = mc.world.getBlockState(new BlockPos(iX, minY, iZ));
                if (state.getBlock() != Blocks.AIR) {
                    return false;
                }
            }
        }
        return true;
    }

    // Firework mode
//    @Override
//    public void onUpdate() {
    //if (event.getPhase() != TickEvent.Phase.START) return;


    // Normal/Boost/Control mode
    @SubscribeEvent
    public void onElytra(ElytraEvent event) {
        if (mc.world == null || mc.player == null || !hasElytra || !mc.player.isElytraFlying()) return;

        if (mode.getValue() == Mode.Packet || mode.getValue() == Mode.Firework) return;

        if (event.getEntity() == mc.player && mc.player.isServerWorld() || mc.player.canPassengerSteer() && !mc.player.isInWater() || mc.player != null && mc.player.capabilities.isFlying && !mc.player.isInLava() || Objects.requireNonNull(mc.player).capabilities.isFlying && mc.player.isElytraFlying()) {

            event.setCanceled(true);

            if (mode.getValue() != Mode.Boost) {

                Vec3d lookVec = mc.player.getLookVec();

                float pitch = mc.player.rotationPitch * 0.017453292F;

                double lookDist = Math.sqrt(lookVec.x * lookVec.x + lookVec.z * lookVec.z);
                double motionDist = Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
                double lookVecDist = lookVec.length();

                float cosPitch = MathHelper.cos(pitch);
                cosPitch = (float) ((double) cosPitch * (double) cosPitch * Math.min(1.0D, lookVecDist / 0.4D));

                // Vanilla Glide
                if (mode.getValue() != Mode.Control) {
                    mc.player.motionY += -0.08D + (double) cosPitch * (0.06D / downFactor.getValue());
                }

                // Downwards movement
                if (mode.getValue() == Mode.Control) {
                    // Goes down when sneaking, glides otherwise
                    if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                        mc.player.motionY = -sneakDownSpeed.getValue();
                    } else if (!mc.gameSettings.keyBindJump.isKeyDown()) {
                        mc.player.motionY = -0.00000000000003D * downFactor.getValue();
                    }
                } else if (mode.getValue() != Mode.Control && mc.player.motionY < 0.0D && lookDist > 0.0D) {
                    // Uses pitch go go down and gain speed
                    double downSpeed = mc.player.motionY * -0.1D * (double) cosPitch;
                    mc.player.motionY += downSpeed;
                    mc.player.motionX += (lookVec.x * downSpeed / lookDist) * factor.getValue();
                    mc.player.motionZ += (lookVec.z * downSpeed / lookDist) * factor.getValue();
                }

                // Upwards Movement
                if (pitch < 0.0F && mode.getValue() != Mode.Control) {
                    // Normal/Boost mode - uses pitch to go up
                    double rawUpSpeed = motionDist * (double) (-MathHelper.sin(pitch)) * 0.04D;
                    mc.player.motionY += rawUpSpeed * 3.2D * upFactor.getValue();
                    mc.player.motionX -= lookVec.x * rawUpSpeed / lookDist;
                    mc.player.motionZ -= lookVec.z * rawUpSpeed / lookDist;
                } else if (mode.getValue() == Mode.Control && mc.gameSettings.keyBindJump.isKeyDown()) {
                    // Control mode - goes up for as long as possible, then accelerates, then goes up again
                    if (motionDist > upFactor.getValue() / upFactor.getMax().intValue()) {
                        double rawUpSpeed = motionDist * 0.01325D;
                        mc.player.motionY += rawUpSpeed * 3.2D;
                        mc.player.motionX -= lookVec.x * rawUpSpeed / lookDist;
                        mc.player.motionZ -= lookVec.z * rawUpSpeed / lookDist;
                    } else {
                        double[] dir = PlayerUtil.forward(speed.getValue());
                        mc.player.motionX = dir[0];
                        mc.player.motionZ = dir[1];
                    }
                }

                // Turning
                if (lookDist > 0.0D) {
                    mc.player.motionX += (lookVec.x / lookDist * motionDist - mc.player.motionX) * 0.1D;
                    mc.player.motionZ += (lookVec.z / lookDist * motionDist - mc.player.motionZ) * 0.1D;
                }

                if (mode.getValue() == Mode.Control && !mc.gameSettings.keyBindJump.isKeyDown()) {
                    // Sets motion in control mode
                    double[] dir = PlayerUtil.forward(speed.getValue());
                    mc.player.motionX = dir[0];
                    mc.player.motionZ = dir[1];
                }

                if (!noDrag.getValue()) {
                    mc.player.motionX *= 0.9900000095367432D;
                    mc.player.motionY *= 0.9800000190734863D;
                    mc.player.motionZ *= 0.9900000095367432D;
                }

                // Max speed
                double finalDist = Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);

                if (speedLimit.getValue() && finalDist > maxSpeed.getValue()) {
                    mc.player.motionX *= maxSpeed.getValue() / finalDist;
                    mc.player.motionZ *= maxSpeed.getValue() / finalDist;
                }

                mc.player.move(MoverType.SELF, mc.player.motionX, mc.player.motionY, mc.player.motionZ);
            } else {
                boolean shouldBoost = false;
                float moveForward = mc.player.movementInput.moveForward;

                if (cruiseControl.getValue()) {
                    if (mc.gameSettings.keyBindJump.isKeyDown()) {
                        height += upFactor.getValue() * 0.5;
                    } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                        height -= downFactor.getValue() * 0.5;
                    }

                    if (forceHeight.getValue()) {
                        height = manualHeight.getValue();
                    }

                    double horizSpeed = Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
                    double horizPct = MathHelper.clamp(horizSpeed / 1.7, 0.0, 1.0);
                    double heightPct = 1 - Math.sqrt(horizPct);
                    double minAngle = 0.6;

                    if (horizSpeed >= minUpSpeed.getValue() && instantFlyTimer.hasReached(2000 * packetDelay.getValue().longValue())) {
                        double pitch = -((45 - minAngle) * heightPct + minAngle);

                        double diff = (height + 1 - mc.player.posY) * 2;
                        double heightDiffPct = MathHelper.clamp(Math.abs(diff), 0.0, 1.0);
                        double pDist = -Math.toDegrees(Math.atan2(Math.abs(diff), horizSpeed * 30.0)) * Math.signum(diff);

                        double adjustment = (pDist - pitch) * heightDiffPct;

                        mc.player.rotationPitch = (float) pitch;
                        mc.player.rotationPitch += (float) adjustment;
                        mc.player.prevRotationPitch = mc.player.rotationPitch;
                    } else {
                        mc.player.rotationPitch = 0.25F;
                        mc.player.prevRotationPitch = 0.25F;
                        moveForward = 1F;
                    }
                }

                Vec3d vec3d = mc.player.getLookVec();

                float f = mc.player.rotationPitch * 0.017453292F;

                double d6 = Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
                double d8 = Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
                double d1 = vec3d.length();
                float f4 = MathHelper.cos(f);
                f4 = (float) ((double) f4 * (double) f4 * Math.min(1.0D, d1 / 0.4D));
                mc.player.motionY += -0.08D + (double) f4 * 0.06D;

                if (mc.player.motionY < 0.0D && d6 > 0.0D) {
                    double d2 = mc.player.motionY * -0.1D * (double) f4;
                    mc.player.motionY += d2;
                    mc.player.motionX += vec3d.x * d2 / d6;
                    mc.player.motionZ += vec3d.z * d2 / d6;
                }

                if (f < 0.0F) {
                    double d10 = d8 * (double) (-MathHelper.sin(f)) * 0.04D;
                    mc.player.motionY += d10 * 3.2D;
                    mc.player.motionX -= vec3d.x * d10 / d6;
                    mc.player.motionZ -= vec3d.z * d10 / d6;
                }

                if (d6 > 0.0D) {
                    mc.player.motionX += (vec3d.x / d6 * d8 - mc.player.motionX) * 0.1D;
                    mc.player.motionZ += (vec3d.z / d6 * d8 - mc.player.motionZ) * 0.1D;
                }

                if (!noDrag.getValue()) {
                    mc.player.motionX *= 0.9900000095367432D;
                    mc.player.motionY *= 0.9800000190734863D;
                    mc.player.motionZ *= 0.9900000095367432D;
                }

                float yaw = mc.player.rotationYaw * 0.017453292F;

                if (f > 0F && (mc.player.motionY < 0D || shouldBoost)) {
                    if (moveForward != 0F && instantFlyTimer.hasReached(2000 * packetDelay.getValue().longValue()) && staticTimer.hasReached(1000 * staticDelay.getValue().longValue())) {
                        if (stopMotion.getValue()) {
                            mc.player.motionX = 0;
                            mc.player.motionZ = 0;
                        }
                        instantFlyTimer.reset();
                        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    } else if (!instantFlyTimer.hasReached(2000 * packetDelay.getValue().longValue())) {
                        mc.player.motionX -= moveForward * Math.sin(yaw) * factor.getValue() / 20F;
                        mc.player.motionZ += moveForward * Math.cos(yaw) * factor.getValue() / 20F;
                        staticTimer.reset();
                    }
                }

                double finalDist = Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);

                if (speedLimit.getValue() && finalDist > maxSpeed.getValue()) {
                    mc.player.motionX *= maxSpeed.getValue() / finalDist;
                    mc.player.motionZ *= maxSpeed.getValue() / finalDist;
                }

                if (freeze.getValue() && Keyboard.isKeyDown(Keyboard.KEY_LMENU)) {
                    mc.player.setVelocity(0, 0, 0);
                }

                mc.player.move(MoverType.SELF, mc.player.motionX, mc.player.motionY, mc.player.motionZ);
            }
        }
    }

    // Packet Mode
    @SubscribeEvent
    public void onWalkingUpdatePlayer(UpdateWalkingPlayerEvent event) {
        if (event.getEra().equals(UpdateWalkingPlayerEvent.Era.PRE)) {
            if (!mc.player.onGround && mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA && mode.getValue() == Mode.Packet) {
                if (infDurability.getValue() || !mc.player.isElytraFlying()) {
                    mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerMove(MoveEvent event) {
        if (mode.getValue() == Mode.Packet) {
            if (mc.player.onGround) {
                return;
            }

            if (!hasElytra) return;

            if (accelerate.getValue()) {
                if (rSpeed) {
                    curSpeed = 1.0;
                    rSpeed = false;
                }
                if (curSpeed < factor.getValue()) {
                    curSpeed += 0.1 * acceleration.getValue();
                }
                if (curSpeed - 0.1 > factor.getValue()) {
                    curSpeed -= 0.1 * acceleration.getValue();
                }
            } else {
                curSpeed = factor.getValue();
            }

            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.motionY = upFactor.getValue();
                event.setY(mc.player.motionY);
            } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.motionY = -downFactor.getValue();
                event.setY(mc.player.motionY);
            } else if (strict.getValue() == StrictMode.Normal) {
                if (mc.player.ticksExisted % 32 == 0 && !rSpeed && (Math.abs(event.getX()) >= 0.05 || Math.abs(event.getZ()) >= 0.05)) {
                    mc.player.motionY = -2.0E-4;
                    event.setY(0.006200000000000001);
                } else {
                    mc.player.motionY = -2.0E-4;
                    event.setY(-2.0E-4);
                }
            } else if (strict.getValue() == StrictMode.Glide) {
                mc.player.motionY = -0.00001F;
                event.setY(-0.00001F);
            } else {
                mc.player.motionY = 0.0;
                event.setY(0.0);
            }

            event.setX(event.getX() * (rSpeed ? 0.0 : curSpeed));
            event.setZ(event.getZ() * (rSpeed ? 0.0 : curSpeed));

            if (antiKick.getValue() && event.getX() == 0.0 && event.getZ() == 0.0 && !rSpeed) {
                event.setX(Math.sin(Math.toRadians(mc.player.ticksExisted % 360)) * 0.03);
                event.setZ(Math.cos(Math.toRadians(mc.player.ticksExisted % 360)) * 0.03);
            }

            rSpeed = false;
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer && strict.getValue() == StrictMode.NCP && mode.getValue() == Mode.Packet && !rSpeed && (Math.abs(mc.player.motionX) >= 0.05 || Math.abs(mc.player.motionZ) >= 0.05)) {
            double randomV = 1.0E-8 + 1.0E-8 * (1.0 + random.nextInt(1 + (random.nextBoolean() ? random.nextInt(34) : random.nextInt(43))));
            if (mc.player.onGround || mc.player.ticksExisted % 2 == 0) {
                CPacketPlayer packetPlayer = event.getPacket();
                ((ICPacketPlayer) event.getPacket()).setY(((CPacketPlayer) event.getPacket()).getY(mc.player.posY) + randomV);
            } else {
                ((ICPacketPlayer) event.getPacket()).setY(((CPacketPlayer) event.getPacket()).getY(mc.player.posY) - randomV);
            }
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (mc.player == null || mc.world == null) return;
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            if (mode.getValue() == Mode.Packet || mode.getValue() == Mode.Firework) {
                if (mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA) {
                    rSpeed = true;
                }
                if (mc.player.isElytraFlying()) {
                    rocketTimer.reset();
                    if (mc.player != null) {
                        height = mc.player.posY;
                    }
                }
            }
        } else if (event.getPacket() instanceof SPacketEntityMetadata) {
            SPacketEntityMetadata packet = event.getPacket();
            if (packet.getEntityId() == mc.player.getEntityId()) {
                getFencing().tickManager.reset();
                if (mode.getValue() == Mode.Packet) {
                    event.setCanceled(true);
                }
            }
        }
    }

    private enum Mode {
        Boost, Control, Firework, Packet
    }

    private enum StrictMode {
        None, Normal, NCP, Glide
    }
}
