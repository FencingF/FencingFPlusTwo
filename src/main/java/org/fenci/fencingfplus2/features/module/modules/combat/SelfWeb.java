package org.fenci.fencingfplus2.features.module.modules.combat;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.player.InventoryUtil;
import org.fenci.fencingfplus2.util.player.PlayerUtil;
import org.fenci.fencingfplus2.util.world.BlockUtil;

public class SelfWeb extends Module {

    public static final Setting<Boolean> enableInHole = new Setting<>("EnableInHole", false);
    public static SelfWeb INSTANCE;

    public SelfWeb() {
        super("SelfWeb", "Automatically puts you in a web", Category.Combat);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (PlayerUtil.isInBurrow(mc.player) || !InventoryUtil.isInHotbar(Item.getItemFromBlock(Blocks.WEB))) {
            this.toggle(true);
            return;
        }
        int oldslot = mc.player.inventory.currentItem;
        InventoryUtil.switchToSlot(InventoryUtil.getHotbarSlot(Item.getItemFromBlock(Blocks.WEB)), true);
        BlockUtil.placeBlock(new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ)), EnumHand.MAIN_HAND, true, true, true);
        mc.player.connection.sendPacket(new CPacketHeldItemChange(oldslot));
        if (this.isOn()) this.toggle(true);
    }
}
