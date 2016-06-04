package com.isaacpc.mariskalrock;

import android.app.Application;
import android.view.MenuItem;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.BitmapAjaxCallback;
import com.isaacpc.mariskalrock.bd.DatabaseManager;
import com.isaacpc.mariskalrock.log.Log;

public class MariskalRockApplication extends Application {

    private static final String LOG_TAG = "MariskalRockApplication";

    public static Long DEFAULT_CACHED_IMAGE_TIME = 604800000L; //7 DIAS.

    private static MariskalRockApplication instance;

    private static DatabaseManager databaseManager;
    private static AQuery aq;
    private MenuItem itemRefresh;


    public MariskalRockApplication() {
        super();
        instance = this;

        configureAndroidQuery();
    }

    public MenuItem getItemRefresh() {
        return itemRefresh;
    }

    public void setItemRefresh(MenuItem itemRefresh) {
        this.itemRefresh = itemRefresh;
    }

    public static MariskalRockApplication getInstance() {
        return instance;
    }

    /**
     *
     */
    private void configureAndroidQuery() {

        Log.i(LOG_TAG, "Se configuran parámetros de Android-Query");

        // set the max number of concurrent network connections, default is 4
        AjaxCallback.setNetworkLimit(4);

        // set the max number of icons (image width <= 50) to be cached in
        // memory, default is 20
        BitmapAjaxCallback.setIconCacheLimit(20);

        // set the max number of images (image width > 50) to be cached in
        // memory, default is 20
        BitmapAjaxCallback.setCacheLimit(40);

        // set the max size of an image to be cached in memory, default is 1600
        // pixels (ie. 400x400)
        BitmapAjaxCallback.setPixelLimit(400 * 400);

        // set the max size of the memory cache, default is 1M pixels (4MB)
        BitmapAjaxCallback.setMaxPixelLimit(2000000);
    }


    @Override
    public void onLowMemory() {
        BitmapAjaxCallback.clearCache();
    }

    /**
     * @return
     */
    public static AQuery getAQueryInstance() {
        if (aq != null) {
            return aq;
        } else {
            aq = new AQuery(MariskalRockApplication.getInstance());
            aq.hardwareAccelerated11();
            return aq;
        }
    }

    public static DatabaseManager getDatabaseManagerInstance() {

        if (databaseManager != null) {
            return databaseManager;
        } else {
            databaseManager = new DatabaseManager(MariskalRockApplication.getInstance());
            return databaseManager;
        }
    }

}
