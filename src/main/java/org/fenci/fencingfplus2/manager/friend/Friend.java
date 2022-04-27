package org.fenci.fencingfplus2.manager.friend;

import org.fenci.fencingfplus2.util.player.PlayerUtil;

import java.util.UUID;

public class Friend {
    private final UUID uuid;
    private String alias;

    public Friend(String alias) {
        this.alias = alias;
        this.uuid = PlayerUtil.getUUIDFromName(alias);
    }

    public Friend(UUID uuid, String alias) {
        this.uuid = uuid;
        this.alias = alias;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getAlias() {
        return this.alias == null ? ("Friend" + this.hashCode()) : this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
