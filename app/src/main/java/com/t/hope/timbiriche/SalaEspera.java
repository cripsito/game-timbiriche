package com.t.hope.timbiriche;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class SalaEspera extends ActionBarActivity {

    ImageView paloma;
    ProgressBar cargando;
    ImageButton iniciar,reintentar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WifiManager wifi = (WifiManager)getSystemService( Context.WIFI_SERVICE );
        if(wifi != null){
            WifiManager.MulticastLock lock = wifi.createMulticastLock("Log_Tag");
            lock.acquire();
        }

        setContentView(R.layout.activity_sala_espera);

        paloma = (ImageView) findViewById(R.id.paloma);
        cargando=(ProgressBar) findViewById(R.id.cargando);
        paloma.setVisibility(View.GONE);
        iniciar =(ImageButton) findViewById(R.id.iniciarmul);
        iniciar.setVisibility(View.GONE);
        reintentar =(ImageButton) findViewById(R.id.reintentarc);
        reintentar.setVisibility(View.GONE);


        String pe="";//partida exitosa
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
            paloma.setVisibility(View.GONE);
            iniciar.setVisibility(View.VISIBLE);
        }
        else {
            conexion();
        }

    }
    //----------------------------INICIA-CONEXION-------------------------------
    private Handler mHandler = new Handler();
    ConexionM conexion;
    ConexionU conexion2;
    boolean encontrado=false;
    boolean unavez=true;
    public void conexion(){
        conexion=new ConexionM();
        conexion2=new ConexionU();
        conexion2.servidor();
        conexion.start();
        conexion.EnvioPaquetes(conexion2.PuertoLocal);


        mHandler.postDelayed(new Runnable() {
            public void run() {
                escuchadorConexion();
            }
        },  300);//4000
        mHandler.postDelayed(new Runnable() {
            public void run() {
                if(!encontrado){
                    cargando.setVisibility(View.GONE);
                    reintentar.setVisibility(View.VISIBLE);
                    conexion2.CerrarConexiones2();
                }
            }
        },  10000);
    }

    public void escuchadorConexion(){
        if (conexion.entrada.recibiendo){
            conexion2.cliente(conexion.DireccionServidor(), conexion.nuevopueto);
            for (int i = 0; i < 3; i++) {
                conexion2.sCliente.enviarMSG("-"+conexion2.PuertoLocal+"+");
                conexion2.sCliente.enviarMSG("_"+conexion.IpLocal+"+");
                Log.d("!!!!!","ip Emergencia: "+conexion.IpLocal);
            }
            conexion2.start();
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    adicional();
                }
            }, 1700);
            encontrado=true;
            cargando.setVisibility(View.GONE);
            paloma.setVisibility(View.VISIBLE);
        }
        else {
            if (conexion2.sServidor.BanderaPE){
                conexion2.cliente(conexion2.sServidor.IpDeEmergencia, conexion2.sServidor.PuertoDeEmergencia);
                Notificaciones no1=new Notificaciones(getApplicationContext(),"puerto recibido: "+conexion2.sServidor.PuertoDeEmergencia+"\nIP recibida: "+conexion2.sServidor.IpDeEmergencia);
                no1.mostrar();
                for (int i = 0; i < 3; i++) {
                    conexion2.sCliente.enviarMSG("CONECCION CORRECTA");
                }
                conexion2.start();
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        adicional();
                    }
                }, 1700);
                encontrado=true;
                conexion.entrada.direccionRecibida=conexion2.sServidor.IpDeEmergencia;
                conexion.IpServidor=conexion2.sServidor.IpDeEmergencia;
                conexion.nuevopueto=conexion2.sServidor.PuertoDeEmergencia;
                conexion.entrada.nuevopueto=conexion2.sServidor.PuertoDeEmergencia;
                cargando.setVisibility(View.GONE);
                paloma.setVisibility(View.VISIBLE);
            }
        }
        mHandler.postDelayed(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!encontrado){
                    escuchadorConexion();
                }

            }
        }, 50);
    }

    public void adicional(){
        conexion2.sCliente.enviarMSG("-I iniciar");

        if (conexion2.abrirSiguiente){
            if (unavez){
                unavez =false;
                iniciapartida(this.getCurrentFocus());
                abrirTab();
            }
        }
        if(conexion2.recibiendo()){//mensaje del cliente
            conexion2.enviarInicio();
            conexion2.enviarInicioStart();
        }
        if (conexion2.Iniciado()){//mensaje del servidor
            paloma.setVisibility(View.GONE);
            iniciar.setVisibility(View.VISIBLE);
        }

        mHandler.postDelayed(new Runnable() {
            public void run() {
                if (unavez) {
                    adicional();
                }
            }
        }, 150);
    }
    //----------------------------TERMINA CONEXION-------------------------------

    public void abrirTab(){
        conexion2.terminado=true;
        conexion2.abrirSiguiente=true;
        unavez =false;
        mHandler.postDelayed(new Runnable() {
            public void run() {
                Log.d("+++++","---- CERRAR CONEXIONES----");
                conexion2.CerrarConexiones();
            }
        },600);

        System.gc();
        Intent intent = new Intent(this, Tableros.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void iniciapartida(View view){
        conexion2.sServidor.enviarMSG("-S seguir");
        conexion2.sServidor.enviarMSG("-S seguir");


        try {
            OutputStreamWriter fout = new OutputStreamWriter(openFileOutput("ConexionExitosa.txt", Context.MODE_PRIVATE));
            fout.write("si");
            fout.close();
        } catch (Exception exx) {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }
        try {
            OutputStreamWriter fout = new OutputStreamWriter(openFileOutput("PuertoLocal.txt", Context.MODE_PRIVATE));
            fout.write(""+conexion2.PuertoLocal);
            fout.close();
        } catch (Exception exx) {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }
        try {
            OutputStreamWriter fout = new OutputStreamWriter(openFileOutput("PuertoServidor.txt", Context.MODE_PRIVATE));
            fout.write(""+conexion2.PuertoServidor);
            fout.close();
        } catch (Exception exx) {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }
        try {
            OutputStreamWriter fout = new OutputStreamWriter(openFileOutput("IPLocal.txt", Context.MODE_PRIVATE));
            fout.write(""+conexion.entrada.direccionlocal);
            fout.close();
        } catch (Exception exx) {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }
        try {
            OutputStreamWriter fout = new OutputStreamWriter(openFileOutput("IPServidor.txt", Context.MODE_PRIVATE));

            if(!conexion2.sServidor.IpDeEmergencia.equalsIgnoreCase("")){
                fout.write(""+conexion2.sServidor.IpDeEmergencia);
            }
            else{
                if(!conexion.entrada.DireccionRecv().equalsIgnoreCase("")){
                    fout.write(""+conexion.entrada.DireccionRecv());
                }
            }

            fout.close();
        } catch (Exception exx) {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }
    }


    public void reintentar(View view){
        Intent intent = new Intent(view.getContext(), SalaEspera.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sala_espera, menu);
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
    public void liberar(){
        Bitmap a;
        //Bitmap backgroud= BitmapFactory.decodeStream(getResources().openRawResource(R.mipmap.fondo));
        BitmapDrawable backgroudD;;

    }
    public void cargar(){

    }
}
