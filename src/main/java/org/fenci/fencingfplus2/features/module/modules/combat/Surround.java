package org.fenci.fencingfplus2.features.module.modules.combat;

import net.minecraft.block.BlockContainer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.ClientMessage;
import org.fenci.fencingfplus2.util.client.Timer;
import org.fenci.fencingfplus2.util.player.InventoryUtil;
import org.fenci.fencingfplus2.util.player.PlayerUtil;
import org.fenci.fencingfplus2.util.render.RenderUtil;
import org.fenci.fencingfplus2.util.world.BlockUtil;
import org.fenci.fencingfplus2.util.world.CityUtil;
import org.fenci.fencingfplus2.util.world.HoleUtil;

import static org.fenci.fencingfplus2.util.world.BlockUtil.getRenderColor;

public class Surround extends Module {
    public Surround() {
        super("Surround", "Automatically surrounds you with obsidian", Category.Combat);
    }

    public static final Setting<Double> delay = new Setting<>("Delay", 0.0, 0.0, 3);
    public static final Setting<Boolean> render = new Setting<>("Render", true);
    public static final Setting<Boolean> renderAfterDone = new Setting<>("RenderAfterComplete", true);
    public static final Setting<Boolean> rotate = new Setting<>("Rotate", true);
    public static final Setting<Boolean> supportingBlocks = new Setting<>("SupportBlocks", true);
    public static final Setting<Boolean> antiGhost = new Setting<>("AntiGhost", true);
    public static final Setting<Center> centerMode = new Setting<>("CenterMode", Center.Motion);
    public static final Setting<WhenDisable> disableOn = new Setting<>("DisableOn", WhenDisable.OutOfHole);

    Timer delayTimer = new Timer();

    @Override
    public void onEnable() {
        if (!centerMode.getValue().equals(Center.Off)) {
            if (centerMode.getValue().equals(Center.TP)) {
                mc.player.setVelocity(0.0D, 0.0D, 0.0D);
                mc.player.setPosition(Math.floor(mc.player.posX) + 0.5, mc.player.posY, Math.floor(mc.player.posZ) + 0.5);
                mc.player.connection.sendPacket(new CPacketPlayer.Position(Math.floor(mc.player.posX) + 0.5, mc.player.posY, Math.floor(mc.player.posZ) + 0.5, true));
                delayTimer.reset();
            } else {
                mc.player.motionX = (Math.floor(mc.player.posX) + 0.5D - mc.player.posX) / 2.0D;
                mc.player.motionZ = (Math.floor(mc.player.posZ) + 0.5D - mc.player.posZ) / 2.0D;
                delayTimer.reset();
            }
        }
    }
    //TODO: Fix placing when entites are where the block goes
    @Override
    public void onUpdate() {
        if (!InventoryUtil.isInHotbar(Item.getItemFromBlock(Blocks.OBSIDIAN))) {
            ClientMessage.sendModuleMessage("Surround", "Could not find obsidian in hotbar. Disabling!");
            this.toggle(true);
        }
//        if (CityUtil.getSurroundPositions().isEmpty()) {
//            ClientMessage.sendMessage("Empty");
//        }

        int previousSlot = Surround.mc.player.inventory.currentItem;
        if (!HoleUtil.isInHole(mc.player) && InventoryUtil.isInHotbar(Item.getItemFromBlock(Blocks.OBSIDIAN))) {
            for (BlockPos pos : CityUtil.getSurroundPositions()) {
                if ((delayTimer.hasReached((long) (delay.getValue() * 100)) || delay.getValue() == 0) && BlockUtil.getBlock(pos).equals(Blocks.AIR)) {
                    InventoryUtil.switchTo(Item.getItemFromBlock(Blocks.OBSIDIAN));
                    BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, rotate.getValue(), false);
                    InventoryUtil.switchToSlot(previousSlot);
                    if (antiGhost.getValue() && !(BlockUtil.getBlock(pos) instanceof BlockContainer)) {
                        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, pos, EnumFacing.UP));
                    }
                    delayTimer.reset();
                }
            }
        }

        if (disableOn.getValue().equals(WhenDisable.OnMove) && mc.gameSettings.keyBindJump.isKeyDown() || mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() && (HoleUtil.isInHole(mc.player))) { //removed && !enableInHole.getValue() from the HoleUtil.isInHole paranthese
            this.toggle(true);
        } else if (disableOn.getValue().equals(WhenDisable.OnComplete) && HoleUtil.isInHole(mc.player)) { //removed && !enableInHole.getValue()
            this.toggle(true);
        } else if (disableOn.getValue().equals(WhenDisable.OutOfHole) && !HoleUtil.isInHole(mc.player)) {
            this.toggle(true);
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (render.getValue() && this.isOn()) {
            for (BlockPos pos : CityUtil.getSurroundPositions()) {
                if (!CityUtil.getSurroundPositions().isEmpty()) {
                    RenderUtil.drawBox(RenderUtil.generateBB(pos.getX(), pos.getY(), pos.getZ()), getRenderColor(pos).getRed() / 255f, getRenderColor(pos).getGreen() / 255f, getRenderColor(pos).getBlue() / 255f, getRenderColor(pos).getAlpha() / 255f);
                }
            }
            BlockPos pos = PlayerUtil.getPlayerPos(mc.player);
            for (EnumFacing facing : EnumFacing.values()) {
                if (facing.equals(EnumFacing.UP) || facing.equals(EnumFacing.DOWN)) continue;
                if (CityUtil.getSurroundPositions().isEmpty() && renderAfterDone.getValue()) {
                    BlockPos neighbor = pos.offset(facing);
                    RenderUtil.drawBox(RenderUtil.generateBB(neighbor.getX(), neighbor.getY(), neighbor.getZ()), getRenderColor(neighbor).getRed() / 255f, getRenderColor(neighbor).getGreen() / 255f, getRenderColor(neighbor).getBlue() / 255f, getRenderColor(neighbor).getAlpha() / 255f);
                }
            }
        }
    }

    @Override
    public void onDisable() {
        delayTimer.reset();
    }

    public enum Center {
        Motion, TP, Off
    }

    public enum WhenDisable {
        OutOfHole, OnMove, OnComplete
    }
}
