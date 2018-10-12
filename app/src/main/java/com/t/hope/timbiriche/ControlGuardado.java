package com.t.hope.timbiriche;

import android.content.Intent;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class ControlGuardado extends Thread{


    String ruta_archivo = "almacenamiento.obj";

    Almacenamiento mi_archivo =new Almacenamiento("almacenamiento.obj");

    ObjectOutputStream file1;
    ObjectInputStream file2;

    Intent intento;

    ControlGuardado(Intent a){
        intento = a;
        run();

    }

    @Override
    public void run() {
        //super.run();
        while (true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                guardar();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    public void guardar() throws IOException {
        try {
            file1 = new ObjectOutputStream(new FileOutputStream( this.ruta_archivo ));
        } catch (IOException ex) {

        }
        try {

            file1.writeObject(intento);
            file1.close();

        } catch (IOException ex) {

        }
        mi_archivo.escribirObjeto((Object) intento);
    }
}
