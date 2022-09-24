package org.fenci.fencingfplus2.features.module.modules.combat;

import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.network.PacketEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;

public class FastProjectile extends Module {
    public static final Setting<Float> speed = new Setting<>("Speed", 1.2f, 1.0, 10.0);

    public FastProjectile() {
        super("FastProjectile", "Increases your projectile speed", Category.Combat);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST) //put it on highest so it can't get canceled by velocity first
    public void onPacket(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketEntityVelocity) {
            SPacketEntityVelocity sPacketEntityVelocity = event.getPacket();
            if (sPacketEntityVelocity.getEntityID() == mc.player.entityId) return;
            sPacketEntityVelocity.motionX *= speed.getValue();
            sPacketEntityVelocity.motionY *= speed.getValue();
            sPacketEntityVelocity.motionZ *= speed.getValue();
        }
    }
}
