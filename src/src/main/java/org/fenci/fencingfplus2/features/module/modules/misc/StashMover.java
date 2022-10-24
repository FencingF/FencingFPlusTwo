package org.fenci.fencingfplus2.features.module.modules.misc;

//import baritone.api.BaritoneAPI;
//import baritone.api.pathing.goals.GoalBlock;

import ca.weblite.objc.Client;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.client.OptionChangeEvent;
import org.fenci.fencingfplus2.events.network.StashMovePacketEvent;
import org.fenci.fencingfplus2.events.player.ClickBlockEvent;
import org.fenci.fencingfplus2.features.commands.commands.StashCommand;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.features.module.modules.combat.PacketMine;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.ClientClient;
import org.fenci.fencingfplus2.util.client.ClientMessage;
import org.fenci.fencingfplus2.util.client.ClientServer;
import org.fenci.fencingfplus2.util.custompackets.Packet;
import org.fenci.fencingfplus2.util.custompackets.packets.*;
import org.fenci.fencingfplus2.util.player.InventoryUtil;
import org.fenci.fencingfplus2.util.player.PlayerUtil;
import org.fenci.fencingfplus2.util.render.RenderUtil;
import org.fenci.fencingfplus2.util.world.BlockUtil;
import org.fenci.fencingfplus2.util.world.SerializableBlockPos;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.fenci.fencingfplus2.features.commands.commands.StashCommand.*;

public class StashMover extends Module {

    public static StashMover INSTANCE;

    public static final Setting<Mode> mode = new Setting<>("Mode", Mode.Moving);
    public static final Setting<Boolean> start = new Setting<>("Start", false);
    public static final Setting<Boolean> storeInEnderChest = new Setting<>("UseEchest", false);
    public static final Setting<Boolean> renderChests = new Setting<>("RenderChests", true);

    public StashMover() {
        super("StashMover", "Automatically moves your stash", Category.Misc);
        INSTANCE = this;
    }

    //TODO: Make it so only 1 INSTANCE has to select the chests.

    public static Set<SerializableBlockPos> chestsToMove = new HashSet<>();
    public static Set<SerializableBlockPos> chestsToMoveTo = new HashSet<>(); //home chests

    @Override
    public void onEnable() {
        if (mode.getValue().equals(Mode.Moving)) ClientMessage.sendMessage("Select dubs you want to move, and where you want to move them to using the \"stash\" command. When you are ready turn on the \"start\" setting under this module");
        if (PacketMine.INSTANCE.isOn()) {
            ClientMessage.sendMessage("Disabling Packetmine");
            PacketMine.INSTANCE.setToggled(false);
        }
    }

    @Override
    public void onDisable() {
        start.setValue(false);
    }

    @SubscribeEvent
    public void onOptionChangeEvent(OptionChangeEvent event) {
        if (event.getOption().equals(mode) && this.isOn()) {
            this.toggle(true);
        }
        if (event.getOption().equals(start) && start.getValue()) {
            if (mode.getValue().equals(Mode.Loading)) {
                try {
                    if (port == 0) {
                        ClientMessage.sendMessage("Port is null, please change this using the @stash command.");
                        return;

                    } else if (ip == null) {
                        ClientMessage.sendMessage("IP is null, please change this using the @stash command.");
                        return;

                    }
                    //ClientServer.INSTANCE.startServer(port);
                    ClientServer.startServer(port);
                } catch (Exception exception) {
                    ClientMessage.sendErrorMessage("Error could not start server.");
                    exception.printStackTrace();
                }
            } else if (mode.getValue().equals(Mode.Moving)) {
                try {
                    if (ip == null) {
                        ClientMessage.sendMessage("IP is null, please change this using the @stash command.");
                        return;

                    } else if (port == 0) {
                        ClientMessage.sendMessage("Port is null, please change this using the @stash command.");
                        return;

                    }
                    //new ClientClient();
                    ClientClient.startClient(ip, port, new DubInfoPacket("DubInfoPacket", chestsToMove, chestsToMoveTo, mc.player.getName()));
                } catch (Exception exception) {
                    ClientMessage.sendMessage("Could not connect to server.");
                    exception.printStackTrace();
                }
            }
        }
    }

    @SubscribeEvent
    public void onClientPacket(StashMovePacketEvent event) {
        if (event.getRecievedPacket() instanceof DubInfoPacket) {
            DubInfoPacket dubInfoPacket = (DubInfoPacket) event.getRecievedPacket();
            ClientMessage.sendMessage("Received " + dubInfoPacket.getName() + " from " + dubInfoPacket.getPlayer());
            chestsToMove = dubInfoPacket.getChestsToMove();
            chestsToMoveTo = dubInfoPacket.getChestsToMoveTo();
        } else if (event.getRecievedPacket() instanceof JoinPacket) {
            JoinPacket joinPacket = (JoinPacket) event.getRecievedPacket();
            ClientMessage.sendMessage("Received " + joinPacket.getName() + " from " + joinPacket.getUsername());
        } else if (event.getRecievedPacket() instanceof EmptyDubPacket) {
            EmptyDubPacket emptyDubPacket = (EmptyDubPacket) event.getRecievedPacket();
            SerializableBlockPos emptyDub = emptyDubPacket.getEmptyDub();
            ClientMessage.sendMessage("Received " + emptyDubPacket.getName() + " from " + emptyDubPacket.getPlayer());
            chestsToMove.remove(emptyDub);
        } else if (event.getRecievedPacket() instanceof LeavePacket) {
            LeavePacket leavePacket = (LeavePacket) event.getRecievedPacket();
            ClientMessage.sendMessage("Received " + leavePacket.getName() + " from " + leavePacket.getUsername());
        } else if (event.getRecievedPacket() instanceof MoverPacket) {
            MoverPacket packet = (MoverPacket) event.getRecievedPacket();
            MoverStage stage = packet.getMoverStage();
            ClientMessage.sendMessage("Received " + packet.getName() + " from " + packet.getPlayer());
            if (mode.getValue().equals(Mode.Loading) && stage.equals(MoverStage.GoingUpTower)) {}

        }
    }

    @SubscribeEvent
    public void onClick(ClickBlockEvent event) {
        if (!unselectingHome && !selectingHome && !selectingMove && !unselectingMove) return;
        if (BlockUtil.getBlock(event.getPos()) instanceof BlockChest) {
            BlockPos doubleChest = BlockUtil.getDoubleChestIfDouble(event.getPos());
            if (selectingHome) {
                chestsToMoveTo.add(new SerializableBlockPos(event.getPos()));
                if (doubleChest != null) {
                    chestsToMoveTo.add(new SerializableBlockPos(doubleChest));
                }
                ClientMessage.sendMessage("Added home chest.");
            } else if (selectingMove) {
                chestsToMove.add(new SerializableBlockPos(event.getPos()));
                if (doubleChest != null) {
                    chestsToMove.add(new SerializableBlockPos(doubleChest));
                }
                ClientMessage.sendMessage("Added move chest.");
            } else if (unselectingHome) {
                for (SerializableBlockPos serializableBlockPos : chestsToMoveTo) {
                    if (serializableBlockPos.getX() == event.getPos().getX() && serializableBlockPos.getY() == event.getPos().getY() && serializableBlockPos.getZ() == event.getPos().getZ()) {
                        chestsToMoveTo.remove(serializableBlockPos);
                        break;
                    }
                    if (doubleChest != null) {
                        if (serializableBlockPos.getX() == doubleChest.getX() && serializableBlockPos.getY() == doubleChest.getY() && serializableBlockPos.getZ() == doubleChest.getZ()) {
                            chestsToMoveTo.remove(serializableBlockPos);
                            break;
                        }
                    }
                }
                ClientMessage.sendMessage("Removed home chest.");
            } else if (unselectingMove) {
               for (SerializableBlockPos serializableBlockPos : chestsToMove) {
                   if (serializableBlockPos.getX() == event.getPos().getX() && serializableBlockPos.getY() == event.getPos().getY() && serializableBlockPos.getZ() == event.getPos().getZ()) {
                       chestsToMove.remove(serializableBlockPos);
                       break;
                   }
                   if (doubleChest != null) {
                       if (serializableBlockPos.getX() == doubleChest.getX() && serializableBlockPos.getY() == doubleChest.getY() && serializableBlockPos.getZ() == doubleChest.getZ()) {
                           chestsToMove.remove(serializableBlockPos);
                           break;
                       }
                   }
               }
                ClientMessage.sendMessage("Removed move chest.");
            }
        } else {
            ClientMessage.sendMessage("Block must be a chest.");
        }
    }

    @Override
    public void onRender3D() {
        if (renderChests.getValue()) {
            for (SerializableBlockPos chest : chestsToMoveTo) {
                RenderUtil.drawBox(RenderUtil.generateBB(chest.getX(), chest.getY(), chest.getZ()), 50/255f, 50/255f, 255/255f, 70/255f);
            }
            for (SerializableBlockPos chest : chestsToMove) {
                RenderUtil.drawBox(RenderUtil.generateBB(chest.getX(), chest.getY(), chest.getZ()), 255/255f, 50/255f, 50/255f, 70/255f);
            }
        }
    }

//    public Mover getMover() { //this is the data that will be sent through packets
//        if (mode.getValue().equals(Mode.Loading)) return null;
//
//        return new Mover(mc.player, invFull(), );
//    }

    public MoverStage getMoverStage() {
        MoverStage stage = null;
        if (isAtStashToMoveTo() && !InventoryUtil.isInHotbar(Items.ENDER_PEARL)) {
            stage = MoverStage.GoingUpTower;
        } else if (isAtStashToMoveTo() && InventoryUtil.isInHotbar(Items.ENDER_PEARL)) {
            stage = MoverStage.ThrowingPearl;
        } else if (pearlIsSet()) {
            stage = MoverStage.Dying;
        } else if (pearlIsSet() && !isAtStashToMoveTo()) {
            stage = MoverStage.GrabbingItems;
        } else if (pearlIsSet() && !isAtStashToMoveTo() && invFull()) {
            stage = MoverStage.ReadyToLoad; //right after you are loaded set the isPearlSet to false
        } else if (isAtStashToMoveTo() && invFull()) {
            stage = MoverStage.StoreItems;
        }
        return stage;
    }

    public boolean isAtStashToMoveTo() { //if player is at the stash that they are moving the items to
        return false; //TODO
    }

    public boolean pearlIsSet() {
        return false; //TODO
    }

    public boolean invFull() {
        return chestsToMove.size() == 0 || PlayerUtil.isInventoryFull();
    }

    public enum MoverStage implements Serializable {
        GoingUpTower, ThrowingPearl, Dying, GrabbingItems, ReadyToLoad, StoreItems
    }

    public enum Mode {
        Moving, Loading
    }
}
