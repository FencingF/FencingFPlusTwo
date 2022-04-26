package org.fenci.fencingfplus2.features.commands.commands;

import org.fenci.fencingfplus2.features.commands.Command;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.features.module.modules.client.Preferences;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.ClientMessage;

import java.util.List;

public class DefaultCommand extends Command {
    public DefaultCommand() {
        super("default", "Allows you to reset a modules settings to default", "default [module]");
    }

    @Override
    public void runCommand(List<String> args) {
        if (args.size() >= 1) {
            try {
                if (getFencing().moduleManager.getModuleByName(args.get(0)) != null) {
                    Module module = getFencing().moduleManager.getModuleByName(args.get(0));
                    for (Setting setting : module.getSettings()) {
                        if (!Preferences.defaultsResetDrawn.getValue() && setting.getName().equals("Drawn")) continue;
                        if (!Preferences.defaultsResetKey.getValue() && setting.getName().equals("Keybind")) continue;
                        setting.setValue(setting.getDefaultValue());
                    }
                    ClientMessage.sendMessage("Reset " + module.getName() + " to default settings!");
                } else {
                    ClientMessage.sendMessage("Could not find module.");
                }
            } catch (Exception ignored) {}
        } else {
            ClientMessage.sendMessage(getSyntax());
        }
    }
}
