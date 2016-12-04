package com.amador.androidbox;

import android.os.AsyncTask;
import android.os.Environment;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Amador Fernandez Gonzalez
 *         <p>
 *         Clase asincrona para descargar los archivos desde la nube al dispositivo
 */

public class DowloadFile extends AsyncTask<FileMetadata, Void, Void> {

    private DbxClientV2 dbxClientV2;
    private OnDowloadListener listener;

    /**
     * Interfaz que implementatara todo aquel que quiera saber cuando finaliza
     * la descarga y obtener asi el mensaje asociado
     */
    public interface OnDowloadListener {

        void onDowloadComplete(String msg);

    }

    /**
     * Selector del escuchador
     **/
    public void setDowloadListener(OnDowloadListener listener) {

        this.listener = listener;
    }

    public DowloadFile(DbxClientV2 dbxClientV2) {


        this.dbxClientV2 = dbxClientV2;
    }

    @Override
    protected Void doInBackground(FileMetadata... fileMetadatas) {

        FileMetadata metadata = fileMetadatas[0];
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), metadata.getName());
        BufferedOutputStream bf = null;

        try {

            bf = new BufferedOutputStream(new FileOutputStream(file));
            dbxClientV2.files().download(metadata.getPathLower(), metadata.getRev()).download(bf);
            if (listener != null) {

                listener.onDowloadComplete("Se descargo el archivo en: " + Environment.getExternalStorageDirectory().getAbsolutePath());
            }

        } catch (Exception e) {

            if (listener != null) {

                listener.onDowloadComplete("Se produjo el siguiente error: " + e.getMessage());
            }
        } finally {

            if (bf != null) {

                try {
                    bf.close();
                } catch (IOException e) {
                    //
                }
            }
        }

        return null;
    }


}
