package com.marvin.uebung7;
import java.net.*;
import java.io.*;
/**
 * Created by wwi15sca on 09.03.17.
 */
public class Client {
    public static final int DEFAULT_PORT = 5000;

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        PrintWriter out = null;

        PrintWriter networkOut = null;
        BufferedReader networkIn = null;

        try {
            DatagramSocket clientsocket = new DatagramSocket();
            DatagramPacket packet = null;
            DatagramPacket broadcastpacket = null;

            BufferedReader userIn = new BufferedReader(
                    new InputStreamReader(System.in)
            );
            while (true) {
                String msg = userIn.readLine();

                if (msg.getBytes().length > 1024)
                {
                    System.out.println("Message zu lang bzw größer als buffer! Dropping message..");
                    continue;
                }

                packet = new DatagramPacket(msg.getBytes(),msg.length(),InetAddress.getLocalHost(),4999);
                broadcastpacket = new DatagramPacket(msg.getBytes(),msg.length(),InetAddress.getByName("255.255.255.255"),4998);
                clientsocket.send(packet);
                clientsocket.send(broadcastpacket);

            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
