package com.amador.androidbox;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Amador Fernandez Gonzalez
 *         <p>
 *         Clase asincrona para la subida de archivos a Dropbox
 */

public class UploadFiles extends AsyncTask {

    private DbxClientV2 dbxClient;
    private File file;
    private Context context;
    private UploadFinish listener;

    public interface UploadFinish {

        void onUploadComplete(String msg);


    }

    public void setListener(UploadFinish listener) {

        this.listener = listener;
    }

    public UploadFiles(DbxClientV2 dbxClient, File file, Context context) {
        this.dbxClient = dbxClient;
        this.file = file;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            // Upload to Dropbox
            InputStream inputStream = new FileInputStream(file);
            dbxClient.files().uploadBuilder("/" + file.getName())
                    .withMode(WriteMode.OVERWRITE)
                    .uploadAndFinish(inputStream);


            Log.d("Upload Status", "Success");
        } catch (DbxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (listener != null) {

            listener.onUploadComplete("Archivo subido con exito");
        }

    }
}
