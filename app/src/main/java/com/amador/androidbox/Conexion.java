package com.amador.androidbox;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * @author Amador Fernandez Gonzalez
 *         <p>
 *         Clase Singleton que representa a la conexión y proporciona metodos
 *         para la obtencion de la instancia y verificar si el dispositivo esta
 *         o no conectado a la red</p>
 */

public class Conexion {

    private static Conexion instance;
    private Context context;

    public static Conexion getInstance(Context context) {

        if (instance == null) {

            instance = new Conexion(context);
        }

        return instance;
    }

    private Conexion(Context context) {

        this.context = context;
    }

    //Lo tipico
    public boolean isNetworkAvailable() {
        boolean result = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())
            result = true;

        return result;

    }
}
