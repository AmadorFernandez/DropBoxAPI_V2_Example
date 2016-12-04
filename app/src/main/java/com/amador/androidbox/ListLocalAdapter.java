package com.amador.androidbox;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

/**
 * @author Amador Fernandez Gonzalez
 *         <p>
 *         Simple adaptafor para los archivos locales. Su constructor permite la
 *         extración de los archivos que esten en el path que se le suministra.
 */

public class ListLocalAdapter extends ArrayAdapter<File> {

    private Context context;
    private String path;

    public ListLocalAdapter(Context context, String path) {
        super(context, R.layout.item_files);
        this.context = context;
        this.path = path;
        extractFiles();


    }

    private void extractFiles() {

        File file = new File(path);
        File[] files = file.listFiles();
        clear();

        if (files != null) {

            for (File tmp : files) {

                add(tmp);
            }


        }

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        FilesHolder holder;

        if (vi == null) {

            holder = new FilesHolder();
            vi = LayoutInflater.from(this.context).inflate(R.layout.item_files, null);
            holder.imv = (ImageView) vi.findViewById(R.id.imvLocalFiles);
            holder.txv = (TextView) vi.findViewById(R.id.txvNameLocalFile);
            vi.setTag(holder);

        } else {

            holder = (FilesHolder) vi.getTag();
        }

        if (getItem(position).isDirectory()) {

            holder.imv.setImageResource(R.drawable.folder);

        } else {

            holder.imv.setImageResource(R.drawable.file);

        }

        holder.txv.setText(getItem(position).getName());


        return vi;
    }

    class FilesHolder {

        ImageView imv;
        TextView txv;

    }
}
