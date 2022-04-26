package org.fenci.fencingfplus2.features.module.modules.render;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.render.RenderUtil;
import org.fenci.fencingfplus2.util.world.HoleUtil;

public class HoleESP extends Module {
    public HoleESP() {
        super("HoleESP", "Shows you nearby holes", Category.Render);
    }

    public static final Setting<Float> range = new Setting<>("Range", 5f, 0, 20);
    public static final Setting<Integer> obiRed = new Setting<>("ObiRed", 90, 0, 255);
    public static final Setting<Integer> obiGreen = new Setting<>("ObiGreen", 30, 0, 255);
    public static final Setting<Integer> obiBlue = new Setting<>("ObiBlue", 150, 0, 255);
    public static final Setting<Integer> obiAlpha = new Setting<>("ObiAlpha", 50, 0, 255);
    public static final Setting<Integer> bedrockRed = new Setting<>("BedrockRed", 20, 0, 255);
    public static final Setting<Integer> bedrockGreen = new Setting<>("BedrockGreen", 180, 0, 255);
    public static final Setting<Integer> bedrockBlue = new Setting<>("BedrockBlue", 40, 0, 255);
    public static final Setting<Integer> bedrockAlpha = new Setting<>("BedrockAlpha", 50, 0, 255);

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (!fullNullCheck()) return;
        for (BlockPos pos : HoleUtil.findObbyHoles(range.getValue())) {
            RenderUtil.drawBox(RenderUtil.generateBB(pos.getX(), pos.getY(), pos.getZ()), obiRed.getValue() / 255f, obiGreen.getValue() / 255f, obiBlue.getValue() / 255f, obiAlpha.getValue() / 255f);
        }
        for (BlockPos pos : HoleUtil.findBRockHoles(range.getValue())) {
            RenderUtil.drawBox(RenderUtil.generateBB(pos.getX(), pos.getY(), pos.getZ()), bedrockRed.getValue() / 255f, bedrockGreen.getValue() / 255f, bedrockBlue.getValue() / 255f, bedrockAlpha.getValue() / 255f);
        }
    }
}
