package com.isaacpc.mariskalrock.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RemoteViews;

import com.isaacpc.mariskalrock.R;
import com.isaacpc.mariskalrock.bd.NoticiaEntity;
import com.isaacpc.mariskalrock.common.WidgetConstants;
import com.isaacpc.mariskalrock.log.Log;
import com.isaacpc.mariskalrock.thread.ImageDownloaderService;
import com.isaacpc.mariskalrock.utils.StringUtils;

public class SmallWidgetComposer extends WidgetComposerBase {

    private static final String LOG_TAG = "SmallWidgetComposer";

    public static void compose(final Context context, final AppWidgetManager appWidgetManager, final int widgetId, NoticiaEntity item) {

	Log.d(LOG_TAG, "Se rellena el layout del widget pequeño");

	RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_small_layout);

	// titulo
	String titulo = item.getTitle();
	remoteViews.setTextViewText(R.id.txtTitulo, titulo);

	// descripcion
	String descripcion = item.getDescription();
	if (!StringUtils.isEmpty(descripcion)) {
	    descripcion = StringUtils.replaceSpecialCharacters(descripcion);
	}
	remoteViews.setTextViewText(R.id.txtDescripcion, descripcion);

	// link
	String link = item.getLink();

	// configuracion del evento onclick.
	if (!StringUtils.isEmpty(link)) {
	    Intent intentClick = new Intent(Intent.ACTION_VIEW);
	    intentClick.setData(Uri.parse(link));
	    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentClick, 0);
	    remoteViews.setOnClickPendingIntent(R.id.llContenedor, pendingIntent);
	}

	// Configuracion evento Retry
	Intent active = new Intent(context, WidgetLargeProvider.class);
	active.setAction(WidgetConstants.ACTION_WIDGET_RETRY);
	PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
	remoteViews.setOnClickPendingIntent(R.id.btRetry, actionPendingIntent);

	remoteViews.setViewVisibility(R.id.errorLayout, View.GONE);
	remoteViews.setViewVisibility(R.id.loadingLayout, View.GONE);
	remoteViews.setViewVisibility(R.id.llContenedor, View.VISIBLE);

	// Se actualiza
	appWidgetManager.updateAppWidget(widgetId, remoteViews);

	// Imagen -- Se llama al IntentService para actualizar la imagen
	Intent msgIntent = new Intent(context, ImageDownloaderService.class);

	msgIntent.putExtra(ImageDownloaderService.PARAM_IMAGE_URL, item.getImageURL());
	msgIntent.putExtra(ImageDownloaderService.PARAM_WIDGET_ID, widgetId);
	msgIntent.putExtra(ImageDownloaderService.PARAM_LAYOUT, R.layout.widget_small_layout);
	context.startService(msgIntent);

	// -----------------------------------------------

    }
}
