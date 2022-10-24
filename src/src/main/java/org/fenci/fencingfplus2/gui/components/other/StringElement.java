package org.fenci.fencingfplus2.gui.components.other;

import org.fenci.fencingfplus2.features.hud.HUDElement;
import org.fenci.fencingfplus2.gui.components.button.Button;
import org.fenci.fencingfplus2.util.render.RenderUtil;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class StringElement extends Button {

    private final HUDElement element;

    Map<HUDElement, Boolean> elementMap = new HashMap<>();

    public StringElement(HUDElement element) {
        super(element.getName(), element.getPosX(), element.getPosY(), element.getWidth(), element.getHeight());
        this.element = element;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        super.drawComponent(mouseX, mouseY, partialTicks);
        RenderUtil.drawRect(x, y, width, height, new Color(137, 137, 137, 100).getRGB());
//        FencingFPlus2.INSTANCE.fontManager.drawStringWithShadow("", (float) element.getPosX(), (float) element.getPosY(), element.getColor());
        if (isMouseInBounds(mouseX, mouseY) && elementMap.size() == 0) { //TODO: Fix being able to pick up more than one element at a time
            if (Mouse.isButtonDown(0)) {
                if (elementMap.size() == 0) {
                    elementMap.put(element, true);
                }
            }
        }
        for (HUDElement element1 : elementMap.keySet()) {
            if (elementMap.get(element1)) {
                //set the position of the middle of the element to the mouse position
                element.setPosX(mouseX - element.getWidth() / 2f);
                element.setPosY(mouseY - element.getHeight() / 2f);
                x = mouseX - element.getWidth() / 2f;
                y = mouseY - element.getHeight() / 2f;
            }
        }

        if (!Mouse.isButtonDown(0)) {
            elementMap.clear();
        }
    }

    @Override
    protected void mouseClickedInFrame(int button) {}
}
