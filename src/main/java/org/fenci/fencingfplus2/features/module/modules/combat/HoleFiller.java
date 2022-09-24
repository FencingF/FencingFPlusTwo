package org.fenci.fencingfplus2.features.module.modules.combat;

import net.minecraft.block.BlockContainer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.Timer;
import org.fenci.fencingfplus2.util.math.CrystalUtil;
import org.fenci.fencingfplus2.util.player.InventoryUtil;
import org.fenci.fencingfplus2.util.world.BlockUtil;
import org.fenci.fencingfplus2.util.world.HoleUtil;

import java.util.HashSet;
import java.util.Set;

public class HoleFiller extends Module {
    //public static final Setting<Mode> mode = new Setting<>("Mode", Mode.Normal);
    public static final Setting<Float> delay = new Setting<>("Delay", 0f, 0, 3);
    public static final Setting<Float> range = new Setting<>("Range", 5f, 1, 6);
    //public static final Setting<Float> robotDistance = new Setting<>("RobotDistance", 0.5f, 0.1, 3);
    public static final Setting<Boolean> swing = new Setting<>("Swing", true);
    public static final Setting<Boolean> antiGhost = new Setting<>("AntiGhost", false);
    public static final Setting<Boolean> rayTrace = new Setting<>("Raytrace", false);
    public static final Setting<Boolean> rotate = new Setting<>("Rotate", true);
    public static final Setting<Boolean> toggle = new Setting<>("Toggle", true);
    Timer delayTimer = new Timer();

    public HoleFiller() {
        super("HoleFiller", "Automatically fills holes", Category.Combat);
    }

    @Override
    public void onUpdate() {
        for (BlockPos hole : getHoles()) {
            if (!InventoryUtil.isInHotbar(Item.getItemFromBlock(Blocks.OBSIDIAN))) continue;
            if ((delayTimer.hasReached((long) (delay.getValue() * 100)) || delay.getValue() == 0) && BlockUtil.getBlock(hole).equals(Blocks.AIR)) {
                int oldSlot = mc.player.inventory.currentItem;
                InventoryUtil.switchTo(Item.getItemFromBlock(Blocks.OBSIDIAN));
                BlockUtil.placeBlock(hole, EnumHand.MAIN_HAND, rotate.getValue(), false, swing.getValue());
                InventoryUtil.switchToSlot(oldSlot);
            }
            if (antiGhost.getValue() && !(BlockUtil.getBlock(hole) instanceof BlockContainer)) {
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, hole, EnumFacing.UP));
            }
            delayTimer.reset();
        }
        //if (toggle.getValue() && getHoles().isEmpty() && !mode.getValue().equals(Mode.Robot)) this.toggle(true);
        if (toggle.getValue() && getHoles().isEmpty()) this.toggle(true);
    }

    public Set<BlockPos> getHoles() {
        Set<BlockPos> holes = new HashSet<>();
        //if (mode.getValue().equals(Mode.Normal)) {
            for (BlockPos hole : HoleUtil.getAllHoles(range.getValue())) {
                if (!CrystalUtil.canSeePos(hole) && rayTrace.getValue()) continue;
                holes.add(hole);
            }
        //}
//        if (mode.getValue().equals(Mode.Robot)) {
//            holes.addAll(HoleUtil.allPlayersCloseToHoles(range.getValue(), robotDistance.getValue()));
//        }

        return holes;
    }

//    public enum Mode {
//        Normal, Robot
//    }
}
