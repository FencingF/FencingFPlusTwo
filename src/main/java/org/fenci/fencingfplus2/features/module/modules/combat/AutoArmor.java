package org.fenci.fencingfplus2.features.module.modules.combat;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.fenci.fencingfplus2.features.module.Module;

public class AutoArmor extends Module {

    public static AutoArmor INSTANCE;

    public AutoArmor() {
        super("AutoArmor", "Automatically equips armor", Category.Combat);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (!(mc.player == null || mc.world == null)) {
            if (mc.player.ticksExisted % 2 == 0) return;

            if (mc.currentScreen instanceof GuiContainer && !(mc.currentScreen instanceof InventoryEffectRenderer))
                return;
            int[] bestArmorSlots = new int[4];
            int[] bestArmorValues = new int[4];


            for (int armorType = 0; armorType < 4; armorType++) {
                ItemStack oldArmor = mc.player.inventory.armorItemInSlot(armorType);

                if (oldArmor.getItem() instanceof ItemArmor)
                    bestArmorValues[armorType] =
                            ((ItemArmor) oldArmor.getItem()).damageReduceAmount;

                bestArmorSlots[armorType] = -1;
            }


            for (int slot = 0; slot < 36; slot++) {
                ItemStack stack = mc.player.inventory.getStackInSlot(slot);

                if (stack.getCount() > 1)
                    continue;

                if (!(stack.getItem() instanceof ItemArmor))
                    continue;

                ItemArmor armor = (ItemArmor) stack.getItem();
                int armorType = armor.armorType.ordinal() - 2;

                if (armorType == 2 && mc.player.inventory.armorItemInSlot(armorType).getItem().equals(Items.ELYTRA))
                    continue;

                int armorValue = armor.damageReduceAmount;

                if (armorValue > bestArmorValues[armorType]) {
                    bestArmorSlots[armorType] = slot;
                    bestArmorValues[armorType] = armorValue;
                }
            }
            for (int armorType = 0; armorType < 4; armorType++) {
                int slot = bestArmorSlots[armorType];
                if (slot == -1) continue;
                ItemStack oldArmor = mc.player.inventory.armorItemInSlot(armorType);
                if (oldArmor != ItemStack.EMPTY || mc.player.inventory.getFirstEmptyStack() != -1) {
                    if (slot < 9) slot += 36;
                    mc.playerController.windowClick(0, 8 - armorType, 0, ClickType.QUICK_MOVE, mc.player);
                    mc.playerController.windowClick(0, slot, 0, ClickType.QUICK_MOVE, mc.player);
                    break;
                }
            }
        }
    }
}
