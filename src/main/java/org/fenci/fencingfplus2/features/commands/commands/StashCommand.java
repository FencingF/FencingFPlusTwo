package org.fenci.fencingfplus2.features.commands.commands;

import org.fenci.fencingfplus2.features.commands.Command;
import org.fenci.fencingfplus2.features.module.modules.misc.StashMover;
import org.fenci.fencingfplus2.util.client.ClientMessage;

import java.util.List;

public class StashCommand extends Command {
    public StashCommand() {
        super("stash", "Allows you to configure the StashMover module", "stash [select/unselect/stop/ip/port] [actualIp/actualPort] [homedubs/movedubs]");
    }

    public static boolean selectingHome = false;
    public static boolean selectingMove = false;
    public static boolean unselectingHome = false;
    public static boolean unselectingMove = false;

    public static String ip = null;
    public static int port = 0;


    @Override
    public void runCommand(List<String> args) {
        if (args.size() >= 1) {
            if ((args.get(0).equalsIgnoreCase("select") || args.get(0).equalsIgnoreCase("unselect")) && !StashMover.INSTANCE.isOn()) {
                ClientMessage.sendMessage("Please turn on the StashMover module first.");
                return;
            }
            if (args.get(0).equalsIgnoreCase("select")) {
                if (args.get(1).equalsIgnoreCase("homedubs")) {
                    ClientMessage.sendMessage("Listening for homedubs, click on dubs to select them.");
                    selectingHome = true;
                    selectingMove = false;
                    unselectingHome = false;
                    unselectingMove = false;
                } else if (args.get(1).equalsIgnoreCase("movedubs")) {
                    ClientMessage.sendMessage("Listening for movedubs, click on dubs to select them.");
                    selectingMove = true;
                    selectingHome = false;
                    unselectingHome = false;
                    unselectingMove = false;
                }
            } else if (args.get(0).equalsIgnoreCase("unselect")) {
                if (args.get(1).equalsIgnoreCase("homedubs")) {
                    ClientMessage.sendMessage("Listening for homedubs, click on dubs to unselect them.");
                    unselectingHome = true;
                    selectingHome = false;
                    selectingMove = false;
                    unselectingMove = false;
                } else if (args.get(1).equalsIgnoreCase("movedubs")) {
                    ClientMessage.sendMessage("Listening for movedubs, click on dubs to unselect them.");
                    unselectingMove = true;
                    selectingHome = false;
                    selectingMove = false;
                    unselectingHome = false;
                }
            } else if (args.get(0).equalsIgnoreCase("stop")) {
                ClientMessage.sendMessage("No longer listening for dubs.");
                selectingHome = false;
                selectingMove = false;
                unselectingHome = false;
                unselectingMove = false;
            } else if (args.get(0).equalsIgnoreCase("ip")) {
                ip = args.get(1);
                ClientMessage.sendMessage("Ip set to: " + ip);
            } else if (args.get(0).equalsIgnoreCase("port")) {
                port = Integer.parseInt(args.get(1));
                ClientMessage.sendMessage("Port set to: " + port);
            } else {
                ClientMessage.sendErrorMessage(getSyntax());
            }
        } else {
            ClientMessage.sendErrorMessage(getSyntax());
        }
    }
}
