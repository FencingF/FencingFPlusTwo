package org.fenci.fencingfplus2.features.module.modules.misc;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.fenci.fencingfplus2.features.module.Module;

public class AntiLevitation extends Module {
    public AntiLevitation() {
        super("AntiLevitation", "Removes the levitation effect", Category.Misc);
    }

    @Override
    public void onUpdate() {
        if (mc.player.isPotionActive(MobEffects.LEVITATION)) {
            mc.player.removeActivePotionEffect(MobEffects.LEVITATION);
        }
    }
}
