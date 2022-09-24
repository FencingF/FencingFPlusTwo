package org.fenci.fencingfplus2.features.commands.commands;

import org.fenci.fencingfplus2.features.commands.Command;

import java.util.List;

//made by flixzit pasted by andrew
public class DiscordCommand extends Command {
    public DiscordCommand() {
        super("discord", "shows all commands", "discord");
    }

    @Override
    public void runCommand(List<String> args) {
        try {
            //opens up a url in the default browser
            java.awt.Desktop.getDesktop().browse(java.net.URI.create("https://discord.gg/6te3fK3bAu"));
        } catch (Exception ignored) {
        }
    }
}
