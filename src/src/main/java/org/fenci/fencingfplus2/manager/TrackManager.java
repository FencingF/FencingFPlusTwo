package org.fenci.fencingfplus2.manager;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class TrackManager {
    public List<BlockPos> checkPos = new ArrayList<>();
    public List<BlockPos> startScanPos = new ArrayList<>();

    public void addCheckPos(BlockPos pos) {
        if (!checkPos.isEmpty()) checkPos.clear();
        checkPos.add(pos);
    }

    public void addScanPos(BlockPos pos) {
        if (!startScanPos.isEmpty()) startScanPos.clear();
        startScanPos.add(pos);
    }

    public List<BlockPos> getCheckPos() {
        return checkPos;
    }

    public List<BlockPos> getStartScanPos() {
        return startScanPos;
    }
}
