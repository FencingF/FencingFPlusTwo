package org.fenci.fencingfplus2.util.custompackets.packets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import org.fenci.fencingfplus2.util.custompackets.Packet;
import org.fenci.fencingfplus2.util.world.SerializableBlockPos;

public class EmptyDubPacket extends Packet {
    private final SerializableBlockPos emptyDub;
    private final String player;

    public EmptyDubPacket(String name, SerializableBlockPos emptyDub, String player) {
        super(name);
        this.emptyDub = emptyDub;
        this.player = player;
    }

    public SerializableBlockPos getEmptyDub() {
        return emptyDub;
    }

    public String getPlayer() {
        return player;
    }
}
