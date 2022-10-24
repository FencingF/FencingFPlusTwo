package org.fenci.fencingfplus2.features.module.modules.player;



import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;

public class ViewLock extends Module {
    public static final Setting<Mode> hand = new Setting<>("Mode", Mode.Both);
    public static final Setting<Float> yaw = new Setting<>("Yaw", 1f, 0, 8);
    public static final Setting<Float> pitch = new Setting<>("Pitch", 1f, -90, 90);

    public ViewLock() {
        super("ViewLock", "Locks your view", Category.Player);
    }

    @Override
    public void onUpdate() {
        if (hand.getValue().equals(Mode.Yaw) || hand.getValue().equals(Mode.Both)) {

            mc.player.rotationYaw = yaw.getValue() * 45;
        }
        if (hand.getValue().equals(Mode.Pitch) || hand.getValue().equals(Mode.Both)) {

            mc.player.rotationPitch = pitch.getValue();
        }
    }

    public enum Mode {
        Both, Yaw, Pitch
    }
}