package org.fenci.fencingfplus2.util.custompackets.packets;

import net.minecraft.entity.player.EntityPlayer;
import org.fenci.fencingfplus2.features.module.modules.misc.StashMover;
import org.fenci.fencingfplus2.util.custompackets.Packet;

public class MoverPacket extends Packet {
    private final String player;
    private final StashMover.MoverStage moverStage;

    public MoverPacket(String name, String player, StashMover.MoverStage moverStage) {
        super(name);
        this.player = player;
        this.moverStage = moverStage;
    }

    public String getPlayer() {
        return player;
    }

    public StashMover.MoverStage getMoverStage() {
        return moverStage;
    }

}
