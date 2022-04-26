package org.fenci.fencingfplus2.features.module.modules.combat;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.client.OptionChangeEvent;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.features.module.modules.movement.NoSlow;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.ClientMessage;
import org.fenci.fencingfplus2.util.player.InventoryUtil;
import org.fenci.fencingfplus2.util.player.PlayerUtil;

public class Offhand extends Module {

    public static Offhand INSTANCE;

    public Offhand() {
        super("Offhand","Allows you to manage your offhand for pvp", Category.Combat);
        INSTANCE = this;
    }

    public static final Setting<Integer> switchAt = new Setting<>("Health", 20, 0, 36);
    public static final Setting<Boolean> rightClickSwordGap = new Setting<>("SwordGap", true);
    public static final Setting<OffItem> itemToHold = new Setting<>("OffhandItem", OffItem.Totems);
    public static final Setting<Display> display = new Setting<>("Display", Display.Item);

    @Override
    public void onEnable() {
        if (StrictTotem.INSTANCE.isOn()) {
            StrictTotem.INSTANCE.toggle(true);
        }
    }

    @Override
    public String getDisplayInfo() {
        return display.getValue().equals(Display.Item) ? itemToHold.getValue().toString() : String.valueOf(InventoryUtil.getItemCount(Items.TOTEM_OF_UNDYING, true));
    }

    @Override
    public void onUpdate() {
        if (mc.currentScreen instanceof GuiContainer && !(mc.currentScreen instanceof GuiInventory)) return;
        if (mc.player.getHeldItemOffhand().getItem() != getOffhandItem() && InventoryUtil.getItemCount(getOffhandItem(), false) != 0) {
            int slot = InventoryUtil.getItemSlot(getOffhandItem());
            if (slot != -1) {
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
                mc.playerController.updateController();
            }
        }
    }

    public Item getOffhandItem() {
        Item itemOffhand;
        if (mc.player.fallDistance > 15f || PlayerUtil.getPlayerHealth(mc.player) <= switchAt.getValue() || itemToHold.getValue().equals(OffItem.Totems)) {
            itemOffhand = Items.TOTEM_OF_UNDYING;
        } else if (itemToHold.getValue().equals(OffItem.Gapples) || rightClickSwordGap.getValue() && mc.gameSettings.keyBindUseItem.isKeyDown() && mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD) {
            itemOffhand = Items.GOLDEN_APPLE;
        } else {
            itemOffhand = Items.END_CRYSTAL;
        }
        return itemOffhand;
    }

    @SubscribeEvent
    public void onOptionChangeEvent(OptionChangeEvent event) {
        if (event.getOption().equals(itemToHold) && itemToHold.getValue().equals(OffItem.Crystals) && NoSlow.INSTANCE.isOn() && NoSlow.bypass.getValue().equals(NoSlow.Bypass.FencingF)) {
            ClientMessage.sendModuleMessage("Offhand", "You have NoSlow bypass mode \"FencingF\" on. This will not allow you to multitask crystals. Please change this mode to fix this.");
        }
    }

    public enum OffItem {
        Totems, Crystals, Gapples
    }

    public enum Display {
        Item, Count
    }
}
