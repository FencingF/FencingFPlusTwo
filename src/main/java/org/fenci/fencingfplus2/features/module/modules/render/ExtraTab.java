package org.fenci.fencingfplus2.features.module.modules.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.features.module.modules.misc.NickHider;
import org.fenci.fencingfplus2.setting.Setting;

public class ExtraTab extends Module {

    public static final Setting<Integer> players = new Setting<>("Players", 275, 0, 1000);

    private static ExtraTab INSTANCE;

    public ExtraTab() {
        super("ExtraTab", "Makes it so you can see all players on the server", Category.Render);
        INSTANCE = this;
    }

    public static ExtraTab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ExtraTab();
        }
        return INSTANCE;
    }

    public static String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn) {
        String name = networkPlayerInfoIn.getDisplayName() != null ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
        if (FencingFPlus2.INSTANCE.friendManager.isFriendByName(name)) {
            return ChatFormatting.AQUA + name;
        }

        if (name.equalsIgnoreCase("FencingF") || name.equalsIgnoreCase("_FencingF_") || name.equalsIgnoreCase("AndrewMC12")) {
            return ChatFormatting.GOLD + name;
        }
        return name;
    }
}
