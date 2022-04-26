package org.fenci.fencingfplus2.features.module.modules.movement;

import net.minecraft.network.play.client.CPacketPlayer;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;

import static org.fenci.fencingfplus2.util.player.PlayerUtil.getBlockHeight;

public class Step extends Module {
    public Step() {
        super("Step", "Allows you to automatically step up blocks", Category.Movement);
    }

    public static final Setting<Stepmode> mode = new Setting<>("Mode", Stepmode.Vanilla);
    public static final Setting<Float> height = new Setting<>("Height", 2f, 1f, 6f);

    @Override
    public String getDisplayInfo() {
        return mode.getValue().name();
    }

    @Override
    public void onUpdate() {
        if (mode.getValue().equals(Stepmode.Vanilla)) {
            mc.player.stepHeight = height.getValue();
        } else if (mode.getValue().equals(Stepmode.Normal)) {
            mc.player.stepHeight = 0.6f;
            if (height.getValue() > 2) height.setValue(2f);
            double n = getBlockHeight();

            if (n < 0 || n > 2) return;

            if (n == 2.0 && height.getValue() == 2) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.42, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.78, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.63, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.51, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.9, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.21, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.45, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.43, mc.player.posZ, mc.player.onGround));
                mc.player.setPosition(mc.player.posX, mc.player.posY + 2.0, mc.player.posZ);
            }
            if (n == 1.5 && height.getValue() >= 1.5) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805212, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.00133597911214, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16610926093821, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.24918707874468, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.1707870772188, mc.player.posZ, mc.player.onGround));
                mc.player.setPosition(mc.player.posX, mc.player.posY + 1.0, mc.player.posZ);
            }
            if (n == 1.0 && height.getValue() >= 1) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805212, mc.player.posZ, mc.player.onGround));
                mc.player.setPosition(mc.player.posX, mc.player.posY + 1.0, mc.player.posZ);
            }
        }
    }

    @Override
    public void onDisable() {
        mc.player.stepHeight = 0.6f;
    }

    public enum Stepmode {
        Vanilla, Normal
    }
}
