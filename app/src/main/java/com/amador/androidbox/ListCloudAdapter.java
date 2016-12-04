package com.amador.androidbox;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.Metadata;

import java.util.ArrayList;

/**
 * Created by amador on 3/12/16.
 */

public class ListCloudAdapter extends ArrayAdapter<Metadata> {

    private Context context;




    public ListCloudAdapter(Context context, ArrayList<Metadata> metadatas) {
        super(context, R.layout.item_files, metadatas);
        this.context = context;


    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        FilesHolder holder;

        if(vi == null){

            holder = new FilesHolder();
            vi = LayoutInflater.from(this.context).inflate(R.layout.item_files, null);
            holder.imv = (ImageView)vi.findViewById(R.id.imvLocalFiles);
            holder.txv = (TextView)vi.findViewById(R.id.txvNameLocalFile);
            vi.setTag(holder);

        }else{

            holder = (FilesHolder)vi.getTag();
        }

        if(getItem(position) instanceof FileMetadata){

            holder.imv.setImageResource(R.drawable.file);

        }else if(getItem(position) instanceof FolderMetadata){

            holder.imv.setImageResource(R.drawable.folder);
        }

        holder.txv.setText(getItem(position).getName());

        notifyDataSetChanged();
        return vi;
    }

    class FilesHolder{

        ImageView imv;
        TextView txv;

    }
}
