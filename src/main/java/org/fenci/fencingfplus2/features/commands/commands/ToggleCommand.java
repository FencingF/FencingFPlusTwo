package org.fenci.fencingfplus2.features.commands.commands;

import org.fenci.fencingfplus2.features.commands.Command;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.util.client.ClientMessage;

import java.util.List;

public class ToggleCommand extends Command {
    public ToggleCommand() {
        super("toggle", "Allows you to toggle modules", "toggle" + " " + "[module]");
    }

    @Override
    public void runCommand(List<String> args) {
        if (args.size() >= 1) {
            for (Module module : getFencing().moduleManager.getModules()) {
                if (module.getName().equalsIgnoreCase(args.get(0))) {
                    module.toggle(false);
                }
            }
        } else {
            ClientMessage.sendErrorMessage(getSyntax());
        }
    }
}
