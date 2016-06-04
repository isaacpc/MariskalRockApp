package com.isaacpc.mariskalrock.fragment;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isaacpc.mariskalrock.MariskalRockApplication;
import com.isaacpc.mariskalrock.R;
import com.isaacpc.mariskalrock.adapter.VideoAdapter;
import com.isaacpc.mariskalrock.bd.VideoEntity;
import com.isaacpc.mariskalrock.common.BroadcastConstants;
import com.isaacpc.mariskalrock.common.Constants;
import com.isaacpc.mariskalrock.common.Constants.Categoria;
import com.isaacpc.mariskalrock.log.Log;

public class VideoFragment extends FragmentBase {

    private static final String LOG_TAG = "VideoFragment";

    // Lista que se muestra en esta pantalla
    private List<VideoEntity> itemList;

    // handler for received Intents for the "my-event" event
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctx, Intent intent) {
            Log.i(LOG_TAG, "Se actualiza videos a través del Receiver");

            mSwipeRefreshLayout.setRefreshing(false);
            itemList = databaseManager.getVideosEntityList();

            showDataInList();
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "OnResume()");

        configureEvents(Categoria.VIDEO);

        this.getActivity().registerReceiver(receiver, new IntentFilter(BroadcastConstants.BROADCAST_VIDEOS));
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
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved) {
        return inflater.inflate(R.layout.lista_generica, group, false);
    }


    /**
     *
     */
    private void showDataInList() {
        showListView();
        listFeed.setAdapter(new VideoAdapter(MariskalRockApplication.getInstance(), itemList));
    }

    // Inner Class para cargar la bbdd y demás al cargar el activity si no
    // los
    // tiene
    private class LoaderAsyncTask extends
            AsyncTask<Void, Void, List<VideoEntity>> {

        @Override
        protected List<VideoEntity> doInBackground(Void... voids) {

            // carga la BBDD
            databaseManager = MariskalRockApplication.getDatabaseManagerInstance();

            // carga el AQ
            aq = MariskalRockApplication.getAQueryInstance();

            // se obtienen los datos de BBDD
            final List<VideoEntity> list = databaseManager.getVideosEntityList();

            return list;
        }

        @Override
        protected void onPreExecute() {

            showLoadingView();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<VideoEntity> list) {

            if (list == null || list.isEmpty()) {

                // Si no hay datos, los descarga, los parsea y
                // los guarda en
                // base de datos en un hilo diferente.
                Log.i(LOG_TAG, "No hay datos en la BBDD. Descarga el Feed.");
                callFeedUpdaterService(Constants.Categoria.VIDEO);

            } else {
                // Muestra los datos en la lista
                showListView();
                listFeed.setAdapter(new VideoAdapter(MariskalRockApplication.getInstance(), list));
            }

            super.onPostExecute(list);
        }
    }
}
