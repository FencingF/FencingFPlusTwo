package org.fenci.fencingfplus2.features.module.modules.misc;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.Timer;
import org.fenci.fencingfplus2.util.player.InventoryUtil;

import java.util.HashMap;
import java.util.Map;

public class HotbarReplenish extends Module {
    public HotbarReplenish() {
        super("HotbarReplenish", "Refills your hotbar.", Category.Misc);
    }

    public static final Setting<Integer> count = new Setting<>("Count", 32, 1, 63);
    public static final Setting<Float> delay = new Setting<>("Delay", 3.0f, 0.0, 10.0);

    Timer delayTimer = new Timer();

    @Override
    public void onUpdate() {
        if (mc.currentScreen instanceof GuiContainer && !(mc.currentScreen instanceof GuiInventory)) return;
        if (getHotbarSlots().isEmpty() || getClickSlots().isEmpty()) return;
        for (Integer hotbarSlot : getClickSlots().values()) {
            if (delayTimer.hasReached(delay.getValue().longValue() * 50)) {
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, hotbarSlot, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, getClickSlots().get(hotbarSlot), 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, hotbarSlot, 0, ClickType.PICKUP, mc.player);
                mc.playerController.updateController();
            }
            delayTimer.reset();
        }
    }

    public Map<ItemStack, Integer> getHotbarSlots() {
        Map<ItemStack, Integer> hotbarSlots = new HashMap<>();
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getMaxStackSize() < 2) continue;
            if (mc.player.inventory.getStackInSlot(i).stackSize > count.getValue()) continue;
            if (mc.player.inventory.getStackInSlot(i).getItem().equals(Items.AIR)) continue;
            hotbarSlots.put(mc.player.inventory.getStackInSlot(i), i);
        }
        return hotbarSlots;
    }

    public Map<Integer, Integer> getClickSlots() { //first is inv slot second is hotbar slot
        Map<Integer, Integer> clickSlots = new HashMap<>();
        for (ItemStack stack1 : getHotbarSlots().keySet()) {
            clickSlots.put(InventoryUtil.findItemWithName(stack1.getDisplayName(), stack1.getItem()), InventoryUtil.getHotbarSlot(stack1.getItem()));
        }
        return clickSlots;
    }
}