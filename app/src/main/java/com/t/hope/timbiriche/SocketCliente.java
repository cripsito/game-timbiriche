package com.t.hope.timbiriche;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Hope on 17/05/2015.
 */
public class SocketCliente extends Thread {
    int noCte = 0;
    static Socket cliente;  // Declaramos el socket del cliente
    static ServerSocket servidor;
    InputStreamReader entradaSocket;
    DataOutputStream salida, salida1;
    BufferedReader entrada;
    boolean ConexionExitosa=false;
    boolean TerminoDeConexion=false;

    // control de lectura
    int control=0;
    String Mensajes[]=new String[100000];
    //

    public SocketCliente(String ip,int puerto)//cliente
    {
        try {
            this.cliente = new Socket(ip, puerto);

            // creacion de entrada de datos para lectura de mensajes
            this.entradaSocket = new InputStreamReader(cliente.getInputStream());
            this.entrada = new BufferedReader(entradaSocket);

            //creacion de salida de datos para el envio de mensajes
            this.salida = new DataOutputStream(cliente.getOutputStream());
            //this.salida.writeUTF("Conectado \n");

        } catch (Exception e) {
        };

    }

    public void run() {
        String texto;
        while (!TerminoDeConexion) {
            try {
                texto = entrada.readLine();//regresa el mensaje el servidor haya enviado(escuchador)
                if(texto.length()>=3)
                {
                    Log.d("#####", "MENSAJE ("+control+"): " + texto);
                    if(texto.substring(2).equalsIgnoreCase("-N conexion exitosa")){
                        ConexionExitosa=true;
                    }
                    if(texto.substring(2).equalsIgnoreCase("-R solicitud")||texto.substring(2).equalsIgnoreCase("solicitud")) {
                        this.enviarMSG("-N conexion exitosa");
                    }
                    control++;
                    Mensajes[control]=texto;

                }

            } catch (IOException e) {
            }catch (NullPointerException e){
            }
        }
    }

    public void enviarMSG(String msg) {
        System.out.println("ENVIANDO");
        try {
            this.salida = new DataOutputStream(cliente.getOutputStream());
            this.salida.writeUTF(msg + "\n");

        } catch (IOException e) {
            System.out.println("Problemas al Enviar");
        } catch (NullPointerException e){

        }

    }

    public String leerMSG() {
        //va a dar un retur de entrada
        try {
            return entrada.readLine();//regresa el mensaje el cliente haya enviado(escuchador)
        } catch (Exception e) {
        };
        return null;

    }

    public void desconectar() {
        try {
            cliente.close();
        } catch (Exception e) {
        }
        try {
            servidor.close();
        } catch (Exception e) {
        };
        TerminoDeConexion=true;
    }
}
