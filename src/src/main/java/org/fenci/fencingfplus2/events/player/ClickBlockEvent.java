package org.fenci.fencingfplus2.events.player;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;


// ClickBlockEvent is a class that extends Event and is used to help with some modules.
public class ClickBlockEvent extends Event {
    BlockPos pos;
    EnumFacing facing;

    public ClickBlockEvent(BlockPos pos, EnumFacing facing) {
        this.pos = pos;
        this.facing = facing;
    }

    public BlockPos getPos() {
        return pos;
    }

    public EnumFacing getFacing() {
        return facing;
    }
}
