package org.fenci.fencingfplus2.features.module.modules.misc;

import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;

public class AntiPauseScreen extends Module {
    public AntiPauseScreen() {
        super("AntiPauseScreen", "Doesn't show the pause screen", Category.Misc);
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiIngameMenu && fullNullCheck()) {
            event.setGui(null);
        }
    }
}
