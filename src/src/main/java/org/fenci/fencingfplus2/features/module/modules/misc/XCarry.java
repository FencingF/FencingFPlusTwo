package org.fenci.fencingfplus2.features.module.modules.misc;

import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.network.PacketEvent;
import org.fenci.fencingfplus2.features.module.Module;

public class XCarry extends Module {

    public XCarry() {
        super("XCarry", "Allows you to carry items in your crafting slots", Category.Misc);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketCloseWindow && ((CPacketCloseWindow) event.getPacket()).windowId == 0) {
            if (((CPacketCloseWindow) event.getPacket()).windowId == mc.player.inventoryContainer.windowId) {
                event.setCanceled(true);
            }
        }
    }
}