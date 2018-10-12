package com.t.hope.timbiriche;

import android.app.ActionBar;
import android.app.Notification;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.AccessibleObject;
import java.util.Objects;

public class Almacenamiento extends ActionBarActivity {

    String Ruta;

    Almacenamiento(String r){
        Ruta=r;
    }

    void escribir(String texto) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(openFileOutput(Ruta, Context.MODE_PRIVATE));
        out.write(texto);
        out.close();
    }
    public String leer() throws IOException {
        BufferedReader archivo = new BufferedReader(new InputStreamReader(openFileInput(Ruta)));
        String aux=archivo.readLine();
        archivo.close();
        return aux;
    }
    void escribirObjeto(Object obj) throws IOException {
        ObjectOutputStream archivo  = new ObjectOutputStream(new FileOutputStream( Ruta ));
        archivo.writeObject(obj);
        archivo.close();
    }
    public Object leerObjeto() throws IOException, ClassNotFoundException {
        ObjectInputStream archivo = new ObjectInputStream(new FileInputStream( Ruta ));
        Object aux=(Object) archivo.readObject();
        archivo.close();
        return aux;
    }
}