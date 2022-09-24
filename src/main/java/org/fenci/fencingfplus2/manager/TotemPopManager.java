package org.fenci.fencingfplus2.manager;

import com.mojang.realmsclient.gui.ChatFormatting;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.player.DeathEvent;
import org.fenci.fencingfplus2.events.player.TotemPopEvent;
import org.fenci.fencingfplus2.features.module.modules.chat.Notifier;
import org.fenci.fencingfplus2.util.Globals;
import org.fenci.fencingfplus2.util.client.ClientMessage;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TotemPopManager implements Globals {

    private final Map<EntityPlayer, Integer> pops = new ConcurrentHashMap<>();
    private final Set<EntityPlayer> announcers = new ConcurrentSet<>();
    String totemSpelling;

    public void onUpdate() {
        if (!this.announcers.isEmpty() && Notifier.INSTANCE.isToggled() && Notifier.INSTANCE.totemPops.getValue()) {
            for (EntityPlayer player : this.announcers) {
                int count = this.pops.getOrDefault(player, -1);
                if (count != -1) {
                    if (count == 1) {
                        totemSpelling = " totem!";
                    } else if (count > 1) {
                        totemSpelling = " totems!";
                    }
                    if (player.getName().equals(mc.player.getName())) {
                        ClientMessage.sendMessage(ChatFormatting.LIGHT_PURPLE + "I" + ChatFormatting.RESET + " have popped " + ChatFormatting.LIGHT_PURPLE + count + ChatFormatting.RESET + totemSpelling);
                    }
                    if (player.getName().equals("Flixzit") || player.getName().equals("FencingF") || player.getName().equals("_FencingF_") || player.getName().equals("AndrewMCPE12") || player.getName().equals("TheInfInventor") || player.getName().equals("CodeTitan") || player.getName().equals("entryway") || player.getName().equals("PulzarYT") || player.getName().equals("Visa_User") || player.getName().equals("MrAllNet") || player.getName().equals("MrAltNet") || player.getName().equals("AndrewMC12") && !player.getName().equals(mc.player.getName())) {
                        ClientMessage.sendMessage("The legend " + ChatFormatting.GOLD + player.getName() + ChatFormatting.RESET + " has popped " + ChatFormatting.GOLD + count + ChatFormatting.RESET + totemSpelling);
                    } else if (getFencing().friendManager.isFriendByName(player.getName()) && !player.getName().equals(mc.player.getName())) {
                        ClientMessage.sendMessage("Your friend " + ChatFormatting.AQUA + player.getName() + ChatFormatting.RESET + " has popped " + ChatFormatting.AQUA + count + ChatFormatting.RESET + totemSpelling);
                    } else if (!getFencing().friendManager.isFriendByName(player.getName()) && !player.getName().equals(mc.player.getName())) {
                        ClientMessage.sendMessage(ChatFormatting.LIGHT_PURPLE + player.getName() + ChatFormatting.RESET + " has popped " + ChatFormatting.LIGHT_PURPLE + count + ChatFormatting.RESET + totemSpelling);
                    }
                }

                this.announcers.remove(player);
            }
        }
    }

    @SubscribeEvent
    public void onTotemPop(TotemPopEvent event) {
        if (event.getPlayer() != null) {
            this.announcers.add(event.getPlayer());
            this.pops.merge(event.getPlayer(), 1, Integer::sum);
        }
    }


    @SubscribeEvent
    public void onDeath(DeathEvent event) {
        if (Notifier.INSTANCE.totemPops.getValue() && Notifier.INSTANCE.isToggled()) {
            if (this.pops.containsKey(event.getPlayer())) {
                int count = this.pops.getOrDefault(event.getPlayer(), -1);
                if (count != -1) {
                    if (count == 1) {
                        totemSpelling = " totem!";
                    } else if (count > 1) {
                        totemSpelling = " totems!";
                    }
                    if (event.getPlayer().equals(mc.player)) {
                        ClientMessage.sendMessage(ChatFormatting.LIGHT_PURPLE + "I" + ChatFormatting.RESET + " just died after popping " + ChatFormatting.LIGHT_PURPLE + count + ChatFormatting.RESET + totemSpelling);
                    }
                    if (event.getPlayer().getName().equals("Flixzit") || event.getPlayer().getName().equals("FencingF") || event.getPlayer().getName().equals("_FencingF_") || event.getPlayer().getName().equals("AndrewMC12") || event.getPlayer().getName().equals("TheInfInventor") || event.getPlayer().getName().equals("CodeTitan") || event.getPlayer().getName().equals("entryway") || event.getPlayer().getName().equals("Visa_User") || event.getPlayer().getName().equals("MrAllNet") || event.getPlayer().getName().equals("MrAltNet") || event.getPlayer().getName().equals("PulzarYT") && !event.getPlayer().equals(mc.player)) {
                        ClientMessage.sendMessage("The legend " + ChatFormatting.GOLD + event.getPlayer().getName() + ChatFormatting.RESET + " died after popping " + ChatFormatting.GOLD + count + ChatFormatting.RESET + totemSpelling);
                    } else if (getFencing().friendManager.isFriendByName(event.getPlayer().getName()) && !event.getPlayer().equals(mc.player)) {
                        ClientMessage.sendMessage("Your friend " + ChatFormatting.AQUA + event.getPlayer().getName() + ChatFormatting.RESET + " died after popping " + ChatFormatting.AQUA + count + ChatFormatting.RESET + totemSpelling);
                    } else if (!getFencing().friendManager.isFriendByName(event.getPlayer().getName()) && !event.getPlayer().equals(mc.player)) {
                        ClientMessage.sendMessage(ChatFormatting.LIGHT_PURPLE + event.getPlayer().getName() + ChatFormatting.RESET + " died after popping " + ChatFormatting.LIGHT_PURPLE + count + ChatFormatting.RESET + totemSpelling);
                    }
                }
                this.pops.remove(event.getPlayer());
                this.announcers.remove(event.getPlayer());
            }
        }
    }

    /*
    @SubscribeEvent
    public void onDeath(DeathEvent event) {
        if (this.pops.containsKey(event.getPlayer())) {
            int count = this.pops.getOrDefault(event.getPlayer(), -1);
            if (count != -1) {
                ClientMessage.sendMessage(event.getPlayer().getName() + " died after popping " + count + " totems!");
                // send message of that they died with x amount of totems
            }
            this.pops.remove(event.getPlayer());
            if (this.announcers.contains(event.getPlayer())) {
                this.announcers.remove(event.getPlayer());
            }
        }
    }
     */
}
