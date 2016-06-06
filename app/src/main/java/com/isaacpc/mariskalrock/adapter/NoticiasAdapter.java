package com.isaacpc.mariskalrock.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.isaacpc.mariskalrock.R;
import com.isaacpc.mariskalrock.bd.NoticiaEntity;
import com.isaacpc.mariskalrock.log.Log;

public class NoticiasAdapter extends MRBaseAdapter {

        private static final String LOG_TAG = "ListaGenericaAdapter";

        private final List<NoticiaEntity> itemList;

        public NoticiasAdapter(final Context context, final List<NoticiaEntity> itemList1) {
                super(context);
                itemList = itemList1;
        }

        @Override
        public int getCount() {
                return itemList.size();
        }

        @Override
        public  Object getItem(final int position) {

                return itemList.get(position);
        }

        @Override
        public  long getItemId(final int position) {
                return 0;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

                ViewHolder holder;
                Log.d(LOG_TAG, itemList.get(position).getTitle());

                if (convertView == null) {
                        convertView = inflater.inflate(R.layout.item_generico, null);
                        holder = new ViewHolder();

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

                final NoticiaEntity item = itemList.get(position);

                holder.titulo.setText(item.getTitle());
                holder.fecha.setText(item.getDateString());
                holder.descripcion.setText(item.getDescription());

                // se recicla el AQuery.
                // aq = aq.recycle(convertView);

                //actions
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

                //gestiona la imagen
                manageThumbnail(holder.thumbnail, holder.pbThumbnail, position, convertView, parent, item.getImageURL());

                return convertView;
        }

        private static class ViewHolder {
                TextView titulo;
                TextView fecha;
                TextView descripcion;
                ImageView thumbnail;
                ProgressBar pbThumbnail;
                TextView btShow;
                TextView btShare;
        }
}
