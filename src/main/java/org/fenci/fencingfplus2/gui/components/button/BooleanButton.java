package org.fenci.fencingfplus2.gui.components.button;

import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.features.module.modules.client.ClickGUI;
import org.fenci.fencingfplus2.features.module.modules.client.CustomFont;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.render.ColorUtil;
import org.fenci.fencingfplus2.util.render.RenderUtil;
import org.fenci.fencingfplus2.util.render.ScaleUtil;

import java.awt.*;

import static org.fenci.fencingfplus2.features.module.modules.client.ClickGUI.*;

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
            color = new Color(ClickGUI.getred.getValue(), ClickGUI.getgreen.getValue(), ClickGUI.getblue.getValue(), getAlpha.getValue()).getRGB();
        } else if (isMouseInBounds(mouseX, mouseY)) {
            color = new Color(backgroundred.getValue(), backgroundgreen.getValue(), backgroundblue.getValue(), backgroundalpha.getValue()).getRGB();
        }

        if (color != -1) {
            RenderUtil.drawRect(x, y, width, height, color);
        }
        if (CustomFont.INSTANCE.isOn()) {
            FencingFPlus2.INSTANCE.fontManager.drawStringWithShadow(setting.getName(), (float) (x + 2.3), ScaleUtil.centerTextY((float) y, (float) height), -1);
        } else {
            mc.fontRenderer.drawStringWithShadow(setting.getName(), (float) (x + 2.3), ScaleUtil.centerTextY((float) y, (float) height), -1);
        }
    }

    @Override
    protected void mouseClickedInFrame(int button) {
        setting.setValue(!setting.getValue());
    }
}
