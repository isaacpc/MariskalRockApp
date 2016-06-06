package com.isaacpc.mariskalrock.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.isaacpc.mariskalrock.R;
import com.isaacpc.mariskalrock.common.WidgetConstants;
import com.isaacpc.mariskalrock.log.Log;

public class WidgetHelper {

    private static final String LOG_TAG = "WidgetHelper";
    protected Context context;


    public WidgetHelper(Context context) {
        super();
        this.context = context;
    }

    /**
     * Pinta una pantalla de error.
     */

    protected void showErrorView(final AppWidgetManager appWidgetManager, final int[] allWidgetIds) {

        Log.i(LOG_TAG, "Muestra pantalla de error");

        // Se actualizan los widgets
        for (final int widgetId : allWidgetIds) {
            Log.i(LOG_TAG, "WIDGET:" + widgetId);
            final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_small_layout);

            remoteViews.setViewVisibility(R.id.llContenedor, View.GONE);
            remoteViews.setViewVisibility(R.id.loadingLayout, View.GONE);
            remoteViews.setViewVisibility(R.id.errorLayout, View.VISIBLE);

            // Configura el evento de onRetry
            final Intent iRetry = new Intent(context, WidgetLargeProvider.class);
            iRetry.setAction(WidgetConstants.ACTION_WIDGET_RETRY);
            final PendingIntent actionPendingIntentRetry = PendingIntent.getBroadcast(context, 0, iRetry, 0);
            remoteViews.setOnClickPendingIntent(R.id.btReintento, actionPendingIntentRetry);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    /**
     * Pinta una pantalla de cargando.
     */
    protected void showLoadingView(final AppWidgetManager appWidgetManager, final int[] allWidgetIds) {

        Log.i(LOG_TAG, "Se muestra la pantalla de Cargando...");

        // Se actualizan los widgets
        for (final int widgetId : allWidgetIds) {
            final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_small_layout);

            remoteViews.setViewVisibility(R.id.llContenedor, View.INVISIBLE);
            remoteViews.setViewVisibility(R.id.loadingLayout, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.errorLayout, View.INVISIBLE);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}
