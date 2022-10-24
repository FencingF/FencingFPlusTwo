package org.fenci.fencingfplus2.features.hud.elements;

import net.minecraft.client.Minecraft;
import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.features.hud.HUDElement;
import org.fenci.fencingfplus2.features.module.modules.client.CustomFont;
import org.fenci.fencingfplus2.util.render.ColorUtil;

public class FPS extends HUDElement {
    public FPS() {
        super("FPS", 2f, 12f);
    }

    @Override
    public int getWidth() {
        return CustomFont.INSTANCE.isOn() ? FencingFPlus2.INSTANCE.fontManager.getStringWidth("FPS: " + Minecraft.getDebugFPS()) : mc.fontRenderer.getStringWidth("FPS: " + Minecraft.getDebugFPS());
    }

    @Override
    public int getHeight() {
        return CustomFont.INSTANCE.isOn() ? FencingFPlus2.INSTANCE.fontManager.getTextHeight() : mc.fontRenderer.FONT_HEIGHT;
    }

    @Override
    public void onHudRender() {
        if (CustomFont.INSTANCE.isOn()) {
            FencingFPlus2.INSTANCE.fontManager.drawStringWithShadow("FPS: " + Minecraft.getDebugFPS(), getPosX(), getPosY(), ColorUtil.displayColor());
        } else {
            mc.fontRenderer.drawStringWithShadow("FPS: " + Minecraft.getDebugFPS(), getPosX(), getPosY(), ColorUtil.displayColor());
        }
    }
}
