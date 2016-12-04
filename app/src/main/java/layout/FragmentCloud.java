package layout;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amador.androidbox.Client;
import com.amador.androidbox.Conexion;
import com.amador.androidbox.DowloadFile;
import com.amador.androidbox.DowloadMetadataFiles;
import com.amador.androidbox.ListCloudAdapter;
import com.amador.androidbox.MainActivity;
import com.amador.androidbox.Preferences;
import com.amador.androidbox.R;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.Metadata;

import java.util.ArrayList;

/**
 * @author Amador Fernandez Gonzalez
 *
 * Clase para el fragment que permite explorar los archivos en la cuenta de
 * DropBox
 */
public class FragmentCloud extends Fragment {


    private ListView listView;
    private ListCloudAdapter adapter;
    private TextView textView;
    private DbxClientV2 clientV2;
    private MainActivity mainActivity;
    private DowloadMetadataFiles dowloadFiles;
    private DowloadMetadataFiles.OnDowloadListener listener;
    private Context context;
    private String token;
    private ImageView imageView;
    private String pathActual;
    private ProgressDialog progressDialog;
    private String pathAnterior;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
        mainActivity = (MainActivity)context;
    }

    public FragmentCloud() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.header_list, null);
        imageView = (ImageView)view.findViewById(R.id.imvReturn);
        textView = (TextView)view.findViewById(R.id.txvPath);
        final View rootView = inflater.inflate(R.layout.fragment_cloud, container, false);
        listView = (ListView)rootView.findViewById(R.id.listCloud);
        listView.addHeaderView(view);
        context = rootView.getContext();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Descargando...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pathActual = "";
        token = Preferences.getInstance(context).getToken();
        //Manejador para el fin de la descarga. Implementa clase anonima
        listener = new DowloadMetadataFiles.OnDowloadListener() {
            @Override
            public void finishDowload(ArrayList<Metadata> metadatas) {


                adapter = new ListCloudAdapter(rootView.getContext(), metadatas);
                listView.setAdapter(adapter);
            }
        };

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Conexion.getInstance(context).isNetworkAvailable()) {

                    pathAnterior = pathActual;
                    int fin = pathActual.length() - new StringBuilder(pathActual).reverse().indexOf("/") - 1;
                    pathActual = pathActual.substring(0, fin);
                    if (pathActual.equals("/")) {

                        pathActual = "";
                        textView.setText("/");
                    }
                    dowloadFiles = new DowloadMetadataFiles(clientV2, pathActual);
                    dowloadFiles.setDowloadListener(listener);
                    dowloadFiles.execute();
                    textView.setText(pathActual);

                }else {

                    mainActivity.setResultMsg("No hay una conexion a internet");
                }

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(i > 0){

                    if(Conexion.getInstance(context).isNetworkAvailable()) {

                        if (adapter.getItem(i - 1) instanceof FolderMetadata) {

                            clientV2 = Client.getInstance(token, context).getClientV2();
                            pathActual = adapter.getItem(i - 1).getPathLower();
                            dowloadFiles = new DowloadMetadataFiles(clientV2, pathActual);
                            textView.setText(pathActual);
                            dowloadFiles.setDowloadListener(listener);
                            dowloadFiles.execute();

                        } else if (adapter.getItem(i - 1) instanceof FileMetadata) {

                            clientV2 = Client.getInstance(token, context).getClientV2();
                            pathActual = adapter.getItem(i - 1).getPathLower();
                            DowloadFile dowloadFile = new DowloadFile(clientV2);
                            DowloadFile.OnDowloadListener listenerDowload = new DowloadFile.OnDowloadListener() {
                                @Override
                                public void onDowloadComplete(String msg) {

                                    progressDialog.dismiss();
                                    mainActivity.setResultMsg(msg);

                                }
                            };
                            progressDialog.show();
                            dowloadFile.setDowloadListener(listenerDowload);
                            dowloadFile.execute((FileMetadata)adapter.getItem(i-1));

                        }

                    } else {

                        mainActivity.setResultMsg("No hay una conexion a internet");
                    }



                }
            }
        });

        textView.setText("/");
        clientV2 = Client.getInstance(token, context).getClientV2();
        dowloadFiles = new DowloadMetadataFiles(clientV2, pathActual);
        dowloadFiles.setDowloadListener(listener);
        dowloadFiles.execute();
        return rootView;
    }



}
