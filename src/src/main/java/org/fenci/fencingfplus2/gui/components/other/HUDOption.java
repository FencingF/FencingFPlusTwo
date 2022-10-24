package org.fenci.fencingfplus2.gui.components.other;

import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.features.hud.HUDElement;
import org.fenci.fencingfplus2.features.module.modules.client.ClickGUI;
import org.fenci.fencingfplus2.features.module.modules.client.CustomFont;
import org.fenci.fencingfplus2.gui.components.Component;
import org.fenci.fencingfplus2.gui.components.button.Button;
import org.fenci.fencingfplus2.util.render.ColorUtil;
import org.fenci.fencingfplus2.util.render.RenderUtil;
import org.fenci.fencingfplus2.util.render.ScaleUtil;

import java.awt.*;
import java.util.ArrayList;

public class HUDOption extends Button {

    private final HUDElement hudElement;
    private static final ArrayList<Component> components = new ArrayList<>();

    public HUDOption(HUDElement hudElement) {
        super(hudElement.getName(), hudElement.getPosX(), hudElement.getPosY(), hudElement.getWidth(), hudElement.getHeight());
        this.hudElement = hudElement;

        init();
    }

    public static void init() {
        components.clear();
        for (HUDElement element : FencingFPlus2.INSTANCE.hudElementManager.getElements()) {
            if (!element.isEnabled()) continue;
            components.add(new StringElement(element));
        }
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        int color = -1;
        if (hudElement.isEnabled()) {
            color = new Color(ClickGUI.getred.getValue(), ClickGUI.getgreen.getValue(), ClickGUI.getblue.getValue(), ClickGUI.getAlpha.getValue()).getRGB();
        } else if (isMouseInBounds(mouseX, mouseY)) {
            color = new Color(ClickGUI.backgroundred.getValue(), ClickGUI.backgroundgreen.getValue(), ClickGUI.backgroundblue.getValue(), ClickGUI.backgroundalpha.getValue()).getRGB();
        }

        if (color != -1) {
            RenderUtil.drawRect(x, y, width, height, color);
        }

        if (CustomFont.INSTANCE.isOn()) {
            FencingFPlus2.INSTANCE.fontManager.drawStringWithShadow(hudElement.getName(), (float) (x + 2.3), ScaleUtil.centerTextY((float) y, (float) height), -1);
        } else {
            mc.fontRenderer.drawStringWithShadow(hudElement.getName(), (float) (x + 2.3), ScaleUtil.centerTextY((float) y, (float) height), -1);
        }

        for (Component component : components) {
            component.setX(component.getX());
            component.setY(component.getY());
            component.drawComponent(mouseX, mouseY, partialTicks);
//            for (HUDElement element : FencingFPlus2.INSTANCE.hudElementManager.getElements()) {
//                element.setPosX(component.getX());
//                element.setPosY(component.getY());
//            }
        }

//        if (opened) {
//            double start = 15.0;
//            for (org.fenci.fencingfplus2.gui.components.Component component : components) {
//                if (!component.isVisible()) {
//                    continue;
//                }
//
//                component.setX(x + 2.0);
//                component.setY(y + start);
//                component.setWidth(width - 4.0);
//
//                component.drawComponent(mouseX, mouseY, partialTicks);
//
//                start += component.getHeight() + 1.0;
//            }
//        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        for (Component component : components) {
            if (component.isMouseInBounds(mouseX, mouseY)) {
                component.mouseClicked(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        components.forEach((component) -> component.mouseReleased(mouseX, mouseY, state));
    }

    @Override
    protected void mouseClickedInFrame(int button) {
        if (button == 0) {
            hudElement.toggle();
        } //else if (button == 1) {
//            opened = !opened;
//        }
    }

    @Override
    public double getHeight() {
        return 14.0;
    }
}
