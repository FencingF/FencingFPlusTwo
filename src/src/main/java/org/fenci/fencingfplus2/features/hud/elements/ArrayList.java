package org.fenci.fencingfplus2.features.hud.elements;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.events.client.ModuleToggleEvent;
import org.fenci.fencingfplus2.features.hud.HUDElement;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.features.module.modules.client.CustomFont;
import org.fenci.fencingfplus2.util.render.ColorUtil;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayList extends HUDElement {
    public ArrayList() {
        super("ArrayList", 2f, 40f);
    }

//    @Override
//    public int getWidth() {
//        List<Module> modules = getFencing().moduleManager.getModules().stream().filter((module) -> module.isToggled() && module.isDrawn()).collect(Collectors.toList());
//        //get longest module name from the list
//        int longest = 0;
//        for (Module module : modules) {
//            if (FencingFPlus2.INSTANCE.fontManager.getStringWidth(module.getFullDisplay()) > longest) {
//                longest = FencingFPlus2.INSTANCE.fontManager.getStringWidth(module.getFullDisplay());
//            }
//        }
//        return longest;
//    }

    @Override
    public int getHeight() {
        List<Module> modules = getFencing().moduleManager.getModules().stream().filter((module) -> module.isToggled() && module.isDrawn()).collect(Collectors.toList());
        //get longest module name from the list
        int height = 0;
        for (Module module : modules) {
            if (FencingFPlus2.INSTANCE.fontManager.getStringWidth(module.getFullDisplay()) > height) {
                height += FencingFPlus2.INSTANCE.fontManager.getTextHeight() + 1.5;
            }
        }
        return height;
    }

    @Override
    public void onHudRender() {
        ScaledResolution resolution = new ScaledResolution(mc);

        List<Module> modules = getFencing().moduleManager.getModules().stream().filter((module) -> module.isToggled() && module.isDrawn()).collect(Collectors.toList());

        if (!modules.isEmpty()) {
            if (CustomFont.INSTANCE.isOn()) {
                modules.sort(Comparator.comparingInt((mod) -> -FencingFPlus2.INSTANCE.fontManager.getStringWidth(mod.getFullDisplay()))); //TODO: Add two more quadrants
            } else {
                modules.sort(Comparator.comparingInt((mod) -> -mc.fontRenderer.getStringWidth(mod.getFullDisplay()))); //TODO: Add two more quadrants
            }
            float y = getPosY();
            for (Module module : modules) {
                String display = module.getFullDisplay();

                float x;
                if (getPosX() < resolution.getScaledWidth() / 2f) {
                    x = getPosX();
                } else {
                    if (CustomFont.INSTANCE.isOn()) {
                        x = resolution.getScaledWidth() - FencingFPlus2.INSTANCE.fontManager.getStringWidth(display) - 2.0f + getPosX() - resolution.getScaledWidth() + getWidth();
                    } else {
                        x = resolution.getScaledWidth() - mc.fontRenderer.getStringWidth(display) - 2.0f + getPosX() - resolution.getScaledWidth() + getWidth();
                    }
                }
                if (CustomFont.INSTANCE.isOn()) {
                    FencingFPlus2.INSTANCE.fontManager.drawStringWithShadow(display, x, y, ColorUtil.displayColor());
                    y += FencingFPlus2.INSTANCE.fontManager.getTextHeight() + 1.5f;
                } else {
                    mc.fontRenderer.drawStringWithShadow(display, x, y, ColorUtil.displayColor());
                    y += mc.fontRenderer.FONT_HEIGHT + 1.5f;
                }
            }
        }
    }
}
