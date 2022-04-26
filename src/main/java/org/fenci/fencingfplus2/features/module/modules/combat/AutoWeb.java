package org.fenci.fencingfplus2.features.module.modules.combat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.player.InventoryUtil;
import org.fenci.fencingfplus2.util.player.PlayerUtil;
import org.fenci.fencingfplus2.util.world.BlockUtil;

public class AutoWeb extends Module {
    public AutoWeb() {
        super("AutoWeb", "Automatically webs players", Category.Combat);
    }

    public static final Setting<Float> range = new Setting<>("Range", 5f, 1, 8);
    //public static final Setting<Boolean> silent = new Setting<>("Silent", true);

    @Override
    public void onUpdate() {
        EntityPlayer target = PlayerUtil.findClosestTarget();
        if (target == null) return;
        if (target.getDistance(mc.player) > range.getValue() || !InventoryUtil.isInHotbar(Item.getItemFromBlock(Blocks.WEB)) || target.isInWater() || target.isInLava() || target.isInWeb || !BlockUtil.getBlock(target.getPosition()).equals(Blocks.AIR)) return;
        int oldSlot = mc.player.inventory.currentItem;
        InventoryUtil.switchToSlot(InventoryUtil.getHotbarSlot(Item.getItemFromBlock(Blocks.WEB)), true);
        mc.player.setVelocity(0.0D, 0.0D, 0.0D);
        BlockUtil.placeBlock(target.getPosition(), EnumHand.MAIN_HAND, true, false);
        mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));

    }

//    public Set<BlockPos> placePos(EntityPlayer player) {
//        Set<BlockPos> positions = new HashSet<>();
//
//        BlockPos pos = player.getPosition();
//
//        for (EnumFacing facing : EnumFacing.values()) {
//            if (!BlockUtil.canPlaceBlock(pos.offset(facing))) continue;
//            positions.add(pos.offset(facing));
//        }
//        return positions;
//    }
}
