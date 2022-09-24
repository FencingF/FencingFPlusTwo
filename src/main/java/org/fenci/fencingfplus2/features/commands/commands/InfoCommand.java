package org.fenci.fencingfplus2.features.commands.commands;

import org.fenci.fencingfplus2.features.commands.Command;
import org.fenci.fencingfplus2.util.client.ClientMessage;

import java.util.List;

public class InfoCommand extends Command {
    public InfoCommand() {
        super("info", "shows what all the modules do", "info" + " " + "module");
    }

    @Override
    public void runCommand(List<String> args) {
        try {
            //gets the module from the module manager and prints the description from moduleName
            if (args.size() >= 1) ClientMessage.sendMessage(getFencing().moduleManager.getModuleByName(args.get(0)).getDescription());
        } catch (Exception ignored) {
        }
    }
}