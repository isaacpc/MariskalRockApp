package com.isaacpc.mariskalrock.thread;

import java.util.Calendar;
import java.util.List;

import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.XmlDom;
import com.isaacpc.mariskalrock.MariskalRockApplication;
import com.isaacpc.mariskalrock.bd.DatabaseManager;
import com.isaacpc.mariskalrock.bd.NoticiaEntity;
import com.isaacpc.mariskalrock.bd.PodcastEntity;
import com.isaacpc.mariskalrock.bd.VideoEntity;
import com.isaacpc.mariskalrock.common.BroadcastConstants;
import com.isaacpc.mariskalrock.common.Constants;
import com.isaacpc.mariskalrock.common.Constants.Categoria;
import com.isaacpc.mariskalrock.log.Log;
import com.isaacpc.mariskalrock.parser.NoticiasParser;
import com.isaacpc.mariskalrock.parser.PodcastsParser;
import com.isaacpc.mariskalrock.parser.VideosParser;
import com.isaacpc.mariskalrock.utils.DateUtils;
import com.isaacpc.mariskalrock.utils.PreferencesUtils;

/**
 * Se conecta a internet y hace la descarga de los elementos y los guarda en
 * BBDD. Después, lanza un Broadcast para quien quiera actualizarse que lo haga.
 * 
 * @author elisaac
 * 
 */
public class FeedUpdaterService extends IntentService {

        private static final int AJAX_OK = 200;
        private static final String LOG_TAG = "FeedUpdaterService";
        public static final String FEED_CATEGORIA = "feed_categoria";
        public static final String SOURCE_NOTICIAS_BROADCAST = "next_feed_broadcast";

        private AQuery aq = null;
        private DatabaseManager databaseManager = null;
        private Categoria feedCategoria = null;

        private String sourceNoticiasBroadcast = null; // para distinguir entre si las
        // noticias se piden desde el
        // widget o desde la app

        public FeedUpdaterService() {
                super("FeedUpdaterService");

                Log.i(LOG_TAG, "Se crea el servicio FeedUpdaterService.");

                aq = MariskalRockApplication.getAQueryInstance();

                // carga la BBDD
                databaseManager = MariskalRockApplication.getDatabaseManagerInstance();
        }

        @Override
        protected void onHandleIntent(Intent intent) {

                feedCategoria = (Categoria) intent.getExtras().get(FEED_CATEGORIA);
                sourceNoticiasBroadcast = intent.getExtras().getString(SOURCE_NOTICIAS_BROADCAST);

                switch (feedCategoria) {
                case NOTICIAS:
                        updateFeedNoticias(true);
                        break;
                case PODCAST:
                        updateFeedPocast();
                        break;
                case VIDEO:
                        updateFeedVideo();
                        break;
                case NOTICIAS_ALARMA:
                        updateFeedNoticias(false);
                        break;
                default:
                        Log.w(LOG_TAG, "La categoría a actualiza no está registrada.");
                }
        }


        private void updateFeedNoticias(final Boolean sendBroadcast) {

                // TODO Comprobar si hace menos de X minutos que se ha actualizado (con
                // sysdate y la fecha de BBDD)
                // para no actualizar todo el rato.

                aq.ajax(Constants.FEED_NOTICIAS_URL, XmlDom.class, new AjaxCallback<XmlDom>() {

                        @Override
                        public void callback(String url, XmlDom xml, AjaxStatus status) {

                                List<NoticiaEntity> itemList;

                                if (status.getCode() == AJAX_OK) {
                                        // parsea el xml
                                        itemList = new NoticiasParser().parseDom(xml);

                                        // si hay datos, carga la lista y cambia la pantalla
                                        // además guarda los objetos en BBDD
                                        if (itemList != null && !itemList.isEmpty()) {

                                                // Guardar los datos en BBDD
                                                databaseManager.saveNoticiasEntityList(itemList, true);

                                                //guarda la hora de actualización de las noticias
                                                saveUpdateDate();
                                        } else {
                                                Log.w(LOG_TAG, "No se han obtenido items del feed");
                                        }
                                } else {
                                        Log.e(LOG_TAG, "No se ha podido obtener el feed:" + status.getError());
                                }

                                if(sendBroadcast){
                                        Log.i(LOG_TAG, "Se envia BroadCast " + sourceNoticiasBroadcast);
                                        final Intent intent = new Intent();
                                        intent.setAction(sourceNoticiasBroadcast);
                                        sendBroadcast(intent);
                                }

                                stopSelf();
                        }
                });
        }

        /**
         * Actualiza el feed de podcast
         */
        private void updateFeedPocast() {

                // TODO Comprobar si hace menos de X minutos que se ha actualizado (con
                // sysdate y la fecha de BBDD)
                // para no actualizar todo el rato.

                aq.ajax(Constants.FEED_PODCAST_URL, XmlDom.class, new AjaxCallback<XmlDom>() {

                        @Override
                        public void callback(String url, XmlDom xml, AjaxStatus status) {

                                List<PodcastEntity> itemList;

                                if (status.getCode() == AJAX_OK) {
                                        // parsea el xml
                                        itemList = new PodcastsParser().parseDom(xml);

                                        // si hay datos, carga la lista y cambia la pantalla
                                        // además guarda los objetos en BBDD
                                        if (itemList != null && !itemList.isEmpty()) {

                                                // Guardar los datos en BBDD
                                                databaseManager.savePodcastsEntityList(itemList, true);

                                        } else {
                                                Log.e(LOG_TAG, "No se han obtenido items del feed");
                                        }
                                } else {
                                        Log.e(LOG_TAG, "No se ha podido obtener el feed:" + status.getError());
                                }

                                Log.i(LOG_TAG, "Se envia BroadCast " + BroadcastConstants.BROADCAST_PODCASTS);
                                final Intent intent = new Intent();
                                intent.setAction(BroadcastConstants.BROADCAST_PODCASTS);
                                sendBroadcast(intent);

                                stopSelf();
                        }
                });
        }

        /**
         * Actualiza el feed de videos
         */
        private void updateFeedVideo() {

                // TODO Comprobar si hace menos de X minutos que se ha actualizado (con
                // sysdate y la fecha de BBDD)
                // para no actualizar todo el rato.

                aq.ajax(Constants.FEED_VIDEO_URL, JSONObject.class, new AjaxCallback<JSONObject>() {

                        @Override
                        public void callback(String url, JSONObject json, AjaxStatus status) {

                                List<VideoEntity> itemList;

                                if (status.getCode() == AJAX_OK) {
                                        // parsea el xml
                                        itemList = new VideosParser().parse(json);

                                        // si hay datos, carga la lista y cambia la pantalla
                                        // además guarda los objetos en BBDD
                                        if (itemList != null && !itemList.isEmpty()) {

                                                // Guardar los datos en BBDD
                                                databaseManager.saveVideosEntityList(itemList, true);

                                        } else {
                                                Log.e(LOG_TAG, "No se han obtenido items del feed");
                                        }
                                } else {
                                        Log.e(LOG_TAG, "No se ha podido obtener el feed:" + status.getError());
                                }

                                Log.i(LOG_TAG, "Se envia BroadCast " + BroadcastConstants.BROADCAST_VIDEOS);
                                final Intent intent = new Intent();
                                intent.setAction(BroadcastConstants.BROADCAST_VIDEOS);
                                sendBroadcast(intent);

                                stopSelf();
                        }
                });
        }


        /**
         * Guarda la hora en la que se actualiza el feed de noticias
         */
        private void saveUpdateDate(){
                Log.i(LOG_TAG, "Guarda la hora de la actualización actual");
                PreferencesUtils.setPreferenceValue(Constants.PREF_LAST_UPDATE_NOTICIAS, 
                                DateUtils.CalendarToString(Calendar.getInstance(), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS),
                                MariskalRockApplication.getInstance());

        }

}