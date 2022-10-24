package org.fenci.fencingfplus2.features.module.modules.player;

import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.ClientMessage;

public class AutoRespawn extends Module {
    public static final Setting<Boolean> deathCoords = new Setting<>("DeathCoords", false);

    public AutoRespawn() {
        super("AutoRespawn", "Automatically respawns you", Category.Player);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST) // before a gui change event can be canceled
    public void onDisplayDeathScreen(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiGameOver) {
            if (deathCoords.getValue()) {
                ClientMessage.sendOverwriteClientMessage("You died at: " + (int) mc.player.posX + " " + (int) mc.player.posY + " " + (int) mc.player.posZ + ".");
            }
            mc.player.respawnPlayer();
        }
    }
}
