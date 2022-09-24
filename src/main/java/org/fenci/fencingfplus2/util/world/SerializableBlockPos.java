package org.fenci.fencingfplus2.util.world;

import jdk.nashorn.internal.ir.annotations.Immutable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mutable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public final class SerializableBlockPos implements Serializable {
    private final int x;
    private final int y;
    private final int z;

    public SerializableBlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public SerializableBlockPos(BlockPos pos) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public BlockPos toBlockPos() {
        return new BlockPos(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SerializableBlockPos that = (SerializableBlockPos) o;
        return x == that.x &&
                y == that.y &&
                z == that.z;
    }

    public static boolean isBlockPosInList(ArrayList<SerializableBlockPos> list, SerializableBlockPos pos) {
        for (SerializableBlockPos serializableBlockPos : list) {
            if (serializableBlockPos.getX() == pos.getX() && serializableBlockPos.getY() == pos.getY() && serializableBlockPos.getZ() == pos.getZ()) {
                return true;
            }
        }
        return false;
    }
}
