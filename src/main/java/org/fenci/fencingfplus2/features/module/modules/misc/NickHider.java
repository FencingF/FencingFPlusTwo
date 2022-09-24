package org.fenci.fencingfplus2.features.module.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.util.client.ClientMessage;

public class NickHider extends Module {
    private static NickHider INSTANCE;

    public NickHider() { //renamed to fix the "You" error
        super("NickHider", "Changes name", Category.Misc);
        NickHider.INSTANCE = this;
    }

    public static NickHider getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NickHider();
        }
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        ClientMessage.sendMessage(ChatFormatting.GRAY + "Success! Name successfully changed to " + ChatFormatting.GREEN + mc.player.getName());
    }

    @Override
    public void onDisable() {
        ClientMessage.sendMessage(ChatFormatting.GRAY + "Success! Name successfully changed to " + ChatFormatting.GREEN + mc.player.getName());
    }
}