package org.fenci.fencingfplus2.util.player;

import net.minecraft.inventory.ClickType;
import net.minecraft.item.*;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import org.fenci.fencingfplus2.util.Globals;

import java.util.Objects;

public class InventoryUtil implements Globals {

    public static int itemCount;

    public static int getItemSlot(Item items) {
        for (int i = 0; i < 36; ++i) {
            Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item == items) {
                if (i < 9) {
                    i += 36;
                }

                return i;
            }
        }
        return -1;
    }

    public static int getHotbarSlot(Item items) {
        int slot = -1;
        for (int i = 0; i < 9; i++) {
            Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item == items) {
                slot = i;
                break;
            }
        }
        return slot;
    }

    public static int getSkullSlot() {
        int slot = -1;
        for (int i = 0; i < 9; i++) {
            Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item instanceof ItemSkull) {
                slot = i;
                break;
            }
        }
        return slot;
    }

    public static boolean isHolding(Item item) {
        return mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem().equals(item) || mc.player.getHeldItem(EnumHand.OFF_HAND).getItem().equals(item);
    }

    public static int findItemWithName(String itemName, Item item) {
        int slot = -1;
        for (int i = 9; i < 36; i++) {
            Item item1 = mc.player.inventory.getStackInSlot(i).getItem();
            String item1Name = mc.player.inventory.getStackInSlot(i).getDisplayName();
            if (item1 == item && Objects.equals(itemName, item1Name)) {
                slot = i;
                break;
            }
        }
        return slot;
    }

    public static int getItemCount(Item item, boolean includeOffhand) {
        if (!includeOffhand) {
            itemCount = mc.player.inventory.mainInventory.stream().filter(stack -> stack.getItem() == item).mapToInt(ItemStack::getCount).sum();
        } else if (includeOffhand) {
            itemCount = mc.player.inventory.mainInventory.stream().filter(stack -> stack.getItem() == item).mapToInt(ItemStack::getCount).sum() + mc.player.inventory.offHandInventory.stream().filter(stack -> stack.getItem() == item).mapToInt(ItemStack::getCount).sum();
        }
        return itemCount;
    }

    public static void clickSlot(int id) {
        if (id != -1) {
            try {
                mc.playerController.windowClick(mc.player.openContainer.windowId, getClickSlot(id), 0, ClickType.PICKUP, mc.player);
            } catch (Exception ignored) {

            }
        }
    }

    public static void clickSlot(int id, int otherRows) {
        if (id != -1) {
            try {
                mc.playerController.windowClick(mc.player.openContainer.windowId, getClickSlot(id) + otherRows, 0, ClickType.PICKUP, mc.player);
            } catch (Exception ignored) {

            }
        }
    }

    public static int getClickSlot(int id) {
        if (id == -1) {
            return id;
        }

        if (id < 9) {
            id += 36;
            return id;
        }

        if (id == 39) {
            id = 5;
        } else if (id == 38) {
            id = 6;
        } else if (id == 37) {
            id = 7;
        } else if (id == 36) {
            id = 8;
        } else if (id == 40) {
            id = 45;
        }

        return id;
    }

    public static ItemStack getItemStack(int id) {
        try {
            return mc.player.inventory.getStackInSlot(id);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static void switchToSlot(int slot) {
        if (slot != -1) {
            mc.player.inventory.currentItem = slot;
        }
    }

    public static void switchToSlot(final int slot, final boolean silent) {
        mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        if (!silent) mc.player.inventory.currentItem = slot;

//        if (mc.player.connection.getNetworkManager().isChannelOpen()) {
//            mc.player.connection.getNetworkManager().processReceivedPackets();
//        } else {
//            mc.player.connection.getNetworkManager().handleDisconnection();
//        }
    }

    public static boolean isInHotbar(Item item) {
        for (int i = 0; i < 9; ++i) {
            mc.player.inventory.getStackInSlot(i);
            if (mc.player.inventory.getStackInSlot(i).getItem() == item) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSkullInHotbar() {
        for (int i = 0; i < 9; ++i) {
            mc.player.inventory.getStackInSlot(i);
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemSkull) {
                return true;
            }
        }
        return false;
    }

    public static int findToolsInHotbar() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            mc.player.inventory.getStackInSlot(i);
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemSword || mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemTool) {
                slot = i;
                break;
            }
        }
        return slot;
    }

    public static void switchTo(Item item) {
        if (isInHotbar(item)) {
            for (int a = 0; a < 9; a++) {
                if (mc.player.inventory.getStackInSlot(a).getItem() == item) {
                    mc.player.inventory.currentItem = a;
                }
            }
        }
    }
}
