package com.isaacpc.mariskalrock.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.isaacpc.mariskalrock.R;
import com.isaacpc.mariskalrock.activity.PreferencesActivity;
import com.isaacpc.mariskalrock.bd.DatabaseManager;
import com.isaacpc.mariskalrock.common.BroadcastConstants;
import com.isaacpc.mariskalrock.common.Constants.Categoria;
import com.isaacpc.mariskalrock.log.Log;
import com.isaacpc.mariskalrock.thread.FeedUpdaterService;
import com.isaacpc.mariskalrock.thread.ShareReceiver;
import com.isaacpc.mariskalrock.utils.ConnectionUtils;

public abstract class FragmentBase extends Fragment {

    private static final String LOG_TAG = "FragmentBase";
    /**
     * Constantes para los botones del actionBar
     */
    protected static final int ACTION_BAR_REFRESH = 1;
    protected static final int ACTION_BAR_INFO = 2;

    protected AQuery aq;

    protected DatabaseManager databaseManager;
    protected RelativeLayout loadingLayout;
    protected RelativeLayout errorLayout;
    protected Button btRetry;
    protected BroadcastReceiver shareBroadcastReceiver;

    protected ListView listFeed;
    protected SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.lista_generica, container, false);
        return root;
    }

    protected void catchComponents() {
        Log.i(LOG_TAG, "Se capturan los componentes");
        mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.activity_main_swipe_refresh_layout);
        listFeed = (ListView) getView().findViewById(R.id.listFeed);
        loadingLayout = (RelativeLayout) getView().findViewById(R.id.loadingLayout);
        errorLayout = (RelativeLayout) getView().findViewById(R.id.errorLayout);
        btRetry = (Button) getView().findViewById(R.id.btRetry);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            final Activity a = getActivity();
            if (a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }
    }

    /**
     * Configura los eventos para los elementos
     */
    protected void configureEvents(final Categoria categoria) {
        Log.i(LOG_TAG, "Se configuran los eventos: " + categoria);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callFeedUpdaterService(categoria);
            }
        });

        btRetry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.w(LOG_TAG, "Se ejecuta el reintento");
                callFeedUpdaterService(categoria);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.i(LOG_TAG, "onActivityCreated");

        configureBroadcasts();
        catchComponents();
    }

    /**
     * Configura los broadcasts
     */
    protected final void configureBroadcasts() {
        // handler for received Intents for the "my-event" event
        shareBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctx, Intent intent) {
                final Bundle bundle = intent.getExtras();
                final String url = bundle.getString(ShareReceiver.PARAM_LINK);
                share(url);
            }
        };
    }

    /**
     * Muestra las preferencias
     */
    public void showPreferences(Activity activity) {
        final Intent intent = new Intent(activity, PreferencesActivity.class);
        startActivityForResult(intent, 10);
    }

    /**
     * Muestra la pantalla de cargando
     */
    protected void showLoadingView() {
        loadingLayout.setVisibility(View.VISIBLE);
//                listFeed.setVisibility(View.INVISIBLE);
        errorLayout.setVisibility(View.INVISIBLE);
    }

    /**
     * Muestra la lista de resultados
     */
    protected void showListView() {

        loadingLayout.setVisibility(View.INVISIBLE);
        listFeed.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.INVISIBLE);
    }

    /**
     * Muestra la pantalla de error
     */
    protected void showErrorView() {
        Log.w(LOG_TAG, "Pinta la pantalla de error");
        loadingLayout.setVisibility(View.INVISIBLE);
        listFeed.setVisibility(View.INVISIBLE);
        errorLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Descarga el feed de la red
     */
    public void callFeedUpdaterService(final Categoria categoria) {

        Log.i(LOG_TAG, "callFeedUpdaterService. No hay datos en BBDD de la categoria " + categoria + ". Se van a descargar si hay conexión.");

        if (ConnectionUtils.hasConnection(this.getActivity())) {

            Log.i(LOG_TAG, "No hay datos en la BBDD. Descarga el Feed.");
            final Intent msgIntent = new Intent(this.getActivity(), FeedUpdaterService.class);

            //para indicar que viene de la app y no de los widgets
            if (categoria == Categoria.NOTICIAS) {
                msgIntent.putExtra(FeedUpdaterService.SOURCE_NOTICIAS_BROADCAST, BroadcastConstants.BROADCAST_NOTICIAS);
            }

            msgIntent.putExtra(FeedUpdaterService.FEED_CATEGORIA, categoria);
            this.getActivity().startService(msgIntent);
        } else {

            Log.i(LOG_TAG, "No hay conexión. Se muestra la pantalla de error con el Retry.");
            if (listFeed != null && listFeed.getAdapter() != null && listFeed.getAdapter().getCount() > 0) {
                // TODO mostrar crouton
                Log.i(LOG_TAG, "Mostrar crouton");
            } else {
                showErrorView();
            }
        }
    }

    @Override
    public void onResume() {
        // Crea el receiver para actualizar la lista cuando se ha cambiado algo
        this.getActivity().registerReceiver(shareBroadcastReceiver, new IntentFilter(BroadcastConstants.BROADCAST_SHARE));

        super.onResume();
    }

    @Override
    public void onPause() {
        // Unregister since the activity is not visible
        this.getActivity().unregisterReceiver(shareBroadcastReceiver);
        super.onPause();
    }

    /**
     * Comparte un contenido
     */
    protected final void share(final String url) {
        Log.i(LOG_TAG, "share(): " + url);
        final Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, this.getActivity().getString(R.string.intro_share));
        intent.putExtra(Intent.EXTRA_TEXT, url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.getActivity().startActivity(Intent.createChooser(intent, this.getActivity().getString(R.string.reproducciones_subtitle)));
    }
}