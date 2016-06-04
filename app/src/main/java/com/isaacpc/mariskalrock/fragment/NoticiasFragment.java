package com.isaacpc.mariskalrock.fragment;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;

import com.isaacpc.mariskalrock.MariskalRockApplication;
import com.isaacpc.mariskalrock.adapter.NoticiasAdapter;
import com.isaacpc.mariskalrock.bd.NoticiaEntity;
import com.isaacpc.mariskalrock.common.BroadcastConstants;
import com.isaacpc.mariskalrock.common.Constants.Categoria;
import com.isaacpc.mariskalrock.log.Log;

public class NoticiasFragment extends FragmentBase {

        private static final String LOG_TAG = "NoticiasFragment";

        // Lista que se muestra en esta pa
        private List<NoticiaEntity> itemList;


        // handler for received Intents for the "my-event" event 
        private final BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context ctx, Intent intent) {
                Log.i(LOG_TAG, "Se actualiza Noticias a través del Receiver");
                mSwipeRefreshLayout.setRefreshing(false);
                itemList = databaseManager.getNoticiasEntityList();
                showDataInList();
                }
        };


        @Override
        public void onResume() {
                super.onResume();
                Log.i(LOG_TAG, "OnResume()");

                //configura los eventos de la pantalla para la sección de noticias
                configureEvents(Categoria.NOTICIAS);

                //registra el receiver para actualizar las noticias
                this.getActivity().registerReceiver(receiver, new IntentFilter(BroadcastConstants.BROADCAST_NOTICIAS));

                //Carga las noticias en la lista
                new LoaderAsyncTask().execute();
        }


        @Override
        public void onPause() {
                Log.i(LOG_TAG, "OnPause()");
                // Unregister since the activity is not visible
                this.getActivity().unregisterReceiver(receiver);
                super.onPause();
        } 


        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);

                Log.i(LOG_TAG, "onActivityCreated()");
        }

        /**
         * 
         */
        private void showDataInList() {
                showListView();
                listFeed.setAdapter(new NoticiasAdapter(MariskalRockApplication.getInstance(), itemList));
        }

        // Inner Class para cargar la bbdd y demás al cargar el activity si no los
        // tiene
        public class LoaderAsyncTask extends AsyncTask<Void, Void, List<NoticiaEntity>> {

                @Override
                protected List<NoticiaEntity> doInBackground(Void... voids) {

                        // carga la BBDD
                        databaseManager = MariskalRockApplication.getDatabaseManagerInstance();

                        // carga el AQ
                        aq = MariskalRockApplication.getAQueryInstance();

                        // se obtienen los datos de BBDD
                        final List<NoticiaEntity> list = databaseManager.getNoticiasEntityList();

                        return list;
                }

                @Override
                protected void onPreExecute() {
                        showLoadingView();
                        super.onPreExecute();
                }

                @Override
                protected void onPostExecute(List<NoticiaEntity> list) {

                        if (list.isEmpty()) {

                                // Si no hay datos, los descarga
                                callFeedUpdaterService(Categoria.NOTICIAS);

                        } else {
                                // Muestra los datos en la lista
                                showListView();
                                listFeed.setAdapter(new NoticiasAdapter(MariskalRockApplication.getInstance(), list));
                        }
                        super.onPostExecute(list);
                }
        }

}