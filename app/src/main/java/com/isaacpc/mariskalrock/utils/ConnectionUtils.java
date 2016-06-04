package com.isaacpc.mariskalrock.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class ConnectionUtils {

        /**
         * Verifica la conexión de internet
         * 
         * @param ctx
         * @return
         */
        public static boolean hasConnection(Context context) {

                if(context==null){
                        return false;
                }

                final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                final boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                return isConnected;
        }

        /**
         * 
         * @param context
         * @return
         */
        public static boolean isWifiConnected(Context context) {

                final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                return wifiManager.isWifiEnabled();
        }
}
