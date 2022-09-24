package org.fenci.fencingfplus2.events.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

// DeathEvent is a class that extends Event and is used to help with some modules.
public class DeathEvent extends Event {
    private final EntityPlayer player;

    public DeathEvent(EntityPlayer player) {
        this.player = player;
    }

    public EntityPlayer getPlayer() {
        return player;
    }
}
