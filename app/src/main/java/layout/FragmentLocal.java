package layout;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
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
import com.amador.androidbox.ListLocalAdapter;
import com.amador.androidbox.MainActivity;
import com.amador.androidbox.Preferences;
import com.amador.androidbox.R;
import com.amador.androidbox.UploadFiles;
import com.dropbox.core.v2.DbxClientV2;

/**
 * @author Amador Fernandez Gonzalez
 *         <p>
 *         Clase para manejar el fragment que permite explorar los archivos del dispositivo
 */
public class FragmentLocal extends Fragment {


    private ListView listView;
    private ListLocalAdapter adapter;
    private ImageView imageView;
    private TextView textView;
    private MainActivity mainActivity;
    private String pathActual;
    private String pathAnterior;
    private Context context;
    private String token;
    private ProgressDialog progressBar;
    //Manejador para el fin de la descarga
    private UploadFiles.UploadFinish listener = new UploadFiles.UploadFinish() {
        @Override
        public void onUploadComplete(String msg) {

            progressBar.dismiss();
            mainActivity.setResultMsg(msg);
        }
    };


    public FragmentLocal() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.header_list, null);
        final View rootView = inflater.inflate(R.layout.fragment_local_files, container, false);
        listView = (ListView) rootView.findViewById(R.id.listLocal);
        imageView = (ImageView) view.findViewById(R.id.imvReturn);
        textView = (TextView) view.findViewById(R.id.txvPath);
        pathActual = Environment.getExternalStorageDirectory().getAbsolutePath();
        listView.addHeaderView(view);
        context = rootView.getContext();
        token = Preferences.getInstance(context).getToken();
        adapter = new ListLocalAdapter(rootView.getContext(), pathActual);
        listView.setAdapter(adapter);
        textView.setText(pathActual);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pathAnterior != null) {

                    pathActual = pathAnterior;
                    adapter = new ListLocalAdapter(rootView.getContext(), pathActual);
                    listView.setAdapter(adapter);
                    int fin = pathActual.length() - new StringBuilder(pathActual).reverse().indexOf("/");
                    pathAnterior = pathActual.substring(0, fin);
                    textView.setText(pathActual);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i > 0) {

                    if (adapter.getItem(i - 1).isDirectory()) {

                        pathAnterior = pathActual;
                        pathActual = adapter.getItem(i - 1).getAbsolutePath();
                        adapter = new ListLocalAdapter(rootView.getContext(), pathActual);
                        listView.setAdapter(adapter);
                        textView.setText(pathActual);

                    } else if (adapter.getItem(i - 1).isFile()) {

                        if (Conexion.getInstance(context).isNetworkAvailable()) {

                            DbxClientV2 client = Client.getInstance(token, context).getClientV2();
                            UploadFiles uploadFiles = new UploadFiles(client, adapter.getItem(i - 1), rootView.getContext());
                            uploadFiles.setListener(listener);
                            lanzarProgreso();
                            uploadFiles.execute();

                        } else {

                            mainActivity.setResultMsg("No hay conexión disponible");

                        }

                    }
                }
            }
        });

        return rootView;
    }


    private void lanzarProgreso() {

        progressBar = new ProgressDialog(context);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setCancelable(false);
        progressBar.setMessage("Subiendo archivo");
        progressBar.show();
    }

}
