package org.fenci.fencingfplus2.features.commands.commands;

import org.fenci.fencingfplus2.features.commands.Command;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.ClientMessage;

import java.util.List;

public class SetCommand extends Command {
    public SetCommand() {
        super("set", "Allows you to specifiy a setting that isn't normally available", "set [module] [setting] [value]");
    }

    //TODO: Finish this

    @Override
    public void runCommand(List<String> args) {
        if (args.size() >= 3) {
            try {
                if (getFencing().moduleManager.getModuleByName(args.get(0)) != null) {
                    Module moduleToChange = getFencing().moduleManager.getModuleByName(args.get(0));
                    for (Setting setting : moduleToChange.getSettings()) {
                        if (setting.getName().equalsIgnoreCase(args.get(1))) {
                            setting.setValue(args.get(2));
                            ClientMessage.sendMessage("Set " + setting.getName() + " to " + args.get(2));
                        }
                    }
                }
            } catch (Exception ignored) {
            }
        } else {
            ClientMessage.sendErrorMessage(getSyntax());
        }
    }
}
