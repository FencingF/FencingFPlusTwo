package org.fenci.fencingfplus2.features.module.modules.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.network.PacketEvent;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.ClientMessage;

public class PacketLogger extends Module {
    public static final Setting<Boolean> outgoing = new Setting<>("Outgoing", false);
    public static final Setting<Boolean> incoming = new Setting<>("Incoming", false);

    public PacketLogger() {
        super("PacketLogger", "Tells you which packets you are sending and receiving", Category.Misc);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (outgoing.getValue()) {
            ClientMessage.sendMessage(event.getPacket().toString());
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (incoming.getValue()) {
            ClientMessage.sendMessage(event.getPacket().toString());
        }
    }
}
