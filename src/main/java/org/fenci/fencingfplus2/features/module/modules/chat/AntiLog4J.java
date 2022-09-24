package org.fenci.fencingfplus2.features.module.modules.chat;

import net.minecraft.network.play.server.SPacketChat;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.events.network.PacketEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.util.client.ClientMessage;

public class AntiLog4J extends Module { //TODO
    public AntiLog4J() {
        super("AntiLog4J", "Blocks messages that contain the key aspect for the log4shell 0-day exploit.", Category.Chat);
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketChat) {
            String text = ((SPacketChat) event.getPacket()).getChatComponent().getUnformattedText();
            if (text.contains("${") || text.contains("$<") || text.contains("$:-") || text.contains("jndi:ldap")) {
                //DO NOT USE THE logger IN CornerStore CLASS THAT WOULD BE PRINTING THE RCE CODE INTO LOG4J.
                ClientMessage.sendMessage("Potential RCE Exploit Blocked.");
                event.setCanceled(true);
            }
        }
    }
}