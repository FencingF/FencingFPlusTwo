package org.fenci.fencingfplus2.features.module.modules.movement;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.network.PacketEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;

public class NoFall extends Module {
    public NoFall() {
        super("NoFall", "cancels your fall", Module.Category.Movement);
    }

    public static final Setting<Mode> packet = new Setting<>("Packet", Mode.Packet);
    //settings
    public static final Setting<Integer> distance = new Setting<>("Distance", 3, 1, 5);

    @Override
    public void onUpdate() {
        if (mc.player.fallDistance > distance.getValue() && packet.getValue() == Mode.Anti) {
            mc.player.motionY = 1;
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer && mc.player.fallDistance > distance.getValue() && packet.getValue() == Mode.Packet) {
            event.setCanceled(true);
        }
    }

    public enum Mode {
        Packet,
        Anti
    }
}

