package org.fenci.fencingfplus2.features.module.modules.misc;

import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.network.PacketEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.util.client.ClientMessage;
import org.fenci.fencingfplus2.util.client.Timer;

import java.util.ArrayList;
import java.util.List;

public class ECFinder extends Module {
    public ECFinder() {
        super("ECFinder", "Finds e-chests", Category.Misc);
    }

    Timer timer = new Timer();
    List<Integer> ids = new ArrayList<>();

    @Override
    public void onUpdate() {
        for (TileEntity entity : mc.world.loadedTileEntityList) {
            if (entity instanceof TileEntityEnderChest) {
                ClientMessage.sendMessage(entity.getPos());
            }
        }
//        if (timer.hasReached(1500)) {
//            ids.clear();
//            timer.reset();
//        }
//        if (ids.size() >= 3) {
//            ClientMessage.sendMessage("Rubberbanding");
//        }
    }

//    @SubscribeEvent
//    public void onPacket(PacketEvent.Send event) {
//        if (event.getPacket() instanceof CPacketConfirmTeleport) {
//            CPacketConfirmTeleport packet = event.getPacket();
//            ClientMessage.sendMessage(packet.getTeleportId());
//            ids.add(packet.getTeleportId());
//        }
//    }
}
