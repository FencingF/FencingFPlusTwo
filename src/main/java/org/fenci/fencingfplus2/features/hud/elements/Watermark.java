package org.fenci.fencingfplus2.features.hud.elements;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.features.hud.HUDElement;
import org.fenci.fencingfplus2.features.module.modules.client.CustomFont;
import org.fenci.fencingfplus2.features.module.modules.client.HUDEditor;
import org.fenci.fencingfplus2.util.render.ColorUtil;

public class Watermark extends HUDElement {

    public static String renderedElement;

    public Watermark() {
        super("Watermark", 2f, 2f);
    }

    @Override
    public int getWidth() {
        return renderedElement == null ? 0 : FencingFPlus2.INSTANCE.fontManager.getStringWidth(renderedElement);
    }

    @Override
    public int getHeight() {
        return CustomFont.INSTANCE.isOn() ? FencingFPlus2.INSTANCE.fontManager.getTextHeight() : mc.fontRenderer.FONT_HEIGHT;
    }

    @Override
    public void onHudRender() {
        if (CustomFont.INSTANCE.isOn()) {
            FencingFPlus2.INSTANCE.fontManager.drawStringWithShadow(renderedElement = FencingFPlus2.NAME + " v" + FencingFPlus2.VERSION, (float) getPosX(), (float) getPosY(), ColorUtil.displayColor());
        } else {
            mc.fontRenderer.drawStringWithShadow(renderedElement = FencingFPlus2.NAME + " v" + FencingFPlus2.VERSION, (float) getPosX(), (float) getPosY(), ColorUtil.displayColor());
        }
    }
}