package org.fenci.fencingfplus2.manager.friend;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import org.fenci.fencingfplus2.events.client.FriendEvent;

import java.util.ArrayList;
import java.util.UUID;

public class FriendManager {
    private final ArrayList<Friend> friends = new ArrayList<>();
    EntityPlayer friendedPlayer;
    UUID frienduuid;

    public ArrayList<Friend> getFriends() {
        return friends;
    }

    public void add(Friend friend) {
        this.friends.add(friend);
        MinecraftForge.EVENT_BUS.post(new FriendEvent(friend.getAlias(), friend.getUuid(), FriendEvent.Type.Add));
    }

    public void remove(Friend friend) {
        this.friends.remove(friend);
        MinecraftForge.EVENT_BUS.post(new FriendEvent(friend.getAlias(), friend.getUuid(), FriendEvent.Type.Remove));
    }

    public Friend getFriend(UUID uuid) {
        return this.friends.stream().filter((friend) -> friend.getUuid().equals(uuid)).findFirst().orElse(null);
    }

    public EntityPlayer getPlayer() {
        for (EntityPlayer player : Minecraft.getMinecraft().world.playerEntities) {
            if (player.getUniqueID().equals(getAllFriends())) {
                friendedPlayer = player;
            }
        }
        return friendedPlayer;
    }

    public UUID getAllFriends() {
        for (Friend friend : getFriends()) {
            frienduuid = friend.getUuid();
        }
        return frienduuid;
    }

    public boolean isFriendByName(String alias) {
        for (Friend friend : getFriends()) {
            if (friend.getAlias().equals(alias)) {
                return true;
            }
        }
        return false;
    }

    public boolean isFriend(UUID uuid) {
        return this.friends.stream().anyMatch((friend) -> friend.getUuid().equals(uuid));
    }
}
