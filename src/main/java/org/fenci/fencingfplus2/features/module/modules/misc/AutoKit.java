package org.fenci.fencingfplus2.features.module.modules.misc;


import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.events.network.PacketEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.ClientMessage;

import java.util.Objects;

public class AutoKit extends Module {
    public static final Setting<KitMode> kitMode = new Setting<>("Mode", KitMode.IPBased);
    String ip;

    public AutoKit() {
        super("AutoKit", "Automatically gives you a kit", Category.Misc);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketRespawn) {
            if (kitMode.getValue().equals(KitMode.Always)) {
                mc.player.connection.sendPacket(new CPacketChatMessage("/kit " + FencingFPlus2.INSTANCE.kitManager.getAnyManagedKitName()));
            }
            if (kitMode.getValue().equals(KitMode.IPBased)) {
                ip = Objects.requireNonNull(Minecraft.getMinecraft().getCurrentServerData()).serverIP;
                if (FencingFPlus2.INSTANCE.kitManager.serverIpKits.containsKey(ip)) {
                    Objects.requireNonNull(mc.getConnection()).sendPacket(new CPacketChatMessage("/kit " + FencingFPlus2.INSTANCE.kitManager.getKitFromIp(ip)));
                } else {
                    ClientMessage.sendOverwriteClientMessage("No kit found for this server.");
                }
            }
        }
    }

    public enum KitMode {
        IPBased, Always
    }
}
