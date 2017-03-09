package com.marvin.uebung6;

import com.sun.tools.javac.util.Convert;

import java.net.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
/**
 * Created by wwi15sca on 02.03.17.
 */
public class Server {
    public final static int DEFAULT_PORT = 8888;

    public static void main(String[] args) {
        int port = DEFAULT_PORT;

        PrintWriter out = null;

        File dir = new File("Messages");
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
                System.out.println("DIR created");
            }
        }
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
                if (port < 0 || port >= 65536) {
                    System.out.println("Port between 0 and 65536");
                    return;
                }
            } catch (NumberFormatException nex) {
            }
        }
        try {
            ServerSocket server = new ServerSocket(port);
            Socket connection = null;
            BufferedReader networkIn = null;

            while (true) {
                try {
                    connection = server.accept();
                    out = new PrintWriter(connection.getOutputStream());
                    networkIn = new BufferedReader(
                            new InputStreamReader(connection.getInputStream())
                    );
                    while(true)
                    {
                        String line = networkIn.readLine();
                        if (line.startsWith("SAVE")) {
                            List<String> lines = Arrays.asList(line.substring(5));

                            Random rand = new Random();
                            String key = Integer.toString(rand.nextInt(10000));

                            Path file = Paths.get("Messages/" + key + ".txt");
                            Files.write(file,lines);
                            out.println("KEY: " + key);
                            out.flush();
                        } else if (line.startsWith("GET")) {

                            Path file = Paths.get("Messages/" + line.substring(4) + ".txt");
                            if(!Files.exists(file))
                                out.println("FAILED");
                            else{
                                List<String> readlines = Files.readAllLines(file);
                                out.println("OK " + readlines.get(0));
                            }
                            out.flush();
                        }
                        if(line.equals(".")) break;
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        if (out != null) out.close();
                        if (connection != null) connection.close();
                    } catch (IOException exe) {
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}

