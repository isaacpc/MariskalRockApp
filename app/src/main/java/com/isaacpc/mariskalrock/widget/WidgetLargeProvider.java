package com.isaacpc.mariskalrock.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;

import com.isaacpc.mariskalrock.common.BroadcastConstants;
import com.isaacpc.mariskalrock.common.WidgetConstants;
import com.isaacpc.mariskalrock.log.Log;
import com.isaacpc.mariskalrock.utils.ConnectionUtils;

public class WidgetLargeProvider extends WidgetProviderBase {

    private static final String LOG_TAG = "WidgetLargeProvider";

    @Override
    public void onEnabled(Context context) {
        Log.i(LOG_TAG, "onEnabled()");
        super.onEnabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(LOG_TAG, "onDelete()");
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(LOG_TAG, "onReceive() :  " + intent.getAction());

        if (intent.getAction().equals(WidgetConstants.ACTION_WIDGET_NEXT_NEWS) ||
                intent.getAction().equals(WidgetConstants.ACTION_WIDGET_PREVIOUS_NEWS)
                || intent.getAction().equals(BroadcastConstants.BROADCAST_FEED_UPDATED_WIDGET)) {

            // no hace falta actualizar nada solo muestra los datos de la BBDD
            launchService(context, intent.getAction(), UpdateLargeWidgetService.class);

        } else if (intent.getAction().equals(WidgetConstants.ACTION_WIDGET_RETRY)) {
            // actualiza los feeds
            launchFeedUpdateService(context);

        } else if (intent.getAction().equals(BroadcastConstants.BROADCAST_CONNECTIVITY_CHANGE) && ConnectionUtils.hasConnection(context)) {

            showLoadingView(context);
            launchFeedUpdateService(context);

        } else {
            Log.d(LOG_TAG, "onReceive() :  " + intent.getAction());
            super.onReceive(context, intent);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // actualiza los feeds
        launchFeedUpdateService(context);
    }
}