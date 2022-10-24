package org.fenci.fencingfplus2.util.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import org.fenci.fencingfplus2.features.module.modules.chat.Notifier;
import org.fenci.fencingfplus2.features.module.modules.client.ClickGUI;
import org.fenci.fencingfplus2.features.module.modules.client.HUD;

import java.awt.*;

public class ColorUtil {

    public static ChatFormatting nameColorFromSetting;
    public static ChatFormatting nameBracketFromSetting;
    int r;
    int g;
    int b;
    int a;


    public ColorUtil(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 255;
    }

    public static Color releasedDynamicRainbow(int delay, float saturation, float brightness) {
        double rainbowState = Math.ceil((double) (System.currentTimeMillis() + (long) delay) / 20.0);
        return Color.getHSBColor((float) (rainbowState % 360.0 / 360.0), saturation / 255.0f, brightness / 255.0f);
    }

    public static int displayColor() {
        return new Color(ClickGUI.getred.getValue(), ClickGUI.getgreen.getValue(), ClickGUI.getblue.getValue()).getRGB();
    }

    public static ChatFormatting getDimentionColor(int dimension) {
        switch (dimension) {
            case -1:
                return ChatFormatting.DARK_RED;
            case 0:
                return ChatFormatting.DARK_GREEN;
            case 1:
                return ChatFormatting.DARK_PURPLE;
            default:
                return ChatFormatting.GRAY;
        }
    }

    public static int toHex(int r, int g, int b) {
        return 0xFF000000 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
    }

    public static float[] toRGBA(int hex) {
        // r, g, b, a
        return new float[]{(hex >> 16 & 0xff) / 255.0f, (hex >> 8 & 0xff) / 255.0f, (hex & 0xff) / 255.0f, (hex >> 24 & 0xff) / 255.0f};
    }

    public static int getHealthColor(final Entity entity) {
        final int scale = (int) Math.round(255.0 - ((EntityLivingBase) entity).getHealth() * 255.0 / ((EntityLivingBase) entity).getMaxHealth());
        final int damageColor = 255 - scale << 8 | scale << 16;
        return 0xFF000000 | damageColor;
    }

    public static int getHealthColor(final float health) {
        final int scale = (int) Math.round(255.0 - health * 255.0 / Minecraft.getMinecraft().player.getMaxHealth());
        final int damageColor = 255 - scale << 8 | scale << 16;
        return 0xFF000000 | damageColor;
    }

    public static ChatFormatting getNameColorFromSetting(Notifier.NameColor setting) {
        if (setting.equals(Notifier.NameColor.DarkRed)) {
            nameColorFromSetting = ChatFormatting.DARK_RED;
        }
        if (setting.equals(Notifier.NameColor.Red)) {
            nameColorFromSetting = ChatFormatting.RED;
        }
        if (setting.equals(Notifier.NameColor.Gold)) {
            nameColorFromSetting = ChatFormatting.GOLD;
        }
        if (setting.equals(Notifier.NameColor.Yellow)) {
            nameColorFromSetting = ChatFormatting.YELLOW;
        }
        if (setting.equals(Notifier.NameColor.DarkGreen)) {
            nameColorFromSetting = ChatFormatting.DARK_GREEN;
        }
        if (setting.equals(Notifier.NameColor.Green)) {
            nameColorFromSetting = ChatFormatting.GREEN;
        }
        if (setting.equals(Notifier.NameColor.Aqua)) {
            nameColorFromSetting = ChatFormatting.AQUA;
        }
        if (setting.equals(Notifier.NameColor.DarkAqua)) {
            nameColorFromSetting = ChatFormatting.DARK_AQUA;
        }
        if (setting.equals(Notifier.NameColor.DarkBlue)) {
            nameColorFromSetting = ChatFormatting.DARK_BLUE;
        }
        if (setting.equals(Notifier.NameColor.Blue)) {
            nameColorFromSetting = ChatFormatting.BLUE;
        }
        if (setting.equals(Notifier.NameColor.LightPurple)) {
            nameColorFromSetting = ChatFormatting.LIGHT_PURPLE;
        }
        if (setting.equals(Notifier.NameColor.DarkPurple)) {
            nameColorFromSetting = ChatFormatting.DARK_PURPLE;
        }
        if (setting.equals(Notifier.NameColor.White)) {
            nameColorFromSetting = ChatFormatting.WHITE;
        }
        if (setting.equals(Notifier.NameColor.Gray)) {
            nameColorFromSetting = ChatFormatting.GRAY;
        }
        if (setting.equals(Notifier.NameColor.DarkGray)) {
            nameColorFromSetting = ChatFormatting.DARK_GRAY;
        }
        if (setting.equals(Notifier.NameColor.Black)) {
            nameColorFromSetting = ChatFormatting.BLACK;
        }
        return nameColorFromSetting;
    }

    public static ChatFormatting getBracketColorFromSetting(Notifier.BracketColor setting) {
        if (setting.equals(Notifier.BracketColor.DarkRed)) {
            nameBracketFromSetting = ChatFormatting.DARK_RED;
        }
        if (setting.equals(Notifier.BracketColor.Red)) {
            nameBracketFromSetting = ChatFormatting.RED;
        }
        if (setting.equals(Notifier.BracketColor.Gold)) {
            nameBracketFromSetting = ChatFormatting.GOLD;
        }
        if (setting.equals(Notifier.BracketColor.Yellow)) {
            nameBracketFromSetting = ChatFormatting.YELLOW;
        }
        if (setting.equals(Notifier.BracketColor.DarkGreen)) {
            nameBracketFromSetting = ChatFormatting.DARK_GREEN;
        }
        if (setting.equals(Notifier.BracketColor.Green)) {
            nameBracketFromSetting = ChatFormatting.GREEN;
        }
        if (setting.equals(Notifier.BracketColor.Aqua)) {
            nameBracketFromSetting = ChatFormatting.AQUA;
        }
        if (setting.equals(Notifier.BracketColor.DarkAqua)) {
            nameBracketFromSetting = ChatFormatting.DARK_AQUA;
        }
        if (setting.equals(Notifier.BracketColor.DarkBlue)) {
            nameBracketFromSetting = ChatFormatting.DARK_BLUE;
        }
        if (setting.equals(Notifier.BracketColor.Blue)) {
            nameBracketFromSetting = ChatFormatting.BLUE;
        }
        if (setting.equals(Notifier.BracketColor.LightPurple)) {
            nameBracketFromSetting = ChatFormatting.LIGHT_PURPLE;
        }
        if (setting.equals(Notifier.BracketColor.DarkPurple)) {
            nameBracketFromSetting = ChatFormatting.DARK_PURPLE;
        }
        if (setting.equals(Notifier.BracketColor.White)) {
            nameBracketFromSetting = ChatFormatting.WHITE;
        }
        if (setting.equals(Notifier.BracketColor.Gray)) {
            nameBracketFromSetting = ChatFormatting.GRAY;
        }
        if (setting.equals(Notifier.BracketColor.DarkGray)) {
            nameBracketFromSetting = ChatFormatting.DARK_GRAY;
        }
        if (setting.equals(Notifier.BracketColor.Black)) {
            nameBracketFromSetting = ChatFormatting.BLACK;
        }
        return nameBracketFromSetting;
    }
}
