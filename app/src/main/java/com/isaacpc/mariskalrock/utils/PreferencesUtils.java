package com.isaacpc.mariskalrock.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.isaacpc.mariskalrock.common.Constants;
import com.isaacpc.mariskalrock.log.Log;

public class PreferencesUtils {

        private static final String LOG_TAG = "PreferencesUtils";

        /**
         * Obtiene el valor de un preferences
         * 
         * @param key
         * @param context
         * @return
         */
        public static String getPreferenceValue(String key, Context context) {

                final SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(context);
                return p.getString(key, null);
        }

        /**
         * Obtiene un valor de preferences como Integer
         * 
         * @param key
         * @param context
         * @return
         */
        public static Integer getPreferenceValueInt(String key, Context context) {

                final SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(context);
                return p.getInt(key, 0);
        }

        /**
         * Salva un valor en preferences
         * 
         * @param key
         * @param context
         * @return
         */
        public static void setPreferenceValue(String key, String value, Context context) {

                final Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

                if (editor == null) {
                        Log.w(LOG_TAG, "No se puede guardar en Preferences. Editor es null");

                } else {
                        editor.putString(key, value);
                        editor.commit();

                }
        }

        /**
         * Salva un valor Integer en preferences
         * 
         * @param key
         * @param context
         * @return
         */
        public static void setPreferenceValue(final String key, final Integer value, final Context context) {

                final Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

                if (editor == null) {
                        Log.w(LOG_TAG, "No se puede guardar en Preferences. Editor es null");

                } else {
                        editor.putInt(key, value);
                        editor.commit();
                }
        }

        /**
         * True si elige siempre o true si elige wifi y está seleccionada
         * 
         * @param key
         * @param context
         * @return
         */
        public static boolean isUsableRadio(final Context context) {

                String value = PreferencesUtils.getPreferenceValue(Constants.PREFERENCES_CONEXION_RADIO, context);

                if (StringUtils.isEmpty(value)) {
                        value = "siempre";
                }

                return (Constants.PREF_DESCARGA_SIEMPRE.equals(value) || (Constants.PREF_DESCARGA_WIFI.equals(value) && ConnectionUtils.isWifiConnected(context)));
        }
}
