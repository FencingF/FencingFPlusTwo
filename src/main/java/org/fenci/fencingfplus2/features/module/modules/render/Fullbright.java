package org.fenci.fencingfplus2.features.module.modules.render;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.Globals;

public class Fullbright extends Module {
    public Fullbright() {
        super("Fullbright", "Makes your game brighter", Category.Render);
    }

    public static final Setting<Enum> mode = new Setting<>("Mode", lightMode.Gamma);
    public static float oldGamma = -1.0f;

    @Override
    public String getDisplayInfo() {
        return mode.getValue().name();
    }

    @Override
    protected void onDisable() {
        if (oldGamma != -1.0f) {
            Globals.mc.gameSettings.gammaSetting = oldGamma;
            oldGamma = -1.0f;
        }

        if (Globals.mc.player.isPotionActive(MobEffects.NIGHT_VISION)) {
            Globals.mc.player.removePotionEffect(MobEffects.NIGHT_VISION);
        }
    }

    @Override
    public void onUpdate() {
        if (mode.getValue() == lightMode.Gamma) {
            if (oldGamma == -1.0f) {
                oldGamma = Globals.mc.gameSettings.gammaSetting;
            }

            if (Globals.mc.player.isPotionActive(MobEffects.NIGHT_VISION)) {
                Globals.mc.player.removePotionEffect(MobEffects.NIGHT_VISION);
            }

            Globals.mc.gameSettings.gammaSetting = 100.0f;
        } else if (mode.getValue() == lightMode.Potion) {
            if (oldGamma != -1.0f) {
                Globals.mc.gameSettings.gammaSetting = oldGamma;
                oldGamma = -1.0f;
            }

            if (!Globals.mc.player.isPotionActive(MobEffects.NIGHT_VISION)) {
                Globals.mc.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, (int) Long.MAX_VALUE));
            }
        }
    }

    public enum lightMode {
        /**
         * Changes the game's gramma value directly
         */
        Gamma,

        /**
         * Adds the NIGHT_VISION potion effect infinitely to the player
         */
        Potion
    }
}
