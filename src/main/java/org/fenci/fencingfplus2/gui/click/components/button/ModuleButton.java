package org.fenci.fencingfplus2.gui.click.components.button;

import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.features.module.modules.client.ClickGUI;
import org.fenci.fencingfplus2.gui.click.components.Component;
import org.fenci.fencingfplus2.gui.click.components.other.Slider;
import org.fenci.fencingfplus2.setting.Keybind;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.render.RenderUtil;
import org.fenci.fencingfplus2.util.render.ScaleUtil;

import java.awt.*;
import java.util.ArrayList;

public class ModuleButton extends Button {
    private final Module module;
    private final ArrayList<Component> components = new ArrayList<>();

    private boolean opened = false;

    public ModuleButton(Module module) {
        super(module.getName(), 0.0, 0.0, 0.0, 13.0);
        this.module = module;

        init();
    }

    private void init() {
        for (Setting setting : module.getSettings()) {
            if (setting instanceof Keybind) {
                components.add(new KeybindButton((Keybind) setting));
            } else {
                if (setting.getValue() instanceof Boolean) {
                    components.add(new BooleanButton(setting));
                } else if (setting.getValue() instanceof Enum) {
                    components.add(new EnumButton(setting));
                } else if (setting.getValue() instanceof Number) {
                    components.add(new Slider(setting));
                }
            }
        }
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        int color = -1;
        if (module.isToggled()) {
            color = new Color(ClickGUI.getred.getValue(), ClickGUI.getgreen.getValue(), ClickGUI.getblue.getValue()).getRGB();
        } else if (isMouseInBounds(mouseX, mouseY)) {
            color = 0x77000000;
        }

        if (color != -1) {
            RenderUtil.drawRect(x, y, width, height, color);
        }

        mc.fontRenderer.drawStringWithShadow(module.getName(), (float) (x + 2.3), ScaleUtil.centerTextY((float) y, (float) height), -1);

        if (opened) {
            double start = 15.0;
            for (Component component : components) {
                if (!component.isVisible()) {
                    continue;
                }

                component.setX(x + 2.0);
                component.setY(y + start);
                component.setWidth(width - 4.0);

                component.drawComponent(mouseX, mouseY, partialTicks);

                start += component.getHeight() + 1.0;
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (opened) {
            for (Component component : components) {
                if (component.isMouseInBounds(mouseX, mouseY)) {
                    component.mouseClicked(mouseX, mouseY, button);
                }
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (opened) {
            components.forEach((component) -> component.mouseReleased(mouseX, mouseY, state));
        }
    }

    @Override
    public void keyTyped(char character, int code) {
        super.keyTyped(character, code);
        if (opened) {
            components.forEach((component) -> component.keyTyped(character, code));
        }
    }

    @Override
    protected void mouseClickedInFrame(int button) {
        if (button == 0) {
            module.toggle(false);
        } else if (button == 1) {
            opened = !opened;
        }
    }

    @Override
    public double getHeight() {
        double origin = 14.0;
        if (opened) {
            for (Component component : components) {
                if (component.isVisible()) {
                    origin += component.getHeight() + 1.0;
                }
            }

            origin += 1.0;
        }

        return origin;
    }
}
