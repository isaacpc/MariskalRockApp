package com.isaacpc.mariskalrock.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.isaacpc.mariskalrock.MariskalRockApplication;
import com.isaacpc.mariskalrock.R;
import com.isaacpc.mariskalrock.common.BroadcastConstants;
import com.isaacpc.mariskalrock.thread.ShareReceiver;
import com.isaacpc.mariskalrock.utils.ImageUtils;
import com.isaacpc.mariskalrock.utils.StringUtils;

public class MRBaseAdapter extends BaseAdapter {

        private static final float PROPORTION_WIDTH = 16.0f;
        private static final float PROPORTION_HEIGHT = 9.0f;

        // action id
        private static final int ID_VER = 1;
        private static final int ID_COMPARTIR = 2;

        protected Boolean imageDownloadPreference; 
        protected Context context;
        protected AQuery aq;
        protected LayoutInflater inflater;

        public MRBaseAdapter(final Context context1) {

                this.setContext(context1);

                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                aq = MariskalRockApplication.getAQueryInstance();

                imageDownloadPreference = ImageUtils.isDownloadableImage(context1);
        }

        @Override
        public int getCount() {
                // TODO Auto-generated method stub
                return 0;
        }

        @Override
        public   Object getItem(final int position) {
                // TODO Auto-generated method stub
                return null;
        }

        @Override
        public  long getItemId(final int position) {
                // TODO Auto-generated method stub
                return 0;
        }

        @Override
        public View getView(final int position, final View convertView,
                        final ViewGroup parent) {
                // TODO Auto-generated method stub
                return null;
        }

        protected final ImageOptions getGenericImageOptions() {

                final ImageOptions options = new ImageOptions();
                options.fileCache = true;
                options.memCache = true;
                options.ratio = 1;
                options.anchor = (float) 1.0;
                options.ratio = PROPORTION_HEIGHT / PROPORTION_WIDTH;
                options.animation = AQuery.FADE_IN_NETWORK;
                options.fallback = R.drawable.ic_fallback_grande;

                return options;
        }


        /**
         * @return the context
         */
        public final Context getContext() {
                return context;
        }

        /**
         * @param context the context to set
         */
        public final void setContext(final Context context1) {
                context = context1;
        }

        protected final void manageThumbnail(final ImageView thumbnail, final ProgressBar pbThumbnail, final int position, 
                        final View convertView, final ViewGroup parent, final String imageUrl) {
                final ImageOptions options = getGenericImageOptions();

                // Si la URL es vacía se le asigna el fallback.
                if (StringUtils.isEmpty(imageUrl)) {
                        loadFallbackImage(thumbnail);
                } else {
                        /*
                         * Si está en caché se obtiene. 
                         * Si no está en cache, y se puede descargar, se descarga, si no, se muestra fallback del tirón.
                         */
                        final Bitmap cachedImage = aq.getCachedImage(imageUrl);

                        if(cachedImage!=null){
                                //carga esta imagen
                                aq.id(thumbnail).image(cachedImage, options.ratio);

                        } else if(imageDownloadPreference){
                                //Descarga la imagen
                                aq.id(thumbnail).progress(pbThumbnail).image(imageUrl, options);
                        } else {
                                loadFallbackImage(thumbnail);
                        }
                }
        }


        /**
         * Pone la imagen por defecto
         * @param thumbnail
         */
        private void loadFallbackImage(final ImageView thumbnail) {
                aq.id(thumbnail).getImageView().setImageResource(R.drawable.ic_fallback_grande);
                aq.id(thumbnail).getImageView().setScaleType(ScaleType.CENTER_INSIDE);
        }

        /**
         * Comparte un contenido
         */
        protected final void share(final String url) {
                //lanzar el broadcast
                final Intent intent = new Intent();
                intent.putExtra(ShareReceiver.PARAM_LINK, url);
                intent.setAction(BroadcastConstants.BROADCAST_SHARE);
                context.sendBroadcast(intent);
        }

        /**
         * Lanza el navegador para ir a la url indicada
         */
        protected final void goToContent(final String url) {
                Intent nextActivity = null;

                // se muestra el video en el navegador
                nextActivity = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                nextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(nextActivity);
        }
}
