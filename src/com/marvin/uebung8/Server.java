package com.marvin.uebung8;

import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by wwi15sca on 09.03.17.
 */
public class Server {
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
                    DatagramPacket packet = new DatagramPacket(new byte[1024],1024);
                    serversocket.receive(packet);

                    byte[] clientbyte = packet.getData();
                    String clientmsg = new String(clientbyte,0, packet.getLength());

                    if (clientmsg.startsWith("READ"))
                    {
                        int commapos = clientmsg.indexOf(",",5);

                        String filename = clientmsg.substring(5,commapos);

                        String lineno = clientmsg.substring(commapos+1);

                        Path file = Paths.get("Filedump/" + filename + ".txt");
                        if(!Files.exists(file))
                        {
                            String msg = "FAILED no file found";
                            DatagramPacket returnp = new DatagramPacket(msg.getBytes(),msg.length(), packet.getAddress(),packet.getPort());
                            serversocket.send(returnp);
                        }
                        else
                        {
                            List<String> readlines = Files.readAllLines(file);
                            String msg = ("SUCCESS " + readlines.get(Integer.parseInt(lineno)-1));
                            System.out.println(msg);
                            DatagramPacket returnp = new DatagramPacket(msg.getBytes(),msg.length(), packet.getAddress(),packet.getPort());
                            serversocket.send(returnp);
                        }

                    }
                    if (clientmsg.startsWith("WRITE"))
                    {
                        int commapos = clientmsg.indexOf(",",5);
                        int comma2 = clientmsg.indexOf(",", commapos+1);

                        String filename = clientmsg.substring(6,commapos);

                        String lineno = clientmsg.substring(commapos+1,comma2);

                        String data = clientmsg.substring(comma2+1);

                        Path file = Paths.get("Filedump/" + filename + ".txt");
                        if(!Files.exists(file))
                        {
                            String msg = "FAILED no file found";
                            DatagramPacket returnp = new DatagramPacket(msg.getBytes(),msg.length(), packet.getAddress(),packet.getPort());
                            serversocket.send(returnp);
                            continue;
                        }
                        else
                        {
                            List<String> readlines = Files.readAllLines(file);
                            try {
                                readlines.set(Integer.parseInt(lineno)-1,data);
                            }
                            catch (IndexOutOfBoundsException e){
                                String msg = "FAILED line not found";
                                DatagramPacket returnp = new DatagramPacket(msg.getBytes(),msg.length(), packet.getAddress(),packet.getPort());
                                serversocket.send(returnp);
                                continue;
                            }

                            file = Paths.get("Filedump/" + filename + ".txt");
                            Files.write(file,readlines);

                            String msg = "SUCCESS";
                            DatagramPacket returnp = new DatagramPacket(msg.getBytes(),msg.length(), packet.getAddress(),packet.getPort());
                            serversocket.send(returnp);
                            continue;
                        }

                    }

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
