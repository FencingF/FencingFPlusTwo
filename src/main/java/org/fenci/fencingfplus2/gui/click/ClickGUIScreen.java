package org.fenci.fencingfplus2.gui.click;

import net.minecraft.client.gui.GuiScreen;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.gui.click.components.Panel;
import org.fenci.fencingfplus2.gui.click.components.button.ModuleButton;
import org.fenci.fencingfplus2.util.Globals;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClickGUIScreen extends GuiScreen implements Globals {
    private static ClickGUIScreen INSTANCE;

    private final ArrayList<Panel> panels = new ArrayList<>();

    private ClickGUIScreen() {
        double x = 4.0;
        for (Category category : Category.values()) {
            List<Module> modules = getFencing().moduleManager.getModules()
                    .stream().filter((module) -> module.getCategory().equals(category))
                    .collect(Collectors.toList());

            if (modules.isEmpty()) {
                continue;
            }

            panels.add(new Panel(category.name(), x, 4.0, 88.0, 15.0) {
                @Override
                public void init() {
                    modules.forEach((module) -> buttons.add(new ModuleButton(module)));
                }
            });

            x += 92.0;
        }
    }

    public static ClickGUIScreen getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGUIScreen();
        }

        return INSTANCE;
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
