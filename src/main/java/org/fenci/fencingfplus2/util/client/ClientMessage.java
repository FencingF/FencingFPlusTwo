package org.fenci.fencingfplus2.util.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;
import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.features.module.modules.client.HUD;
import org.fenci.fencingfplus2.manager.friend.Friend;
import org.fenci.fencingfplus2.util.Globals;
import org.fenci.fencingfplus2.util.render.ColorUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientMessage implements Globals {

    private static String opener() {
        return ColorUtil.getBracketColorFromSetting(HUD.bracketColor.getValue()) + "[" + ColorUtil.getNameColorFromSetting(HUD.nameColor.getValue()) + FencingFPlus2.NAME + ColorUtil.getBracketColorFromSetting(HUD.bracketColor.getValue()) + "] " + ChatFormatting.RESET;
    }

    private static String moduleOpener(String moduleName) {
        return ColorUtil.getBracketColorFromSetting(HUD.bracketColor.getValue()) + "[" + ColorUtil.getNameColorFromSetting(HUD.nameColor.getValue()) + FencingFPlus2.NAME + ColorUtil.getBracketColorFromSetting(HUD.bracketColor.getValue()) + "] " + ChatFormatting.WHITE + "[" + moduleName + "] " + ChatFormatting.RESET;
    }

    public static void sendMessage(String message) {
        sendClientMessage(opener() + message);
    }

    public static void sendMessage(Vec3d message) {
        sendClientMessage(opener() + message);
    }

    public static void sendMessage(boolean message) {
        sendClientMessage(opener() + message);
    }

    public static void sendMessage(double message) {
        sendClientMessage(opener() + message);
    }

    public static void sendMessage(Friend message) {
        sendClientMessage(opener() + message);
    }

    public static void sendMessage(BlockPos message) {
        sendClientMessage(opener() + message);
    }

    public static void sendMessage(double message, String messagee) {
        sendClientMessage(opener() + message);
    }

    public static void sendMessage(float message) {
        sendClientMessage(opener() + message);
    }

    public static void sendMessage(int message) {
        sendClientMessage(opener() + message);
    }

    public static void sendModuleMessage(String module, String message) {
        sendClientMessage(moduleOpener(module) + message);
    }

    public static void sendErrorMessage(String message) {
        sendClientMessage(opener() + ChatFormatting.RED + message);
    }

    private static void sendClientMessage(String message) {
        if (mc.player != null) {
            mc.player.sendMessage(new ChatMessage(message));
        }
    }

    public static void sendOverwriteClientMessage(String message) {
        if (mc.player != null) {
            final ITextComponent itc = new TextComponentString(opener() + message);
            mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(itc, 5936);
        }
    }


    public static void sendMessage(String message, boolean perm) {
        if (mc.player == null) return;
        try {
            TextComponentString component = new TextComponentString(opener() + message);
            int i = perm ? 0 : 12076;
            mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(component, i);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    public static void sendRainbowMessage(String message) {
        StringBuilder stringBuilder = new StringBuilder(message);
        stringBuilder.insert(0, "\u00a7+");
        mc.player.sendMessage(new ChatMessage(stringBuilder.toString()));
    }

    public static class ChatMessage extends TextComponentBase {
        String message_input;

        public ChatMessage(String message) {
            Pattern p = Pattern.compile("&[0123456789abcdefrlosmk]");
            Matcher m = p.matcher(message);
            StringBuffer sb = new StringBuffer();

            while (m.find()) {
                String replacement = "\u00A7" + m.group().substring(1);
                m.appendReplacement(sb, replacement);
            }

            m.appendTail(sb);
            this.message_input = sb.toString();
        }

        public String getUnformattedComponentText() {
            return this.message_input;
        }

        @Override
        public ITextComponent createCopy() {
            return new ChatMessage(this.message_input);
        }
    }
}
