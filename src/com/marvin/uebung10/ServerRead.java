package com.marvin.uebung10;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by wwi15sca on 16.03.17.
 */
public class ServerRead implements Runnable{

    int id;
    String clientmsg;
    DatagramSocket serversocket;
    DatagramPacket packet;

    public ServerRead (int id, String clientmsg, DatagramSocket serversocket, DatagramPacket packet) {
        this.id = id;
        this.clientmsg = clientmsg;
        this.serversocket = serversocket;
        this.packet = packet;
    }

    @Override
    public void run()
    {
        try{
            int commapos = clientmsg.indexOf(",", 5);

            String filename = clientmsg.substring(5, commapos);

            String lineno = clientmsg.substring(commapos + 1);

            Path file = Paths.get("Filedump/" + filename + ".txt");
            if (!Files.exists(file)) {
                String msg = "FAILED no file found";
                DatagramPacket returnp = new DatagramPacket(msg.getBytes(), msg.length(), packet.getAddress(), packet.getPort());
                serversocket.send(returnp);
            } else {
                List<String> readlines = Files.readAllLines(file);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String msg = ("SUCCESS " + readlines.get(Integer.parseInt(lineno) - 1));
                System.out.println(msg);
                DatagramPacket returnp = new DatagramPacket(msg.getBytes(), msg.getBytes().length, packet.getAddress(), packet.getPort());
                serversocket.send(returnp);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
