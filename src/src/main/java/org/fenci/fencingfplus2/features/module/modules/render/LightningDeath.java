package org.fenci.fencingfplus2.features.module.modules.render;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.player.DeathEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;

public class LightningDeath extends Module {
    //public static final Setting<Boolean> lightningDeath = new Setting<>("LightningDeath", true);
    //public static final Setting<Boolean> self = new Setting<>("Self", false);
    public static EntityOtherPlayerMP player;
    //public static final Setting<NetherWeather> netherWeather = new Setting<>("NetherWeather", NetherWeather.Snow);
    public static EntityPlayer entity;
    public static long time;

    public LightningDeath() {
        super("LightningDeath", "Adds effects", Category.Render);
    }

    @Override
    public void onUpdate() {
        //if (netherWeather.getValue().equals(NetherWeather.Rain)) mc.world.rainingStrength = 5;
    }

    @SubscribeEvent
    public void onDeath(DeathEvent event) {
        if (!event.getPlayer().equals(mc.player) /*&& self.getValue() && lightningDeath.getValue()*/) {
            final EntityLightningBolt bolt = new EntityLightningBolt(mc.world, Double.longBitsToDouble(Double.doubleToLongBits(2.700619365101586E307) ^ 0x7FC33AA2E6830ED7L), Double.longBitsToDouble(Double.doubleToLongBits(4.288545480809007E306) ^ 0x7F986DA963B0A5BFL), Double.longBitsToDouble(Double.doubleToLongBits(3.3865723560928404E307) ^ 0x7FC81CFA62BC4207L), false);
            mc.world.playSound(event.getPlayer().getPosition(), SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.WEATHER, Float.intBitsToFloat(Float.floatToIntBits(13.150525f) ^ 0x7ED2688D), Float.intBitsToFloat(Float.floatToIntBits(10.325938f) ^ 0x7EA5370B), false);
            bolt.setLocationAndAngles(event.getPlayer().posX, event.getPlayer().posY, event.getPlayer().posZ, Float.intBitsToFloat(Float.floatToIntBits(3.2116163E38f) ^ 0x7F719D7B), Float.intBitsToFloat(Float.floatToIntBits(2.2278233E38f) ^ 0x7F279A51));
            mc.world.spawnEntity(bolt);
        }
    }

    public enum NetherWeather {
        Off, Snow, Rain
    }
}
