package org.fenci.fencingfplus2.features.commands.commands;

import org.fenci.fencingfplus2.features.commands.Command;
import org.fenci.fencingfplus2.util.client.ClientMessage;

import java.util.List;

//made by flixzit the best coder uwu
public class CalcCommand extends Command {
    public CalcCommand() {
        super("calc", "Calculates two numbers", "calc" + " " + " [+|*|/|-] " + " ");
    }

    @Override
    public void runCommand(List<String> args) {
        try {

            //TODO Fix error messages, plus make it more user friendly.
            if (args.size() <= 3)
            // mostly done by GitHub copilot lmao
                if (args.get(1).equalsIgnoreCase("+") || args.get(1).equalsIgnoreCase("-") || args.get(1).equalsIgnoreCase("*") || args.get(1).equalsIgnoreCase("/")) {
                    if (args.get(1).equalsIgnoreCase("+")) {
                        long num1 = Long.parseLong(args.get(0));
                        long num2 = Long.parseLong(args.get(2));
                        long sum = num1 + num2;
                        ClientMessage.sendMessage(num1 + " and " + num2 + " is " + sum);
                    }
                    if (args.get(1).equalsIgnoreCase("-")) {
                        long num1 = Long.parseLong(args.get(0));
                        long num2 = Long.parseLong(args.get(2));
                        long sum = num1 - num2;
                        ClientMessage.sendMessage(num1 + " and " + num2 + " is " + sum);
                    }
                    if (args.get(1).equalsIgnoreCase("*")) {
                        long num1 = Long.parseLong(args.get(0));
                        long num2 = Long.parseLong(args.get(2));
                        long sum = num1 * num2;
                        ClientMessage.sendMessage(num1 + " and " + num2 + " is " + sum);
                    }
                    if (args.get(1).equalsIgnoreCase("/")) {
                        double num1 = Double.parseDouble(args.get(0));
                        double num2 = Double.parseDouble(args.get(2));
                        double sum = num1 / num2;
                        ClientMessage.sendMessage(num1 + " quotient " + num2 + " is " + sum);
                    }
                } else {
                    ClientMessage.sendErrorMessage(getSyntax());
                }


        } catch (Exception ignored) {
        }
    }
}
