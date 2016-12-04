package com.amador.androidbox;

import android.content.Context;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.http.OkHttp3Requestor;
import com.dropbox.core.v2.DbxClientV2;

/**
 * @author Amador Fernandez Gonzalez
 *         <p>
 *         Clase singleton que representa al cliente en la aplicación
 */
public class Client {

    private DbxClientV2 clientV2;
    private static Client instance;
    private static Context context;

    public DbxClientV2 getClientV2() {

        return clientV2;
    }

    public static Client getInstance(String token, Context cont) {

        if (instance == null) {

            instance = new Client(token);
        }

        context = cont;
        return instance;
    }

    private Client(String token) {

        DbxRequestConfig requestConfig = DbxRequestConfig.newBuilder(
                "AndroidBox").withHttpRequestor(OkHttp3Requestor.INSTANCE).build();

        if (token != null) {
            clientV2 = new DbxClientV2(requestConfig, token);
        } else {

            clientV2 = new DbxClientV2(requestConfig, Preferences.getInstance(context).getToken());
        }

    }
}
