package com.isaacpc.mariskalrock.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.isaacpc.mariskalrock.common.Constants;

public class ImageUtils {

    /**
     * @param bm
     * @param newHeight
     * @param newWidth
     * @return
     */
    public static Bitmap getResizedBitmap(Bitmap bm, float newHeight, float newWidth) {
        final int width = bm.getWidth();
        final int height = bm.getHeight();
        final float scaleWidth = (newWidth) / width;
        final float scaleHeight = (newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        final Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        final Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }


    /**
     * @return
     */
    public static Boolean isDownloadableImage(final Context context) {

        String imageDownloadPreference = PreferencesUtils.getPreferenceValue(Constants.PREFERENCES_DOWNLOAD_IMAGES, context);

        if (imageDownloadPreference == null) {
            PreferencesUtils.setPreferenceValue(Constants.PREFERENCES_DOWNLOAD_IMAGES, Constants.PREF_DESCARGA_SIEMPRE, context);
            imageDownloadPreference = Constants.PREF_DESCARGA_SIEMPRE;
        }

        /*
         * Si no hay conexión o no quiere descargar las imagenes: false
         * Si hay wifi y quiere descargar por Wifi: true
         * Si hay conexión y quiere descargar siempre: true
         */

        if (!ConnectionUtils.hasConnection(context) || Constants.PREF_DESCARGA_NUNCA.equals(imageDownloadPreference)) {
            return false;
        } else if (ConnectionUtils.isWifiConnected(context) && Constants.PREF_DESCARGA_WIFI.equals(imageDownloadPreference)) {
            return true;
        } else if (ConnectionUtils.hasConnection(context) && Constants.PREF_DESCARGA_SIEMPRE.equals(imageDownloadPreference)) {
            return true;
        } else {
            return false;
        }
    }
}
