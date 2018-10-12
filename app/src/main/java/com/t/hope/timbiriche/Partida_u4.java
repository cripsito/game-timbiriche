package com.t.hope.timbiriche;

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
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class Partida_u4 extends ActionBarActivity {

    BufferedReader fin=null;
    String colores[]={"azul","azul2","amarilla","morada","naranja","roja","rosa","verde","verde2"};
    String color="";
    String color2="";
    String dif="";
    String mDrawableName = "com.t.hope.timbiriche";
    String NombreJugador1;
    int Turno=1;
    int Tablero[][]=new int[9][9];
    int TableroPuntos[][]=new int[4][4];
    int Dimencion=4;
    int Puntos1=0;
    int Puntos2=0;
    boolean yaTiro2=false;
    boolean TurnoExtra1=false;
    boolean TurnoExtra2=false;
    boolean PartidaTerminada=false;
    int Dificultad=1;
    //VARIABLES DE CONEXION
    String pe="";
    boolean partidaonline=false;
    int CoordenadasX,CoordenadasY;
    ImageView turno1,turno2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida_u4);
        try{
            fin = new BufferedReader(new InputStreamReader(openFileInput("dificultad.txt")));
            dif = fin.readLine();
            fin.close();
            fin = new BufferedReader(new InputStreamReader(openFileInput("NombreJugador1.txt")));
            NombreJugador1 = fin.readLine();
            fin.close();
        }
        catch (Exception ex) {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
        }
        if (dif.equalsIgnoreCase("0")){
            Dificultad=1;
        }else if(dif.equalsIgnoreCase("1")){
            Dificultad=2;
        }

        try{
            fin = new BufferedReader(new InputStreamReader(openFileInput("ColorJugador1.txt")));
            color = fin.readLine();
            fin.close();
        }
        catch (Exception ex) {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
        }
        boolean salir=true;
        while (salir){
            color2=colores[(int)Math.floor((Math.random() * colores.length) + 0)];
            if(!color.equalsIgnoreCase(color2)){
                salir=false;
            }
            Log.i("while: ","seleccion de color");
        }
        ImageView c1=(ImageView)findViewById(R.id.color1);
        c1.setImageResource(color(color,4));
        c1=(ImageView)findViewById(R.id.color2);
        c1.setImageResource(color(color2, 4));
        TextView  tw=(TextView)findViewById(R.id.nombre1);
        tw.setText(NombreJugador1);
        TextView  tw2=(TextView)findViewById(R.id.nombre2);
        if(color2.equalsIgnoreCase("azul")||color2.equalsIgnoreCase("azul2")){
            tw2.setText("Frambueza");
        }
        if(color2.equalsIgnoreCase("verde")||color2.equalsIgnoreCase("verde2")){
            tw2.setText("Melon");
        }
        if(color2.equalsIgnoreCase("morada")){
            tw2.setText("Zarzamora");
        }
        if(color2.equalsIgnoreCase("amarilla")){
            tw2.setText("Manzana");
        }
        if(color2.equalsIgnoreCase("rosa")){
            tw2.setText("Guallaba");
        }
        if(color2.equalsIgnoreCase("roja")){
            tw2.setText("Cereza");
        }
        if(color2.equalsIgnoreCase("naranja")){
            tw2.setText("Naranja");
        }

        Intent intent = new Intent(this, InstruccionesPartidaU.class);
        startActivity(intent);

        //------------------COMPROBAR SI CONEXION PREVIA---------------
        turno1= (ImageView) findViewById(R.id.turno1);
        turno2= (ImageView) findViewById(R.id.turno2);
        turno1.setVisibility(View.INVISIBLE);
        turno2.setVisibility(View.INVISIBLE);
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
            try{
                fin = new BufferedReader(new InputStreamReader(openFileInput("ColorJugador2.txt")));
                color2 = fin.readLine();
                c1.setImageResource(color(color2, 4));
                fin.close();
                fin = new BufferedReader(new InputStreamReader(openFileInput("NombreJugador2.txt")));
                tw2.setText(fin.readLine());
                fin.close();
            }
            catch (Exception ex) {
                Log.e("Ficheros", "Error al leer fichero desde memoria interna");
            }
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    conexion();
                }
            }, 300);
        }
        //------------------COMPROBAR SI CONEXION PREVIA---------------
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_partida_u4, menu);
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
    //Seleccion y completar casas

    public int color(String colorLinea, int horientacion){
        if(colorLinea.equalsIgnoreCase("azul")){
            if(horientacion==1){
                return R.mipmap.lineaazul01;
            }else if(horientacion==2){
                return R.mipmap.lineaazul02;
            }else if(horientacion==3){
                return R.mipmap.cuadroazul;
            }else{
                return R.mipmap.colorazul;
            }
        }
        if(colorLinea.equalsIgnoreCase("azul2")){
            if(horientacion==1){
                return R.mipmap.lineaazul201;
            }else if(horientacion==2){
                return R.mipmap.lineaazul202;
            }else if(horientacion==3){
                return R.mipmap.cuadroazul2;
            }else{
                return R.mipmap.colorazul2;
            }
        }
        if(colorLinea.equalsIgnoreCase("amarilla")){
            if(horientacion==1){
                return R.mipmap.lineaamarilla01;
            }else if(horientacion==2){
                return R.mipmap.lineaamarilla02;
            }else if(horientacion==3){
                return R.mipmap.cuadroamarillo;
            }else{
                return R.mipmap.coloramarillo;
            }
        }
        if(colorLinea.equalsIgnoreCase("morada")){
            if(horientacion==1){
                return R.mipmap.lineamorada01;
            }else if(horientacion==2){
                return R.mipmap.lineamorada02;
            }else if(horientacion==3){
                return R.mipmap.cuadromorado;
            }else{
                return R.mipmap.colormorado;
            }
        }
        if(colorLinea.equalsIgnoreCase("naranja")){
            if(horientacion==1){
                return R.mipmap.lineanaranja01;
            }else if(horientacion==2){
                return R.mipmap.lineanaranja02;
            }else if(horientacion==3){
                return R.mipmap.cuadronaranja;
            }else{
                return R.mipmap.colornaranja;
            }
        }
        if(colorLinea.equalsIgnoreCase("roja")){
            if(horientacion==1){
                return R.mipmap.linearoja01;
            }else if(horientacion==2){
                return R.mipmap.linearoja02;
            }else if(horientacion==3){
                return R.mipmap.cuadrorojo;
            }else{
                return R.mipmap.colorrojo;
            }
        }
        if(colorLinea.equalsIgnoreCase("rosa")){
            if(horientacion==1){
                return R.mipmap.linearosa01;
            }else if(horientacion==2){
                return R.mipmap.linearosa02;
            }else if(horientacion==3){
                return R.mipmap.cuadrorosa;
            }else{
                return R.mipmap.colorrosa;
            }
        }
        if(colorLinea.equalsIgnoreCase("verde")){
            if(horientacion==1){
                return R.mipmap.lineaverde01;
            }else if(horientacion==2){
                return R.mipmap.lineaverde02;
            }else if(horientacion==3){
                return R.mipmap.cuadroverde;
            }else{
                return R.mipmap.colorverde;
            }
        }
        if(colorLinea.equalsIgnoreCase("verde2")){
            if(horientacion==1){
                return R.mipmap.lineaverde201;
            }else if(horientacion==2){
                return R.mipmap.lineaverde202;
            }else if(horientacion==3){
                return R.mipmap.cuadroverde2;
            }else{
                return R.mipmap.colorverde2;
            }
        }
        if(colorLinea.equalsIgnoreCase("trans")){
            if(horientacion==1){
                return R.mipmap.lineatrans01;
            }else if(horientacion==2){
                return R.mipmap.lineatrans02;
            }else if(horientacion==3){
                return R.mipmap.cuadrotrans;
            }
        }

        return  R.mipmap.lineatrans01;

    }

    public void SeleccionarLinea(View view){
        Log.i(" Seleccionar Linea: ", "1");
        if(Turno==1) {

            ImageButton boton = (ImageButton) findViewById(view.getId());
            if(boton.getResources().getIdentifier(mDrawableName , "drawable", getPackageName())==color("trans",1)){
                boton.setImageResource(color(color, 1));
            }

            switch (view.getId()) {
                case (R.id.l1t0): {//primeros 4
                    if(Tablero[1][0]==0){
                        Tablero[1][0] = Turno;
                        CoordenadasX=1;
                        CoordenadasY=0;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l3t0): {
                    if(Tablero[3][0]==0){
                        Tablero[3][0] = Turno;
                        CoordenadasX=3;
                        CoordenadasY=0;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l5t0): {
                    if(Tablero[5][0]==0){
                        Tablero[5][0] = Turno;
                        CoordenadasX=5;
                        CoordenadasY=0;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l7t0): {
                    if(Tablero[7][0]==0){
                        Tablero[7][0] = Turno;
                        CoordenadasX=7;
                        CoordenadasY=0;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l1t2): {//terceros 4
                    if(Tablero[1][2]==0){
                        Tablero[1][2] = Turno;
                        CoordenadasX=1;
                        CoordenadasY=2;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l3t2): {
                    if(Tablero[3][2]==0){
                        Tablero[3][2] = Turno;
                        CoordenadasX=3;
                        CoordenadasY=2;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l5t2): {
                    if(Tablero[5][2]==0){
                        Tablero[5][2] = Turno;
                        CoordenadasX=5;
                        CoordenadasY=2;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l7t2): {
                    if(Tablero[7][2]==0){
                        Tablero[7][2] = Turno;
                        CoordenadasX=7;
                        CoordenadasY=2;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l1t4): {//quintos 4
                    if(Tablero[1][4]==0){
                        Tablero[1][4] = Turno;
                        CoordenadasX=1;
                        CoordenadasY=4;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l3t4): {
                    if(Tablero[3][4]==0){
                        Tablero[3][4] = Turno;
                        CoordenadasX=3;
                        CoordenadasY=4;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l5t4): {
                    if(Tablero[5][4]==0){
                        Tablero[5][4] = Turno;
                        CoordenadasX=5;
                        CoordenadasY=4;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l7t4): {
                    if(Tablero[7][4]==0){
                        Tablero[7][4] = Turno;
                        CoordenadasX=7;
                        CoordenadasY=4;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l1t6): {//septimos 4
                    if(Tablero[1][6]==0){
                        Tablero[1][6] = Turno;
                        CoordenadasX=1;
                        CoordenadasY=6;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l3t6): {
                    if(Tablero[3][6]==0){
                        Tablero[3][6] = Turno;
                        CoordenadasX=3;
                        CoordenadasY=6;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l5t6): {
                    if(Tablero[5][6]==0){
                        Tablero[5][6] = Turno;
                        CoordenadasX=5;
                        CoordenadasY=6;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l7t6): {
                    if(Tablero[7][6]==0){
                        Tablero[7][6] = Turno;
                        CoordenadasX=7;
                        CoordenadasY=6;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l1t8): {//novenos 4
                    if(Tablero[1][8]==0){
                        Tablero[1][8] = Turno;
                        CoordenadasX=1;
                        CoordenadasY=8;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l3t8): {
                    if(Tablero[3][8]==0){
                        Tablero[3][8] = Turno;
                        CoordenadasX=3;
                        CoordenadasY=8;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l5t8): {
                    if(Tablero[5][8]==0){
                        Tablero[5][8] = Turno;
                        CoordenadasX=5;
                        CoordenadasY=8;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l7t8): {
                    if(Tablero[7][8]==0){
                        Tablero[7][8] = Turno;
                        CoordenadasX=7;
                        CoordenadasY=8;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
            }
            BuscarCuadro(color);
        }
        Log.i(" TURNO AUN: ","--"+Turno);
        MostrarTablero();
        if(partidaonline){
            conexion2.sServidor.enviarMSG("-CoX-"+CoordenadasX);
            conexion2.sServidor.enviarMSG("-CoY-" + CoordenadasY);
            if(!TurnoExtra1){
                Turno=2;
                conexion2.sServidor.enviarMSG("-Tu Turno");
                turno1.setVisibility(View.INVISIBLE);
                turno2.setVisibility(View.VISIBLE);
            }
        }else {
            if(!TurnoExtra1){
                ControlTiro2();
            }
        }
        TurnoExtra1=false;
        ComprobarResultados();
        ControlLineas();
    }

    public void SeleccionarLinea2(View view){
        Log.i(" Seleccionar Linea: ","1");
        if(Turno==1) {

            ImageButton boton = (ImageButton) findViewById(view.getId());
            if(boton.getResources().getIdentifier(mDrawableName , "drawable", getPackageName())==color("trans",2)){
                boton.setImageResource(color(color, 2));
            }

            switch (view.getId()) {
                case (R.id.l0t1): {//segundos 5
                    if(Tablero[0][1]==0){
                        Tablero[0][1] = Turno;
                        CoordenadasX=0;
                        CoordenadasY=1;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l2t1): {
                    if(Tablero[2][1]==0){
                        Tablero[2][1] = Turno;
                        CoordenadasX=2;
                        CoordenadasY=1;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l4t1): {
                    if(Tablero[4][1]==0){
                        Tablero[4][1] = Turno;
                        CoordenadasX=4;
                        CoordenadasY=1;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l6t1): {
                    if(Tablero[6][1]==0){
                        Tablero[6][1] = Turno;
                        CoordenadasX=6;
                        CoordenadasY=1;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l8t1): {
                    if(Tablero[8][1]==0){
                        Tablero[8][1] = Turno;
                        CoordenadasX=8;
                        CoordenadasY=1;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l0t3): {//cuartos 5
                    if(Tablero[0][3]==0){
                        Tablero[0][3] = Turno;
                        CoordenadasX=0;
                        CoordenadasY=3;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l2t3): {
                    if(Tablero[2][3]==0){
                        Tablero[2][3] = Turno;
                        CoordenadasX=2;
                        CoordenadasY=3;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l4t3): {
                    if(Tablero[4][3]==0){
                        Tablero[4][3] = Turno;
                        CoordenadasX=4;
                        CoordenadasY=3;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l6t3): {
                    if(Tablero[6][3]==0){
                        Tablero[6][3] = Turno;
                        CoordenadasX=6;
                        CoordenadasY=3;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l8t3): {
                    if(Tablero[8][3]==0){
                        Tablero[8][3] = Turno;
                        CoordenadasX=8;
                        CoordenadasY=3;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l0t5): {//sextos 5
                    if(Tablero[0][5]==0){
                        Tablero[0][5] = Turno;
                        CoordenadasX=0;
                        CoordenadasY=5;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l2t5): {
                    if(Tablero[2][5]==0){
                        Tablero[2][5] = Turno;
                        CoordenadasX=2;
                        CoordenadasY=5;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l4t5): {
                    if(Tablero[4][5]==0){
                        Tablero[4][5] = Turno;
                        CoordenadasX=4;
                        CoordenadasY=5;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l6t5): {
                    if(Tablero[6][5]==0){
                        Tablero[6][5] = Turno;
                        CoordenadasX=6;
                        CoordenadasY=5;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l8t5): {
                    if(Tablero[8][5]==0){
                        Tablero[8][5] = Turno;
                        CoordenadasX=8;
                        CoordenadasY=5;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l0t7): {//octavos 5
                    if(Tablero[0][7]==0){
                        Tablero[0][7] = Turno;
                        CoordenadasX=0;
                        CoordenadasY=7;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l2t7): {
                    if(Tablero[2][7]==0){
                        Tablero[2][7] = Turno;
                        CoordenadasX=2;
                        CoordenadasY=7;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l4t7): {
                    if(Tablero[4][7]==0){
                        Tablero[4][7] = Turno;
                        CoordenadasX=4;
                        CoordenadasY=7;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l6t7): {
                    if(Tablero[6][7]==0){
                        Tablero[6][7] = Turno;
                        CoordenadasX=6;
                        CoordenadasY=7;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
                case (R.id.l8t7): {
                    if(Tablero[8][7]==0){
                        Tablero[8][7] = Turno;
                        CoordenadasX=8;
                        CoordenadasY=7;
                    }else{
                        TurnoExtra1=true;
                    }
                }
                break;
            }
            BuscarCuadro(color);
        }
        Log.i(" TURNO AUN: ","--"+Turno);
        MostrarTablero();
        if(partidaonline){
            conexion2.sServidor.enviarMSG("-CoX-"+CoordenadasX);
            conexion2.sServidor.enviarMSG("-CoY-" + CoordenadasY);
            if(!TurnoExtra1){
                Turno=2;
                conexion2.sServidor.enviarMSG("-Tu Turno");
                turno1.setVisibility(View.INVISIBLE);
                turno2.setVisibility(View.VISIBLE);
            }
        }else {
            if(!TurnoExtra1){
                ControlTiro2();
            }
        }
        TurnoExtra1=false;
        ComprobarResultados();
        ControlLineas();
    }



    //Seleccion y completar casas

    //Buscador de Casas

    public void BuscarCuadro(String actual){
        Log.i(" BuscarCuadro: ",actual);
        int Limite=Dimencion*2+1;
        ImageView cuadro=null;
        boolean a=false,b=false,c=false,d=false;

        for (int centrox=1, x=0;centrox<Limite;centrox+=2, x++){

            for (int centroy=1, y=0;centroy<Limite;centroy+=2, y++){

                a=false;
                b=false;
                c=false;
                d=false;

                if(Tablero[centrox][centroy-1]>0){
                    a=true;

                }
                if(Tablero[centrox+1][centroy]>0){
                    b=true;

                }
                if(Tablero[centrox][centroy+1]>0){
                    c=true;

                }
                if(Tablero[centrox-1][centroy]>0){
                    d=true;

                }

                if(a&&b&&c&&d){
                    if(TableroPuntos[x][y]==0){
                        TableroPuntos[x][y]=Turno;
                        switch (Turno){
                            case 1:{
                                if(BuscarPuntos(1)>Puntos1){
                                    TurnoExtra1=true;
                                }else{
                                    TurnoExtra1=false;
                                }

                            }break;
                            case 2:{
                                if(BuscarPuntos(2)>Puntos2){
                                    TurnoExtra2=true;
                                }else{
                                    TurnoExtra2=false;
                                }

                            }break;
                        }
                    }

                }

            }

        }



        ContarPuntos();


        String aas="";
        for (int i=0;i<3;i++){
            for (int j=0;j<3;j++){
                aas=aas+TableroPuntos[i][j];
            }
            aas=aas+"\n";
        }

        //Depede del tablero (3x3)

        cuadro=(ImageView)findViewById(R.id.cuadro0s0);
        cuadro.setImageResource(color(ControlLineas2(TableroPuntos[0][0]),3));

        cuadro=(ImageView)findViewById(R.id.cuadro0s1);
        cuadro.setImageResource(color(ControlLineas2(TableroPuntos[0][1]),3));

        cuadro=(ImageView)findViewById(R.id.cuadro0s2);
        cuadro.setImageResource(color(ControlLineas2(TableroPuntos[0][2]),3));

        cuadro=(ImageView)findViewById(R.id.cuadro0s3);
        cuadro.setImageResource(color(ControlLineas2(TableroPuntos[0][3]),3));

        cuadro=(ImageView)findViewById(R.id.cuadro1s0);
        cuadro.setImageResource(color(ControlLineas2(TableroPuntos[1][0]),3));

        cuadro=(ImageView)findViewById(R.id.cuadro1s1);
        cuadro.setImageResource(color(ControlLineas2(TableroPuntos[1][1]),3));

        cuadro=(ImageView)findViewById(R.id.cuadro1s2);
        cuadro.setImageResource(color(ControlLineas2(TableroPuntos[1][2]),3));

        cuadro=(ImageView)findViewById(R.id.cuadro1s3);
        cuadro.setImageResource(color(ControlLineas2(TableroPuntos[1][3]),3));

        cuadro=(ImageView)findViewById(R.id.cuadro2s0);
        cuadro.setImageResource(color(ControlLineas2(TableroPuntos[2][0]),3));

        cuadro=(ImageView)findViewById(R.id.cuadro2s1);
        cuadro.setImageResource(color(ControlLineas2(TableroPuntos[2][1]),3));

        cuadro=(ImageView)findViewById(R.id.cuadro2s2);
        cuadro.setImageResource(color(ControlLineas2(TableroPuntos[2][2]),3));

        cuadro=(ImageView)findViewById(R.id.cuadro2s3);
        cuadro.setImageResource(color(ControlLineas2(TableroPuntos[2][3]),3));

        cuadro=(ImageView)findViewById(R.id.cuadro3s0);
        cuadro.setImageResource(color(ControlLineas2(TableroPuntos[3][0]),3));

        cuadro=(ImageView)findViewById(R.id.cuadro3s1);
        cuadro.setImageResource(color(ControlLineas2(TableroPuntos[3][1]),3));

        cuadro=(ImageView)findViewById(R.id.cuadro3s2);
        cuadro.setImageResource(color(ControlLineas2(TableroPuntos[3][2]),3));

        cuadro=(ImageView)findViewById(R.id.cuadro3s3);
        cuadro.setImageResource(color(ControlLineas2(TableroPuntos[3][3]),3));



    }

    //inteligencia del juego

    public void ControlTiro2(){
        Log.i(" ControlTiro: ","");
        ControlLineas();
        Turno=2;
        yaTiro2=false;
        TirarEnU();
        if(TurnoExtra2){//recuerda que esto no funcionaba en para el turno extra2
            TurnoExtra2=false;
        } yaTiro2=false;
        if(Dificultad==1){
            if (!yaTiro2) {
                TirarLIP(2);
                Log.i("Tiro en ", "Tolerancia 1");
            }
            if (!yaTiro2) {
                TirarLIP(1);
                Log.i("Tiro en ", "Tolerancia 2");
            }

        }else {
            if (!yaTiro2) {
                TirarLIP(1);
                Log.i("Tiro en ", "Tolerancia 1");
            }
            if (!yaTiro2) {
                TirarLIP(2);
                Log.i("Tiro en ", "Tolerancia 2");
            }
        }
        BuscarCuadro(color2);
        ControlLineas();
        ComprobarResultados();
        Turno=1;
    }



    public void TirarEnU(){
        Log.i(" TirarEnU: ","");
        int Limite=Dimencion*2+1;
        boolean a=false,b=false,c=false,d=false;
        boolean entro=true;

        while (entro) {
            entro=false;
            for (int centrox = 1, x = 0; centrox < Limite; centrox += 2, x++) {

                for (int centroy = 1, y = 0; centroy < Limite; centroy += 2, y++) {

                    a = false;
                    b = false;
                    c = false;
                    d = false;

                    if (Tablero[centrox][centroy - 1] > 0) {
                        a = true;
                    }
                    if (Tablero[centrox + 1][centroy] > 0) {
                        b = true;
                    }
                    if (Tablero[centrox][centroy + 1] > 0) {
                        c = true;
                    }
                    if (Tablero[centrox - 1][centroy] > 0) {
                        d = true;
                    }

                    if (!(a && b && c && d)) {
                        if (a && b && c) {
                            Tablero[centrox - 1][centroy] = Turno;
                            TableroPuntos[x][y] = Turno;
                            yaTiro2 = true;
                            entro = true;
                        } else if (a && b && d) {
                            Tablero[centrox][centroy + 1] = Turno;
                            TableroPuntos[x][y] = Turno;
                            yaTiro2 = true;
                            entro = true;
                        } else if (a && c && d) {
                            Tablero[centrox + 1][centroy] = Turno;
                            TableroPuntos[x][y] = Turno;
                            yaTiro2 = true;
                            entro = true;
                        } else if (b && c && d) {
                            Tablero[centrox][centroy - 1] = Turno;
                            TableroPuntos[x][y] = Turno;
                            yaTiro2 = true;
                            entro = true;
                        }
                    }
                }
            }
        }
    }

    //Recuerda que la tolerancia no estodo cuando sea horizontal tiene que comprobar a los lados y vertical hacia arriba y abajo
    public void TirarLIP(int Tolerancia){
        Log.i(" TirarLIP: ","");
        int Limite=Dimencion*2+1;
        boolean a=false,b=false,c=false,d=false;
        boolean entro=true;
        boolean pa=false,pb=false,pc=false,pd=false;
        boolean Terminado=true;

        entro=false;

        int num150= (int)Math.floor((Math.random() * 2) + 1);
        int num250= (int)Math.floor((Math.random() * 2) + 1);
        int inix,iniy,incrementox,incrementoy,limitex,limitey;

        if(num150==1){
            inix=1;
            incrementox=2;
            limitex=Limite;
        }
        else {
            inix=Limite-2;
            incrementox=-2;
            limitex=-1;
        }
        if(num250==1){
            iniy=1;
            incrementoy=2;
            limitey=Limite;
        }
        else {
            iniy=Limite-2;
            incrementoy=-2;
            limitey=-1;
        }

        for (int centrox = inix, x = 0; centrox != limitex; centrox += incrementox, x++) {

            for (int centroy = iniy, y = 0; centroy != limitey; centroy += incrementoy, y++) {

                int permiso=0;

                pa = false;
                pb = false;
                pc = false;
                pd = false;

                /////////////////////////////////////////

                if (Tablero[centrox][centroy - 1] == 0) {
                    if (ComprobarVecinos(centrox,centroy,1,Tolerancia)){
                        pa = true;
                    }else {
                        pa = false;
                    }
                }else{
                    pa = false;
                    permiso++;
                }
                if (Tablero[centrox + 1][centroy] == 0) {
                    if (ComprobarVecinos(centrox,centroy,2,Tolerancia)){
                        pb = true;
                    }else {
                        pb = false;
                    }
                }else{
                    pb = false;
                    permiso++;
                }
                if (Tablero[centrox][centroy + 1] == 0) {
                    if (ComprobarVecinos(centrox,centroy,3,Tolerancia)){
                        pc = true;
                    }else {
                        pc = false;
                    }
                }else{
                    pc = false;
                    permiso++;
                }
                if (Tablero[centrox - 1][centroy] == 0) {
                    if (ComprobarVecinos(centrox,centroy,4,Tolerancia)){
                        pd = true;
                    }else {
                        pd = false;
                    }
                }else{
                    pd = false;
                    permiso++;
                }

                Log.i("Permiso = "+permiso,"Tolerancia = "+Tolerancia);
                if(permiso<=Tolerancia&&!(!pa&&!pb&&!pc&&!pd)) {


                    while (Terminado) {
                        Terminado = false;
                        int aux = (int) Math.floor((Math.random() * 4) + 1);
                        switch (aux) {
                            case 1: {
                                if (pa) {
                                    Tablero[centrox][centroy - 1] = Turno;
                                    yaTiro2 = true;
                                    Log.i("lugar de tiro",""+centrox+"-"+centroy);
                                }else {
                                    Terminado = true;
                                }
                            }
                            break;
                            case 2: {
                                if (pb) {
                                    Tablero[centrox + 1][centroy] = Turno;
                                    yaTiro2 = true;
                                    Log.i("lugar de tiro",""+centrox+"-"+centroy);
                                }else {
                                    Terminado = true;
                                }
                            }
                            break;
                            case 3: {
                                if (pc) {
                                    Tablero[centrox][centroy + 1] = Turno;
                                    yaTiro2 = true;
                                    Log.i("lugar de tiro",""+centrox+"-"+centroy);
                                }else {
                                    Terminado = true;
                                }
                            }
                            break;
                            case 4: {
                                if (pd) {
                                    Tablero[centrox - 1][centroy] = Turno;
                                    yaTiro2 = true;
                                    Log.i("lugar de tiro",""+centrox+"-"+centroy);
                                }else {
                                    Terminado = true;
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    public boolean ComprobarVecinos(int x, int y, int lugar, int toler){
        int Limite = Dimencion*2;
        int ContadorLineas=0;
        switch (lugar){
            case 1:{
                y-=2;
            }break;
            case 2:{
                x+=2;
            }break;
            case 3:{
                y+=2;
            }break;
            case 4:{
                x-=2;
            }break;
        }
        if(x>0&&x<Limite&&y>0&&y<Limite){
            if (Tablero[x][y - 1] > 0) {
                ContadorLineas++;
            }
            if (Tablero[x + 1][y] > 0) {
                ContadorLineas++;
            }
            if (Tablero[x][y + 1] > 0) {
                ContadorLineas++;
            }
            if (Tablero[x - 1][y] > 0) {
                ContadorLineas++;
            }
            return ContadorLineas <= toler;
        }else {
            return true;
        }
    }

    //Termina inteligencia del juego

    public void ControlLineas(){
        Log.i(" Refrescar Lineas: ","(Control Lineas)");
        //primeros 4
        ImageButton iba=(ImageButton)findViewById(R.id.l1t0);
        iba.setImageResource(color(ControlLineas2(Tablero[1][0]),1));

        iba=(ImageButton)findViewById(R.id.l3t0);
        iba.setImageResource(color(ControlLineas2(Tablero[3][0]),1));

        iba=(ImageButton)findViewById(R.id.l5t0);
        iba.setImageResource(color(ControlLineas2(Tablero[5][0]),1));

        iba=(ImageButton)findViewById(R.id.l7t0);
        iba.setImageResource(color(ControlLineas2(Tablero[7][0]),1));
        //terceros 4
        iba=(ImageButton)findViewById(R.id.l1t2);
        iba.setImageResource(color(ControlLineas2(Tablero[1][2]),1));

        iba=(ImageButton)findViewById(R.id.l3t2);
        iba.setImageResource(color(ControlLineas2(Tablero[3][2]),1));

        iba=(ImageButton)findViewById(R.id.l5t2);
        iba.setImageResource(color(ControlLineas2(Tablero[5][2]),1));

        iba=(ImageButton)findViewById(R.id.l7t2);
        iba.setImageResource(color(ControlLineas2(Tablero[7][2]),1));
        //quintos 4
        iba=(ImageButton)findViewById(R.id.l1t4);
        iba.setImageResource(color(ControlLineas2(Tablero[1][4]),1));

        iba=(ImageButton)findViewById(R.id.l3t4);
        iba.setImageResource(color(ControlLineas2(Tablero[3][4]),1));

        iba=(ImageButton)findViewById(R.id.l5t4);
        iba.setImageResource(color(ControlLineas2(Tablero[5][4]),1));

        iba=(ImageButton)findViewById(R.id.l7t4);
        iba.setImageResource(color(ControlLineas2(Tablero[7][4]),1));
        //septimos 4
        iba=(ImageButton)findViewById(R.id.l1t6);
        iba.setImageResource(color(ControlLineas2(Tablero[1][6]),1));

        iba=(ImageButton)findViewById(R.id.l3t6);
        iba.setImageResource(color(ControlLineas2(Tablero[3][6]),1));

        iba=(ImageButton)findViewById(R.id.l5t6);
        iba.setImageResource(color(ControlLineas2(Tablero[5][6]),1));

        iba=(ImageButton)findViewById(R.id.l7t6);
        iba.setImageResource(color(ControlLineas2(Tablero[7][6]),1));

        //novenos 4
        iba=(ImageButton)findViewById(R.id.l1t8);
        iba.setImageResource(color(ControlLineas2(Tablero[1][8]),1));

        iba=(ImageButton)findViewById(R.id.l3t8);
        iba.setImageResource(color(ControlLineas2(Tablero[3][8]),1));

        iba=(ImageButton)findViewById(R.id.l5t8);
        iba.setImageResource(color(ControlLineas2(Tablero[5][8]),1));

        iba=(ImageButton)findViewById(R.id.l7t8);
        iba.setImageResource(color(ControlLineas2(Tablero[7][8]),1));

        //////////////////////////////////
        //////////////////////////////////
        //segundos 5
        iba=(ImageButton)findViewById(R.id.l0t1);
        iba.setImageResource(color(ControlLineas2(Tablero[0][1]),2));

        iba=(ImageButton)findViewById(R.id.l2t1);
        iba.setImageResource(color(ControlLineas2(Tablero[2][1]),2));

        iba=(ImageButton)findViewById(R.id.l4t1);
        iba.setImageResource(color(ControlLineas2(Tablero[4][1]),2));

        iba=(ImageButton)findViewById(R.id.l6t1);
        iba.setImageResource(color(ControlLineas2(Tablero[6][1]),2));

        iba=(ImageButton)findViewById(R.id.l8t1);
        iba.setImageResource(color(ControlLineas2(Tablero[8][1]),2));
        //cuartos 5
        iba=(ImageButton)findViewById(R.id.l0t3);
        iba.setImageResource(color(ControlLineas2(Tablero[0][3]),2));

        iba=(ImageButton)findViewById(R.id.l2t3);
        iba.setImageResource(color(ControlLineas2(Tablero[2][3]),2));

        iba=(ImageButton)findViewById(R.id.l4t3);
        iba.setImageResource(color(ControlLineas2(Tablero[4][3]),2));

        iba=(ImageButton)findViewById(R.id.l6t3);
        iba.setImageResource(color(ControlLineas2(Tablero[6][3]),2));

        iba=(ImageButton)findViewById(R.id.l8t3);
        iba.setImageResource(color(ControlLineas2(Tablero[8][3]),2));
        //sextos 5
        iba=(ImageButton)findViewById(R.id.l0t5);
        iba.setImageResource(color(ControlLineas2(Tablero[0][5]),2));

        iba=(ImageButton)findViewById(R.id.l2t5);
        iba.setImageResource(color(ControlLineas2(Tablero[2][5]),2));

        iba=(ImageButton)findViewById(R.id.l4t5);
        iba.setImageResource(color(ControlLineas2(Tablero[4][5]),2));

        iba=(ImageButton)findViewById(R.id.l6t5);
        iba.setImageResource(color(ControlLineas2(Tablero[6][5]),2));

        iba=(ImageButton)findViewById(R.id.l8t5);
        iba.setImageResource(color(ControlLineas2(Tablero[8][5]),2));
        //octavos 5
        iba=(ImageButton)findViewById(R.id.l0t7);
        iba.setImageResource(color(ControlLineas2(Tablero[0][7]),2));

        iba=(ImageButton)findViewById(R.id.l2t7);
        iba.setImageResource(color(ControlLineas2(Tablero[2][7]),2));

        iba=(ImageButton)findViewById(R.id.l4t7);
        iba.setImageResource(color(ControlLineas2(Tablero[4][7]),2));

        iba=(ImageButton)findViewById(R.id.l6t7);
        iba.setImageResource(color(ControlLineas2(Tablero[6][7]),2));

        iba=(ImageButton)findViewById(R.id.l8t7);
        iba.setImageResource(color(ControlLineas2(Tablero[8][7]),2));

    }

    public String ControlLineas2(int busqueda){
        switch (busqueda){
            case 0:{
                return "trans";
            }
            case 1:{
                return color;
            }
            case 2:{
                return color2;
            }
        }
        return "trans";
    }

    public void ContarPuntos(){
        Puntos1=0;
        Puntos2=0;
        for (int x=0;x<TableroPuntos.length;x++){
            for (int y=0;y<TableroPuntos.length;y++){
                if(TableroPuntos[x][y]==1){
                    Puntos1++;
                }
                if(TableroPuntos[x][y]==2){
                    Puntos2++;
                }
            }
        }
    }

    public int BuscarPuntos(int p){
        int p1=0,p2=0;
        for (int x=0;x<TableroPuntos.length;x++){
            for (int y=0;y<TableroPuntos.length;y++){
                if(TableroPuntos[x][y]==1){
                    p1++;
                }
                if(TableroPuntos[x][y]==2){
                    p2++;
                }
            }
        }
        if(p==1){
            return p1;
        }else{
            return p2;
        }
    }

    public void MostrarTablero(){
        String finala="-\n";
        int par=Dimencion*2+1;
        for (int x=0;x<par;x++){
            for (int y=0;y<par;y++){
                finala+="-"+Tablero[x][y];
            }
            finala+="\n";
        }
        Log.d("*****", "\n"+finala);
    }

    public void ComprobarResultados(){
        int j1=0,j2=0;
        int total=0;

        for (int x=0;x<TableroPuntos.length;x++){
            for (int y=0;y<TableroPuntos.length;y++){
                if(TableroPuntos[x][y]!=0){
                    total++;
                }
            }
        }

        try {
            OutputStreamWriter fout = new OutputStreamWriter(openFileOutput("Puntos1.txt", Context.MODE_PRIVATE));
            fout.write(""+BuscarPuntos(1));
            fout.close();
            fout = new OutputStreamWriter(openFileOutput("Puntos2.txt", Context.MODE_PRIVATE));
            fout.write(""+BuscarPuntos(2));
            fout.close();

        } catch (Exception exx) {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }

        TextView tw=(TextView)findViewById(R.id.puntos1);
        tw.setText(""+BuscarPuntos(1));
        tw=(TextView)findViewById(R.id.puntos2);
        tw.setText(""+BuscarPuntos(2));

        if((Dimencion*Dimencion)==total&&!PartidaTerminada){
            PartidaTerminada=true;
            Intent intent = new Intent(this, ResultadoPartida.class);
            startActivity(intent);
        }
    }
    //----------------------------INICIA SEGUIR CONEXION-------------------------------
    private Handler mHandler = new Handler();
    ConexionU conexion2;
    boolean encontrado=false;
    String puertoL,puertoS,IPL,IPS;
    int PuertoL=0,PuertoS=0;
    boolean unavez=true;
    boolean respuestaConexion=true;
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
        conexion2.establecerPuertoLocal(PuertoL-1);
        conexion2.servidor();


        if(PuertoL>PuertoS){
            Turno=1;
            conexion2.MiTurno=true;
            turno1.setVisibility(View.VISIBLE);
            turno2.setVisibility(View.INVISIBLE);
        }
        else {
            Turno=2;
            conexion2.MiTurno=false;
            turno1.setVisibility(View.INVISIBLE);
            turno2.setVisibility(View.VISIBLE);
        }


        mHandler.postDelayed(new Runnable() {
            public void run() {
                conexion2.cliente(IPS, PuertoS-1);
                conexion2.start();
                conexion2.sCliente.enviarMSG("-CONEXION CORRECTA AL SERVIDOR");
                Log.d("!!!!!", "ENVIOS AL SERVIDOR");
                conexion2.establecerConexionAlServidor();
                conexion2.establecerConexionServidorStart();
            }
        }, 700);

        mHandler.postDelayed(new Runnable() {
            public void run() {
                escuchadorConexion();
            }
        }, 1700);
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
        contadorRespuestaConexion-=100;
        conexion2.ServidorEnviarMensaje("-C Conectado");
        if(conexion2.MiTurno){

            ControlLineas();
            ComprobarResultados();
            Turno=1;
            turno1.setVisibility(View.VISIBLE);
            turno2.setVisibility(View.INVISIBLE);
            conexion2.MiTurno=false;
        }
        if(conexion2.nuevasCoordenadas){
            conexion2.nuevasCoordenadas=false;
            Log.d("XXXXX", "Coordenada: "+conexion2.coordenadasX);
            Log.d("YYYYY", "Coordenada: "+conexion2.coordenadasY);
            Tablero[conexion2.coordenadasX][conexion2.coordenadasY] = 2;
            MostrarTablero();
        }
        ControlLineas();
        ComprobarResultados();
        if(Turno==2){
            BuscarCuadro(color2);
        }

        if(conexion2.renovarConexion){
            conexion2.renovarConexion=false;
            contadorRespuestaConexion=500;
        }
        if (contadorRespuestaConexion<0){
            unavez=false;
            //mostrar ventana de perdida de conexion
            Intent intent = new Intent(this, ConexionPerdida.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        conexion2.ServidorEnviarMensaje("-C Conectado");
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
