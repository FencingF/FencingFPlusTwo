package org.fenci.fencingfplus2.features.module.modules.player;

import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumHand;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.Globals;
import org.fenci.fencingfplus2.util.player.InventoryUtil;

public class FastPlace extends Module {

    public FastPlace() {
        super("FastPlace", "Places items faster", Category.Player);
    }
    // items
    public static final Setting<Boolean> xp = new Setting<>("XP", true);
    public static final Setting<Boolean> blocks = new Setting<>("Blocks", false);
    public static final Setting<Boolean> crystals = new Setting<>("Crystals", false);

    @Override
    public void onUpdate() {
        if ((xp.getValue() && InventoryUtil.isHolding(Items.EXPERIENCE_BOTTLE)) || blocks.getValue() && (mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBlock || mc.player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemBlock) || (crystals.getValue() && InventoryUtil.isHolding(Items.END_CRYSTAL))) {
            Globals.mc.rightClickDelayTimer = 0;
        }
    }
}
