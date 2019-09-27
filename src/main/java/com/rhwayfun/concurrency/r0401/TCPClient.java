package com.rhwayfun.concurrency.r0401;

import java.io.*;
import java.net.Socket;

/**
 * Created by wssjdi@gmail.com
 */
public class TCPClient {
    private static boolean isshutdown = false;

    public static void main(String[] args) {
        try {
            Socket client = new Socket("10.42.0.185", 4444);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            writer.write(1);
            writer.flush();
            String msg = reader.readLine();
            System.out.println("received:" + msg);
            writer.close();
            reader.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
