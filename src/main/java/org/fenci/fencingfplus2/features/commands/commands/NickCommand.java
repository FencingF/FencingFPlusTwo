package org.fenci.fencingfplus2.features.commands.commands;

import org.fenci.fencingfplus2.features.commands.Command;
import org.fenci.fencingfplus2.util.client.ClientMessage;

import java.util.List;

public class NickCommand extends Command {
    public NickCommand() {
        super("nick", "Changes your name client side", "nick [newname]");
    }

    public static String newName = null;

    @Override
    public void runCommand(List<String> args) {
        if (args.size() >= 1) {
            try {
                newName = args.get(0);
                ClientMessage.sendMessage("Set username to " + newName);
            } catch (Exception ignored) {
            }
        } else {
            ClientMessage.sendMessage(getSyntax());
        }
    }
}
