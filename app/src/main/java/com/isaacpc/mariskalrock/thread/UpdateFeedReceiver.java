package com.isaacpc.mariskalrock.thread;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.isaacpc.mariskalrock.log.Log;

/**
 * Tareas a realizar cuando la alarma sea lanzada por Android
 */

public class UpdateFeedReceiver extends android.content.BroadcastReceiver {


        private static final String LOG_TAG = "UpdateReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {

                Log.d(LOG_TAG, "onReceive() : Se actualizan las noticias desde la alarma.");

                Toast.makeText(context, "Tu lógica de negocio irá aquí. En caso de requerir más de unos milisegundos, debería de la tarea a un servicio", 
                                Toast.LENGTH_LONG).show();


                /*
                 * HAY QUE CREAR UN SERVICIO QUE SIMPLEMENTE ACTUALICE LA BBDD CADA X TIEMPO. 
                 * AL ARRANCAR LOS FRAGMENTS, CARGARÁN LOS DATOS DE LA BBDD.
                 * 
                 * ¿CUANDO REINICIAR EL CONTADOR?
                 * 
                 * DESHABILITAR EL CONTADOR AL ENTRAR EN LA APP Y HABILITARLO AL SALIR CON LOS VALORES DE LAS PREFERENCIAS.
                 * 
                 * 
                 * 
                 */
                //
                //                final Intent msgIntent = new Intent(context, FeedUpdaterService.class);
                //                msgIntent.putExtra(FeedUpdaterService.FEED_CATEGORIA, Constants.Categoria.NOTICIAS);
                //                msgIntent.putExtra(FeedUpdaterService.SOURCE_NOTICIAS_BROADCAST, BroadcastConstants.BROADCAST_NOTICIAS);
                //                context.startService(msgIntent);
        }
}