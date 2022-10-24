package org.fenci.fencingfplus2.features.module.modules.player;

import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.network.PacketEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;

public class PacketCancel extends Module {
    public static final Setting<Boolean> cPacketInput = new Setting<>("CPacketInput", false);
    public static final Setting<Boolean> cPacketPlayer = new Setting<>("CPacketPlayer", false);
    public static final Setting<Boolean> cPacketEntityAction = new Setting<>("CPacketEntityAction", false);
    public static final Setting<Boolean> cPacketUseEntity = new Setting<>("CPacketUseEntity", false);
    public static final Setting<Boolean> cPacketVehicleMove = new Setting<>("CPacketVehicleMove", false);
    public static final Setting<Boolean> sPacketCloseWindow = new Setting<>("SPacketCloseWindow", false);

    public PacketCancel() {
        super("PacketCancel", "Cancels Packets", Category.Player);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketInput && cPacketInput.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlayer && cPacketPlayer.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketEntityAction && cPacketEntityAction.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketUseEntity && cPacketUseEntity.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketVehicleMove && cPacketVehicleMove.getValue()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketCloseWindow && sPacketCloseWindow.getValue()) {
            event.setCanceled(true);
        }
    }
}
