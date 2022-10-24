package org.fenci.fencingfplus2.features.module.modules.movement;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.client.OptionChangeEvent;
import org.fenci.fencingfplus2.events.network.PacketEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.features.module.modules.exploit.UnDesync;
import org.fenci.fencingfplus2.features.module.modules.player.ChestSwap;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.ClientMessage;
import org.fenci.fencingfplus2.util.client.Timer;
import org.fenci.fencingfplus2.util.player.PlayerUtil;
import org.fenci.fencingfplus2.util.render.RenderUtil;
import org.fenci.fencingfplus2.util.world.BlockUtil;
import org.spongepowered.asm.mixin.SoftOverride;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.fenci.fencingfplus2.util.render.RenderUtil.generateBB;

public class AutoCtrlEfly extends Module {

    public static AutoCtrlEfly INSTANCE;


    public AutoCtrlEfly() {
        super("AutoCtrlEfly", "Automatically uses future efly", Category.Movement);
        INSTANCE = this;
    }

//    public static final Keybind futureBind = new Keybind("FuturePrefix", Keyboard.KEY_PERIOD);
//    public static final Keybind otherbind = new Keybind("LambdaPrefix", Keyboard.KEY_SEMICOLON);
    public static final Setting<Mode> futureOnly = new Setting<>("Mode", Mode.FutureLambda);
    public static final Setting<Direction> direction = new Setting<>("Direction", Direction.North);
    public static final Setting<Float> fallDistance = new Setting<>("FallDistance", 0.3f, 0.3f, 0.7f);
    public static final Setting<Integer> rubberbandPackets = new Setting<>("RubberbandPackets", 2, 2, 3);
    public static final Setting<Boolean> renderBreakBlocks = new Setting<>("RenderBreakBlocks", true);
    public static final Setting<Boolean> help = new Setting<>("Help", false);
    public static final Setting<Boolean> debug = new Setting<>("Debug", false);

    Timer timer = new Timer();
    Timer delay = new Timer();
    List<Integer> lagPackets = new ArrayList<>();

    boolean eflyStatus;
    boolean isDesync;

    BlockPos breakingBlock;
    int onGround = 0;
    boolean pauseOnNoFocus;

    @SubscribeEvent
    public void onOption(OptionChangeEvent event) {
        if (event.getOption().equals(help) && help.getValue()) {
            ClientMessage.sendMessage("This module is a way to automate future control efly, it can be used with lambda control efly as well. To use it make sure your future prefix is set to \".\" If you are using future only then maker sure you have the correct setting for elytrafly and turn the module off. If you are using lambda only use the normal lambda efly config that goes with future but change the \"SpeedC\" setting to 3.5 and bind the module to the same key as this module. If you are using future and lambda for the efly then make sure you have the correct future \"flight\" config along with the correct lambda elytrafly config\" these configs can be found on BIKMUNNI's youtube page, you then need bind lambda to the same key as this module, and turn off future flight. If this doesn't make sense message me on discord.");
            help.setValue(false);
        }
    }
    @Override
    public void onEnable() {
//        if (futureBind.getValue().equals(Keyboard.KEY_NONE)) ClientMessage.sendMessage("Make sure to set the efly keybind to your future efly bind");
        eflyStatus = false;
        if (mc.player.inventory.getStackInSlot(38).getItem().equals(Items.ELYTRA)) {
            ChestSwap.INSTANCE.setToggled(true);
        }
        //change pauseOnLostFocus to false
        pauseOnNoFocus = mc.gameSettings.pauseOnLostFocus;
        mc.gameSettings.pauseOnLostFocus = false;
        PlayerUtil.centerPlayer();
    }

    @Override
    public void onUpdate() {
        if (debug.getValue()) ClientMessage.sendMessage("Status: " + eflyStatus + " | Collided: " + mc.player.collided);
        if (/*!PlayerUtil.isRubberbanding(mc.player) && */ mc.player.isElytraFlying()) {
            mc.player.rotationYaw = direction.getValue().getYaw();
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
            AutoWalk.INSTANCE.setToggled(true);
        }

        if ((lagPackets.size() >= rubberbandPackets.getValue() || mc.player.collided) && mc.player.inventory.getStackInSlot(38).getItem().equals(Items.ELYTRA) && eflyStatus) { //turn off elytra
            if (debug.getValue()) ClientMessage.sendMessage("Rubberbanding");
            if (futureOnly.getValue().equals(Mode.FutureLambda)) mc.player.sendChatMessage(".toggle flight");
            if (futureOnly.getValue().equals(Mode.FutureOnly)) mc.player.sendChatMessage(".toggle elytrafly");
            ClientMessage.sendMessage("toggled flight");
            ChestSwap.INSTANCE.toggle(true);
            delay.reset();
            eflyStatus = false;
        }

        if (mc.player.onGround || !mc.player.isElytraFlying() && delay.hasReached(3500) && getBreakBlocks().isEmpty()/*&& !PlayerUtil.isRubberbanding(mc.player)*/) { //turn elytra on again
//            KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
            if (mc.player.onGround && !eflyStatus && getBreakBlocks().isEmpty()) mc.player.jump();
            if (!eflyStatus) {
                if (mc.player.inventory.getStackInSlot(38).getItem().equals(Items.DIAMOND_CHESTPLATE) && mc.player.fallDistance > fallDistance.getValue()) {
                    if (futureOnly.getValue().equals(Mode.FutureLambda)) mc.player.sendChatMessage(".toggle flight");
                    if (futureOnly.getValue().equals(Mode.FutureOnly)) mc.player.sendChatMessage(".toggle elytrafly");
                    ChestSwap.INSTANCE.toggle(true);
                    eflyStatus = true;
                    onGround = 0;
                }
            }
        }

        if (mc.player.onGround && onGround == 0) {
            PlayerUtil.centerPlayer();
            UnDesync.INSTANCE.toggle(true);
            onGround = 1;
        }

        if (getClosestBlock() != null && mc.player.onGround) {
            breakingBlock = getClosestBlock();
            if (!BlockUtil.getBlock(breakingBlock).equals(Blocks.AIR)) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
                breakBlock(breakingBlock);
            }
        }

        if (breakingBlock != null) {
            if (BlockUtil.getBlock(breakingBlock).equals(Blocks.AIR)) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                if (getClosestBlock() != null) {
                    breakingBlock = getClosestBlock();
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
                    breakBlock(breakingBlock);
                } else {
                    breakingBlock = null;
                }
            }
        }

        if (timer.hasReached(1500)) {
            lagPackets.clear();
//            rubberbanding = false;
            timer.reset();
        }

        if (mc.player.inventory.getStackInSlot(38).getItem().equals(Items.DIAMOND_CHESTPLATE)) AutoWalk.INSTANCE.setToggled(false);
    }

    public void breakBlock(BlockPos pos) {
        PlayerUtil.switchToBestTool(pos);
        if (!BlockUtil.getBlock(pos).equals(Blocks.AIR)) {
            //look at block
            float[] angle = PlayerUtil.getAngleToBlock(pos);
//            mc.player.rotationYaw = angle[0];
            mc.player.rotationYaw = direction.getValue().getYaw();
            mc.player.rotationPitch = angle[1];
//            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(angle[0], angle[1], true));
        }
    }

    public BlockPos getClosestBlock() {
        BlockPos closestBlock = null;
        for (BlockPos pos : getBreakBlocks()) {
            if (closestBlock == null) {
                closestBlock = pos;
            } else {
                if (mc.player.getDistance(pos.getX(), pos.getY(), pos.getZ()) < mc.player.getDistance(closestBlock.getX(), closestBlock.getY(), closestBlock.getZ())) {
                    closestBlock = pos;
                }
            }
        }
        return closestBlock;
    }

    public Set<BlockPos> getBreakBlocks() {
        Set<BlockPos> blocks = new HashSet<>();
        BlockPos playerPos = PlayerUtil.getPlayerPos(mc.player);
        for (EnumFacing facing : EnumFacing.values()) {
            if (facing.equals(EnumFacing.UP) || facing.equals(EnumFacing.DOWN)) continue;
            if (!facing.equals(direction.getValue().getFacing())) continue;
            BlockPos offset = playerPos.offset(facing);
            BlockPos secondOffset = offset.offset(EnumFacing.UP);
            if (!BlockUtil.getBlock(offset).equals(Blocks.AIR)) blocks.add(offset);
            if (!BlockUtil.getBlock(offset.offset(facing)).equals(Blocks.AIR)) blocks.add(offset.offset(facing));
            if (!BlockUtil.getBlock(secondOffset).equals(Blocks.AIR)) blocks.add(secondOffset);
            if (!BlockUtil.getBlock(secondOffset.offset(facing)).equals(Blocks.AIR)) blocks.add(secondOffset.offset(facing));
        }
        return blocks;
    }

    @Override
    public void onRender3D() {
        if (!renderBreakBlocks.getValue()) return;
        if (breakingBlock != null) RenderUtil.drawBox(generateBB(breakingBlock.getX(), breakingBlock.getY(), breakingBlock.getZ()), 30 / 255f, 255 / 255f, 30 / 255f, 100 / 255f);
        for (BlockPos pos : getBreakBlocks()) {
            RenderUtil.drawBox(generateBB(pos.getX(), pos.getY(), pos.getZ()), 255 / 255f, 30 / 255f, 30 / 255f, 100 / 255f);
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketConfirmTeleport) {
            CPacketConfirmTeleport packet = event.getPacket();
            lagPackets.add(packet.getTeleportId());
        }
//        if (event.getPacket() instanceof SPacketPlayerPosLook) {
//            rubberbanding = true;
//            timer.reset();
//        }
    }

    @Override
    public void onDisable() {
        AutoWalk.INSTANCE.setToggled(false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
        mc.gameSettings.pauseOnLostFocus = pauseOnNoFocus;
    }

    public enum Direction {
        North(new FlyData(EnumFacing.NORTH, 4.0f)), South(new FlyData(EnumFacing.SOUTH, 0.0f)), East(new FlyData(EnumFacing.EAST, 6.0f)), West(new FlyData(EnumFacing.WEST, 2.0f));

        private final FlyData data;

        Direction(FlyData data) {
            this.data = data;
        }

        public float getYaw() {
            return data.getYaw() * 45.0f;
        }

        public EnumFacing getFacing() {
            return data.getFacing();
        }
    }

    public static class FlyData {
        private final EnumFacing facing;
        private final float yaw;

        public FlyData(EnumFacing facing, float yaw) {
            this.facing = facing;
            this.yaw = yaw;
        }

        public EnumFacing getFacing() {
            return facing;
        }

        public float getYaw() {
            return yaw;
        }
    }

    public enum Mode {
        FutureLambda, FutureOnly, LambdaOnly
    }
}
