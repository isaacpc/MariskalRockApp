package com.isaacpc.mariskalrock.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DeviceUtils {

    /**
     * Obtiene las dimensiones de la pantalla
     * 
     * @param ctx
     * @return
     */
    public static DisplayMetrics getDisplayDimensions(Context ctx) {

	WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);

	DisplayMetrics metrics = new DisplayMetrics();
	wm.getDefaultDisplay().getMetrics(metrics);

	return metrics;
    }
}
