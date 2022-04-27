package org.fenci.fencingfplus2.features.module.modules.misc;

import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;

public class Timer extends Module {
    public Timer() {
        super("Timer", "Changes tick rate client side", Category.Misc);
    }

    public static final Setting<Float> tickRate = new Setting<>("TickRate", 1.0f, 0, 50);

    @Override
    public String getDisplayInfo() {
        return tickRate.getValue().toString();
    }

    @Override
    public void onUpdate() {
        getFencing().tickManager.setTicks(tickRate.getValue());
    }

    @Override
    public void onDisable() {
        getFencing().tickManager.setTicks(1.0f);
    }
}
