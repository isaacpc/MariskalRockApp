package com.isaacpc.mariskalrock.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.isaacpc.mariskalrock.common.BroadcastConstants;
import com.isaacpc.mariskalrock.common.Constants;
import com.isaacpc.mariskalrock.common.WidgetConstants;
import com.isaacpc.mariskalrock.common.WidgetConstants.NewsPosition;
import com.isaacpc.mariskalrock.log.Log;
import com.isaacpc.mariskalrock.thread.FeedUpdaterService;

public abstract class WidgetProviderBase extends AppWidgetProvider {

        private static final String LOG_TAG = "WidgetProviderBase";

        /**
         * Se descargan los datos del Feed
         */
        protected void launchFeedUpdateService(Context context) {

                final Intent msgIntent = new Intent(context, FeedUpdaterService.class);
                msgIntent.putExtra(FeedUpdaterService.FEED_CATEGORIA, Constants.Categoria.NOTICIAS);
                msgIntent.putExtra(FeedUpdaterService.SOURCE_NOTICIAS_BROADCAST, BroadcastConstants.BROADCAST_FEED_UPDATED_WIDGET);
                context.startService(msgIntent);
        }

        /**
         * Se llama al servicio para mostar la info en el widget
         */
        protected void launchService(Context context, String accion, Class<?> clazzIntent) {

                Log.i(LOG_TAG, "Se lanza el servicio: " + accion + " | IntentService: " + clazzIntent);

                final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                final ComponentName thisWidget = new ComponentName(context, this.getClass());
                final int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

                final Intent msgIntent = new Intent(context, clazzIntent);
                msgIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
                msgIntent.putExtra(WidgetConstants.ACTION_WIDGET_ACCION, accion);

                // Intents para el movimiento
                if (WidgetConstants.ACTION_WIDGET_NEXT_NEWS.equalsIgnoreCase(accion)) {
                        msgIntent.putExtra(WidgetConstants.ACTION_WIDGET_POSITION, NewsPosition.NEXT);

                } else if (WidgetConstants.ACTION_WIDGET_PREVIOUS_NEWS.equalsIgnoreCase(accion)) {
                        msgIntent.putExtra(WidgetConstants.ACTION_WIDGET_POSITION, NewsPosition.PREVIOUS);
                }

                context.startService(msgIntent);
        }

        /**
         * Muestra la pantalla de carga
         */
        protected void showLoadingView(Context context) {
                // actualiza los feeds
                final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                final ComponentName thisWidget = new ComponentName(context, this.getClass());
                final int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
                new WidgetHelper(context).showLoadingView(appWidgetManager, allWidgetIds);        

        }

}
