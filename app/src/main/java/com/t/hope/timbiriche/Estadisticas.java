package com.t.hope.timbiriche;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class Estadisticas extends ActionBarActivity {
    BufferedReader fin=null;
    String ganadas,perdidas,empatadas;
    TextView g,p,e;

    public void menuprincipal(View view){
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);
        g=(TextView)findViewById(R.id.pung);
        p=(TextView)findViewById(R.id.punp);
        e=(TextView)findViewById(R.id.pune);
        try{
            fin = new BufferedReader(new InputStreamReader(openFileInput("PartidasGanadas.txt")));
            fin.close();
        }
        catch (Exception ex) {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
            try {
                OutputStreamWriter fout = new OutputStreamWriter(openFileOutput("PartidasGanadas.txt", Context.MODE_PRIVATE));
                fout.write("0");
                fout.close();
                 fout = new OutputStreamWriter(openFileOutput("PartidasPerdidas.txt", Context.MODE_PRIVATE));
                fout.write("0");
                fout.close();
                 fout = new OutputStreamWriter(openFileOutput("PartidasEmpatadas.txt", Context.MODE_PRIVATE));
                fout.write("0");
                fout.close();
            } catch (Exception exx) {
                Log.e("Ficheros", "Error al escribir fichero a memoria interna");
            }
        }
        try{
            fin = new BufferedReader(new InputStreamReader(openFileInput("PartidasGanadas.txt")));
            ganadas = fin.readLine();
            fin.close();
            fin = new BufferedReader(new InputStreamReader(openFileInput("PartidasPerdidas.txt")));
            perdidas = fin.readLine();
            fin.close();
            fin = new BufferedReader(new InputStreamReader(openFileInput("PartidasEmpatadas.txt")));
            empatadas = fin.readLine();
            fin.close();
        }catch (Exception e){

        }
        g.setText(ganadas);
        p.setText(perdidas);
        e.setText(empatadas);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_estadisticas, menu);
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
