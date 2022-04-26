package org.fenci.fencingfplus2.features.module.modules.combat;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
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

import java.awt.*;

import static org.fenci.fencingfplus2.util.world.CityUtil.cityBlockToMine;

public class AutoCity extends Module {
    public static AutoCity INSTANCE;

    public AutoCity() {
        super("AutoCity", "Automatically cities people", Category.Combat);
        INSTANCE = this;
    }

    public static final Setting<Integer> range = new Setting<>("Range", 5, 1, 10);
    public static final Setting<Boolean> render = new Setting<>("Render", true);
    //public static final Setting<Boolean> abortOnSwitch = new Setting<>("AbortOnSwitch", true);
    public static final Setting<MineMode> mineMode = new Setting<>("MineMode", MineMode.Normal);
    public static final Setting<Switch> switchSetting = new Setting<>("Switch", Switch.Normal);

    BlockPos posToMine;
    IBlockState currentBlockState;
    boolean sendSecondPacket;
    Timer delay = new Timer();
    Timer switchBackDelay = new Timer();
    int oldSlot;
    boolean readyToSwitchBack;
    boolean readyToToggle;

    @Override
    public void onEnable() {
        oldSlot = mc.player.inventory.currentItem;
        readyToSwitchBack = false;
        sendSecondPacket = false;
        readyToToggle = false;
        switchBackDelay.reset();
        if (CityUtil.cityBlockToMine() != null) {
            posToMine = cityBlockToMine();
            if (mineMode.getValue().equals(MineMode.Packet)) {
                AutoCity.mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, posToMine, EnumFacing.UP));
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, posToMine, EnumFacing.UP));
                delay.reset();
            }
            if (mineMode.getValue().equals(MineMode.Normal)) {
                InventoryUtil.switchTo(Items.DIAMOND_PICKAXE);
                AutoCity.mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, posToMine, EnumFacing.UP));
                delay.reset();
                sendSecondPacket = true;
                switchSetting.setValue(Switch.Normal);
            }
        } else if (CityUtil.cityBlockToMine() == null) {
            ClientMessage.sendOverwriteClientMessage("Could not find any blocks to mine. Disabling");
            this.toggle(true);
        }
    }

    @Override
    public void onUpdate() {
        if (CityUtil.cityBlockToMine() != null && posToMine != null) {
            if (PlayerUtil.getDistance(mc.player, posToMine) > range.getValue()) {
                ClientMessage.sendOverwriteClientMessage("Out of range. Disabling");
                this.toggle(true);
            }

            if (BlockUtil.getBlock(posToMine).equals(Blocks.AIR)) {
                ClientMessage.sendOverwriteClientMessage("Done mining. Disabling");
                this.toggle(true);
            }

            if (delay.hasReached((long) (getBreakTime())) && sendSecondPacket && mineMode.getValue().equals(MineMode.Normal)) {
                InventoryUtil.switchTo(Items.DIAMOND_PICKAXE);
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, posToMine, EnumFacing.UP));
                delay.reset();
                readyToSwitchBack = true;
            }

            if (mineMode.getValue().equals(MineMode.Packet) && delay.hasReached((long) (getBreakTime())) && this.isOn()) {
                if (!switchSetting.getValue().equals(Switch.Off)) {
                    InventoryUtil.switchTo(Items.DIAMOND_PICKAXE);
                    delay.reset();
                    this.toggle(true);
                }
            }

        } else if (CityUtil.cityBlockToMine() == null) {
            ClientMessage.sendOverwriteClientMessage("Could not find any blocks to mine. Disabling");
            this.toggle(true);
        }

        if (readyToSwitchBack) {
            if (switchBackDelay.hasReached(100)) {
                InventoryUtil.switchToSlot(oldSlot);
                readyToToggle = true;
            }
        }

        if (!readyToSwitchBack) {
            switchBackDelay.reset();
        }
    }

    float breakTime;

    public float getBreakTime() {
        this.currentBlockState = mc.world.getBlockState(posToMine);
        final ItemStack pick = new ItemStack(Items.DIAMOND_PICKAXE);
        breakTime = pick.getDestroySpeed(this.currentBlockState) / 3.71f;
        return breakTime * 1000;
    }

    public Color getRenderColor() {
        return !delay.hasReached((long) (getBreakTime() - 200)) ? new Color(210, 24, 33, 100) : new Color(39, 252, 50, 100);
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (render.getValue() && this.isOn() && PlayerUtil.findClosestTarget() != null && CityUtil.cityBlockToMine() != null) {
            RenderUtil.drawBox(RenderUtil.generateBB(posToMine.getX(), posToMine.getY(), posToMine.getZ()), getRenderColor().getRed() / 255f, getRenderColor().getGreen() / 255f, getRenderColor().getBlue() / 255f, 100 / 255f);
        }
    }

    @Override
    public void onDisable() {
        sendSecondPacket = false;
        readyToSwitchBack = false;
        readyToToggle = false;
        switchBackDelay.reset();
    }

    public enum Switch {
        Normal, Off //switchback is temp broken
    }

    public enum MineMode {
        Normal, Packet
    }
}
