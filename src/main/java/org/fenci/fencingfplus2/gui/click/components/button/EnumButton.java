package org.fenci.fencingfplus2.gui.click.components.button;

import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.render.ScaleUtil;

public class EnumButton extends Button {
    private final Setting<Enum> setting;

    public EnumButton(Setting<Enum> setting) {
        super(setting.getName(), 0.0, 0.0, 0.0, 13.0);
        this.setting = setting;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        mc.fontRenderer.drawStringWithShadow(setting.getName() + ": " + setting.getValue().name(), (float) (x + 2.3), ScaleUtil.centerTextY((float) y, (float) height), -1);
    }

    @Override
    protected void mouseClickedInFrame(int button) {
        setting.setValue(Setting.increase(setting.getValue()));
    }
}
