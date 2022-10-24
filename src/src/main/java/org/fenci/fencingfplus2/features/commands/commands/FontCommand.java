package org.fenci.fencingfplus2.features.commands.commands;

import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.features.commands.Command;
import org.fenci.fencingfplus2.features.module.modules.client.CustomFont;
import org.fenci.fencingfplus2.util.client.ClientMessage;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FontCommand extends Command {
    public FontCommand() {
        super("font", "Changes the font of the client.", "font [reset/set/size/list] [font name/size]");
    }

    String[] numbers = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    String info = "";

    @Override
    public void runCommand(List<String> args) {
        if (args.size() >= 1) {
            String type = args.get(0);
            if (args.size() >= 2) {
                info = args.get(1);
                info = info.replaceAll("_", " ");
            }

            if (type.equals("reset")) {
                FencingFPlus2.INSTANCE.fontManager.reset();
                return;
            }

            if (type.equals("list")) {
                for (String string : FencingFPlus2.INSTANCE.fontManager.fonts) {
                    ClientMessage.sendMessage(string);
                }
                return;
            }

            if (type.equals("set") && !Objects.equals(info, "")) {
//                FencingFPlus2.INSTANCE.fontManager.setFont(info);
                for (CustomFont.Fonts font : CustomFont.Fonts.values()) {
                    if (font.getName().equalsIgnoreCase(info)) {
                        CustomFont.font.setValue(font);
                        return;
                    }
                }
                ClientMessage.sendMessage("Font set to " + info + ".");
                return;
            }

            if (type.equals("size") && !Objects.equals(info, "")) {
                for (char character : info.toCharArray()) {
                    if (!Arrays.asList(numbers).contains(String.valueOf(character))) {
                        ClientMessage.sendMessage("Invalid size.");
                        return;
                    }
                }
//                FencingFPlus2.INSTANCE.fontManager.setFontSize(Integer.parseInt(info));
                CustomFont.size.setValue(Integer.parseInt(info));
                ClientMessage.sendMessage("Font size set to " + info + ".");
                return;
            }

            ClientMessage.sendMessage(getSyntax());
        } else {
            ClientMessage.sendMessage(getSyntax());
        }
    }
}
