package org.fenci.fencingfplus2.features.module.modules.chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.client.ModuleToggleEvent;
import org.fenci.fencingfplus2.events.network.ConnectionEvent;
import org.fenci.fencingfplus2.events.player.EntitySpawnEvent;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.ClientMessage;
import org.fenci.fencingfplus2.util.client.Timer;
import org.fenci.fencingfplus2.util.player.InventoryUtil;

public class Notifier extends Module {

    public static Notifier INSTANCE;

    public Notifier() {
        super("Notifier", "Notifies you of stuff", Category.Chat);
        INSTANCE = this;
    }

    public final Setting<Boolean> modules = new Setting<>("Modules", true);
    public final Setting<Boolean> totemPops = new Setting<>("TotemPops", true);
    public final Setting<Boolean> visualrange = new Setting<>("VisualRange", true);
    public final Setting<Boolean> logOuts = new Setting<>("LogOuts", false);
    public final Setting<Boolean> totemwarning = new Setting<>("TotemFailWarning", false);
    private final Timer timer = new Timer();

    @Override
    public void onUpdate() {
        if (mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING && InventoryUtil.getItemCount(Items.TOTEM_OF_UNDYING, true) != 0 && totemwarning.getValue()) {
            if (timer.hasReached(100)) {
                ClientMessage.sendOverwriteClientMessage("There is no totem in your offhand!");
            }
        }
        if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            timer.reset();
        }
    }

    @SubscribeEvent
    public void onModuleToggled(ModuleToggleEvent event) {
        if (this.modules.getValue()) {
            Module module = event.getModule();
            ClientMessage.sendMessage(module.getName() + ChatFormatting.BOLD + (module.isToggled() ? ChatFormatting.GREEN + " Enabled!" : ChatFormatting.RED + " Disabled!"));
        }
    }

    @SubscribeEvent
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (!fullNullCheck()) return;
        if (visualrange.getValue() && event.getEntity() instanceof EntityPlayer && event.getEntity() != mc.player && event.getEntity() != null) {
            if (event.getType().equals(EntitySpawnEvent.Type.Spawn)) {
                if (event.getEntity().equals(getFencing().friendManager.getPlayer())) {
                    ClientMessage.sendOverwriteClientMessage(ChatFormatting.AQUA + event.getEntity().getName() + ChatFormatting.RESET + " has entered visual range!");
                } else {
                    ClientMessage.sendOverwriteClientMessage(event.getEntity().getName() + " has entered visual range!");
                }
            }
            if (event.getType().equals(EntitySpawnEvent.Type.Despawn)) {
                if (event.getEntity().equals(getFencing().friendManager.getPlayer())) {
                    ClientMessage.sendOverwriteClientMessage(ChatFormatting.AQUA + event.getEntity().getName() + ChatFormatting.RESET + " has left visual range!");
                } else {
                    ClientMessage.sendOverwriteClientMessage(event.getEntity().getName() + " has left visual range!");
                }
            }
        }
    }

    @SubscribeEvent
    public void onConnectionEvent(ConnectionEvent event) {
        if (event.getEntity() != mc.player && logOuts.getValue()) {
            if (event.getType().equals(ConnectionEvent.Type.Join)) {
                if (event.getEntity().equals(getFencing().friendManager.getPlayer())) {
                    ClientMessage.sendOverwriteClientMessage(ChatFormatting.AQUA + event.getEntity().getName() + ChatFormatting.RESET + " has just logged in!");
                } else {
                    ClientMessage.sendOverwriteClientMessage(event.getEntity().getName() + " has just logged in!");
                }
            }
            if (event.getType().equals(ConnectionEvent.Type.Leave)) {
                if (event.getEntity().equals(getFencing().friendManager.getPlayer())) {
                    ClientMessage.sendOverwriteClientMessage(ChatFormatting.AQUA + event.getEntity().getName() + ChatFormatting.RESET + " has just logged out!");
                } else {
                    ClientMessage.sendOverwriteClientMessage(event.getEntity().getName() + " has just logged out!");
                }
            }
        }
    }
}
