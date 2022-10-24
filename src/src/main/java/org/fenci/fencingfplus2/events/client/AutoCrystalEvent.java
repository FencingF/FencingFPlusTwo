package org.fenci.fencingfplus2.events.client;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;


//AutoCrystalEvent is a class that extends Event and is used to handle the event of the AutoCrystal module.
public class AutoCrystalEvent extends Event {


    private final BlockPos newPos;
    public final BlockPos oldPos;

    public AutoCrystalEvent(BlockPos newPos, BlockPos oldPos) {
        this.newPos = newPos;
        this.oldPos = oldPos;
    }

    public BlockPos getNewPos() {
        return newPos;
    }

    public BlockPos getOldPos() {
        return oldPos;
    }
}
