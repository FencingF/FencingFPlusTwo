package org.fenci.fencingfplus2.features.module.modules.render;

import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;

public class CustomRenderDistance extends Module {
    public CustomRenderDistance() {
        super("CustomRenderDistance", "Allows you to change your render distance", Category.Render);
    }

    public static final Setting<Integer> chunks = new Setting<>("Chunks", 8, 0, 100);

    int oldChunks;

    /**
     * wasn't ganna add this again but someone requested it
     */

    @Override
    public void onEnable() {
        oldChunks = mc.gameSettings.renderDistanceChunks;
    }

    @Override
    public void onUpdate() {
        mc.gameSettings.renderDistanceChunks = chunks.getValue();
    }

    @Override
    public void onDisable() {
        mc.gameSettings.renderDistanceChunks = oldChunks;
    }
}
