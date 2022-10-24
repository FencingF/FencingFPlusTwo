package org.fenci.fencingfplus2.features.module.modules.render;


import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;

public class CustomRenderDistance extends Module {
    public static final Setting<Integer> chunks = new Setting<>("Chunks", 8, 0, 100);
    int oldChunks;

    public CustomRenderDistance() {
        super("CustomRender", "Allows you to change your render distance for some reason", Category.Render);
    }

    /**
     * wasn't ganna add this again but someone requested it so i added it :D
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
