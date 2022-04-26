package org.fenci.fencingfplus2.events.player;

import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class SwingEvent extends Event {
    SPacketAnimation animation;

    public SwingEvent(SPacketAnimation animation) {
        this.animation = animation;
    }

    public SPacketAnimation getAnimation() {
        return animation;
    }
}
