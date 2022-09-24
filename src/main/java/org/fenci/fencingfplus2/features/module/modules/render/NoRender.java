package org.fenci.fencingfplus2.features.module.modules.render;

import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.network.PacketEvent;
import org.fenci.fencingfplus2.events.render.BossBarEvent;
import org.fenci.fencingfplus2.events.render.RenderArmorEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;

public class NoRender extends Module {

    public static final Setting<Boolean> nausea = new Setting<>("Nausea", true);
    public static final Setting<Boolean> blindness = new Setting<>("Blindness", true);
    public static final Setting<Boolean> weather = new Setting<>("Weather", true);
    public static final Setting<Boolean> explosions = new Setting<>("Explosions", true);
    public static final Setting<Boolean> hurtcam = new Setting<>("Hurtcam", true);
    public static final Setting<Boolean> skylight = new Setting<>("Skylight", false);
    public static final Setting<Boolean> bossBar = new Setting<>("BossBar", false);
    public static final Setting<Boolean> armor = new Setting<>("Armor", false);
    public static final Setting<Boolean> enchantGlint = new Setting<>("EnchantGlint", false);
    //    public static final Setting<Boolean> lava = new Setting<>("Lava", false);
    public static final Setting<Boolean> fire = new Setting<>("Fire", true);
    public static final Setting<Boolean> dynamicFOV = new Setting<>("DynamicFOV", false);

    public static NoRender INSTANCE;

    public NoRender() {
        super("NoRender", "Allows you to not render things", Category.Render);
        INSTANCE = this;
    }

    @SubscribeEvent
    public void renderBlockEvent(RenderBlockOverlayEvent event) {
        if (fire.getValue() && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.FIRE) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onBossRender(BossBarEvent event) {
        if (bossBar.getValue()) {
            event.setCanceled(true);
        }
    }

    @Override
    public void onUpdate() {
        if (blindness.getValue() && mc.player.isPotionActive(MobEffects.BLINDNESS)) {
            mc.player.removePotionEffect(MobEffects.BLINDNESS);
        }
        if (nausea.getValue() && mc.player.isPotionActive(MobEffects.NAUSEA)) {
            mc.player.removePotionEffect(MobEffects.NAUSEA);
        }
        if (weather.getValue()) {
            mc.world.setRainStrength(0.0F);
        }
    }

//    @SubscribeEvent
//    public void onFog(EntityViewRenderEvent.RenderFogEvent event) {
//        if (lava.getValue() && fullNullCheck() && mc.player.ticksExisted >= 20) {
//            event.setCanceled(true);
//        }
//    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event) {
        if (explosions.getValue() && event.getPacket() instanceof SPacketExplosion) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onArmorRender(RenderArmorEvent event) {
        if (armor.getValue()) {
            event.setCanceled(true);
        }
    }
}

