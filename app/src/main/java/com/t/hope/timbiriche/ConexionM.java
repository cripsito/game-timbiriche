package com.t.hope.timbiriche;


import android.util.Log;

/**
 * Created by Hope on 17/05/2015.
 */
public class ConexionM extends Thread {
    public int valor;
    boolean servidor=false,cliente=false;
    public String IpServidor="";
    public String IpLocal="";
    int aux;
    boolean tiempoespera=true;
    MulticastIN entrada;
    MulticastOUT salida;
    int nuevopueto=0;
    ConexionM(){
        entrada= new MulticastIN();
        entrada.start();
    }
    public void EnvioPaquetes(int puerto){
        salida= new MulticastOUT("-"+puerto+"+");
        salida.start();
    }
    public int tiempo(){
        return aux;
    }



    @Override
    public void run() {
        boolean seguir=true;
        while (seguir){
            if(entrada.recibiendo){
                seguir=false;
                IpServidor=entrada.DireccionRecv();
                IpLocal=entrada.DireccionLocal();
                nuevopueto=entrada.nuevopueto;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public String DireccionServidor(){
        return IpServidor;
    }
}
