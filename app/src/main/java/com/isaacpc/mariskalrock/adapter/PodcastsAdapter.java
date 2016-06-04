package com.isaacpc.mariskalrock.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isaacpc.mariskalrock.R;
import com.isaacpc.mariskalrock.bd.PodcastEntity;
import com.isaacpc.mariskalrock.log.Log;

public class PodcastsAdapter extends MRBaseAdapter {

    private static final String LOG_TAG = "ListaGenericaAdapter";

    private final List<PodcastEntity> itemList;

    public PodcastsAdapter(Context context, List<PodcastEntity> itemList) {

        super(context);
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        Log.d(LOG_TAG, itemList.get(position).getTitle());

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_generico, null);
            holder = new ViewHolder();

            holder.layoutImagen = (RelativeLayout) convertView.findViewById(R.id.frameImagenNoticia);
            holder.titulo = (TextView) convertView.findViewById(R.id.txtTitulo);
            holder.fecha = (TextView) convertView.findViewById(R.id.txtFecha);
            holder.descripcion = (TextView) convertView.findViewById(R.id.txtDescripcion);
            holder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
            holder.pbThumbnail = (ProgressBar) convertView.findViewById(R.id.pbThumbnail);
            holder.btShow = (TextView) convertView.findViewById(R.id.btShow);
            holder.btShare = (TextView) convertView.findViewById(R.id.btShare);

            convertView.setTag(R.string.imagen_item, itemList.get(position).getLink());
            Log.d(LOG_TAG, "link: " + position + " " + itemList.get(position).getLink());

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            convertView.setTag(R.string.imagen_item, itemList.get(position).getLink());
        }

        final PodcastEntity item = itemList.get(position);

        holder.titulo.setText(item.getTitle());
        holder.fecha.setText(item.getDateString());
        holder.descripcion.setText(item.getDescription());

        holder.btShow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                goToContent(item.getLink());
            }
        });

        holder.btShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                share(item.getLink());
            }
        });

        return convertView;
    }

    static class ViewHolder {
        RelativeLayout layoutImagen;
        TextView titulo;
        TextView fecha;
        TextView descripcion;
        ImageView thumbnail;
        ProgressBar pbThumbnail;
        TextView btShow;
        TextView btShare;
    }
}