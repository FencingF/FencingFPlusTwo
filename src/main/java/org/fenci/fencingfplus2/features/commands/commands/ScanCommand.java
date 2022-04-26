package org.fenci.fencingfplus2.features.commands.commands;

import net.minecraft.util.math.BlockPos;
import org.fenci.fencingfplus2.features.commands.Command;
import org.fenci.fencingfplus2.util.client.ClientMessage;
import org.fenci.fencingfplus2.util.client.MathUtil;

import java.util.List;

public class ScanCommand extends Command {
    public ScanCommand() {
        super("scan", "For further long-jump modifications", "scan [start/check/clear] [x, y, z]");
    }

    @Override
    public void runCommand(List<String> args) {
        if (args.size() >= 1) {
            if (args.get(0).equalsIgnoreCase("clear")) {
                getFencing().trackerManager.checkPos.clear();
                getFencing().trackerManager.startScanPos.clear();
                ClientMessage.sendModuleMessage("AutoJump", "Cleared positions.");
            }
            if (args.size() >= 4) {
                try {
                    if (args.get(0).equalsIgnoreCase("start")) {
                        if (!MathUtil.isInt(args.get(1)) || !MathUtil.isInt(args.get(2)) || !MathUtil.isInt(args.get(3))) {
                            ClientMessage.sendMessage("Please enter an integer");
                        } else {
                            getFencing().trackerManager.addScanPos(new BlockPos(Integer.parseInt(args.get(1)), Integer.parseInt(args.get(2)), Integer.parseInt(args.get(3))));
                            ClientMessage.sendModuleMessage("AutoJump", "Added " + new BlockPos(Integer.parseInt(args.get(1)), Integer.parseInt(args.get(2)), Integer.parseInt(args.get(3))) + " as your starting scan position.");
                        }
                    } else if (args.get(0).equalsIgnoreCase("check")) {
                        if (!MathUtil.isInt(args.get(1)) || !MathUtil.isInt(args.get(2)) || !MathUtil.isInt(args.get(3))) {
                            ClientMessage.sendMessage("Please enter an integer");
                        } else {
                            getFencing().trackerManager.addCheckPos(new BlockPos(Integer.parseInt(args.get(1)), Integer.parseInt(args.get(2)), Integer.parseInt(args.get(3))));
                            ClientMessage.sendModuleMessage("AutoJump", "Added " + new BlockPos(Integer.parseInt(args.get(1)), Integer.parseInt(args.get(2)), Integer.parseInt(args.get(3))) + " as your check position.");
                        }
                    }
                } catch (Exception ignored) {}
            }
        } else {
            ClientMessage.sendErrorMessage(getSyntax());
        }
    }
}
