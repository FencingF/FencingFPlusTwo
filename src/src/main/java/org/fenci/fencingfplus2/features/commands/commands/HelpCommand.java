package org.fenci.fencingfplus2.features.commands.commands;

import net.minecraft.util.text.TextFormatting;
import org.fenci.fencingfplus2.features.commands.Command;
import org.fenci.fencingfplus2.util.client.ClientMessage;

import java.util.List;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "shows all commands", "help");
    }

    @Override
    public void runCommand(List<String> args) {
        try {
            for (Command command : getFencing().commandManager.getCommands()) {
                ClientMessage.sendMessage(TextFormatting.WHITE + command.getName() + TextFormatting.GRAY + " " + command.getDescription() + " syntax: " + command.getSyntax());
            }
        } catch (Exception ignored) {
        }
    }
}
