package org.fenci.fencingfplus2.events.client;

import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.UUID;

public class FriendEvent extends Event {

    String name;
    UUID uuid;
    Type type;

    public FriendEvent(String name, UUID uuid, Type type) {
        this.name = name;
        this.uuid = uuid;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        Add, Remove
    }
}
