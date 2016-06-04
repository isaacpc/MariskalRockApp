package com.isaacpc.mariskalrock.thread;

import android.content.Context;

public class FeedScheduler {

        //        private static final String LOG_TAG = "FeedScheduler";

        private Context context;

        public FeedScheduler(Context context) {
                super();
                this.context = context;
        }


        /**
         * Se ejecutará una única vez dentro de 'when' segundos el componente registrado
         * @param when Numero de segundos
         */
        public void scheduleNoticias(){
                //
                //                Log.i(LOG_TAG, "Lanza un schedule de noticias");
                //
                //                final Intent intent = new Intent(context, UpdateFeedReceiver.class);
                //                final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);      
                //
                //                final AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                //                am.cancel(pendingIntent); // cancel any existing alarms

                //                final String updateTimeString = PreferencesUtils.getPreferenceValue(Constants.PREFERENCES_FRECUENCIA_SINCRONIZACION, context);
                //                final String updateConnection = PreferencesUtils.getPreferenceValue(Constants.PREFERENCES_FRECUENCIA_CONEXION, this);

                //                final Integer updateTime = Integer.valueOf(updateTimeString);

                //                if(updateTime>0){
                //                        am.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), 10000 , pendingIntent);
                //                am.set(AlarmManager.RTC_WAKEUP,1000, pendingIntent);
                //                                                am.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), updateTime * Constants.HOUR_IN_MILLISECONDS , pendingIntent);
                //                }
        }






        /**
         * @return the context
         */
        public Context getContext() {
                return context;
        }

        /**
         * @param context the context to set
         */
        public void setContext(Context context) {
                this.context = context;
        }

}
