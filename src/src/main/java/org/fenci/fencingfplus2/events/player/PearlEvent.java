package org.fenci.fencingfplus2.events.player;

import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.UUID;

public class PearlEvent extends Event {

    private final int entityId;
    private final UUID uniqueID;
    private final int data;
    private final Type type;

    public PearlEvent(int entityId, UUID uniqueID, int data, Type type) {
        this.entityId = entityId;
        this.uniqueID = uniqueID;
        this.data = data;
        this.type = type;
    }

    public int getEntityId() {
        return entityId;
    }

    public UUID getUniqueID() {
        return uniqueID;
    }

    public int getData() {
        return data;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        Throw, Load
    }
}
