package org.fenci.fencingfplus2.features.module.modules.movement;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.network.PacketEvent;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.Globals;
import org.lwjgl.input.Keyboard;

public class NoSlow extends Module {

    public static NoSlow INSTANCE;

    public NoSlow() {
        super("NoSlow", "Allows you to do stuff without slowing down", Category.Movement);
        INSTANCE = this;
    }

    public static final Setting<Boolean> items = new Setting<>("Items", true);
    // blocks
    //public static final Setting<Boolean> soulsand = new Setting<>("Soulsand", false);
    //public static final Setting<Boolean> slime = new Setting<>("Slime", false); // TODO: 12/18/2021 This stuff
    public static final Setting<Boolean> inventory = new Setting<>("Inventory", true);
    public static final Setting<Boolean> webs = new Setting<>("Webs", true);
    public static final Setting<Integer> lookSpeed = new Setting<>("LookSpeed", 5, 0, 10);
    // anti-cheat
    public static final Setting<Bypass> bypass = new Setting<>("Bypass", Bypass.NCP);
    public static final Setting<Boolean> inventoryMoveBypass = new Setting<>("InvMoveBypass", false);
    private boolean sneaking = false;

    @Override
    public String getDisplayInfo() {
        return bypass.getValue().toString();
    }

    @Override
    public void onDisable() {
        if (fullNullCheck() && sneaking) {
            sneaking = false;
            Globals.mc.player.connection.sendPacket(new CPacketEntityAction(Globals.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
    }

    @Override
    public void onUpdate() {
        if (sneaking && !Globals.mc.player.isHandActive() && !mc.player.isElytraFlying()) {
            sneaking = false;
            Globals.mc.player.connection.sendPacket(new CPacketEntityAction(Globals.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }

        if (inventory.getValue() && !(mc.currentScreen instanceof GuiChat) && !(mc.currentScreen instanceof GuiScreenBook) && AutoWalk.INSTANCE.isOff()) {
            if (Keyboard.isKeyDown(200)) mc.player.rotationPitch -= lookSpeed.getValue();

            if (Keyboard.isKeyDown(208)) mc.player.rotationPitch += lookSpeed.getValue();

            if (Keyboard.isKeyDown(205)) mc.player.rotationYaw += lookSpeed.getValue();

            if (Keyboard.isKeyDown(203)) mc.player.rotationYaw -= lookSpeed.getValue();

            if (mc.player.rotationPitch > 90) mc.player.rotationPitch = 90;

            if (mc.player.rotationPitch < -90) mc.player.rotationPitch = -90;

            for (final KeyBinding keyBinding : KEYS) {
                if (Keyboard.isKeyDown(keyBinding.getKeyCode())) {
                    if (keyBinding.getKeyConflictContext() != KeyConflictContext.UNIVERSAL) {
                        keyBinding.setKeyConflictContext(KeyConflictContext.UNIVERSAL);
                    }
                    KeyBinding.setKeyBindState(keyBinding.getKeyCode(), true);
                } else {
                    KeyBinding.setKeyBindState(keyBinding.getKeyCode(), false);
                }
            }
        }
    }
    public static KeyBinding[] KEYS;

    static {
        KEYS = new KeyBinding[] { mc.gameSettings.keyBindForward, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSprint };
    }

    @SubscribeEvent
    public void onEntityItemUse(LivingEntityUseItemEvent event) {
        if (mc.player.isElytraFlying() || !fullNullCheck()) return;
        if (event.getEntity() == Globals.mc.player && items.getValue()) {
            if (bypass.getValue() == Bypass.Sneak) {
                if (!sneaking) {
                    sneaking = true;
                    Globals.mc.player.connection.sendPacket(new CPacketEntityAction(Globals.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                }
            } else if (bypass.getValue() == Bypass.FencingF && !mc.player.isElytraFlying()) {
                Globals.mc.player.connection.sendPacket(new CPacketHeldItemChange(Globals.mc.player.inventory.currentItem)); // lmao nice
            }
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (!fullNullCheck()) return;
        if (event.getPacket() instanceof CPacketClickWindow) {
            if (inventoryMoveBypass.getValue()) {
                Globals.mc.player.connection.sendPacket(new CPacketEntityAction(Globals.mc.player, CPacketEntityAction.Action.STOP_SPRINTING)); // nice
            }
        } else if (event.getPacket() instanceof CPacketPlayer) {
            if (bypass.getValue() == Bypass.NCP) {
                Globals.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, Globals.mc.player.getPosition(), EnumFacing.DOWN));
            }
        }
    }

    @SubscribeEvent
    public void onInputUpdate(final InputUpdateEvent event) {
        if (NoSlow.mc.player.isHandActive() && !NoSlow.mc.player.isRiding()) {
            final MovementInput movementInput = event.getMovementInput();
            movementInput.moveStrafe *= 5.0f;
            final MovementInput movementInput2 = event.getMovementInput();
            movementInput2.moveForward *= 5.0f;
        }
    }

    public enum Bypass {
        /**
         * Bypasses old NCP versions or shitty ass anti-cheats
         */
        NCP,

        /**
         * Old, sneak method. Only works while strafing.
         */
        Sneak,

        /**
         * The infamous FencingFPlus2+2 NoSlow bypass
         * Skidded into Konass, RusherHack, Future, and every paid client under the sun
         */
        FencingF
    }
}
