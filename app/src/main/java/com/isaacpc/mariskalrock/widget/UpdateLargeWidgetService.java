package com.isaacpc.mariskalrock.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;

import com.isaacpc.mariskalrock.MariskalRockApplication;
import com.isaacpc.mariskalrock.bd.NoticiaEntity;
import com.isaacpc.mariskalrock.common.WidgetConstants;
import com.isaacpc.mariskalrock.common.WidgetConstants.NewsPosition;
import com.isaacpc.mariskalrock.log.Log;

public class UpdateLargeWidgetService extends WidgetServiceBase {

        private static final String LOG_TAG = "UpdateLargeWidgetService";
        NoticiaEntity noticiaActual = null;

        public UpdateLargeWidgetService() {
                super("UpdateLargeWidgetService");
        }

        @Override
        protected void onHandleIntent(Intent intent) {

                context = this.getApplicationContext();
                appWidgetManager = AppWidgetManager.getInstance(context);
                allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

                final WidgetHelper widgetHelper = new WidgetHelper(context);

                // FIXME (TENER LA BBDD PRECARGADA ANTES DE LLEGAR AQUI, PORQUE SI NO,
                // ES MUY LENTO
                if (databaseManager == null) {
                        databaseManager = MariskalRockApplication.getDatabaseManagerInstance();
                }

                if (aq == null) {
                        aq = MariskalRockApplication.getAQueryInstance();
                }

                final Bundle bundle = intent.getExtras();

                final NewsPosition position = (NewsPosition) bundle.get(WidgetConstants.ACTION_WIDGET_POSITION);

                if (position == null) {
                        Log.i(LOG_TAG, "Se Actualiza el widget grande");

                        noticiaActual = databaseManager.getNoticiaActual();

                        if (noticiaActual == null) {
                                Log.i(LOG_TAG, "No hay noticia en BBDD. Se muestra error widget grande");
                                widgetHelper.showErrorView(appWidgetManager, allWidgetIds);

                        } else {
                                Log.i(LOG_TAG, "No hay noticia en BBDD. Se muestra en widget grande");
                                widgetHelper.showLoadingView(appWidgetManager, allWidgetIds);
                                updateWidget(noticiaActual);
                        }
                } else {
                        Log.i(LOG_TAG, "Se mueve a una noticia");
                        showItem(position);
                }
        }

        /**
         * Actualiza los datos del widget
         * 
         * @param context
         * @param noticia
         * @param listSize
         * @param appWidgetManager
         * @param remoteViews
         */
        private void updateWidget(NoticiaEntity item) {

                for (final int widgetId : allWidgetIds) {
                        LargeWidgetComposer.compose(context, appWidgetManager, widgetId, item);
                }

                stopSelf();
        }

        /**
         * Se actualiza el widget con el siguiente o anterior item. Primero busca el
         * currentItem. Una vez que lo tiene, salva el valor y hace el movimiento
         * 
         * @param context
         * @param position
         */
        private void showItem(NewsPosition position) {

                NoticiaEntity noticia = null;

                if (noticiaActual == null) {
                        noticiaActual = databaseManager.getNoticiaActual();
                }

                if (position == NewsPosition.NEXT) {
                        noticia = databaseManager.getNoticiaPosterior(noticiaActual);
                } else {
                        noticia = databaseManager.getNoticiaAnterior(noticiaActual);
                }

                if (noticia != null && noticiaActual != null) {

                        // se actualiza el current en BBDD
                        databaseManager.updateNoticiaActual(noticiaActual, noticia);

                        noticiaActual = noticia;

                        if (noticiaActual != null) {
                                updateWidget(noticiaActual);
                        }
                }
        }
}
