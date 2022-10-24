package org.fenci.fencingfplus2.features.module.modules.misc;

import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.features.module.Module;

public class NoPause extends Module {
    public NoPause() {
        super("NoPause", "Doesn't show the pause screen", Category.Misc);
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiIngameMenu && fullNullCheck()) {
            event.setGui(null);
        }
    }
}
