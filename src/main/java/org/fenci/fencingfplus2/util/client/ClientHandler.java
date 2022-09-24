package org.fenci.fencingfplus2.util.client;

import org.fenci.fencingfplus2.features.module.modules.misc.StashMover;
import org.fenci.fencingfplus2.util.Globals;
import org.fenci.fencingfplus2.util.custompackets.Packet;
import org.fenci.fencingfplus2.util.custompackets.packets.JoinPacket;
import org.fenci.fencingfplus2.util.custompackets.packets.LeavePacket;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private ObjectInputStream bufferedReader;
    private ObjectOutputStream bufferedWriter;
    private String username;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = (new ObjectOutputStream(socket.getOutputStream())); //changed somethings here. go to 14:18 in the video if it doesn't work.
            this.bufferedReader = (new ObjectInputStream(socket.getInputStream()));
            this.username = String.valueOf(bufferedReader.read());
            clientHandlers.add(this);
            broadcastPacket(new JoinPacket("Join packet", username, "has joined!")); //I can maybe get rid of this. I just don't know if its the right packet

        } catch (IOException exception) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        Packet packetFromClient;

        while (socket.isConnected()) {
            try {
                packetFromClient = (Packet) bufferedReader.readObject();
                broadcastPacket(packetFromClient);
            } catch (IOException | ClassNotFoundException exception) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void broadcastPacket(Packet packet) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.username.equals(username)) {
                    clientHandler.bufferedWriter.writeObject(packet);
                    clientHandler.bufferedWriter.write('\n'); //equivalent of newLine
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException exception) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void closeEverything(Socket socket, ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        removeClientHandler();
        try {
            if (socket != null) socket.close();
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastPacket(new LeavePacket("Leave Packet", username, "has disconnected."));
    }
}
