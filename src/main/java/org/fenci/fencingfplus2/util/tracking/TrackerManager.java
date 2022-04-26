package org.fenci.fencingfplus2.util.tracking;

import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.network.PacketEvent;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.util.client.ClientMessage;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class TrackerManager extends Module {
    public TrackerManager() {
        super("AutoJumpTrack", "Tracks your movements", Category.Movement);
        this.player_trackers = new ArrayList <>();
        this.outgoing_packets = new ArrayDeque<>();
        this.incoming_packets = new ArrayList<>();
    }

    public ArrayList<Tracker> player_trackers;
    // Lists of incoming and outgoing packets sent by Trackers
    private final ArrayDeque<TrackerRequest> outgoing_packets;
    private final ArrayList<TrackerRequest> incoming_packets;

    private final int USER_PACKETS = 2;

    // Initialize and add a new Tracker
    public void addNewTracker(int X, int Z) {
        String new_name = Integer.toString(player_trackers.size() + 1);
        player_trackers.add(new Tracker(this, new_name, X, Z));

//            System.out.println("Tracker " + new_name + " added.");
    }

    // Simply loads the parameter TrackerRequest onto the outbound queue.
    public void addOutgoingPacket(TrackerRequest request) {
        outgoing_packets.addFirst(request);
    }

    @Override
    public void onEnable() {
        this.addNewTracker(300, 0);
    }

    @Override
    public void onDisable() {
        player_trackers.clear();
        outgoing_packets.clear();
        incoming_packets.clear();
    }

    public void onUpdate() {
        // Invoke Tracker instances.

//        System.out.println("There are " + Integer.toString(player_trackers.size()) + " active Trackers.");

        // Uses an arraylist to remember what to remove. remove() is async.
        ArrayList<Tracker> trackers_to_remove = new ArrayList <>();
        for (Tracker follower : player_trackers){
            // Only does anything if all requests were computed on.
            follower.update();
            // Only does anything if all requests got a response.
            follower.averagePositionAndUpdateSpeed();

//                System.out.println("outgoing_packets.size() = " + Integer.toString(outgoing_packets.size()));

//            ATTENTION! IF THE TRACKER IS LOST, SPAWN A SPIRAL IN ITS POSITION HERE!
//            if (follower.isLost()) {
//                BlockPos lost_pos = new BlockPos(follower.getX(), 0, follower.getZ());
//
//                trackers_to_remove.add(follower);
//            }
        }
        for (Tracker follower : trackers_to_remove)
            player_trackers.remove(follower);

        // HAND PACKETS THEMSELVES BELOW. TRACKERS HANDLED ABOVE.

        // We can send (10 - packets_in_use) packets per tick.
        int packets_available = 10 - USER_PACKETS;
        for (int i=0; i < packets_available; i++){
            // No requests, do nothing.
            if (outgoing_packets.isEmpty())
                break;
            /* For every request which can be sent during this tick, send a CPacketPlayerDigging packet and
             remove that request from the outgoing queue. */
            TrackerRequest request = outgoing_packets.element();
            BlockPos target = request.getPosition();
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, target, EnumFacing.UP));
            incoming_packets.add(request);
            outgoing_packets.removeFirst();
        }

        if (!incoming_packets.isEmpty())
            System.out.println(incoming_packets.size() + " packets pending response.");

        /* If 1 second has passed, mark the request as unloaded and remove it from the list.
           The purpose of the second arraylist is to remove packets, due to remove() being async */
        ArrayList<TrackerRequest> packets_to_remove = new ArrayList <>();
        for (TrackerRequest request : incoming_packets) {
            // If a chunk got no response for 1000 milliseconds,
            boolean chunk_unloaded = request.timeout(1000);
            if (chunk_unloaded) {
                packets_to_remove.add(request);

                System.out.println("CHUNK_UNLOADED!");
            }
        }
        // Remove all timed out packets from the incoming buffer.
        for (TrackerRequest request : packets_to_remove)
            incoming_packets.remove(request);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event) {
        // Rely on the SPacketBlockChange packet returned in response to a digging packet.

        // This is not receiving packets!
        //System.out.println("PACKET received.");

        if (event.getPacket() instanceof SPacketBlockChange) {
            SPacketBlockChange packetIn = event.getPacket();

            System.out.println("SPacketBlockChange received.");

            // Store the coordinates of the position returned by the event.
            BlockPos received_position = new BlockPos(packetIn.getBlockPosition());
            int REC_X = received_position.getX();
//            int REC_Y = received_position.getY();
            int REC_Z = received_position.getZ();

            // For every request, if that request matches this event,
            // set its result to loaded and remove it from the list.
            ArrayList<TrackerRequest> packets_to_remove = new ArrayList <>();
            for (TrackerRequest request : incoming_packets) {
                int REQ_X = request.getPosition().getX();
                int REQ_Z = request.getPosition().getZ();

                //Command.sendClientSideMessage("Request: (" + Integer.toString(REQ_X) + ", " + Integer.toString(REQ_Z) + ")");
                ClientMessage.sendModuleMessage("JumpTracker", "PlayerJumpExfoliationLevel: (" + REC_X + ", " + REC_Z + ")");

                if (REC_X == REQ_X && REC_Z == REQ_Z) {

                    System.out.println("CHUNK_LOADED!");

                    request.setResult(1);
                    packets_to_remove.add(request);
                }
            }
            for (TrackerRequest request : packets_to_remove)
                incoming_packets.remove(request);
        }
    }

}
