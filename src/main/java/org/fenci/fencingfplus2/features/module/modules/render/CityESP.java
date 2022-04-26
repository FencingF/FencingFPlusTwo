package org.fenci.fencingfplus2.features.module.modules.render;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.render.RenderUtil;
import org.fenci.fencingfplus2.util.world.BlockUtil;
import org.fenci.fencingfplus2.util.world.CityUtil;

public class CityESP extends Module {
    public CityESP() {
        super("CityESP", "Shows blocks you can city", Category.Render);
    }

    public static final Setting<Double> range = new Setting<>("Range", 50.0, 1.0, 128.0);
    public static final Setting<Integer> Red_SETTING1 = new Setting<>("Red", 172, 0, 255);
    public static final Setting<Integer> Green_SETTING1 = new Setting<>("Green", 57, 0, 255);
    public static final Setting<Integer> Blue_SETTING1 = new Setting<>("Blue", 45, 0, 255);
    public static final Setting<Integer> Alpha_SETTING1 = new Setting<>("Alpha", 163, 0, 255);

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (!fullNullCheck()) return;
        for (BlockPos pos : CityUtil.getAllCityPositions()) {
            if (BlockUtil.canBreak(pos)) {
                RenderUtil.drawBox(RenderUtil.generateBB(pos.getX(), pos.getY(), pos.getZ()), Red_SETTING1.getValue() / 255f, Green_SETTING1.getValue() / 255f, Blue_SETTING1.getValue() / 255f, Alpha_SETTING1.getValue() / 255f);
            }
        }
    }
}
