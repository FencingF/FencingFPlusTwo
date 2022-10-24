package org.fenci.fencingfplus2.features.commands.commands;

import org.fenci.fencingfplus2.features.commands.Command;
import org.fenci.fencingfplus2.manager.CommandManager;
import org.fenci.fencingfplus2.util.client.ClientMessage;

import java.util.List;

public class PrefixCommand extends Command {
    public PrefixCommand() {
        super("prefix", "Allows you to change prefix", "prefix [prefix]");
    }

    @Override
    public void runCommand(List<String> args) {
        if (args.size() >= 1) {
            CommandManager.setPrefix(args.get(0));
            ClientMessage.sendMessage("Set the prefix to " + args.get(0) + ".");
        } else {
            ClientMessage.sendErrorMessage(getSyntax());
        }
    }
}
