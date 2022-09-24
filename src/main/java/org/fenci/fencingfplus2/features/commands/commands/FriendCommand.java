package org.fenci.fencingfplus2.features.commands.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.features.commands.Command;
import org.fenci.fencingfplus2.manager.friend.Friend;
import org.fenci.fencingfplus2.util.client.ClientMessage;

import java.util.List;

public class FriendCommand extends Command {
    public FriendCommand() {
        super("friend", "Adds friends to the system", "friend" + " " + "[add/del/list]" + " " + "[playername]");
    }

    @Override
    public void runCommand(List<String> args) {
        if (args.size() >= 1) {
            if (args.size() >= 2) {
                try {
                    if (args.get(0).equalsIgnoreCase("add") && !getFencing().friendManager.isFriendByName(args.get(1))) {
                        if (!FencingFPlus2.INSTANCE.friendManager.isFriendByName(args.get(1))) {
                            FencingFPlus2.INSTANCE.friendManager.add(new Friend(args.get(1)));
                            ClientMessage.sendMessage(args.get(1) + " has been " + ChatFormatting.GREEN + "friended!");
                        } else {
                            ClientMessage.sendMessage(args.get(1) + " is already a friend!");
                        }
                    }
                    if (args.get(0).equalsIgnoreCase("del") && getFencing().friendManager.isFriendByName(args.get(1))) {
                        if (FencingFPlus2.INSTANCE.friendManager.isFriendByName(args.get(1))) {
                            Friend friend = getFencing().friendManager.getFriendByName(args.get(1));
                            FencingFPlus2.INSTANCE.friendManager.remove(friend);
                            ClientMessage.sendMessage(args.get(1) + " has been " + ChatFormatting.RED + "unfriended!");
                        } else {
                            ClientMessage.sendMessage(args.get(1) + " is not a friend!");
                        }
                    }

                } catch (NullPointerException ignored) {
                }
            }
            if (args.get(0).equalsIgnoreCase("list")) {
                if (FencingFPlus2.INSTANCE.friendManager.getFriends().isEmpty()) {
                    ClientMessage.sendMessage("You have no friend lmfao.");
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
