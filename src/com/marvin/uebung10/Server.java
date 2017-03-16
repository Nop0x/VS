package com.marvin.uebung10;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by wwi15sca on 09.03.17.
 */
public class Server{
    public static final int DEFAULT_PORT = 5999;


    public static void main(String[] args) {
        int port = DEFAULT_PORT;

        File dir = new File("Filedump");
        if(!dir.exists()){
            boolean result = false;

            try{
                dir.mkdir();
                result = true;
            }
            catch(SecurityException se){
                //handle it
            }
            if(result) {
                System.out.println("Filedump created");
            }
        }

        try {
            DatagramSocket serversocket = new DatagramSocket(DEFAULT_PORT);

            while(true)
            {
                try {
                    DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
                    serversocket.receive(packet);

                    byte[] clientbyte = packet.getData();
                    String clientmsg = new String(clientbyte, 0, packet.getLength());
                    if (clientmsg.startsWith("READ")) {
                        ServerRead readerrun = new ServerRead(1, clientmsg, serversocket, packet);
                        Thread readerthread = new Thread(readerrun);
                        readerthread.start();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (SocketException e){
            System.out.println("Socket belegt - fehler");
        }

    }
}
