package org.fenci.fencingfplus2.events.player;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class PushEvent extends Event {
    int type;
    public Entity entity;
    public double x;
    public double y;
    public double z;
    public boolean airbone;

    /*
     * Stage 0 = Blocks
     * Stage 1 = Entities
     * Stage 2 = Water
     */

    public PushEvent(Entity entity, double x, double y, double z, boolean airbone, int type) {
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.airbone = airbone;
        this.type = type;
    }

    public PushEvent(Entity entity, int type) {
        this.entity = entity;
        this.type = type;
    }

    public PushEvent(int type) {
        this.type = type;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Entity getEntity() {
        return entity;
    }

    public int getType() {
        return type;
    }

}
