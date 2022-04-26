package org.fenci.fencingfplus2.features.commands.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.features.commands.Command;
import org.fenci.fencingfplus2.manager.friend.Friend;
import org.fenci.fencingfplus2.util.client.ClientMessage;
import org.fenci.fencingfplus2.util.player.PlayerUtil;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class FriendCommand extends Command {
    public FriendCommand() {
        super("friend", "Adds friends to the system", "friend" + " " + "[add/del/list]" + " " + "[playername]");
    }

    @Override
    public void runCommand(List<String> args) {
        if (args.size() >= 1) {
            if (args.size() >= 2) {
                if (args.get(0).equalsIgnoreCase("add") && !getFencing().friendManager.isFriendByName(args.get(1)) && PlayerUtil.isPlayerInRender(args.get(1))) {
                    UUID frienduuid = PlayerUtil.getUUIDFromName(args.get(1));
                    FencingFPlus2.INSTANCE.friendManager.add(new Friend(frienduuid, args.get(1)));
                    assert frienduuid != null;
                    ClientMessage.sendMessage(Objects.requireNonNull(mc.world.getPlayerEntityByUUID(frienduuid)).getName() + " has been " + ChatFormatting.GREEN + "friended!");
                }
                if (args.get(0).equalsIgnoreCase("del") && getFencing().friendManager.isFriendByName(args.get(1)) && PlayerUtil.isPlayerInRender(args.get(1))) {
                    UUID frienduuid = PlayerUtil.getUUIDFromName(args.get(1));
                    Friend friend = FencingFPlus2.INSTANCE.friendManager.getFriend(frienduuid);
                    FencingFPlus2.INSTANCE.friendManager.remove(friend);
                    ClientMessage.sendMessage(friend.getAlias() + " has been " + ChatFormatting.RED + "unfriended!");
                }
                if (!PlayerUtil.isPlayerInRender(args.get(1))) {
                    ClientMessage.sendMessage("Could not find " + args.get(1) + ".");
                }
            }
            if (args.get(0).equalsIgnoreCase("list")) {
                if (FencingFPlus2.INSTANCE.friendManager.getFriends().isEmpty()) {
                    ClientMessage.sendMessage("You have no friends.");
                } else {
                    for (Friend friend : FencingFPlus2.INSTANCE.friendManager.getFriends()) {
                        ClientMessage.sendMessage(friend.getAlias());
                    }
                }
            }
        } else {
            ClientMessage.sendErrorMessage(getSyntax());
        }
    }
}
