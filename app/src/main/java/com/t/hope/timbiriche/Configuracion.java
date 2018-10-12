package com.t.hope.timbiriche;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class Configuracion extends ActionBarActivity {

    BufferedReader fin=null;
    String Color="";
    String Name="";
    int ColorNum=0;


    public void CambiarNombre(View view){
        EditText Nombre2=(EditText)findViewById(R.id.editarnombre);
        String aux = Nombre2.getText().toString();
        try {
            OutputStreamWriter nombre = new OutputStreamWriter(openFileOutput("NombreJugador1.txt", Context.MODE_PRIVATE));
            nombre.write(aux);
            nombre.close();
        } catch (Exception exx) {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }
        CambiarColor(view);
    }

    public void CambiarColorPrueba(View view){
        ImageButton ccolor=(ImageButton)findViewById(R.id.pcolor);
        ColorNum++;
        if(ColorNum>=10){
            ColorNum=1;
        }
        ccolor.setImageResource(color(RegresarString(ColorNum)));

    }

    public void CambiarColor(View view){

        try {
            OutputStreamWriter fout = new OutputStreamWriter(openFileOutput("ColorJugador1.txt", Context.MODE_PRIVATE));
            fout.write(RegresarString(ColorNum));
            fout.close();
        } catch (Exception exx) {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }
        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
        this.finish();
    }

    public String RegresarString (int numm){
        switch (numm){
            case 1:{
                return "azul";
            }
            case 2:{
                return "azul2";
            }
            case 3:{
                return "amarilla";
            }
            case 4:{
                return "morada";
            }
            case 5:{
                return "naranja";
            }
            case 6:{
                return "roja";
            }
            case 7:{
                return "rosa";
            }
            case 8:{
                return "verde";
            }
            case 9:{
                return "verde2";
            }
        }
        return  "azul";
    }

    public int BuscarColorNum(String colorLinea){
        if(colorLinea.equalsIgnoreCase("azul")){
            return 1;
        }
        if(colorLinea.equalsIgnoreCase("azul2")){
            return 2;
        }
        if(colorLinea.equalsIgnoreCase("amarilla")){
            return 3;
        }
        if(colorLinea.equalsIgnoreCase("morada")){
            return 4;
        }
        if(colorLinea.equalsIgnoreCase("naranja")){
            return 5;
        }
        if(colorLinea.equalsIgnoreCase("roja")){
            return 6;
        }
        if(colorLinea.equalsIgnoreCase("rosa")){
            return 7;
        }
        if(colorLinea.equalsIgnoreCase("verde")){
            return 8;
        }
        if(colorLinea.equalsIgnoreCase("verde2")){
            return 9;
        }

        return  R.mipmap.colorazul;

    }

    public int color(String colorLinea){
        if(colorLinea.equalsIgnoreCase("azul")){
            return R.mipmap.colorazul;
        }
        if(colorLinea.equalsIgnoreCase("azul2")){
            return R.mipmap.colorazul2;
        }
        if(colorLinea.equalsIgnoreCase("amarilla")){
            return R.mipmap.coloramarillo;
        }
        if(colorLinea.equalsIgnoreCase("morada")){
            return R.mipmap.colormorado;
        }
        if(colorLinea.equalsIgnoreCase("naranja")){
            return R.mipmap.colornaranja;
        }
        if(colorLinea.equalsIgnoreCase("roja")){
            return R.mipmap.colorrojo;
        }
        if(colorLinea.equalsIgnoreCase("rosa")){
            return R.mipmap.colorrosa;
        }
        if(colorLinea.equalsIgnoreCase("verde")){
            return R.mipmap.colorverde;
        }
        if(colorLinea.equalsIgnoreCase("verde2")){
            return R.mipmap.colorverde2;
        }

        return  R.mipmap.colorazul;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        try{
            fin = new BufferedReader(new InputStreamReader(openFileInput("ColorJugador1.txt")));
            Color = fin.readLine();
            fin.close();
            fin = new BufferedReader(new InputStreamReader(openFileInput("NombreJugador1.txt")));
            Name = fin.readLine();
            fin.close();
            TextView Nombre2=(TextView)findViewById(R.id.editarnombre);
            Nombre2.setText(Name);
            ImageButton ccolor=(ImageButton)findViewById(R.id.pcolor);
            ccolor.setImageResource(color(Color));
            ColorNum=BuscarColorNum(Color);
        }
        catch (Exception ex) {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_configuracion, menu);
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
}
