package org.fenci.fencingfplus2.events.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class TotemPopEvent extends Event {
    private final EntityPlayer player;

    public TotemPopEvent(EntityPlayer player) {
        this.player = player;
    }

    public EntityPlayer getPlayer() {
        return player;
    }
}
