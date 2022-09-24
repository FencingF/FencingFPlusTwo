package org.fenci.fencingfplus2.util.custompackets.packets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import org.fenci.fencingfplus2.util.custompackets.Packet;
import org.fenci.fencingfplus2.util.world.SerializableBlockPos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DubInfoPacket extends Packet {
    private final Set<SerializableBlockPos> chestsToMove;
    private final Set<SerializableBlockPos> chestsToMoveTo;

    private final String player;

    public DubInfoPacket(String name, Set<SerializableBlockPos> chestsToMove, Set<SerializableBlockPos> chestsToMoveTo, String player) {
        super(name);
        this.chestsToMove = chestsToMove;
        this.chestsToMoveTo = chestsToMoveTo;
        this.player = player;
    }

    public Set<SerializableBlockPos> getChestsToMoveTo() {
        return chestsToMoveTo;
    }

    public Set<SerializableBlockPos> getChestsToMove() {
        return chestsToMove;
    }

    public String getPlayer() {
        return player;
    }
}
