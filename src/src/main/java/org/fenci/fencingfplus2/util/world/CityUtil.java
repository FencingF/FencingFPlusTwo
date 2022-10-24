package org.fenci.fencingfplus2.util.world;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.fenci.fencingfplus2.features.module.modules.combat.AutoCity;
import org.fenci.fencingfplus2.features.module.modules.combat.Surround;
import org.fenci.fencingfplus2.util.Globals;
import org.fenci.fencingfplus2.util.player.PlayerUtil;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CityUtil implements Globals {

    public static BlockPos cityBlockToMine() {
        if (mc.world.playerEntities.isEmpty())
            return null;

        BlockPos closestBlock = null;

        for (final BlockPos pos : getCityPositions()) {

            if (closestBlock != null)
                if (mc.player.getDistance(pos.getX(), pos.getY(), pos.getZ()) > mc.player.getDistance(closestBlock.getX(), closestBlock.getY(), closestBlock.getZ()))
                    continue;

            closestBlock = pos;
        }

        return closestBlock;
    }

    public static Set<BlockPos> getCityPositions() {

        Set<BlockPos> feetPositions = new HashSet<>();

        if (PlayerUtil.findClosestTarget() != null && mc.player.getDistance(Objects.requireNonNull(PlayerUtil.findClosestTarget())) <= AutoCity.range.getValue()) {
            BlockPos pos = PlayerUtil.getPlayerPos(Objects.requireNonNull(PlayerUtil.findClosestTarget()));

            for (EnumFacing facing : EnumFacing.values()) {
                if (facing.equals(EnumFacing.UP) || facing.equals(EnumFacing.DOWN)) continue;
                BlockPos neighbor = pos.offset(facing);

                if (BlockUtil.canPlaceBlock(neighbor) && !BlockUtil.getBlock(neighbor).equals(Blocks.BEDROCK) && mc.player.getDistance(neighbor.getX(), neighbor.getY(), neighbor.getZ()) <= AutoCity.range.getValue() && !BlockUtil.getBlock(neighbor).equals(Blocks.AIR)) {
                    feetPositions.add(neighbor);
                }
            }
        }

        return feetPositions;
    }

    public static Set<BlockPos> getSurroundPositions() {

        Set<BlockPos> feetPositions = new HashSet<>();

        BlockPos pos = PlayerUtil.getPlayerPos(mc.player);

        if (!HoleUtil.isInHole(mc.player)) {
            for (EnumFacing facing : EnumFacing.values()) {
                if (facing.equals(EnumFacing.UP) || facing.equals(EnumFacing.DOWN)) continue;
                BlockPos neighbor = pos.offset(facing);
                if (BlockUtil.canPlaceBlock(neighbor)) {
                    feetPositions.add(neighbor);
                } else if (Surround.supportingBlocks.getValue() && !BlockUtil.canPlaceBlock(neighbor)) {
                    for (EnumFacing facing1 : EnumFacing.values()) {
                        if (facing1.equals(EnumFacing.UP)) continue;
                        BlockPos extention = neighbor.offset(facing1);
                        if (BlockUtil.canPlaceBlock(extention)) feetPositions.add(extention);
                    }
                }
            }
        }
        return feetPositions;
    }

    public static Set<BlockPos> getAllCityPositions() {

        Set<BlockPos> feetPositions = new HashSet<>();

        for (EntityPlayer player : mc.world.playerEntities) {

            BlockPos pos = PlayerUtil.getPlayerPos(player);

            for (EnumFacing facing : EnumFacing.values()) {
                if (facing.equals(EnumFacing.UP) || facing.equals(EnumFacing.DOWN)) continue;
                BlockPos neighbor = pos.offset(facing);

                if (BlockUtil.canPlaceBlock(neighbor) && !BlockUtil.getBlock(neighbor).equals(Blocks.BEDROCK) && mc.player.getDistance(neighbor.getX(), neighbor.getY(), neighbor.getZ()) <= AutoCity.range.getValue() && !BlockUtil.getBlock(neighbor).equals(Blocks.AIR)) {
                    feetPositions.add(neighbor);
                }
            }

        }

        return feetPositions;
    }
}
