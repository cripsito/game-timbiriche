package com.t.hope.timbiriche;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class ResultadoPartida extends Activity {

    BufferedReader fin=null;
    String auxp1="",auxp2="";
    int puntos1=0, puntos2=0;
    //VARIABLES DE CONEXION
    String pe="";
    boolean partidaonline=false;
    ImageButton nuevapartida;
    ImageButton reiniciar;
    ProgressBar cargando;
    int ganadas,perdidas,empatadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_partida);
        try{
            fin = new BufferedReader(new InputStreamReader(openFileInput("Puntos1.txt")));
            auxp1 = fin.readLine();
            fin.close();
            fin = new BufferedReader(new InputStreamReader(openFileInput("Puntos2.txt")));
            auxp2 = fin.readLine();
            fin.close();
        }
        catch (Exception ex) {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
        }
        puntos1=Integer.parseInt(auxp1);
        puntos2=Integer.parseInt(auxp2);

        try{
            fin = new BufferedReader(new InputStreamReader(openFileInput("PartidasGanadas.txt")));
            ganadas = Integer.parseInt(fin.readLine());
            fin.close();
            fin = new BufferedReader(new InputStreamReader(openFileInput("PartidasPerdidas.txt")));
            perdidas = Integer.parseInt(fin.readLine());
            fin.close();
            fin = new BufferedReader(new InputStreamReader(openFileInput("PartidasEmpatadas.txt")));
            empatadas = Integer.parseInt(fin.readLine());
            fin.close();
        }catch (Exception e){

        }

        Log.e("Puntos1: "+puntos1, "Puntos2: "+puntos2);
        if(puntos1>puntos2){
            ImageView iv =(ImageView) findViewById(R.id.mensaje);
            iv.setImageResource(R.mipmap.ganaste);
            iv =(ImageView) findViewById(R.id.mensajetop);
            iv.setImageResource(R.mipmap.copa);
            ganadas++;
        }else{
            if(puntos1<puntos2){
                ImageView iv =(ImageView) findViewById(R.id.mensaje);
                iv.setImageResource(R.mipmap.perdiste);
                iv =(ImageView) findViewById(R.id.mensajetop);
                iv.setImageResource(R.mipmap.nubes);
                perdidas++;
            }else{
                ImageView iv =(ImageView) findViewById(R.id.mensaje);
                iv.setImageResource(R.mipmap.empate);
                iv =(ImageView) findViewById(R.id.mensajetop);
                iv.setImageResource(R.mipmap.caritas);
                empatadas++;
            }
        }
        try {
            OutputStreamWriter fout = new OutputStreamWriter(openFileOutput("PartidasGanadas.txt", Context.MODE_PRIVATE));
            fout.write(""+ganadas);
            fout.close();
            fout = new OutputStreamWriter(openFileOutput("PartidasPerdidas.txt", Context.MODE_PRIVATE));
            fout.write(""+perdidas);
            fout.close();
            fout = new OutputStreamWriter(openFileOutput("PartidasEmpatadas.txt", Context.MODE_PRIVATE));
            fout.write(""+empatadas);
            fout.close();
        } catch (Exception exx) {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }


        //------------------COMPROBAR SI CONEXION PREVIA---------------
        nuevapartida= (ImageButton) findViewById(R.id.imageButton7);
        reiniciar= (ImageButton) findViewById(R.id.imageButton6);
        cargando=(ProgressBar) findViewById(R.id.cargandoN);
        nuevapartida.setVisibility(View.INVISIBLE);
        reiniciar.setVisibility(View.INVISIBLE);
        cargando.setVisibility(View.INVISIBLE);
        try{
            BufferedReader fin = new BufferedReader(new InputStreamReader(openFileInput("ConexionExitosa.txt")));
            pe = fin.readLine();
            fin.close();
        }
        catch (Exception ex) {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
            try {
                OutputStreamWriter fout = new OutputStreamWriter(openFileOutput("ConexionExitosa.txt", Context.MODE_PRIVATE));
                fout.write("no");
                fout.close();
            } catch (Exception exx) {
                Log.e("Ficheros", "Error al escribir fichero a memoria interna");
            }
        }

        if (pe.contains("si")){
            partidaonline=true;

            reiniciar.setVisibility(View.INVISIBLE);
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    conexion();
                }
            }, 300);
        }
        else {
            nuevapartida.setVisibility(View.INVISIBLE);
            reiniciar.setVisibility(View.VISIBLE);
        }
        //------------------COMPROBAR SI CONEXION PREVIA---------------
    }

    public void nuevapartida(View view){
        if(!solicitudNuevaPartida){
            conexion2.ServidorEnviarMensaje("-N NuevaPartida");
            conexion2.ServidorEnviarMensaje("-N NuevaPartida");
            cargando.setVisibility(View.VISIBLE);
            reiniciar.setVisibility(View.INVISIBLE);
            nuevapartida.setVisibility(View.INVISIBLE);
        }else {
            conexion2.ServidorEnviarMensaje("-S seguir");
            conexion2.sServidor.enviarMSG("-S seguir");
            //conexion2.terminado=true;
            conexion2.abrirSiguiente=true;

        }
    }
    public void SeleccionTablero(){
        conexion2.ServidorEnviarMensaje("-S seguir");
        conexion2.ServidorEnviarMensaje("-S seguir");
        unavez=false;

        mHandler.postDelayed(new Runnable() {
            public void run() {
                Log.d("+++++", "---- CERRAR CONEXIONES----");
                conexion2.CerrarConexiones();
                conexion2.TerminarProcesamiento();
            }
        }, 600);
        Intent intent = new Intent(this, Tableros.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        System.gc();
        startActivity(intent);
    }

    public void reiniciar(View view){
        Intent intent = new Intent(this, Tableros.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        System.gc();
        startActivity(intent);
    }
    public void menuprincipal(View view){
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        System.gc();
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resultado_partida, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //----------------------------INICIA SEGUIR CONEXION-------------------------------
    private Handler mHandler = new Handler();
    ConexionU conexion2;
    boolean encontrado=false;
    String puertoL,puertoS,IPL,IPS;
    int PuertoL=0,PuertoS=0;
    boolean unavez=true;
    boolean respuestaConexion=true;
    boolean solicitudNuevaPartida=false;
    int contadorRespuestaConexion=1000;

    public void conexion(){
        Log.d("--->", "SEGUIR CONEXION ANTERIOR");
        try{
            BufferedReader fin = new BufferedReader(new InputStreamReader(openFileInput("PuertoLocal.txt")));
            puertoL = fin.readLine();
            PuertoL=Integer.parseInt(puertoL);
            fin.close();
        }
        catch (Exception ex) {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
        }
        try{
            BufferedReader fin = new BufferedReader(new InputStreamReader(openFileInput("PuertoServidor.txt")));
            puertoS = fin.readLine();
            PuertoS=Integer.parseInt(puertoS);
            fin.close();
        }
        catch (Exception ex) {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
        }
        try{
            BufferedReader fin = new BufferedReader(new InputStreamReader(openFileInput("IPLocal.txt")));
            IPL = fin.readLine();
            fin.close();
        }
        catch (Exception ex) {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
        }
        try{
            BufferedReader fin = new BufferedReader(new InputStreamReader(openFileInput("IPServidor.txt")));
            IPS = fin.readLine();
            fin.close();
        }
        catch (Exception ex) {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
        }


        conexion2=new ConexionU();
        conexion2.establecerPuertoLocal(PuertoL+2);
        conexion2.servidor();




        mHandler.postDelayed(new Runnable() {
            public void run() {
                conexion2.cliente(IPS, PuertoS+2);
                conexion2.start();
                conexion2.sCliente.enviarMSG("-CONEXION CORRECTA AL SERVIDOR");
                Log.d("!!!!!", "ENVIOS AL SERVIDOR");
                conexion2.establecerConexionAlServidor();
                conexion2.establecerConexionServidorStart();
            }
        }, 1200);

        mHandler.postDelayed(new Runnable() {
            public void run() {
                escuchadorConexion();
            }
        }, 2000);
    }
    public void escuchadorConexion(){
        for (int i = 0; i < 1; i++) {//manda el cliente
            conexion2.sCliente.enviarMSG("-T tableros");
        }
        if (conexion2.recibiendo()){//si recibimos manda el servidor
            Log.d("!!!!!","RECIBIENDO DE CONEXION EN SERVIDOR");
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    conexion2.ServidorEnviarMensaje("-R Servidor");
                    nuevapartida.setVisibility(View.VISIBLE);
                    adicional();
                }
            }, 800);
            encontrado=true;
        }
        mHandler.postDelayed(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!encontrado) {
                    escuchadorConexion();
                }
            }
        }, 100);
    }
    public void adicional(){
        //----------------ACCIONES A REVISAR EN LA CONEXION-----------
        if (conexion2.abrirSiguiente){
            Log.e("*****", "ABRIENDO SIGUIENTE ");
            if (unavez){
                unavez =false;
                conexion2.terminado=true;
                SeleccionTablero();
            }
        }
        contadorRespuestaConexion-=100;
        if(conexion2.nuevaPartida){
            solicitudNuevaPartida=true;
            conexion2.nuevaPartida=false;
        }
        conexion2.ServidorEnviarMensaje("-C Conectado");
        if(conexion2.renovarConexion){
            conexion2.renovarConexion=false;
            contadorRespuestaConexion=400;
        }
        if (contadorRespuestaConexion<0){
            unavez=false;
            //mostrar ventana de perdida de conexion
            Intent intent = new Intent(this, ConexionPerdida.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        //------------TERMINA ACCIONES A REVISAR EN LA CONEXION---------
        mHandler.postDelayed(new Runnable() {
            public void run() {

                if (unavez) {
                    adicional();
                }
            }
        }, 300);
    }
    //----------------------------TERMINA SEGUIR CONEXION-------------------------------
}
