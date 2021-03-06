package org.fenci.fencingfplus2.manager;

import com.google.common.base.Strings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.fenci.fencingfplus2.events.client.FriendEvent;
import org.fenci.fencingfplus2.events.network.ConnectionEvent;
import org.fenci.fencingfplus2.events.network.PacketEvent;
import org.fenci.fencingfplus2.events.player.DeathEvent;
import org.fenci.fencingfplus2.events.player.TotemPopEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.features.module.modules.client.ClickGUI;
import org.fenci.fencingfplus2.features.module.modules.client.Preferences;
import org.fenci.fencingfplus2.features.module.modules.combat.SelfWeb;
import org.fenci.fencingfplus2.features.module.modules.misc.FakePlayer;
import org.fenci.fencingfplus2.util.Globals;
import org.fenci.fencingfplus2.util.client.ClientMessage;
import org.fenci.fencingfplus2.util.client.Timer;
import org.fenci.fencingfplus2.util.player.PlayerUtil;
import org.fenci.fencingfplus2.util.render.ColorUtil;
import org.fenci.fencingfplus2.util.world.HoleUtil;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Objects;
import java.util.UUID;

import static org.fenci.fencingfplus2.features.module.modules.client.HUD.copyCoords;

public class EventManager implements Globals {
    private static EventManager INSTANCE;

    private EventManager() {
    }

    public static EventManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EventManager();
        }

        return INSTANCE;
    }

    private final Timer logoutTimer = new Timer();

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (fullNullCheck() && event.getEntity() == mc.player) {
            getFencing().popManager.onUpdate();
            getFencing().tickManager.onUpdate();
            for (Module module : getFencing().moduleManager.getModules()) {
                if (module.isToggled()) {
                    module.onUpdate();
                }
            }
            getFencing().tickManager.onUpdate();
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (ClickGUI.rainbow.getValue() && fullNullCheck()) {
            ClickGUI.getred.setValue(ColorUtil.releasedDynamicRainbow(3, ClickGUI.saturation.getValue(), ClickGUI.brightness.getValue()).getRed());
            ClickGUI.getgreen.setValue(ColorUtil.releasedDynamicRainbow(3, ClickGUI.saturation.getValue(), ClickGUI.brightness.getValue()).getGreen());
            ClickGUI.getblue.setValue(ColorUtil.releasedDynamicRainbow(3, ClickGUI.saturation.getValue(), ClickGUI.brightness.getValue()).getBlue());
        }
        if (copyCoords.getValue() && fullNullCheck()) {
            String coordinates = (int) mc.player.posX + " " + (int) mc.player.posY + " " + (int) mc.player.posZ;
            StringSelection stringSelection = new StringSelection(coordinates);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
            ClientMessage.sendMessage("Copied coordinates to clipboard.");
            copyCoords.setValue(false);
        }
//        if (GroundStrafe.smartEnable.getValue() && PlayerUtil.isOnStairs() && Speed.INSTANCE.isOff() && !mc.gameSettings.keyBindJump.isKeyDown()) {
//            GroundStrafe.INSTANCE.setToggled(true);
//        } else if (GroundStrafe.smartEnable.getValue() && !PlayerUtil.isOnStairs()) {
//            GroundStrafe.INSTANCE.setToggled(false);
//        }
        if (SelfWeb.enableInHole.getValue() && HoleUtil.isInHole(mc.player) && !PlayerUtil.isInBurrow(mc.player) && fullNullCheck()) {
            SelfWeb.INSTANCE.setToggled(true);
        }
    }

    @SubscribeEvent
    public void onRenderHudText(RenderGameOverlayEvent.Text event) {
        if (fullNullCheck()) {
            for (Module module : getFencing().moduleManager.getModules()) {
                if (module.isToggled()) {
                    module.onRender2D();
                }
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (fullNullCheck()) {
            for (Module module : getFencing().moduleManager.getModules()) {
                if (module.isOn()) {
                    module.onRender3D();
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketEntityStatus) {
            SPacketEntityStatus packet = event.getPacket();
            if (packet.getOpCode() == 35 && packet.getEntity(mc.world) instanceof EntityPlayer) {
                MinecraftForge.EVENT_BUS.post(new TotemPopEvent((EntityPlayer) packet.getEntity(mc.world)));
            }
        } else if (event.getPacket() instanceof SPacketPlayerListItem && fullNullCheck() && logoutTimer.hasReached(1L)) {
            SPacketPlayerListItem packet = event.getPacket();
            if (!SPacketPlayerListItem.Action.ADD_PLAYER.equals(packet.getAction()) && !SPacketPlayerListItem.Action.REMOVE_PLAYER.equals(packet.getAction())) {
                return;
            }
            packet.getEntries().stream().filter(Objects::nonNull).filter(data -> !Strings.isNullOrEmpty(data.getProfile().getName()) || data.getProfile().getId() != null).forEach(data -> {
                UUID id = data.getProfile().getId();
                switch (packet.getAction()) {
                    case ADD_PLAYER: {
                        String name = data.getProfile().getName();
                        MinecraftForge.EVENT_BUS.post(new ConnectionEvent(id, name, ConnectionEvent.Type.Join));
                        break;
                    }
                    case REMOVE_PLAYER: {
                        EntityPlayer entity = mc.world.getPlayerEntityByUUID(id);
                        if (entity != null) {
                            String logoutName = entity.getName();
                            MinecraftForge.EVENT_BUS.post(new ConnectionEvent(entity, id, logoutName, ConnectionEvent.Type.Leave));
                            break;
                        }
                        MinecraftForge.EVENT_BUS.post(new ConnectionEvent(id, null, ConnectionEvent.Type.Other));
                    }
                    default:
                        break;
                }
            });
        } else if (event.getPacket() instanceof SPacketEntityMetadata) {
            SPacketEntityMetadata packet = event.getPacket();
            Entity entity = mc.world.getEntityByID(packet.getEntityId());
            if (!(entity instanceof EntityPlayer)) {
                return;
            }

            EntityPlayer player = (EntityPlayer) entity;
            if (player.getHealth() <= 0.0f || player.isDead) {
                MinecraftForge.EVENT_BUS.post(new DeathEvent(player));
            }
        } else if (event.getPacket() instanceof SPacketExplosion && FakePlayer.INSTANCE.fakePlayer != null) {
            final SPacketExplosion explosion = event.getPacket();
            if (FakePlayer.INSTANCE.fakePlayer.getDistance(explosion.getX(), explosion.getY(), explosion.getZ()) <= 15 && FakePlayer.INSTANCE.isOn()) {
                final double damage = FakePlayer.calculateDamage(explosion.getX(), explosion.getY(), explosion.getZ(), FakePlayer.INSTANCE.fakePlayer);
                if (damage > 0 && FakePlayer.pops.getValue()) {
                    MinecraftForge.EVENT_BUS.post(new TotemPopEvent(FakePlayer.INSTANCE.fakePlayer));
                    FakePlayer.INSTANCE.fakePlayer.setHealth((float) (FakePlayer.INSTANCE.fakePlayer.getHealth() - MathHelper.clamp(damage, 0, 999)));
                }
            }
        }
    }

    @SubscribeEvent
    public void onFriend(FriendEvent event) {
        if (!event.getUuid().equals(mc.player.getUniqueID())) {
            if (Preferences.msgFriendsOnAdd.getValue() && event.getType().equals(FriendEvent.Type.Add)) {
                mc.player.connection.sendPacket(new CPacketChatMessage("/msg " + event.getName() + " You have been added to my friends list!"));
            }
            if (Preferences.msgFriendsonRemove.getValue() && event.getType().equals(FriendEvent.Type.Remove)) {
                mc.player.connection.sendPacket(new CPacketChatMessage("/msg " + event.getName() + " You have been removed from my friends list!"));
            }
        }
    }
}