package org.fenci.fencingfplus2.features.module.modules.chat;

import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.network.PacketEvent;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;

public class CustomChat extends Module {
    public CustomChat() {
        super("CustomChat", "Allows you to customize chat", Category.Chat);
    }

    public static final Setting<Boolean> suffix = new Setting<>("Suffix", true);

    @SubscribeEvent
    public void onPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            CPacketChatMessage packet = event.getPacket();
            String message = packet.getMessage();
            if (message.startsWith("/") || message.startsWith(getFencing().commandManager.getPrefix())) return;
            if (suffix.getValue()) message += " | FencingF+2";
            packet.message = message;
        }
    }
}
