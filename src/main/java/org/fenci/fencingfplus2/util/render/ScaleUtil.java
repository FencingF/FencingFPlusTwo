package org.fenci.fencingfplus2.util.render;

import org.fenci.fencingfplus2.util.Globals;

public class ScaleUtil implements Globals {
    public static float centerTextY(float y, float height) {
        return (y + (height / 2.0f)) - (mc.fontRenderer.FONT_HEIGHT / 2.0f);
    }
}