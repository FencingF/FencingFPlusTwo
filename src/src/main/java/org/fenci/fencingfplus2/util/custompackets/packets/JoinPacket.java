package org.fenci.fencingfplus2.util.custompackets.packets;

import net.minecraft.entity.player.EntityPlayer;
import org.fenci.fencingfplus2.util.custompackets.Packet;

public class JoinPacket extends Packet {
    private final String username;
    private final String message;

    public JoinPacket(String name, String username, String message) {
        super(name);
        this.username = username;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }
}
