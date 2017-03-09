package com.marvin.uebung7;

import java.net.*;
import java.io.*;
/**
 * Created by wwi15sca on 09.03.17.
 */
public class Broadcastserver {
    public static final int DEFAULT_PORT = 4998;

    public static void main(String[] args) {
        int port = DEFAULT_PORT;

        try {
            DatagramSocket serversocket = new DatagramSocket(DEFAULT_PORT);

            while(true)
            {
                try {
                    DatagramPacket packet = new DatagramPacket(new byte[1024],1024);
                    serversocket.receive(packet);

                    byte[] clientbyte = packet.getData();
                    String clientmsg = new String(clientbyte,0, packet.getLength());
                    System.out.println(clientmsg + " BROADCAST from port " + packet.getPort() + " from ip " + packet.getAddress().getHostName());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (SocketException e){
            System.out.println("Socket belegt - fehler");
        }

    }
}
