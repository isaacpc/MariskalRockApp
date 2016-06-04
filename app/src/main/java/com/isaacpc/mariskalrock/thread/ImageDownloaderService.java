package com.isaacpc.mariskalrock.thread;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.isaacpc.mariskalrock.MariskalRockApplication;
import com.isaacpc.mariskalrock.R;
import com.isaacpc.mariskalrock.log.Log;
import com.isaacpc.mariskalrock.utils.ImageUtils;
import com.isaacpc.mariskalrock.utils.StringUtils;

public class ImageDownloaderService extends IntentService {

        private static final String LOG_TAG = "ImageDownloaderService";

        public final static String PARAM_IMAGE_URL = "imageURL";
        public final static String PARAM_WIDGET_ID = "widgetID";
        public final static String PARAM_LAYOUT = "widget_layout";

        protected AQuery aq = null;
        protected Context context = null;

        public ImageDownloaderService() {
                super("ImageDownloaderService");

                Log.i(LOG_TAG, "Se crea el servicio ImageDownloaderService.");

                aq = MariskalRockApplication.getAQueryInstance();
                context = MariskalRockApplication.getInstance();
        }

        @Override
        protected void onHandleIntent(Intent intent) {

                final Bundle bundle = intent.getExtras();

                final String imageUrl = bundle.getString(PARAM_IMAGE_URL);
                final int widgetId = bundle.getInt(PARAM_WIDGET_ID);
                final int widgetLayout = bundle.getInt(PARAM_LAYOUT);

                Log.i(LOG_TAG, "Descargando la imagen:" + imageUrl);

                if (StringUtils.isEmpty(imageUrl)) {
                        Log.d(LOG_TAG, "La imagen es nula, se pinta ic_fallback");
                        final Drawable drawable = context.getResources().getDrawable(R.drawable.ic_fallback);

                        final Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                        updateWidgetImage(widgetLayout, widgetId, bitmap);

                } else {
                        setImageInRemoteView(imageUrl, widgetId, context.getPackageName(), widgetLayout);
                }
        }

        /**
         * 
         * @param imageUrl
         * @param widgetId
         * @param appWidgetManager
         * @param contextPackage
         * @param layout
         */
        private void setImageInRemoteView(final String imageUrl, final int widgetId, final String contextPackage, final int layout) {

                Log.d(LOG_TAG, "se pone la imagen en el remoteView");

                Bitmap bitmap = null;

                if (!StringUtils.isEmpty(imageUrl)) {
                        // intenta obtener la imagen de cache
                        bitmap = aq.getCachedImage(imageUrl, (int) context.getResources().getDimension(R.dimen.widget_thumbnail_width));
                }

                // Si es null y se pueden descargar las imágenes se obtiene de internet
                if (bitmap == null && ImageUtils.isDownloadableImage(context)) {
                        Log.w(LOG_TAG, "El bitmap es nulo. Se descarga la imagen");

                        final long expire = MariskalRockApplication.DEFAULT_CACHED_IMAGE_TIME; // 7 dias cacheado

                        aq.ajax(imageUrl, Bitmap.class, expire, new AjaxCallback<Bitmap>() {
                                @Override
                                public void callback(String url, Bitmap bitmap, AjaxStatus status) {
                                        // Si no obtiene nada, muestra la imagen fallback
                                        if (bitmap != null) {
                                                Log.d(LOG_TAG, "Se ha obtenido la imagen de internet.");
                                                updateWidgetImage(layout, widgetId, bitmap);
                                        }
                                }
                        });
                } else {
                        Log.i(LOG_TAG, "Obtenida la imagen desde la caché (o fallback si no downloadable");
                        updateWidgetImage(layout, widgetId, bitmap);
                }
        }

        /**
         * 
         * @param layout
         * @param widgetId
         * @param bitmap
         */
        private void updateWidgetImage(int layout, int widgetId, Bitmap bitmap) {

                // TODO optimizar y mejorar la carga de la imagen.

                Log.d(LOG_TAG, "Se Pinta el bitmap en el remoteView");

                final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), layout);

                // se redimensiona la imagen para cargarla en el widget
                //                final Bitmap resizedBitmap = ImageUtils.getResizedBitmap(bitmap, 120, 120);
                remoteViews.setImageViewBitmap(R.id.imagen, bitmap);

                remoteViews.setViewVisibility(R.id.pbThumbnail, View.INVISIBLE);
                remoteViews.setViewVisibility(R.id.imagen, View.VISIBLE);

                final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
}
