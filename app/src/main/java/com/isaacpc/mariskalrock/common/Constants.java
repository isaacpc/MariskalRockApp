package com.isaacpc.mariskalrock.common;

public class Constants {

        public enum Categoria {
                NOTICIAS, PODCAST, VIDEO, NOTICIAS_ALARMA
        };

        public final static int FEED_TIMEOUT = 60;



        public final static String CACHE_PATH_NOTICIAS = "noticias";
        public final static String CACHE_PATH_PODCAST = "podcast";
        public final static String CACHE_PATH_VIDEO = "video";


        //MENU DE PREFERENCIAS
        public static final String PREFERENCES_DOWNLOAD_IMAGES  = "downloadImagePref";
        public static final String PREFERENCES_CONEXION_RADIO   = "radioConnectionPref";
        public static final String PREFERENCES_FRECUENCIA_SINCRONIZACION = "syncFrequency";
        public static final String PREFERENCES_FRECUENCIA_CONEXION = "syncWhen";

        public final static String PREF_DESCARGA_SIEMPRE = "siempre";
        public final static String PREF_DESCARGA_NUNCA = "nunca";
        public final static String PREF_DESCARGA_WIFI = "wifi";


        //ACTUALIZACION DE FEED
        public final static String PREF_LAST_UPDATE_NOTICIAS = "last_update_noticias";
        public final static int REFRESH_NOTICIAS_MIN_TIME = 60; //En minutos.



        public final static String FEED_NOTICIAS_URL = "http://www.mariskalrock.com/index.php/feed/";
        public final static String FEED_PODCAST_URL = "http://www.ivoox.com/feed_fg_f14151_filtro_1.xml";
        public final static String FEED_VIDEO_URL = "https://gdata.youtube.com/feeds/api/users/mariskalrocktv/uploads?alt=json";

        // ID Usuario Facebook: 102402516465227
        public static final String FEED_PORTADAS_URL = "https://graph.facebook.com/368740453164764/photos";
//        public final static String STREAMING_AUDIO_URL = "mms://directo.mariskalrock.com/MariskalRock";
        public final static String STREAMING_AUDIO_URL = "http://radioserver9.profesionalhosting.com:32997";
        public static final int ITEM_TAG_URL = 0;

        //ALARMA
        public static final Long HOUR_IN_MILLISECONDS = 3600000L;

}
