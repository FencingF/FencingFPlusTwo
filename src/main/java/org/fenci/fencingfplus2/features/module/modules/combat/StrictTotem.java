package org.fenci.fencingfplus2.features.module.modules.combat;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.network.PacketEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.util.player.InventoryUtil;

public class StrictTotem extends Module {

    public static StrictTotem INSTANCE;

    public StrictTotem() {
        super("StrictTotem", "AutoTotem for strict servers", Category.Combat);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        if (Offhand.INSTANCE.isOn()) {
            Offhand.INSTANCE.toggle(true);
        }
    }

    @Override
    public String getDisplayInfo() {
        return String.valueOf(InventoryUtil.getItemCount(Items.TOTEM_OF_UNDYING, true));
    }

    @Override
    public void onUpdate() {
        if (mc.currentScreen instanceof GuiContainer && !(mc.currentScreen instanceof GuiInventory)) return;
        int totemslot = InventoryUtil.getItemSlot(Items.TOTEM_OF_UNDYING);
        if (mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING && totemslot != -1) {
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, totemslot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, totemslot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.updateController();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketClickWindow) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
        }
    }
}
