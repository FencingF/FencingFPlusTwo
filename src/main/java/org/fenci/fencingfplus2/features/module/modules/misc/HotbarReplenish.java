package org.fenci.fencingfplus2.features.module.modules.misc;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.Timer;
import org.fenci.fencingfplus2.util.player.InventoryUtil;

import java.util.HashMap;
import java.util.Map;

public class HotbarReplenish extends Module {
    public static final Setting<Integer> count = new Setting<>("Count", 32, 1, 63);
    public static final Setting<Float> delay = new Setting<>("Delay", 3.0f, 0.0, 10.0);
    Timer delayTimer = new Timer();

    public HotbarReplenish() {
        super("HotbarReplenish", "Refills your hotbar.", Category.Misc);
    }



}
