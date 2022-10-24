package org.fenci.fencingfplus2.manager;

import org.fenci.fencingfplus2.gui.font.CustomFont;
import org.fenci.fencingfplus2.util.Globals;

import java.awt.*;
import java.util.Locale;

public class FontManager implements Globals {

    public final String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(Locale.ENGLISH);
    public String fontName = org.fenci.fencingfplus2.features.module.modules.client.CustomFont.font.getName();
    public int fontSize = org.fenci.fencingfplus2.features.module.modules.client.CustomFont.size.getValue();

    private CustomFont font = new CustomFont(new Font(fontName, Font.PLAIN, fontSize), true, false);

    public void setFont() {
        this.font = new CustomFont(new Font(fontName, Font.PLAIN, fontSize), true, false);
    }

    public void reset() {
        this.setFont("Tahoma");
        org.fenci.fencingfplus2.features.module.modules.client.CustomFont.font.setValue(org.fenci.fencingfplus2.features.module.modules.client.CustomFont.Fonts.Tahoma);
        this.setFontSize(16);
        org.fenci.fencingfplus2.features.module.modules.client.CustomFont.size.setValue(16);
        this.setFont();
    }

    public boolean setFont(String fontName) {
        for (String font : fonts) {
            if (fontName.equalsIgnoreCase(font)) {
                this.fontName = font;
                this.setFont();
                return true;
            }
        }
        return false;
    }

    public void setFontSize(int size) {
        this.fontSize = size;
        this.setFont();
    }

    public void drawStringWithShadow(String string, float x, float y, int color) {
        this.drawString(string, x, y, color, true);
    }

    public float drawString(String string, float x, float y, int colour, boolean shadow) {
        if (shadow) {
            return this.font.drawStringWithShadow(string, x, y, colour);
        } else {
            return this.font.drawString(string, x, y, colour);
        }
    }

    public int getTextHeight() {
        return this.font.getStringHeight();
    }

    public int getStringWidth(String string) {
        return this.font.getStringWidth(string);
    }
}
