package org.fenci.fencingfplus2.features.commands.commands;

import org.fenci.fencingfplus2.features.commands.Command;
import org.fenci.fencingfplus2.features.module.modules.client.Preferences;
import org.fenci.fencingfplus2.manager.login.LoginInfo;
import org.fenci.fencingfplus2.manager.login.Token;
import org.fenci.fencingfplus2.util.client.ClientMessage;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;

public class LoginCommand extends Command {

    public LoginCommand() {
        super("login", "Allows you to easily share accounts with a token", "token [generate/login] [login ? token : email, password]");
    }

    @Override
    public void runCommand(List<String> args) {
        try {
            if (args.size() >= 1) {
                if (args.get(0).equalsIgnoreCase("generate")) {
                    Token tokenToCopy = getFencing().loginManager.encrypt(getFencing().loginManager.getMicrosoftToken(new LoginInfo(args.get(1), args.get(2))));
                    if (tokenToCopy == null) {
                        ClientMessage.sendMessage("Unable to generate token");
                        return;
                    }
                    if (Preferences.tokenMethod.getValue().equals(Preferences.TokenMethod.Copy)) {
                        StringSelection stringSelection = new StringSelection(tokenToCopy.toString()); //the string.value of might break stuff I need to double-check this later
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(stringSelection, null);
                        ClientMessage.sendMessage("Copied login token to share.");
                    } else {
                        ClientMessage.sendMessage(tokenToCopy.toString());
                    }
                }
            } else {
                ClientMessage.sendErrorMessage(getSyntax());
            }
        } catch (Exception ignored) {}
    }
}
