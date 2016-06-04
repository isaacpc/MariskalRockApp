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
import com.isaacpc.mariskalrock.adapter.PodcastsAdapter;
import com.isaacpc.mariskalrock.bd.PodcastEntity;
import com.isaacpc.mariskalrock.common.BroadcastConstants;
import com.isaacpc.mariskalrock.common.Constants;
import com.isaacpc.mariskalrock.common.Constants.Categoria;
import com.isaacpc.mariskalrock.log.Log;

public class PodcastFragment extends FragmentBase {

    private static final String LOG_TAG = "PodcastActivity";
    private List<PodcastEntity> itemList;

    // handler for received Intents for the "my-event" event
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctx, Intent intent) {
            Log.i(LOG_TAG, "Se actualiza podcasts a través del Receiver");

            mSwipeRefreshLayout.setRefreshing(false);
            itemList = databaseManager.getPodcastsEntityList();

            showDataInList();
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume()");

        configureEvents(Categoria.PODCAST);
        this.getActivity().registerReceiver(receiver, new IntentFilter(BroadcastConstants.BROADCAST_PODCASTS));
        // se lanza la tarea de actualizacion
        new LoaderAsyncTask().execute();

    }


    @Override
    public void onPause() {
        Log.i(LOG_TAG, "onPause()");
        // Unregister since the activity is not visible
        this.getActivity().unregisterReceiver(receiver);
        super.onPause();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved) {
        return inflater.inflate(R.layout.lista_generica, group, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     *
     */
    private void showDataInList() {
        showListView();
        listFeed.setAdapter(new PodcastsAdapter(MariskalRockApplication.getInstance(), itemList));
    }

    // Inner Class para cargar la bbdd y demás al cargar el activity si no
    // los
    // tiene
    private class LoaderAsyncTask extends
            AsyncTask<Void, Void, List<PodcastEntity>> {

        @Override
        protected List<PodcastEntity> doInBackground(Void... voids) {

            // carga la BBDD
            databaseManager = MariskalRockApplication.getDatabaseManagerInstance();

            // carga el AQ
            aq = MariskalRockApplication.getAQueryInstance();

            // se obtienen los datos de BBDD
            final List<PodcastEntity> list = databaseManager.getPodcastsEntityList();

            return list;
        }

        @Override
        protected void onPreExecute() {

            showLoadingView();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<PodcastEntity> list) {

            if (list == null || list.isEmpty()) {

                // Si no hay datos, los descarga
                callFeedUpdaterService(Constants.Categoria.PODCAST);

            } else {
                // Muestra los datos en la lista
                showListView();
                listFeed.setAdapter(new PodcastsAdapter(MariskalRockApplication.getInstance(), list));
            }

            super.onPostExecute(list);
        }
    }
}
