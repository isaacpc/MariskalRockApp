package com.isaacpc.mariskalrock.adapter;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.isaacpc.mariskalrock.R;
import com.isaacpc.mariskalrock.bd.VideoEntity;
import com.isaacpc.mariskalrock.log.Log;
import com.isaacpc.mariskalrock.utils.DateUtils;

public class VideoAdapter extends MRBaseAdapter {

    static SimpleDateFormat FORMATO_PANTALLA = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.ENGLISH);

    private static final String LOG_TAG = "VideoAdapter";

    private final List<VideoEntity> itemList;
    public VideoAdapter(final Context context, final List<VideoEntity> itemList) {
        super(context);
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(final int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder holder;

        Log.d(LOG_TAG, itemList.get(position).getTitle());

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_video, null);
            holder = new ViewHolder();

            holder.titulo = (TextView) convertView.findViewById(R.id.userVideoTitleTextView);
            holder.contador = (TextView) convertView.findViewById(R.id.viewCount);
            holder.duracion = (TextView) convertView.findViewById(R.id.duration);
            holder.fecha = (TextView) convertView.findViewById(R.id.viewDate);
            holder.descripcion = (TextView) convertView.findViewById(R.id.txtDescripcion);
            holder.thumbnail = (ImageView) convertView.findViewById(R.id.videoThumb);
            holder.pbThumbnail = (ProgressBar) convertView.findViewById(R.id.pbThumbnail);
            holder.btShow = (TextView) convertView.findViewById(R.id.btShow);
            holder.btShare = (TextView) convertView.findViewById(R.id.btShare);

            convertView.setTag(R.string.imagen_item, itemList.get(position).getUrl());
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get a single video from our list
        final VideoEntity item = itemList.get(position);

        holder.titulo.setText(item.getTitle().toUpperCase(Locale.ENGLISH));
        holder.contador.setText(item.getViewCount() + " " + convertView.getContext().getResources().getString(R.string.reproducciones_subtitle));
        holder.duracion.setText(item.getDuration());
        holder.fecha.setText(item.getUploaded());
        holder.descripcion.setText(item.getDescription());

        // conversion del la duracion del video
        if (item.getDuration() != null && item.getDuration().length() > 0) {
            final String dateMinutes = DateUtils.secondsToMinutes(Integer.valueOf(item.getDuration()));
            holder.duracion.setText(dateMinutes);
        } else {
            holder.duracion.setText(item.getDuration());
        }

        // se recicla el AQuery.
//                aq = aq.recycle(convertView);

        //gestiona la imagen
        manageThumbnail(holder.thumbnail, holder.pbThumbnail, position, convertView, parent, item.getImageURL());

        holder.btShow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                goToContent(item.getUrl());
            }
        });

        holder.btShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                share(item.getUrl());
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView titulo;
        TextView contador;
        TextView duracion;
        TextView fecha;
        TextView descripcion;
        ImageView thumbnail;
        ProgressBar pbThumbnail;
        TextView btShow;
        TextView btShare;
    }
}
