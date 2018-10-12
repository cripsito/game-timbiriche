package com.t.hope.timbiriche;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import org.apache.http.conn.util.InetAddressUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Hope on 16/05/2015.
 */
public class MulticastIN extends Thread {
    final static String INET_ADDR = "228.5.6.7";
    final static int PORT = 4002;
    public boolean seguir=true;
    public String direccionRecibida;
    public String direccionlocal;
    boolean recibiendo=false;
    private Handler mHandler = new Handler();
    int nuevopueto=0;

    MulticastIN(){
        direccionlocal=getIPAddress(true);
        direccionRecibida=getIPAddress(true);
    }
    public void detener(){
        seguir=false;
    }

    public String DireccionRecv(){
        return direccionRecibida;
    }
    public String DireccionLocal(){
        return direccionlocal;
    }

    @Override
    public void run() {


        // Get the address that we are going to connect to.
        InetAddress address = null;
        try {
            address = InetAddress.getByName(INET_ADDR);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        // Create a buffer of bytes, which will be used to store
        // the incoming bytes containing the information from the server.
        // Since the message is small here, 256 bytes should be enough.
        byte[] buf = new byte[1024];

        // Create a new Multicast socket (that will allow other sockets/programs
        // to join it as well.
        try (MulticastSocket clientSocket = new MulticastSocket(PORT)){
            //Joint the Multicast group.
            clientSocket.joinGroup(address);
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    if (seguir) {
                        seguir=false;
                    }
                }
            }, 10000);
            while (seguir) {
                // Receive the information and print it.
                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                clientSocket.receive(msgPacket);
                String aux2;
                aux2=msgPacket.getAddress().toString().substring(1);
                String msg = new String(buf, 0, buf.length);
                //System.out.println("Socket 1 received msg: " + msg);

                if(!aux2.equalsIgnoreCase(direccionlocal)){
                    direccionRecibida=aux2;
                    Log.d("--->","LOCAL-"+direccionlocal+"-");
                    Log.d("--->","RECIB-"+direccionRecibida+"-");
                    Log.d("--->", "Socket 1 received msg: " + msg);
                    lectura(msg);
                    recibiendo=true;
                    seguir=false;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim<0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }
    public void lectura(String text){
        String buff="";
        int puert;
        for (int i = 0; i < text.length()-1 ; i++) {
            if (text.charAt(i)=='-'){
                buff+=text.charAt(i+1);
                buff+=text.charAt(i+2);
                buff+=text.charAt(i+3);
                buff+=text.charAt(i+4);
            }
        }
        puert=Integer.parseInt(buff);
        nuevopueto=puert;
        Log.d("--->","Puerto Leido: "+puert);
    }
}
