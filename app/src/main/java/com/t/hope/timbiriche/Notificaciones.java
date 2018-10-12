package com.t.hope.timbiriche;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Hope on 17/05/2015.
 */
public class Notificaciones {
    Context context;
    Toast toast;
    CharSequence text;
    int duration;
    Notificaciones(Context AC,String t){
        context = AC;
        text = t;
        duration = Toast.LENGTH_LONG;
        toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    public void mostrar(){
        toast.show();
    }
}
