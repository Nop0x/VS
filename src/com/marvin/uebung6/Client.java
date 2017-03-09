package com.marvin.uebung6;

import java.net.*;
import java.io.*;
/**
 * Created by wwi15sca on 02.03.17.
 */
public class Client
{
    public static final int serverPort  = 8888;

    public static void main(String[] args){

        String hostname = "localhost";

        if ( args.length > 0)
        {
            hostname = args[0];
        }

        PrintWriter networkOut = null;
        BufferedReader networkIn = null;

        Socket s = null;

        try{
            s = new Socket(hostname,serverPort);
            networkIn = new BufferedReader(
                    new InputStreamReader(s.getInputStream())
            );
            BufferedReader userIn = new BufferedReader(
                    new InputStreamReader(System.in)
            );
            networkOut = new PrintWriter(s.getOutputStream());

            System.out.println("Connected to server");

            while(true){
                String line = userIn.readLine();
                if(line.length() < 1 || line.length() > 1019)
                    System.out.println("Nachricht muss min. 1 oder max 1019 Zeichen haben.");
                if(line.equals(".")) break;
                networkOut.println(line);
                networkOut.flush();
                System.out.println(networkIn.readLine());
            }
        }
        catch (IOException e)
        {
            System.err.println(e);
        }
        finally {
            try {
                if(networkIn != null){
                    networkIn.close();
                }
                if (networkOut != null){
                    networkOut.close();
                }
                if(s != null){
                    s.close();
                }
            }
            catch (IOException e)
            {

            }
        }
    }
}
