package org.fenci.fencingfplus2.util.tracking;

import net.minecraft.util.math.BlockPos;

// Requests sent from a Tracker instance to a TrackerManager instance.
public class TrackerRequest {
    private final long initial_time;
    private BlockPos position;
    private int result;

    public TrackerRequest(BlockPos position, int result) {
        this.position = position;
        this.result = result;
        this.initial_time = System.currentTimeMillis();
    }

    public BlockPos getPosition() {
        return this.position;
    }

    public void setPosition(BlockPos new_position) {
        this.position = new_position;
    }

    public int getResult() {
        return this.result;
    }

    public void setResult(int new_result) {
        this.result = new_result;
    }

    // If timeout_threshold milliseconds have passed, result is -1.
    public boolean timeout(long timeout_threshold) {
        long current_time = System.currentTimeMillis();
        if (current_time - initial_time > timeout_threshold) {
            this.result = -1;
            return true;
        }
        return false;
    }
}
