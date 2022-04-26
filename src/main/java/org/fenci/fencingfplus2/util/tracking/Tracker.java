package org.fenci.fencingfplus2.util.tracking;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class Tracker {
    private static final double speed_threshold = 40.0;

    private final TrackerManager manager_pointer;

    // Holds all sent TrackerRequest objects, for use in determining when to compute.
    private final ArrayList<TrackerRequest> sent_requests;

    private final String name;
    private BlockPos PREV_POS;
    private BlockPos CUR_POS;

    // Velocity requires a direction
    private double speed = 0.0;
//    private double angle = 0.0;

    // Info pertaining to users being tracked right now.
    private long frequency_millis = 0;
    private long previous_millis = 0;
    private boolean is_lost = false;

    public String getName() { return this.name; }
    public int getX() { return this.CUR_POS.getX(); }
    public int getZ() { return this.CUR_POS.getZ(); }
    public double getSpeed() { return this.speed; }
//    public double getAngle() { return this.angle; }

    // Returns true if the last call to averagePositionAndUpdateSpeed() set the lost flag.
    public boolean isLost() { return this.is_lost; }

    // Tell caller whether or not all requests have been received.
    public boolean receivedAllRequests() {
        // Every positional update must have been changed in value.
        for (TrackerRequest request : sent_requests) {
            if (request.getResult() == 0)
                return false;
        }
        return true;
    }

    // Average out the position of the four requests, then update position and speed.
    public void averagePositionAndUpdateSpeed() {

//        System.out.println("Calling averagePositionAndUpdateSpeed()");

        // Check if all packets were actually received.
        if (!this.receivedAllRequests())
            return;
        //Store the current position.
        int x_val = this.getX();
        int z_val = this.getZ();
        // For every request, average coordinate values out with the current position.
        // Assume the Tracker is lost until proven otherwise.
        this.is_lost = true;
        for (TrackerRequest req : sent_requests){
            if (req.getResult() == 1){
                x_val = (int) ( (x_val + req.getPosition().getX()) / 2.0 );
                z_val = (int) ( (z_val + req.getPosition().getZ()) / 2.0 );
                this.is_lost = false;
            }
        }

        CUR_POS = new BlockPos(x_val, 0, z_val);
        // Measure the user's speed after their position has been updated.
        double change_x = CUR_POS.getX() - PREV_POS.getX();
        double change_z = CUR_POS.getZ() - PREV_POS.getZ();
        speed = Math.sqrt( (change_x * change_x) + (change_z * change_z) ) / (frequency_millis / 1000.0);
        // angle =
        // Adjust the Tracker's position.
        PREV_POS = new BlockPos(CUR_POS.getX(), 0, CUR_POS.getZ());

        // VERY IMPORTANT. Clear all requests off the list so that we know we can send more.
        this.sent_requests.clear();

//        System.out.println("Call to averagePositionAndUpdateSpeed() completed.");

    }

    /* FOLLOW TRACKER
     * Note: we could generalize this, giving Tracker's custom functionality (no need for a bunch of classes
     * doing the exact same thing...
     */
    public void update() {
        // Only execute logic every frequency_millis milliseconds.
        long current_millis = System.currentTimeMillis();
        long millis_elapsed = current_millis - previous_millis;
        if (millis_elapsed < frequency_millis)
            return;
        // Update the time value for future checks.
        previous_millis = current_millis;

//        System.out.println("Calling update() on Tracker " + this.name + " !");

        // If the user is moving quickly, update their position more often.
        if (speed > speed_threshold)
            frequency_millis = 2500;
        else
            frequency_millis = 5000;
        // Make sure there are no pending requests.
        if (sent_requests.size() > 0)
            return;

        // Send 4 requests to the TrackerManager, it handles responses.
        int x_val = CUR_POS.getX();
        int z_val = CUR_POS.getZ();
        sent_requests.add(new TrackerRequest(new BlockPos(x_val - 144, 0, z_val - 144),0));
        sent_requests.add(new TrackerRequest(new BlockPos(x_val + 144, 0, z_val + 144),0));
        sent_requests.add(new TrackerRequest(new BlockPos(x_val + 144, 0, z_val - 144),0));
        sent_requests.add(new TrackerRequest(new BlockPos(x_val - 144, 0, z_val + 144),0));
        for (TrackerRequest req : sent_requests)
            manager_pointer.addOutgoingPacket(req);

//        System.out.println("Call to update() succeeded.");
    }

    public Tracker(TrackerManager pointer, String new_name, int X, int Z) {
        this.manager_pointer = pointer;
        this.name = new_name;
        this.sent_requests = new ArrayList <>(4);
        this.PREV_POS = new BlockPos(X, 0, Z);
        this.CUR_POS = new BlockPos(X, 0, Z);

//        System.out.println("Tracker " + new_name + " instantiated.");
    }
}