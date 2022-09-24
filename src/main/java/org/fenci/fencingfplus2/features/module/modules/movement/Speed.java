package org.fenci.fencingfplus2.features.module.modules.movement;

import net.minecraft.block.BlockLiquid;
import net.minecraft.init.MobEffects;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.player.MoveEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.Timer;
import org.fenci.fencingfplus2.util.player.PlayerUtil;

import java.util.Objects;

public class Speed extends Module {

    public static final Setting<Mode> mode = new Setting<>("Mode", Mode.Strafe);
    public static final Setting<Double> timerSpeed = new Setting<>("TimerSpeed", 1.15, 1.0, 1.5);
    public static final Setting<Double> jumpHeight = new Setting<>("JumpHeight", 0.41, 0.0, 1.0);
    public static final Setting<Double> ySpeed = new Setting<>("YSpeed", 0.06, 0.01, 0.15);
    public static Speed INSTANCE;
    private final Timer timer = new Timer();
    //public static final Setting<Boolean> useTimer = new Setting<>("UseTimer", true);
    private boolean slowdown;
    private double playerSpeed;

    public Speed() {
        super("Speed", "Zoom", Category.Movement);
        INSTANCE = this;
    }

    @Override
    public String getDisplayInfo() {
        return mode.getValue().name();
    }

    @Override
    public void onEnable() {
        playerSpeed = PlayerUtil.getBaseMoveSpeed();
    }

    @Override
    public void onDisable() {
        PlayerUtil.resetTimer();
        this.timer.reset();
        getFencing().tickManager.setTicks(1.0f);
    }

    @Override
    public void onUpdate() {
        if (mode.getValue().equals(Mode.YPort)) {
            this.handleYPortSpeed();
        }
    }

    private void handleYPortSpeed() {
        if (!PlayerUtil.isMoving(mc.player) || mc.player.isInWater() && mc.player.isInLava() || mc.player.collidedHorizontally) {
            return;
        }

        if (mc.player.onGround) {
            PlayerUtil.setTimer(1.15f);
            mc.player.jump();
            PlayerUtil.setSpeed(mc.player, PlayerUtil.getBaseMoveSpeed() + ySpeed.getValue());
        } else {
            mc.player.motionY = -1;
            PlayerUtil.resetTimer();
        }
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (mc.player.isInLava() || mc.player.isInWater() || mc.player.isOnLadder() || mc.player.isInWeb) return;

        if (mode.getValue().equals(Mode.Strafe)) {
            double speedY = jumpHeight.getValue();
            if (mc.player.onGround && PlayerUtil.isMoving(mc.player) && timer.hasReached(300)) {
                PlayerUtil.setTimer(timerSpeed.getValue().floatValue());
                if (mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                    speedY += (Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST)).getAmplifier() + 1) * 0.1f;
                }
                event.setY(mc.player.motionY = speedY);
                playerSpeed = PlayerUtil.getBaseMoveSpeed() * (PlayerUtil.isColliding(0, -0.5, 0) instanceof BlockLiquid && !PlayerUtil.isInLiquid() ? 0.9 : 1.901);
                slowdown = true;
                timer.reset();
            } else {
                PlayerUtil.resetTimer();
                if (slowdown || mc.player.collidedHorizontally) {
                    playerSpeed -= (PlayerUtil.isColliding(0, -0.8, 0) instanceof BlockLiquid && !PlayerUtil.isInLiquid()) ? 0.4 : 0.7 * PlayerUtil.getBaseMoveSpeed();
                    slowdown = false;
                } else {
                    playerSpeed -= playerSpeed / 159.0;
                }
            }
            playerSpeed = Math.max(playerSpeed, PlayerUtil.getBaseMoveSpeed());
            double[] dir = PlayerUtil.forward(playerSpeed);
            event.setX(dir[0]);
            event.setZ(dir[1]);
        }
    }

    public enum Mode {
        Strafe, YPort
    }
}
