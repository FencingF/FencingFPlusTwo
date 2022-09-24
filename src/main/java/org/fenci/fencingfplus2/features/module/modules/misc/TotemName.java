package org.fenci.fencingfplus2.features.module.modules.misc;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.client.OptionChangeEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.manager.friend.Friend;
import org.fenci.fencingfplus2.manager.friend.FriendManager;
import org.fenci.fencingfplus2.setting.Setting;

import java.util.Random;

public class TotemName extends Module {
    public static final Setting<Name> name = new Setting<>("Mode", Name.MiniMe);

    public TotemName() {
        super("TotemName", "Changes your totems name to your name", Category.Misc);
    }

    Friend friend;

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

                if (name.getValue().equals(Name.Afriend)) {

                    if (friend == null) return;
                    itemStack.setStackDisplayName(friend.getAlias());
                }
            }
        }
    }

    @SubscribeEvent
    public void onOption(OptionChangeEvent event) {
        if (event.getOption().equals(name)) {
            if (name.getValue().equals(Name.Afriend)) {
                Random random = new Random();
                if (getFencing().friendManager.getFriends().isEmpty()) return;
                friend = getFencing().friendManager.getFriends().get(random.nextInt(getFencing().friendManager.getFriends().size()));
            }
        }

    }

    public enum Name {
        MiniMe, Totem, Name, JOEMAMA, Afriend
    }
}

