package org.fenci.fencingfplus2.gui.components;


import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.features.hud.HUDElement;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.features.module.modules.client.ClickGUI;
import org.fenci.fencingfplus2.features.module.modules.client.CustomFont;
import org.fenci.fencingfplus2.features.module.modules.client.HUDEditor;
import org.fenci.fencingfplus2.gui.components.button.ModuleButton;
import org.fenci.fencingfplus2.gui.components.other.HUDOption;
import org.fenci.fencingfplus2.util.Globals;
import org.fenci.fencingfplus2.util.render.ColorUtil;
import org.fenci.fencingfplus2.util.render.RenderUtil;
import org.fenci.fencingfplus2.util.render.ScaleUtil;

import java.awt.*;
import java.util.ArrayList;

public abstract class Panel extends Component implements Globals {

    protected final ArrayList<ModuleButton> buttons = new ArrayList<>();
    protected final ArrayList<HUDOption> elements = new ArrayList<>();
    private boolean opened = true;
    private double lastX, lastY;
    private boolean dragging = false;

    public Panel(String id, double x, double y, double width, double height) {
        super(id, x, y, width, height);
        init();
    }

    private static int COLOR() {
        return new Color(ClickGUI.getred.getValue(), ClickGUI.getgreen.getValue(), ClickGUI.getblue.getValue(), ClickGUI.getAlpha.getValue()).getRGB();
    }

    public abstract void init();

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        if (dragging) {
            setX(lastX + mouseX);
            setY(lastY + mouseY);
        }

        RenderUtil.drawRect(x, y, width, height, new Color(ClickGUI.insidered.getValue(), ClickGUI.insidegreen.getValue(), ClickGUI.insideblue.getValue(), ClickGUI.insideAlpha.getValue()).getRGB());

        {
            double allHeight = height + getTotalHeight();
            double allHudHeight = height + getTotalHUDHeight();

            double finalTotalHeight = ClickGUI.INSTANCE.isOn() ? allHeight : allHudHeight;

            RenderUtil.drawLine(x, y, x, y + finalTotalHeight, ClickGUI.lineWidth.getValue(), COLOR()); //left line //TODO: Fix the line corners where the lines intersect
            RenderUtil.drawLine(x, y, x + width, y, ClickGUI.lineWidth.getValue(), COLOR()); //top line
            RenderUtil.drawLine(x + width, y, x + width, y + finalTotalHeight, ClickGUI.lineWidth.getValue(), COLOR()); //right line
            RenderUtil.drawLine(x, y + finalTotalHeight, x + width, y + finalTotalHeight, ClickGUI.lineWidth.getValue(), COLOR()); //Bottom line
        }

        if (CustomFont.INSTANCE.isOn()) {
            FencingFPlus2.INSTANCE.fontManager.drawStringWithShadow(id, (float) (x + 2.3), ScaleUtil.centerTextY((float) y, (float) height), -1);
        } else {
            mc.fontRenderer.drawStringWithShadow(id, (float) (x + 2.3), ScaleUtil.centerTextY((float) y, (float) height), -1);
        }
        String moduleCounter = "[" + Module.Category.amountPerCategory(Module.Category.getCategoryFromString(id)) + "]";
        float length = (float) ((x + width));
        if (ClickGUI.moduleCount.getValue() && ClickGUI.INSTANCE.isOn())
            if (CustomFont.INSTANCE.isOn()) {
                FencingFPlus2.INSTANCE.fontManager.drawStringWithShadow(moduleCounter, (length - 18 + adjustDistance(moduleCounter)), ScaleUtil.centerTextY((float) y, (float) height), -1);
            } else {
                mc.fontRenderer.drawStringWithShadow(moduleCounter, (length - 18 + adjustDistance(moduleCounter)), ScaleUtil.centerTextY((float) y, (float) height), -1);
            }
        if (opened) {
            double finalTotalHeight = ClickGUI.INSTANCE.isOn() ? getTotalHeight() : getTotalHUDHeight();
            RenderUtil.drawRect(x, y + height, width, finalTotalHeight, new Color(ClickGUI.backgroundred.getValue(), ClickGUI.backgroundgreen.getValue(), ClickGUI.backgroundblue.getValue(), ClickGUI.backgroundalpha.getValue()).getRGB());

            double start = (y + height) + 1.0;
            for (ModuleButton button : buttons) {
                button.setX(x + 2.0);
                button.setY(start);
                button.setWidth(width - 4.0);
                button.setHeight(14.0);

                button.drawComponent(mouseX, mouseY, partialTicks);

                start += button.getHeight() + 1.0;
            }
            for (HUDOption element : elements) {
                element.setX(x + 2.0);
                element.setY(start);
                element.setWidth(width - 4.0);
                element.setHeight(14.0);

                element.drawComponent(mouseX, mouseY, partialTicks);

                start += element.getHeight() + 1.0;
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseInBounds(mouseX, mouseY, x, y, width, 15.0)) {
            if (button == 0) {
                dragging = !dragging;
                lastX = x - mouseX;
                lastY = y - mouseY;
            } else if (button == 1) {
                opened = !opened;
            }
        }

        buttons.forEach((b) -> b.mouseClicked(mouseX, mouseY, button));
        elements.forEach((b) -> b.mouseClicked(mouseX, mouseY, button));
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0 && dragging) {
            dragging = false;
        }

        buttons.forEach((button) -> button.mouseReleased(mouseX, mouseY, state));
    }

    @Override
    public void keyTyped(char character, int code) {
        buttons.forEach((button) -> button.keyTyped(character, code));
    }

    private int adjustDistance(String renderText) {
        int distance;
        if (mc.standardGalacticFontRenderer.getStringWidth(renderText) == 3) {
            distance = mc.standardGalacticFontRenderer.getStringWidth(renderText);
        } else {
            distance = -3;
        }
        return distance;
    }

    private double getTotalHeight() {
        double origin = 0.0;

        if (opened) {
            for (ModuleButton button : buttons) {
                if (button.isVisible()) {
                    origin += button.getHeight() + 1.5;
                }
            }
        }

        return origin + 1.5;
    }

    private double getTotalHUDHeight() {
        double origin = 0.0;

        if (opened) {
            for (HUDOption button : elements) {
                if (button.isVisible()) {
                    origin += button.getHeight() + 1.5;
                }
            }
        }

        return origin + 1.5;
    }
}
