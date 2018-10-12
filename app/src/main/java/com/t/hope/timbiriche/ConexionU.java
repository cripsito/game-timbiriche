package com.t.hope.timbiriche;

import android.content.Context;
import android.util.Log;

import java.util.Random;

/**
 * Created by Hope on 17/05/2015.
 */
public class ConexionU extends Thread{
    SocketCliente sCliente;
    SocketServidor sServidor;
    int PuertoLocal=0;//puerto LOCAL
    int PuertoServidor=0;
    int anterior=0;
    boolean iniciado=false;
    boolean pausado=false;
    boolean terminado=false;
    boolean abrirSiguiente=false;
    int TableroRecibido=-1;
    String NombreJugador2="";
    String ColorJugador2="";
    Thread envios,hiloMSJ;
    int contadorEnviosInicio=5;//si conexin falla incrementar
    int contadorEnviosSeguir=40;
    boolean MiTurno=false;
    int coordenadasX,coordenadasY;
    boolean nuevasCoordenadas=false;
    boolean renovarConexion=false;
    boolean nuevaPartida=false;
    public boolean Iniciado(){
        return iniciado;
    }
    ConexionU(){
        int min = 4000;
        int max = 6500;
        Random r = new Random();
        int aux = r.nextInt(max - min + 1) + min;
        PuertoLocal=aux;
    }
    void cliente(String ip, int port){
        sCliente=new SocketCliente(ip,port);
        sCliente.start();
        PuertoServidor=port;
    }
    void servidor(){
        sServidor=new SocketServidor("S"+PuertoLocal,PuertoLocal);
        sServidor.start();
    }
    public void establecerPuertoLocal(int pu){
        PuertoLocal=pu;
    }
    public boolean recibiendo(){
        return sServidor.recibiendo();
    }
    public void ClienteEnviarMensaje(String mensaje){
        sCliente.enviarMSG(mensaje);
    }
    public void ServidorEnviarMensaje(String mensaje){
        sServidor.enviarMSG(mensaje);
    }
    public void TerminarProcesamiento(){
        terminado=true;
    }
    public void CerrarConexiones(){
        sServidor.desconectar();
        sCliente.desconectar();
        terminado=true;
    }
    public void CerrarConexiones2(){
        sServidor.desconectar();
        terminado=true;
    }
    void enviarInicio(){

        envios = new Thread(){
            public void run(){
                while (contadorEnviosInicio>0){
                    try {
                        sleep(18);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (PuertoLocal>PuertoServidor){
                        sServidor.enviarMSG("-I iniciar");
                        iniciado=true;
                        //terminado=true;

                    }
                    contadorEnviosInicio--;
                }
            }
        };
    }
    public void enviarInicioStart(){
        envios.start();
    }

    void establecerConexionAlServidor(){
        hiloMSJ = new Thread(){
            public void run(){
                while (contadorEnviosSeguir>0){
                    try {
                        sleep(90);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                        sCliente.enviarMSG("-establecerConexion");
                        Log.d("---->", "seguir");

                    contadorEnviosSeguir--;
                }
            }
        };
    }
    public void establecerConexionServidorStart(){
        hiloMSJ.start();
    }

    @Override
    public void run() {
        while (terminado==false) {
            if (sCliente.control != anterior) {
                anterior++;
                Log.d("YYYYY", "MENSAJE (" + anterior + "): " +sCliente.Mensajes[anterior]);
                if (sCliente.Mensajes[anterior]!=null){
                    //-------------------INSTRUCCIONES DE ENTRADA---------------------
                    if (sCliente.Mensajes[anterior].contains("-I iniciar")) {
                        iniciado = true;
                        Log.d("---->", "iniciando");
                    }
                    if (sCliente.Mensajes[anterior].contains("-S seguir")) {
                        abrirSiguiente=true;
                        terminado=true;
                        Log.d("---->", "seguir");
                    }
                    if (sCliente.Mensajes[anterior].contains("-Tu Turno")) {
                        MiTurno=true;
                        Log.d("---->", "MI TURNO");
                    }
                    if (sCliente.Mensajes[anterior].contains("-C Conectado")) {
                        renovarConexion=true;
                    }
                    if (sCliente.Mensajes[anterior].contains("-N NuevaPartida")) {
                        nuevaPartida=true;
                    }
                    if (sCliente.Mensajes[anterior].contains("-CoX-")) {
                        coordenadasX=Integer.parseInt(sCliente.Mensajes[anterior].substring(4 + sCliente.Mensajes[anterior].indexOf("CoX"), 5 + sCliente.Mensajes[anterior].indexOf("CoX")));
                        Log.d("---->", "CordenadaX: "+coordenadasX);
                    }
                    if (sCliente.Mensajes[anterior].contains("-CoY-")) {
                        coordenadasY=Integer.parseInt(sCliente.Mensajes[anterior].substring(4 + sCliente.Mensajes[anterior].indexOf("CoY"), 5 + sCliente.Mensajes[anterior].indexOf("CoY")));
                        Log.d("---->", "CordenadaY: "+coordenadasY);
                        nuevasCoordenadas=true;
                    }
                    if (sCliente.Mensajes[anterior].contains("-tab-")) {
                        TableroRecibido=Integer.parseInt(sCliente.Mensajes[anterior].substring(4 + sCliente.Mensajes[anterior].indexOf("tab"), 5 + sCliente.Mensajes[anterior].indexOf("tab")));
                        Log.d("---->", "TableroRec: "+TableroRecibido);
                    }
                    if (sCliente.Mensajes[anterior].contains("-nom-")) {
                        NombreJugador2=sCliente.Mensajes[anterior].substring(4 + sCliente.Mensajes[anterior].indexOf("nom"), sCliente.Mensajes[anterior].indexOf("/"));
                        Log.d("---->", "NombreRec: "+NombreJugador2);
                    }
                    if (sCliente.Mensajes[anterior].contains("-col-")) {
                        ColorJugador2=sCliente.Mensajes[anterior].substring(4 + sCliente.Mensajes[anterior].indexOf("col"), sCliente.Mensajes[anterior].indexOf("/"));
                        Log.d("---->", "ColorRec: "+ColorJugador2);
                    }
                    //----------------TERMINO INSTRUCCIONES DE ENTRADA---------------------
                }
            }
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
