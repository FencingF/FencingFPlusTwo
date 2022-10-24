package org.fenci.fencingfplus2.gui;

import net.minecraft.client.gui.GuiScreen;
import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.features.hud.HUDElement;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.features.module.modules.client.ClickGUI;
import org.fenci.fencingfplus2.features.module.modules.client.HUDEditor;
import org.fenci.fencingfplus2.gui.components.Panel;
import org.fenci.fencingfplus2.gui.components.button.ModuleButton;
import org.fenci.fencingfplus2.gui.components.other.HUDOption;
import org.fenci.fencingfplus2.util.Globals;
import org.fenci.fencingfplus2.util.client.ClientMessage;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClickGUIScreen extends GuiScreen implements Globals {
    private static ClickGUIScreen GUIINSTANCE;
    private static ClickGUIScreen EDITORINSTANCE;

    private final ArrayList<Panel> panels = new ArrayList<>();

    private ClickGUIScreen() {
        double x = 4.0;
        if (ClickGUI.INSTANCE.isOn()) {
            for (Module.Category category : Module.Category.values()) {
                List<Module> modules = getFencing().moduleManager.getModules()
                        .stream().filter((module) -> module.getCategory().equals(category))
                        .filter(module -> !module.getName().equals("HUDEditor"))
                        .collect(Collectors.toList());

                if (modules.isEmpty()) {
                    continue;
                }

                panels.add(new Panel(category.name(), x, 4.0, 88.0, 15) {
                    @Override
                    public void init() {
                        modules.forEach((module) -> buttons.add(new ModuleButton(module)));
                    }
                });

                x += 92.0; //amount of space in between each category panel
            }
        }
        if (HUDEditor.INSTANCE.isOn()) {
            panels.add(new Panel("Elements:", 552, 4.0, 88.0, 15) {
                @Override
                public void init() {
                    FencingFPlus2.INSTANCE.hudElementManager.getElements().forEach((component) -> elements.add(new HUDOption(component)));
                }
            });
        }
    }

    public static ClickGUIScreen getGUIINSTANCE() {
        if (GUIINSTANCE == null) {
            GUIINSTANCE = new ClickGUIScreen();
        }
        return GUIINSTANCE;
    }

    public static ClickGUIScreen getEDITORINSTANCE() {
        if (EDITORINSTANCE == null) {
            EDITORINSTANCE = new ClickGUIScreen();
        }
        return EDITORINSTANCE;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawDefaultBackground();

        int scroll = Mouse.getDWheel();
        if (scroll > 0) {
            panels.forEach((panel) -> panel.setY(panel.getY() + 10.0));
        } else if (scroll < 0) {
            panels.forEach((panel) -> panel.setY(panel.getY() - 10.0));
        }

        panels.forEach((panel) -> panel.drawComponent(mouseX, mouseY, partialTicks));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        panels.forEach((panel) -> panel.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        panels.forEach((panel) -> panel.mouseReleased(mouseX, mouseY, state));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        panels.forEach((panel) -> panel.keyTyped(typedChar, keyCode));
    }
}