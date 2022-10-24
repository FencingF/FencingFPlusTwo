package org.fenci.fencingfplus2.features.hud.elements;

import org.fenci.fencingfplus2.features.hud.HUDElement;
import org.fenci.fencingfplus2.util.client.ClientMessage;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import static org.fenci.fencingfplus2.features.module.modules.client.HUD.copyCoords;

public class CopyCoords extends HUDElement {
    public CopyCoords() {
        super("CopyCoords", 2f, 12f);
    }

    @Override
    public void onEnable() {
        String coordinates = (int) mc.player.posX + " " + (int) mc.player.posY + " " + (int) mc.player.posZ;
        StringSelection stringSelection = new StringSelection(coordinates);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        ClientMessage.sendMessage("Copied coordinates to clipboard.");
        this.toggle();
    }

    @Override
    public void onHudRender() {}
}
