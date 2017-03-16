package com.marvin.uebung10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

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
            DatagramPacket packet;
            DatagramPacket rpacket;

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

                packet = new DatagramPacket(msg.getBytes(),msg.length(),InetAddress.getLocalHost(),5999);
                clientsocket.send(packet);
                rpacket = new DatagramPacket(new byte[1024], 1024);
                clientsocket.receive(rpacket);
                String returnmsg = new String(rpacket.getData(),0,rpacket.getLength());
                System.out.println(returnmsg);

            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
