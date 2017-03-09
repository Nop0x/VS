package com.marvin.uebung7;

import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * Created by wwi15sca on 09.03.17.
 */
public class Server {
    public static final int DEFAULT_PORT = 4999;

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
                    System.out.println(clientmsg + " from port " + packet.getPort());

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
