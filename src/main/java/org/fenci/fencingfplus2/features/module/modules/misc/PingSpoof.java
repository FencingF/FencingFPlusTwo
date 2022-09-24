package org.fenci.fencingfplus2.features.module.modules.misc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.network.PacketEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.Timer;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PingSpoof extends Module {
    public static final Setting<Double> delay = new Setting<>("Delay", 5.0, 0.1, 10.0);
    public static final Setting<Boolean> transactions = new Setting<>("Transactions", true);
    private final Queue<Packet<?>> packets = new ConcurrentLinkedQueue<>();
    private final Timer stopwatch = new Timer();
    private boolean sending = false;

    public PingSpoof() {
        super("PingSpoof", "Spoofs your latency to the server", Category.Misc);
    }

    @Override
    protected void onDisable() {
        sendAllPackets();
    }

    @Override
    public void onUpdate() {
        if (stopwatch.hasReached(delay.getValue().longValue() * 1000)) {
            sendAllPackets();
            stopwatch.reset();
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketKeepAlive || (transactions.getValue() && event.getPacket() instanceof CPacketConfirmTransaction) && !sending) {
            packets.add(event.getPacket());
            event.setCanceled(true);
        }
    }

    private void sendAllPackets() {
        sending = true;

        while (!packets.isEmpty()) {
            Packet<?> packet = packets.poll();
            if (packet == null) {
                break;
            }

            mc.player.connection.sendPacket(packet);
        }

        sending = false;
    }
}