package com.isaacpc.mariskalrock.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;

import com.isaacpc.mariskalrock.common.BroadcastConstants;
import com.isaacpc.mariskalrock.common.WidgetConstants;
import com.isaacpc.mariskalrock.log.Log;
import com.isaacpc.mariskalrock.utils.ConnectionUtils;

public class WidgetSmallProvider extends WidgetProviderBase {

    private static final String LOG_TAG = "WidgetSmallProvider";

    @Override
    public void onEnabled(Context context) {
        Log.i(LOG_TAG, "onEnabled()");
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(LOG_TAG, "onDelete()");
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // actualiza los feeds
        launchFeedUpdateService(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(LOG_TAG, "onReceive() :  " + intent.getAction());

        if (intent.getAction().equals(BroadcastConstants.BROADCAST_FEED_UPDATED_WIDGET)) {

            // no hace falta actualizar nada solo muestra los datos de la BBDD
            launchService(context, intent.getAction(), UpdateSmallWidgetService.class);

        } else if (intent.getAction().equals(WidgetConstants.ACTION_WIDGET_RETRY)) {
            // actualiza los feeds
            launchFeedUpdateService(context);

        } else if (intent.getAction().equals(BroadcastConstants.BROADCAST_CONNECTIVITY_CHANGE) && ConnectionUtils.hasConnection(context)) {
            // actualiza los feeds
            showLoadingView(context);
            launchFeedUpdateService(context);


        } else {
            Log.d(LOG_TAG, "onReceive() :  " + intent.getAction());
            super.onReceive(context, intent);
        }
    }
}