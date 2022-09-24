package org.fenci.fencingfplus2.events.network;

import net.minecraftforge.fml.common.eventhandler.Event;
import org.fenci.fencingfplus2.features.module.modules.misc.StashMover;
import org.fenci.fencingfplus2.util.custompackets.Packet;

public class StashMovePacketEvent extends Event {
    private final Packet recievedPacket;

    public StashMovePacketEvent(Packet recievedPacket) {
        this.recievedPacket = recievedPacket;
    }

    public Packet getRecievedPacket() {
        return recievedPacket;
    }
}
