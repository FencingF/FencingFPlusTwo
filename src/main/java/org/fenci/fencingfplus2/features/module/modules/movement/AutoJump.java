package org.fenci.fencingfplus2.features.module.modules.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.BlockContainer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.network.PacketEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.ClientMessage;

public class AutoJump extends Module { //i temporarily changed the setting names so its not chinese. in relase we can rename them

    public static final Setting<Mode> mode = new Setting<>("Mode", Mode.BruteForce);
    public static final Setting<Boolean> loadChunks = new Setting<>("LoadChunks", true); //VeryHigh
    public static final Setting<Boolean> fast = new Setting<>("Fast", true);
    public static final Setting<Boolean> notTooHigh = new Setting<>("RemovePPacket", false); //NotTooHigh
    public static final Setting<Boolean> tileEntitiesOnly = new Setting<>("TileEntites", true);
    public static final Setting<Boolean> entityTracker = new Setting<>("EntityTracker", false);
    public static final Setting<Boolean> disableReset = new Setting<>("DisableReset", true);
    public static final Setting<Integer> minY = new Setting<>("MinY", 64, 0, 256);
    public static final Setting<Integer> maxY = new Setting<>("MaxY", 64, 0, 256);
    public static final Setting<Integer> intervals = new Setting<>("Intervals", 5, 0, 50);
    // static final Setting<Integer> yIntervals = new Setting<>("YInvervals", 0, 0, 50);
    public static final Setting<Boolean> optimize = new Setting<>("Optimize", true);
    public static final Setting<Boolean> publicChat = new Setting<>("PublicChat", false);
    public static final Setting<Search> searchMode = new Setting<>("ScanMode", Search.Scan); //JumpMode
    public static AutoJump INSTANCE;
    BlockPos clickedBlock;
    long spiralX;
    int yValue;
    long spiralZ;
    long steps;
    int ystage;

    public AutoJump() {
        super("AutoJump", "Auto jumps", Category.Movement);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        if (searchMode.getValue().equals(Search.Scan) && getFencing().trackerManager.getStartScanPos().isEmpty()) {
            ClientMessage.sendModuleMessage("AutoJump", "Could not find starting position please use the command @scan to do this.");
            this.toggle(true);
            return;
        }
        if (searchMode.getValue().equals(Search.Check) && getFencing().trackerManager.getCheckPos().isEmpty()) {
            ClientMessage.sendModuleMessage("AutoJump", "Could not find check position please use the command @scan to do this.");
            this.toggle(true);
            return;
        }
        if (searchMode.getValue().equals(Search.Check)) {
            spiralX = getFencing().trackerManager.checkPos.get(0).getX();
            yValue = getFencing().trackerManager.checkPos.get(0).getY();
            spiralZ = getFencing().trackerManager.checkPos.get(0).getZ();
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(new BlockPos(spiralX, yValue, spiralZ), EnumFacing.UP, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
        } else {
            spiralX = getFencing().trackerManager.startScanPos.get(0).getX();
            spiralZ = getFencing().trackerManager.startScanPos.get(0).getZ();
        }
        ystage = minY.getValue();
        steps = intervals.getValue();
    }

    @Override
    public void onUpdate() {
        if (mode.getValue().equals(Mode.BruteForce)) {
            if (searchMode.getValue().equals(Search.Scan)) {
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(getClickPos(), EnumFacing.UP, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            }
        } else {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, getClickPos(), EnumFacing.UP));
        }
//        mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(getClickPos().getX(), getClickPos().getY(), getClickPos().getZ(), 90, 90, onGround.getValue()));
//        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer && notTooHigh.getValue() && fullNullCheck()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketBlockChange && fullNullCheck()) {
            SPacketBlockChange packetIn = event.getPacket();
            if (fast.getValue() && mc.world.isBlockLoaded(packetIn.getBlockPosition(), false)) return;
            ChunkPos chunkPos = new ChunkPos(packetIn.getBlockPosition());
            Chunk chunk = new Chunk(mc.world, chunkPos.getXStart(), chunkPos.getZEnd());
            if (mc.player.getDistanceSq(packetIn.getBlockPosition()) > 72) {
                if (loadChunks.getValue()) {
                    mc.world.doPreChunk(chunkPos.x, chunkPos.z, true);
                    mc.world.markBlockRangeForRenderUpdate(chunkPos.x << 4, 0, chunkPos.z << 4, (chunkPos.x << 4) + 15, 256, (chunkPos.z << 4) + 15);
                }
            }
            if (!loadChunks.getValue() && mc.player.getDistanceSq(packetIn.getBlockPosition()) > 500 * 500) {
                event.setCanceled(true);
            }
            if (!entityTracker.getValue()) {
                if (packetIn.getBlockState().getBlock() instanceof BlockContainer && tileEntitiesOnly.getValue()) {
                    ClientMessage.sendModuleMessage("AutoJump", ChatFormatting.YELLOW + "" + ChatFormatting.BOLD + "" + packetIn.getBlockPosition() + " at " + packetIn.getBlockState().getBlock().getLocalizedName() + " " + chunk.hasEntities);
                    if (publicChat.getValue() && this.isOn()) {
                        mc.player.connection.sendPacket(new CPacketChatMessage(packetIn.getBlockPosition() + " at " + packetIn.getBlockState().getBlock().getLocalizedName()));
                    }
                }
                if (!tileEntitiesOnly.getValue()) {
                    ClientMessage.sendModuleMessage("AutoJump", ChatFormatting.YELLOW + "" + ChatFormatting.BOLD + "" + packetIn.getBlockPosition() + " at " + packetIn.getBlockState().getBlock().getLocalizedName() + " " + chunk.hasEntities);
                    if (publicChat.getValue() && this.isOn()) {
                        mc.player.connection.sendPacket(new CPacketChatMessage(packetIn.getBlockPosition() + " at " + packetIn.getBlockState().getBlock().getLocalizedName()));
                    }
                }

                if (optimize.getValue()) {
                    event.setCanceled(true);
                }

                if (searchMode.getValue().equals(Search.Check) && packetIn.getBlockPosition().equals(new BlockPos(spiralX, yValue, spiralZ))) {
                    this.toggle(true);
                }
            }

        }
    }

    @Override
    public void onDisable() {
        if (disableReset.getValue()) {
            getFencing().trackerManager.startScanPos.clear();
            getFencing().trackerManager.checkPos.clear();
        }
        ystage = minY.getValue();
        steps = intervals.getValue();
    }

    public BlockPos getClickPos() {
        if (ystage == maxY.getValue()) {
            if ((Math.abs(spiralX) <= Math.abs(spiralZ) && (spiralX != spiralZ || spiralX >= 0))) {
                spiralX += ((spiralZ >= 0) ? steps : -steps);
            } else {
                spiralZ += ((spiralX >= 0) ? -steps : steps);
            }
            ystage = minY.getValue();
        } else {
            ystage++;
        }

        clickedBlock = new BlockPos(spiralX, ystage, spiralZ);

        return clickedBlock;
    }

    public enum Mode {
        BruteForce, Old
    }

    public enum Search {
        Scan, Check
    }
}