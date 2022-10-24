package org.fenci.fencingfplus2.util.client;

import net.minecraftforge.common.MinecraftForge;
import org.fenci.fencingfplus2.events.network.StashMovePacketEvent;
import org.fenci.fencingfplus2.util.custompackets.Packet;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientServer {

//    public ClientServer(final int port, Packet packet) { //have do to this on a seperate thread
//        new Thread(() -> {
//            try {
//                ServerSocket serverSocket = new ServerSocket(port);
//                ClientMessage.sendMessage("Server is up, on port: " + port);
//                Socket socket = serverSocket.accept(); //keeps running until the reception of a connection
//                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
//                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
//
//                Packet received = (Packet) inputStream.readObject();
//
//                MinecraftForge.EVENT_BUS.post(new StashMovePacketEvent(received));
//                ClientMessage.sendMessage(received.getName());
//
//                if (packet != null)  outputStream.writeObject(packet);
//
//                serverSocket.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }

    //Attempt 2 was below but it didn't work

//    public static ClientServer INSTANCE;
//
//    private ServerSocket serverSocket;
//
//    public ClientServer(ServerSocket serverSocket) {
//        this.serverSocket = serverSocket;
//        INSTANCE = this;
//    }
//
//    public void runServer() {
//        try {
//            while (!serverSocket.isClosed()) {
//                Socket socket = serverSocket.accept();
//                ClientMessage.sendMessage("A new client has connected!");
//                ClientHandler clientHandler = new ClientHandler(socket);
//                Thread thread = new Thread(clientHandler);
//                thread.start();
//            }
//
//        } catch (IOException exception) {
//            closeServerSocket();
//        }
//    }
//
//    public void closeServerSocket() {
//        try {
//            if (serverSocket != null) {
//                serverSocket.close();
//            }
//        } catch (IOException exception) {
//            exception.printStackTrace();
//        }
//    }
//
//    public void startServer(int port) throws IOException {
//        ServerSocket socket = new ServerSocket(port);
//        ClientServer server = new ClientServer(socket);
//        server.runServer();
//    }

    //attempt 3

    public static void startServer(int port) {
        new Thread(() -> {
            try {
                Socket socket = null;
                ObjectInputStream inputStreamReader = null;
                ObjectOutputStream outputStreamWriter = null;
//                BufferedReader bufferedReader = null;
//                BufferedWriter bufferedWriter = null;
                ServerSocket serverSocket = null;

                serverSocket = new ServerSocket(1234);

                //while(true) {
                    try {

                        socket = serverSocket.accept();

                        outputStreamWriter = new ObjectOutputStream(socket.getOutputStream());
                        inputStreamReader = new ObjectInputStream(socket.getInputStream());

//                        while(!serverSocket.isClosed()) {
//                            Packet packet = (Packet) inputStreamReader.readObject();
//                            ClientMessage.sendMessage("Client Says: " + packet.getName());
//                            MinecraftForge.EVENT_BUS.post(new StashMovePacketEvent(packet));
//                            outputStreamWriter.writeObject(packet);
//                            outputStreamWriter.write('\n');
//                            outputStreamWriter.flush();
//                        }

                        while (!serverSocket.isClosed()) {
                            Packet msgFromClient = (Packet) inputStreamReader.readObject();
                            ClientMessage.sendMessage("Client Says: " + msgFromClient.getName());
                            MinecraftForge.EVENT_BUS.post(new StashMovePacketEvent(msgFromClient));
                            outputStreamWriter.write('\n');
                            outputStreamWriter.flush();
                        }

                        socket.close();
                        inputStreamReader.close();
                        outputStreamWriter.close();

                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                //}
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }).start();
    }
}
