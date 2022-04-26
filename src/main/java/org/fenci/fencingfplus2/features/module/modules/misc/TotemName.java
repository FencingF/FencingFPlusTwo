package org.fenci.fencingfplus2.features.module.modules.misc;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;

public class TotemName extends Module {
    public TotemName() {
        super("TotemName", "Changes your totems name to your name", Category.Misc);
    }

    public static final Setting<Name> name = new Setting<>("Mode", Name.MiniMe);

    @Override
    public void onUpdate() {
        for (int i = 0; i < 45; i++) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem().equals(Items.TOTEM_OF_UNDYING)) {
                if (name.getValue().equals(Name.MiniMe)) {
                    itemStack.setStackDisplayName(mc.player.getName() + "'s Mini-Me");
                }
                if (name.getValue().equals(Name.Totem)) {
                    itemStack.setStackDisplayName(mc.player.getName() + "'s totem");
                }
                if (name.getValue().equals(Name.Name)) {
                    itemStack.setStackDisplayName(mc.player.getName());
                }
                if (name.getValue().equals(Name.JOEMAMA)) {
                    itemStack.setStackDisplayName("JOEMAMA");
                }
            }
        }
    }

    public enum Name {
        MiniMe, Totem, Name, JOEMAMA
    }
}
