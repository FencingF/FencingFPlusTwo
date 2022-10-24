package org.fenci.fencingfplus2.manager;

import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.features.commands.Command;
import org.fenci.fencingfplus2.features.commands.commands.*;
import org.fenci.fencingfplus2.util.client.ClientMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {
    public static String prefix = "@";
    private final ArrayList<Command> commands = new ArrayList<>();

    public CommandManager() {
        commands.add(new BindCommand());
        commands.add(new DefaultCommand());
        commands.add(new DiscordCommand());
        commands.add(new FontCommand());
        commands.add(new CalcCommand());
        commands.add(new FriendCommand());
        commands.add(new HelpCommand());
        commands.add(new InfoCommand());
        commands.add(new KitCommand());
        commands.add(new NickCommand());
        commands.add(new PrefixCommand());
        commands.add(new ScanCommand());
        commands.add(new SetCommand());
        commands.add(new StashCommand());
        commands.add(new ToggleCommand());
    }

    @SubscribeEvent
    public void chatEvent(ClientChatEvent event) {
        if (event.getMessage().startsWith(prefix)) {
            event.setCanceled(true);

            List<String> args = Arrays.asList(event.getMessage().substring(prefix.length()).trim().split(" "));
            if (args.isEmpty()) {
                return;
            }

            for (Command command : commands) {
                if (command.getName().equalsIgnoreCase(args.get(0))) {
                    command.runCommand(args.subList(1, args.size()));
                    return;
                }
            }

            ClientMessage.sendMessage("Invalid command, try " + prefix + "help");
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public static void setPrefix(String newPrefix) {
        prefix = newPrefix;
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }
}
