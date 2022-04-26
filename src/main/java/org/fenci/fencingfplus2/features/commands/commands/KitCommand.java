package org.fenci.fencingfplus2.features.commands.commands;

import org.fenci.fencingfplus2.features.commands.Command;
import org.fenci.fencingfplus2.util.client.ClientMessage;

import java.util.List;

public class KitCommand extends Command {
    public KitCommand() {
        super("autokit", "Kit chooser for autokit", "autokit" + " " + "[serverIp/all]" + " " + "[kitName]");
    }

    @Override
    public void runCommand(List<String> args) {
        if (args.size() >= 2) {
            if (args.get(0).equalsIgnoreCase("all")) {
                if (getFencing().kitManager.getAnyKit().size() == 1) getFencing().kitManager.removeAnyKit();
                getFencing().kitManager.addAnyKit(args.get(1));
                ClientMessage.sendMessage("Kit set to: " + getFencing().kitManager.getAnyManagedKitName() + ", for server: " + args.get(0) + ".");
            } else if (!args.get(0).equalsIgnoreCase("all")) {
                if (!getFencing().kitManager.serverIpKits.containsKey(args.get(1))) {
                    getFencing().kitManager.removeIpKit(args.get(0));
                }
                getFencing().kitManager.addIpKit(args.get(0), args.get(1));
                ClientMessage.sendMessage("Kit set to: " + getFencing().kitManager.getKitFromIp(args.get(0)) + ", for server: " + args.get(0) + ".");
            }
        } else {
            ClientMessage.sendErrorMessage(getSyntax());
        }
    }
}