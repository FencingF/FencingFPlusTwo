package org.fenci.fencingfplus2.features.hud.elements;

import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.features.hud.HUDElement;
import org.fenci.fencingfplus2.features.module.modules.client.ClickGUI;
import org.fenci.fencingfplus2.features.module.modules.client.CustomFont;
import org.fenci.fencingfplus2.util.render.ColorUtil;

import java.awt.*;

public class Direction extends HUDElement {
    public Direction() { //I'm sorry this looks chinese I was too lazy to recode it; cope abt it.
        super("Direction", 2f, 2f);
    }

    private String direction1;
    private String nesw;

    @Override
    public int getWidth() {
        return CustomFont.INSTANCE.isOn() ? FencingFPlus2.INSTANCE.fontManager.getStringWidth(assembleDirection()) : mc.fontRenderer.getStringWidth(assembleDirection());
    }

    @Override
    public int getHeight() {
        return CustomFont.INSTANCE.isOn() ? FencingFPlus2.INSTANCE.fontManager.getTextHeight() : mc.fontRenderer.FONT_HEIGHT;
    }

    private String getDirection() {
        if (mc.player.getAdjustedHorizontalFacing().getAxisDirection().toString().equals("Towards positive")) {
            direction1 = "+";
        } else if (mc.player.getAdjustedHorizontalFacing().getAxisDirection().toString().equals("Towards negative")) {
            direction1 = "-";
        }
        return direction1;
    }

    private String getAxis() {
        return mc.player.getHorizontalFacing().getAxis().getName().toUpperCase();
    }

    private String getNESW() {
        if (getAxis().equals("X") && getDirection().equals("+")) {
            nesw = "East";
        }
        if (getAxis().equals("X") && getDirection().equals("-")) {
            nesw = "West";
        }
        if (getAxis().equals("Z") && getDirection().equals("+")) {
            nesw = "South";
        }
        if (getAxis().equals("Z") && getDirection().equals("-")) {
            nesw = "North";
        }
        return nesw;
    }

    public String assembleDirection() {
        return getNESW() + " [" + getDirection() + getAxis() + "]";
    }

    @Override
    public void onHudRender() {
        if (mc.player.dimension == 0) {
            if (CustomFont.INSTANCE.isOn()) {
                FencingFPlus2.INSTANCE.fontManager.drawStringWithShadow(assembleDirection(), getPosX(), getPosY(), 16777215);
            } else {
                mc.fontRenderer.drawStringWithShadow(assembleDirection(), getPosX(), getPosY(), 16777215);
            }
        } else if (mc.player.dimension == -1) {
            if (CustomFont.INSTANCE.isOn()) {
                FencingFPlus2.INSTANCE.fontManager.drawStringWithShadow(assembleDirection(), getPosX(), getPosY(), 0xd3443d);
            } else {
                mc.fontRenderer.drawStringWithShadow(assembleDirection(), getPosX(), getPosY(), 0xd3443d);
            }
        } else if (mc.player.dimension == 1) {
            if (CustomFont.INSTANCE.isOn()) {
                FencingFPlus2.INSTANCE.fontManager.drawStringWithShadow(assembleDirection(), getPosX(), getPosY(), 0xd65df5);
            } else {
                mc.fontRenderer.drawStringWithShadow(assembleDirection(), getPosX(), getPosY(), 0xd65df5);
            }
        }
    }
}
