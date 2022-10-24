package org.fenci.fencingfplus2.features.hud.elements;

import net.minecraft.client.gui.GuiChat;
import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.features.hud.HUDElement;
import org.fenci.fencingfplus2.features.module.modules.client.CustomFont;
import org.fenci.fencingfplus2.util.render.ColorUtil;

public class Coordinates extends HUDElement {
    public Coordinates() {
        super("Coordinates", 2f, 2f);
    }

    String coords;

    @Override
    public int getWidth() {
        if (coords == null) {
            return 0;
        }
        return CustomFont.INSTANCE.isOn() ? FencingFPlus2.INSTANCE.fontManager.getStringWidth(coords) : mc.fontRenderer.getStringWidth(coords);
    }

    @Override
    public int getHeight() {
        return CustomFont.INSTANCE.isOn() ? FencingFPlus2.INSTANCE.fontManager.getTextHeight() : mc.fontRenderer.FONT_HEIGHT;
    }

    @Override
    public void onHudRender() {
        if (mc.currentScreen instanceof GuiChat) return; //TODO Figure out if the coords are over the chat box
        long netherX = (long) (mc.player.posX / 8);
        long netherY = (long) (mc.player.posY);
        long netherZ = (long) (mc.player.posZ / 8);

        long currX = (long) (mc.player.posX);
        long currY = (long) (mc.player.posY);
        long currZ = (long) (mc.player.posZ);

        long netherX2 = (long) (Math.round((netherX) * 10.0) / 10.0);
        long netherY2 = (long) (Math.round((netherY) * 10.0) / 10.0);
        long netherZ2 = (long) (Math.round((netherZ) * 10.0) / 10.0);

        long currX2 = (long) (Math.round((currX) * 10.0) / 10.0);
        long currY2 = (long) (Math.round((currY) * 10.0) / 10.0);
        long currZ2 = (long) (Math.round((currZ) * 10.0) / 10.0);

        if (mc.player.dimension == 0) {
            String s = "XYZ " + "[" + currX2 + " " + currY2 + " " + currZ2 + "]" + " [" + netherX2 + " " + netherY2 + " " + netherZ2 + "]";
            if (CustomFont.INSTANCE.isOn()) {
                FencingFPlus2.INSTANCE.fontManager.drawStringWithShadow(coords = s, getPosX(), getPosY(), 16777215);
            } else {
                mc.fontRenderer.drawStringWithShadow(coords = s, getPosX(), getPosY(), 16777215);
            }
        } else if (mc.player.dimension == -1) {
            String s = "XYZ " + "[" + currX2 + " " + currY2 + " " + currZ2 + "]" + " [" + currX2 * 8 + " " + currY2 + " " + currZ2 * 8 + "]";
            if (CustomFont.INSTANCE.isOn()) {
                FencingFPlus2.INSTANCE.fontManager.drawStringWithShadow(coords = s, getPosX(), getPosY(), 0xd3443d);
            } else {
                mc.fontRenderer.drawStringWithShadow(coords = s, getPosX(), getPosY(), 0xd3443d);
            }
        } else if (mc.player.dimension == 1) {
            if (CustomFont.INSTANCE.isOn()) {
                FencingFPlus2.INSTANCE.fontManager.drawStringWithShadow(coords = "XYZ " + "[" + currX2 + " " + currY2 + " " + currZ2 + "]", getPosX(), getPosY(), 0xd65df5);
            } else {
                mc.fontRenderer.drawStringWithShadow(coords = "XYZ " + "[" + currX2 + " " + currY2 + " " + currZ2 + "]", getPosX(), getPosY(), 0xd65df5);
            }
        }
    }
}
