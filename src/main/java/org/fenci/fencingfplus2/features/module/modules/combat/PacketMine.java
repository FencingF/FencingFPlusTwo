package org.fenci.fencingfplus2.features.module.modules.combat;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.player.ClickBlockEvent;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.Timer;
import org.fenci.fencingfplus2.util.player.InventoryUtil;
import org.fenci.fencingfplus2.util.render.RenderUtil;
import org.fenci.fencingfplus2.util.world.BlockUtil;

import java.awt.*;

public class PacketMine extends Module {
    public PacketMine() {
        super("PacketMine", "Mines stuff with packets", Category.Combat);
    }

    public static final Setting<Boolean> render = new Setting<>("Render", true);
    public static final Setting<Boolean> waitForNoInteract = new Setting<>("AntiInteract", false);
    public static final Setting<MineMode> mineModeSetting = new Setting<>("Mode", MineMode.Packet);
    public static final Setting<autoSwitch> autoSwitchSetting = new Setting<>("Switch", autoSwitch.Normal);

    BlockPos blockToMine;
    EnumFacing eventFacing;
    Timer timer = new Timer();
    Timer switchTimer = new Timer();
    boolean hasSentPackets;
    boolean sentFirstPacket;
    boolean sentLastPacket;
    boolean stopPacketSpamming;

    @Override
    public void onEnable() {
        blockToMine = null;
        hasSentPackets = false;
        sentFirstPacket = false;
        sentLastPacket = false;
        stopPacketSpamming = false;
    }

    @Override
    public void onUpdate() {
        if (blockToMine == null) {
            eventFacing = null;
            hasSentPackets = false;
            stopPacketSpamming = false;
        }

        if (blockToMine != null && eventFacing != null && AutoCity.INSTANCE.isOff()) {
            //packet mode

            if (mineModeSetting.getValue().equals(MineMode.Packet) && !hasSentPackets) {
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, blockToMine, eventFacing));
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockToMine, eventFacing));
                hasSentPackets = true;
            }

            if (!stopPacketSpamming && hasSentPackets && !autoSwitchSetting.getValue().equals(autoSwitch.Off) && timer.hasReached((long) getBreakTime(blockToMine)) && !sentLastPacket) {
                if (autoSwitchSetting.getValue().equals(autoSwitch.Normal)) {
                    if (waitForNoInteract.getValue() && mc.gameSettings.keyBindUseItem.isKeyDown()) return;
                    InventoryUtil.switchTo(Items.DIAMOND_PICKAXE);
                    stopPacketSpamming = true;
                }
                if (autoSwitchSetting.getValue().equals(autoSwitch.Packet)) {

                }
                sentLastPacket = true;
            }

            //normal mode
            if (mineModeSetting.getValue().equals(MineMode.Normal) && !sentFirstPacket) {
                if (waitForNoInteract.getValue() && mc.gameSettings.keyBindUseItem.isKeyDown()) return;
                InventoryUtil.switchTo(Items.DIAMOND_PICKAXE);
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, blockToMine, eventFacing));
                sentFirstPacket = true;
            }
            if (sentFirstPacket && timer.hasReached((long) getBreakTime(blockToMine))) {
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockToMine, eventFacing));
                sentFirstPacket = false;
            }
            //check if block is air to reset it
            if (BlockUtil.getBlock(blockToMine).equals(Blocks.AIR)) {
                sentLastPacket = false;
                blockToMine = null;
            }
        } else {
            timer.reset();
            switchTimer.reset();
        }
    }

    @SubscribeEvent
    public void onClick(ClickBlockEvent event) {
        if (!BlockUtil.canBreak(event.getPos())) return;
        blockToMine = event.getPos();
        eventFacing = event.getFacing();
    }

    public float getBreakTime(BlockPos posToMine) {
        IBlockState state = mc.world.getBlockState(posToMine);
        ItemStack pick = new ItemStack(Items.DIAMOND_PICKAXE);
        float breakTime = pick.getDestroySpeed(state) / 3.71f;
        return breakTime * 1000;
    }


    public Color getRenderColor() {
        return timer.hasReached((long) getBreakTime(blockToMine) - 200) ? new Color(39, 252, 50, 100) : new Color(210, 24, 33, 100);
    }

    @SubscribeEvent
    public void onRender3d(RenderWorldLastEvent event) {
        if (AutoCity.INSTANCE.isOn() || blockToMine == null || !fullNullCheck()) return;
        if (render.getValue()) RenderUtil.drawBox(RenderUtil.generateBB(blockToMine.getX(), blockToMine.getY(), blockToMine.getZ()), getRenderColor().getAlpha() / 255f, getRenderColor().getGreen() / 255f, getRenderColor().getBlue() / 255f, getRenderColor().getAlpha() / 255f);
    }


    public enum MineMode {
        Packet, Normal
    }

    public enum autoSwitch {
        Normal, Packet, Off
    }
}
