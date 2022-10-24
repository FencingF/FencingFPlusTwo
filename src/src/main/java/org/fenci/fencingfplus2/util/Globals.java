package org.fenci.fencingfplus2.util;

import net.minecraft.client.Minecraft;
import org.fenci.fencingfplus2.FencingFPlus2;

public interface Globals {
    Minecraft mc = Minecraft.getMinecraft();

    default boolean fullNullCheck() {
        return mc.player != null && mc.world != null;
    }

    default FencingFPlus2 getFencing() {
        return FencingFPlus2.INSTANCE;
    }
}
