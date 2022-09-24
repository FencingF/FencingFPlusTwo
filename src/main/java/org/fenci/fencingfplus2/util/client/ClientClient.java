package org.fenci.fencingfplus2.util.client;

import org.fenci.fencingfplus2.util.Globals;
import org.fenci.fencingfplus2.util.custompackets.Packet;

import java.io.*;
import java.net.Socket;

public class ClientClient implements Globals {

    public static void startClient(String ip, int port, Packet message) {
        new Thread(() -> {
            Socket socket = null;
            ObjectInputStream inputStreamReader = null;
            ObjectOutputStream outputStreamWriter = null;

            try {
                socket = new Socket("localhost", 1234);
                outputStreamWriter = new ObjectOutputStream(socket.getOutputStream());
                inputStreamReader = new ObjectInputStream(socket.getInputStream());

//                Scanner scanner = new Scanner(System.in);
//
//                while (true) { //when making this in mc just run it in onplayertick and make it take all these parameters
//                    String message = scanner.nextLine();
//                    bufferedWriter.write(message);
//                    bufferedWriter.newLine();
//                    bufferedWriter.flush();
//
//                    System.out.println("Server Says: " + bufferedReader.readLine() ); //when doing this just interpret the enum instead of printing it.
//
//                    if (message.equalsIgnoreCase("close")) break;
//
//
//                }
                sendPacketToServer(message, outputStreamWriter);
                Packet packet = (Packet) inputStreamReader.readObject();
                ClientMessage.sendMessage("Server Says: " + inputStreamReader.readObject());


            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (socket != null) socket.close();

                    if (inputStreamReader != null) inputStreamReader.close();

                    if (outputStreamWriter != null) outputStreamWriter.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void sendPacketToServer(Packet packet, ObjectOutputStream outputStream) {
        try {
            outputStream.writeObject(packet);
            ClientMessage.sendMessage("Sent " + packet.getName() + " to server");
            outputStream.write('\n');
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
