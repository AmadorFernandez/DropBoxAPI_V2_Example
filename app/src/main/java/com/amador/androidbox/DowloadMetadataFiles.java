package com.amador.androidbox;

import android.os.AsyncTask;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;

import java.util.ArrayList;


/**
 * @author Amador Fernandez Gonzalez
 *         <p>
 *         Clase asincrona que obtiene los metadatos de la cuenta de DropBox
 */

public class DowloadMetadataFiles extends AsyncTask {

    private DbxClientV2 clientV2;
    private ArrayList<Metadata> metadataArrayList;
    private OnDowloadListener listener;
    private String path;

    public interface OnDowloadListener {

        void finishDowload(ArrayList<Metadata> metadatas);
    }

    public void setDowloadListener(OnDowloadListener listener) {

        this.listener = listener;
    }

    public DowloadMetadataFiles(DbxClientV2 clientV2, String path) {

        this.metadataArrayList = new ArrayList<Metadata>();
        this.clientV2 = clientV2;
        this.path = path;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if (listener != null) {

            listener.finishDowload(metadataArrayList);
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        try {

            ListFolderResult listFolderResult = clientV2.files().listFolder(path);
            metadataArrayList.addAll(listFolderResult.getEntries());


        } catch (DbxException e) {
            e.printStackTrace();
        }

        return null;
    }
}
