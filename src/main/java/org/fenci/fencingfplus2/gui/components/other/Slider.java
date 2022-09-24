package org.fenci.fencingfplus2.gui.components.other;

import org.fenci.fencingfplus2.features.module.modules.client.ClickGUI;
import org.fenci.fencingfplus2.gui.components.button.Button;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.render.RenderUtil;
import org.fenci.fencingfplus2.util.render.ScaleUtil;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class Slider extends Button {
    private final Setting<Number> setting;
    private final float difference;

    public Slider(Setting setting) {
        super(setting.getName(), 0.0, 0.0, 0.0, 13.0);
        this.setting = setting;
        this.difference = setting.getMax().floatValue() - setting.getMin().floatValue();
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        if (this.canSetValue(mouseX, mouseY)) {
            this.setValue(mouseX);
        }

        double dynamicWidth = setting.getValue().floatValue() <= setting.getMin().floatValue() ? 0.0 : width * partialMultiplier();
        RenderUtil.drawRect(x, y, dynamicWidth, height, new Color(ClickGUI.getred.getValue(), ClickGUI.getgreen.getValue(), ClickGUI.getblue.getValue()).getRGB());

        mc.fontRenderer.drawStringWithShadow(setting.getName() + ": " + setting.getValue(), (float) (x + 2.3), ScaleUtil.centerTextY((float) y, (float) height), -1);
    }

    @Override
    protected void mouseClickedInFrame(int button) {
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (this.canSetValue(mouseX, mouseY)) {
            this.setValue(mouseX);
        }
    }

    private void setValue(int mouseX) {
        float percent = (float) (((double) (mouseX) - x) / (float) width);

        if (this.setting.getValue() instanceof Float) {
            float result = setting.getMin().floatValue() + difference * percent;
            setting.setValue(Math.round(10.0f * result) / 10.0f);
        } else if (this.setting.getValue() instanceof Double) {
            double result = setting.getMin().doubleValue() + difference * percent;
            setting.setValue(Math.round(10.0 * result) / 10.0);
        } else {
            setting.setValue(Math.round(setting.getMin().intValue() + difference * percent));
        }
    }

    private float part() {
        return setting.getValue().floatValue() - setting.getMin().floatValue();
    }

    private float partialMultiplier() {
        return part() / difference;
    }

    private boolean canSetValue(int mouseX, int mouseY) {
        return isMouseInBounds(mouseX, mouseY) && Mouse.isButtonDown(0); // 0 = left click
    }
}
