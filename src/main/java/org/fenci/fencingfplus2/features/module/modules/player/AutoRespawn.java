package org.fenci.fencingfplus2.features.module.modules.player;

import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.Globals;
import org.fenci.fencingfplus2.util.client.ClientMessage;

public class AutoRespawn extends Module {
    public AutoRespawn() {
        super("AutoRespawn", "Automatically respawns you", Category.Player);
    }

    public static final Setting<Boolean> deathCoords = new Setting<>("DeathCoords", false);

    @SubscribeEvent(priority = EventPriority.HIGHEST) // before a gui change event can be canceled
    public void onDisplayDeathScreen(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiGameOver) {
            if (deathCoords.getValue()) {
                ClientMessage.sendOverwriteClientMessage("You died at: " + (int) Globals.mc.player.posX + " " + (int) Globals.mc.player.posY + " " + (int) Globals.mc.player.posZ + ".");
            }

            Globals.mc.player.respawnPlayer();
        }
    }
}
