package com.t.hope.timbiriche;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServidor extends Thread {
    Socket cliente;  // Declaramos el socket del cliente
    ServerSocket servidor;
    InputStreamReader entradaSocket;
    DataOutputStream salida, salida1;
    BufferedReader entrada;
    boolean ConexionExitosa=false;
    int puerto=0;
    int PuertoDeEmergencia=0;
    String IpDeEmergencia="";
    boolean BanderaPE=false;
    boolean Recibiendo=false;
    boolean TerminoDeConexion=false;
    boolean EnvioDelServidorAlCliente=false;


    public SocketServidor(String nombre,int p)//servidor
    {
        super(nombre);
        puerto=p;

    }

    public void enviarMSG(String msg) {
        try {
            salida.writeUTF(msg + "\n");
            EnvioDelServidorAlCliente=true;
        } catch (Exception e) {
            System.out.println("Problemas al Enviar");
            EnvioDelServidorAlCliente=false;
        };

    }

    public void run() {
        try {
            this.servidor = new ServerSocket(puerto);
            this.cliente = servidor.accept();

            // creacion de entrada de datos para lectura de mensajes
            this.entradaSocket = new InputStreamReader(cliente.getInputStream());
            this.entrada = new BufferedReader(entradaSocket);

            //creacion de salida de datos para el envio de mensajes
            this.salida = new DataOutputStream(cliente.getOutputStream());


        } catch (IOException e) {
            System.out.println("Algun tipo de error a sucedido");
        }
        String text = "error";
        while (!TerminoDeConexion) {
            try {
                text = this.entrada.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e){

            }

            lectura(text);
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
    public boolean recibiendo(){
        return Recibiendo;
    }
    public void lectura(String text){
        if(text!=null){

            Recibiendo=true;
            //-------------------INSTRUCCIONES DE ENTRADA---------------------
            if (!text.contains("error")) {
                Log.d("#####", "MENSAJE en S: " + text);
            }
            //----------------TERMINO INSTRUCCIONES DE ENTRADA---------------------
            String buff="";
            int puert;
            for (int i = 0; i < text.length()-1 ; i++) {
                if (text.charAt(i)=='-'){
                    if(text.charAt(i+5)=='+'){
                        buff+=text.charAt(i+1);
                        buff+=text.charAt(i+2);
                        buff+=text.charAt(i+3);
                        buff+=text.charAt(i+4);
                        BanderaPE=true;
                        puert=Integer.parseInt(buff);
                        PuertoDeEmergencia=puert;
                        Log.d("--->","Puerto Leido: "+puert);
                    }
                }
            }

            buff="";
            for (int i = 0; i < text.length()-1 ; i++) {
                if (text.charAt(i)=='_'){
                    for (int j = i+1; j < text.length()-1; j++) {
                        if (text.charAt(j)!='+'){
                            buff+=text.charAt(j);
                        }
                        else {
                            j=text.length();
                            i=text.length();
                        }
                    }
                    IpDeEmergencia=buff;
                    Log.d("!!!!ip Emergencia: ",IpDeEmergencia);
                }
            }

        }
    }
}
