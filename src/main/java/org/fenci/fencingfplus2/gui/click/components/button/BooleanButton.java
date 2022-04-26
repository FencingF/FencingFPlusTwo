package org.fenci.fencingfplus2.gui.click.components.button;

import org.fenci.fencingfplus2.features.module.modules.client.ClickGUI;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.render.RenderUtil;
import org.fenci.fencingfplus2.util.render.ScaleUtil;

import java.awt.*;

public class BooleanButton extends Button {
    private final Setting<Boolean> setting;

    public BooleanButton(Setting<Boolean> setting) {
        super(setting.getName(), 0.0, 0.0, 0.0, 13.0);
        this.setting = setting;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        int color = -1;
        if (setting.getValue()) {
            color = new Color(ClickGUI.getred.getValue(), ClickGUI.getgreen.getValue(), ClickGUI.getblue.getValue()).getRGB();
        } else if (isMouseInBounds(mouseX, mouseY)) {
            color = 0x77000000;
        }

        if (color != -1) {
            RenderUtil.drawRect(x, y, width, height, color);
        }

        mc.fontRenderer.drawStringWithShadow(setting.getName(), (float) (x + 2.3), ScaleUtil.centerTextY((float) y, (float) height), -1);
    }

    @Override
    protected void mouseClickedInFrame(int button) {
        setting.setValue(!setting.getValue());
    }
}
