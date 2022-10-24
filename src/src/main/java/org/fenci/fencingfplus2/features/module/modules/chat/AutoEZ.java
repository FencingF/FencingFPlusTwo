package org.fenci.fencingfplus2.features.module.modules.chat;

import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.player.DeathEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;

import java.util.Random;

public class AutoEZ extends Module {
    public static final Setting<Boolean> greenText = new Setting<>("GreenText", false);
    String[] ezMessages = {
            "Hahaha get recked kid, FencingF Plus 2 On Tope!",
            "<player> LOL U JUST LMFAO UR PROB A FUTURE USER LMFAOOOOOOO",
            "LMFAO SO BAD MOST LIKELY DOESNT LIKE FENCINGF+2 NOSLOW",
            "hope i don't get banned with the power of fencingf+2!",
            "Damn, you’re so bad… WITHOUT FENCINGF+2 NO SLOW BYPASS",
            "<player> got slapped by FencingF+2",
            "ur trash kid",
            "FencingF+2 only on CTC",
            "Did you know? The FencingF+2 Bar and Grill now serves cheese with broccoli. Get your free cheese with broccoli for [$69] only at FencingF+2 Bar and Grill",
            "<player> get fried by the FencingF+2 bar and grill",
            "You guys just decided to cook all reci pies cheese recipe. It contained broccoli",
            "You have died to fencingf+2 cope about it fencingf+2 on tope! also ur dad cheated on ur mom when he went to get milk.",
            "Yo, fencingf+2 just deleted your unpinned clips from 1 hour ago",
            "EZZZZZZZZZ <player> no more fortnite!",
            "ur like my wife, dead",
            "<player> u just shitted out your ass with the FencingF+2 battle pass",
            "<player> imagine not having the renegade raider fortnite skin",
            "<player> lightwork :yawn: :yawn:",
            "my mom works at amazon lol she will ban u from minecraft <player>",
            "so ez <player> ur like a russian solider in ukraine",
            "lightwork",
            "You died fat monkey",
            "Elon Musk",
            "Shush bozo",
            "Lunar Client on top!",
            "Clown down, just quit already because you have died to FENCINGF+2",
            "Impact+ on top",
            "How do i cpvp can someone teach me im new",
            "future beta plus owns you",
            "<player> tried to start the dream incursion and failed :joy:",
            "FenchingFplos2 bought spawn in 1926",
            "fencingf+2 autoez improves my odds of success in player versus player encounters on 2b2t.org",
            "2builders2tools is backdoored by andrewmc12 <player> ezzzzz",
            "fobuse hacx owns larpville",
            "You Died!",
            "File Edit View Navigate Code Refactor Build Run Tools Git Window Help",
            "that's 2b2t crazy",
            "whatisshue",
            "hockeyware on bottom <player>",
            "<player> IS NOT THE KING OF 2B2T! U MAD NIGGERS! U MAD NEWFAGS!",
            "eclipse ide is trash people",
            "gradle is slow"
    };

    public AutoEZ() {
        super("AutoEZ", "Automatically says ez when you kill someone", Category.Chat);
    }

    @SubscribeEvent
    public void onDeath(DeathEvent event) {
        if (event.getPlayer().equals(mc.player) || getFencing().friendManager.isFriend(event.getPlayer().getUniqueID()) || !fullNullCheck()) return;
        String randomMessage = (ezMessages[new Random().nextInt(ezMessages.length)]);
        for (int i = 0; i < 1; i++) { //added a for loop that loops once so we don't get more than 1 message
            mc.player.connection.sendPacket(new CPacketChatMessage((greenText.getValue() ? ">" : "") + randomMessage.replaceAll("<player>", event.getPlayer().getDisplayNameString())));
        }
    }
}
