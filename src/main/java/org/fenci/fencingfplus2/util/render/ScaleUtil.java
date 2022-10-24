package org.fenci.fencingfplus2.util.render;

import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.features.module.modules.client.CustomFont;
import org.fenci.fencingfplus2.util.Globals;

public class ScaleUtil implements Globals {
    public static float centerTextY(float y, float height) {
        return (y + (height / 2.0f)) - ((CustomFont.INSTANCE.isOn() ? FencingFPlus2.INSTANCE.fontManager.getTextHeight() : mc.fontRenderer.FONT_HEIGHT) / 2.0f);
    }
}