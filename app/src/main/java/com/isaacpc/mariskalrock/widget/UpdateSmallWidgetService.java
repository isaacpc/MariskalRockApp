package com.isaacpc.mariskalrock.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;

import com.isaacpc.mariskalrock.MariskalRockApplication;
import com.isaacpc.mariskalrock.bd.NoticiaEntity;
import com.isaacpc.mariskalrock.log.Log;

public class UpdateSmallWidgetService extends WidgetServiceBase {

    private static final String LOG_TAG = "UpdateSmallWidgetService";

    private NoticiaEntity noticiaActual = null;

    public UpdateSmallWidgetService() {
	super("UpdateSmallWidgetService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

	context = this.getApplicationContext();
	appWidgetManager = AppWidgetManager.getInstance(context);
	allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

	// FIXME (TENER LA BBDD PRECARGADA ANTES DE LLEGAR AQUI, PORQUE SI NO,
	// ES MUY LENTO
	if (databaseManager == null) {
	    databaseManager = MariskalRockApplication.getDatabaseManagerInstance();
	}

	if (aq == null) {
	    aq = MariskalRockApplication.getAQueryInstance();
	}

	// Busca el último elemento de la BBDD.
	noticiaActual = databaseManager.getNoticiaMasNueva();

	if (noticiaActual == null) {
	    Log.i(LOG_TAG, "No hay noticia en BBDD. Se muestra error widget pequeño");
	    showErrorView();

	} else {
	    Log.i(LOG_TAG, "Hay noticia en BBDD. Se muestra en widget pequeño");
	    showLoadingView();
	    updateWidget(noticiaActual);
	}
    }

    /**
     * Actualiza los datos del widget
     * 
     * @param context
     * @param item
     * @param appWidgetManager
     * @param remoteViews
     */
    private void updateWidget(NoticiaEntity item) {

	Log.d(LOG_TAG, "Actualiza con los datos de " + item.getTitle());

	// Se actualizan los widgets
	for (int widgetId : allWidgetIds) {
	    SmallWidgetComposer.compose(context, appWidgetManager, widgetId, item);
	}

	stopSelf();
    }
}
