package com.isaacpc.mariskalrock.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RemoteViews;

import com.isaacpc.mariskalrock.MariskalRockApplication;
import com.isaacpc.mariskalrock.R;
import com.isaacpc.mariskalrock.bd.DatabaseManager;
import com.isaacpc.mariskalrock.bd.NoticiaEntity;
import com.isaacpc.mariskalrock.common.WidgetConstants;
import com.isaacpc.mariskalrock.log.Log;
import com.isaacpc.mariskalrock.thread.ImageDownloaderService;
import com.isaacpc.mariskalrock.utils.StringUtils;

public class LargeWidgetComposer extends WidgetComposerBase {

        private static final String LOG_TAG = "LargeWidgetComposer";

        public static void compose(Context context, final AppWidgetManager appWidgetManager, final int widgetId, NoticiaEntity item) {

                Log.d(LOG_TAG, "Se pinta el Widget Grande...");

                final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_large_layout);

                // titulo
                remoteViews.setTextViewText(R.id.txtTitulo, item.getTitle());

                // descripcion
                String descripcion = item.getDescription();
                if (!StringUtils.isEmpty(descripcion)) {
                        descripcion = StringUtils.replaceSpecialCharacters(descripcion);
                }
                remoteViews.setTextViewText(R.id.txtDescripcion, descripcion);

                // fecha
                remoteViews.setTextViewText(R.id.txtFecha,item.getDateString());

                // link
                final String link = item.getLink();

                // configuracion del evento onclick.
                if (!StringUtils.isEmpty(link)) {
                        final Intent intentClick = new Intent(Intent.ACTION_VIEW);
                        intentClick.setData(Uri.parse(link));
                        final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentClick, 0);
                        remoteViews.setOnClickPendingIntent(R.id.llArticulo, pendingIntent);
                }

                final DatabaseManager databaseManager = MariskalRockApplication.getDatabaseManagerInstance();

                // BOTON DE MÁS NUEVO
                if (databaseManager.getNoticiaPosterior(item) != null) {
                        remoteViews.setViewVisibility(R.id.icNext, View.VISIBLE);
                        remoteViews.setViewVisibility(R.id.icNext_disabled, View.GONE);
                } else {
                        remoteViews.setViewVisibility(R.id.icNext, View.GONE);
                        remoteViews.setViewVisibility(R.id.icNext_disabled, View.VISIBLE);
                }

                // BOTÓN DE MÁS ANTIGUO
                if (databaseManager.getNoticiaAnterior(item) != null) {
                        remoteViews.setViewVisibility(R.id.icPrevious, View.VISIBLE);
                        remoteViews.setViewVisibility(R.id.icPrevious_disabled, View.GONE);
                } else {
                        remoteViews.setViewVisibility(R.id.icPrevious, View.GONE);
                        remoteViews.setViewVisibility(R.id.icPrevious_disabled, View.VISIBLE);
                }

                remoteViews.setViewVisibility(R.id.errorLayout, View.GONE);
                remoteViews.setViewVisibility(R.id.loadingLayout, View.INVISIBLE);
                remoteViews.setViewVisibility(R.id.llContenedor, View.VISIBLE);

                // Se definen los intents para los botones
                final Intent iNext = new Intent(context, WidgetLargeProvider.class);
                iNext.setAction(WidgetConstants.ACTION_WIDGET_NEXT_NEWS);
                final PendingIntent actionPendingIntentNext = PendingIntent.getBroadcast(context, 0, iNext, 0);
                remoteViews.setOnClickPendingIntent(R.id.icNext, actionPendingIntentNext);

                final Intent iPrevious = new Intent(context, WidgetLargeProvider.class);
                iPrevious.setAction(WidgetConstants.ACTION_WIDGET_PREVIOUS_NEWS);
                final PendingIntent actionPendingIntentPrevius = PendingIntent.getBroadcast(context, 0, iPrevious, 0);
                remoteViews.setOnClickPendingIntent(R.id.icPrevious, actionPendingIntentPrevius);

                // Tiempo de espera mientras se pone la imagen nueva
                remoteViews.setViewVisibility(R.id.pbThumbnail, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.imagen, View.INVISIBLE);

                appWidgetManager.updateAppWidget(widgetId, remoteViews);

                // Imagen -- Se llama al IntentService para descargar la imagen asociada
                // ---------------------
                final Intent msgIntent = new Intent(context, ImageDownloaderService.class);

                msgIntent.putExtra(ImageDownloaderService.PARAM_IMAGE_URL, item.getImageURL());
                msgIntent.putExtra(ImageDownloaderService.PARAM_WIDGET_ID, widgetId);
                msgIntent.putExtra(ImageDownloaderService.PARAM_LAYOUT, R.layout.widget_large_layout);
                context.startService(msgIntent);
        }
}