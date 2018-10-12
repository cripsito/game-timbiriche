package com.t.hope.timbiriche;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MulticastOUT extends Thread{
    final static String INET_ADDR = "228.5.6.7";

    final static int PORT = 4002;
    String texto;

    public MulticastOUT(String text)  {
        texto=text;
    }

    public void run() {
        InetAddress addr = null;
        try {
            addr = InetAddress.getByName(INET_ADDR);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


        // Open a new DatagramSocket, which will be used to send the data.

        try (DatagramSocket serverSocket = new DatagramSocket()) {

            for (int i = 0; i < 100; i++) {

                String msg = texto + i;

                // Create a packet that will contain the data
                // (in the form of bytes) and send it.
                DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(),
                        msg.getBytes().length, addr, PORT);
                serverSocket.send(msgPacket);

                //System.out.println("Server sent packet with msg: " + msg);
                try {
                    Thread.sleep(95);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException ex) {

            ex.printStackTrace();

        }
    }
}