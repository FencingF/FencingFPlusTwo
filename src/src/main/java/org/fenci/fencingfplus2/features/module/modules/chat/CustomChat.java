package org.fenci.fencingfplus2.features.module.modules.chat;

import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.network.PacketEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;

public class CustomChat extends Module {
    //public static final Setting<Boolean> suffix = new Setting<>("Suffix", true);

    public CustomChat() {
        super("ChatSuffix", "Allows you to customize chat", Category.Chat);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            CPacketChatMessage packet = event.getPacket();
            String message = packet.getMessage();
            if (message.startsWith("/") || message.startsWith(getFencing().commandManager.getPrefix())) return;
            if (isOn()) message = message + " | FencingF+2";
            packet.message = message;
        }
    }
}
