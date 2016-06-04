package com.isaacpc.mariskalrock.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.isaacpc.mariskalrock.R;
import com.isaacpc.mariskalrock.common.Constants;
import com.isaacpc.mariskalrock.utils.ConnectionUtils;
import com.isaacpc.mariskalrock.utils.MRAnimationUtils;
import com.isaacpc.mariskalrock.utils.PreferencesUtils;

import java.io.IOException;

public class RadioFragment extends Fragment implements OnClickListener {
    private static final String LOG_TAG = "RadioFragment";

    private ImageButton playButton;
    private ImageButton stopButton;
    private ProgressBar progress;
    private TextView txtRadioMessage;

    private boolean playerStarted;
    private Handler uiHandler;

    // Mantiene bloqueado el Wifi si se está retransmitiendo para que no se
    // pierda la señal.
    WifiLock mWifiLock;
    Boolean isLoading = false;

    private MediaPlayer player;

    private Context context;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            final Activity activity = getActivity();
            if (activity != null)
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved) {
        return inflater.inflate(R.layout.radio, group, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        context = this.getActivity();
        catchComponents();

        if (!playerStarted) {
            activeStartButton();
        } else {
            activeStopButton();
        }

        playButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);

        uiHandler = new Handler();

        // Create the Wifi lock (this does not acquire the lock, this just
        // creates it)
        mWifiLock = ((WifiManager) this.getActivity().getSystemService(Context.WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
    }

    protected void catchComponents() {
        playButton = (ImageButton) this.getActivity().findViewById(R.id.playButton);
        stopButton = (ImageButton) this.getActivity().findViewById(R.id.stopButton);
        progress = (ProgressBar) this.getActivity().findViewById(R.id.pbRadio);
        txtRadioMessage = (TextView) this.getActivity().findViewById(R.id.txtRadioMessage);
    }

    /**
     * Called when a view has been clicked.
     */
    @Override
    public void onClick(View v) {

        try {
            if (v.getId() == R.id.playButton) {
                // this.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                stopPlaying();
                if (!thereAreNetwork()) {
                    Log.w(LOG_TAG, "No hay conexion de red");
                    txtRadioMessage.setText(context.getResources().getString(
                            R.string.radio_no_conexion));
                } else {
                    // si player es nulo entonces si ejecuta la radio.
                    if (player == null) {
                        activeStopButton();
                        isLoading = true;
                        startPlaying();
                    } else {
                        Log.i(LOG_TAG, "Hay una instancia de la radio funcionando. No se activa");
                    }
                }
            } else if (v.getId() == R.id.stopButton) {
                // this.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                stopPlaying();
                MRAnimationUtils.setAlphaRadioButtons(context, stopButton,
                        R.anim.button_off);
                activeStartButton();
                progress.setProgress(0);

            } else {
                Log.d(LOG_TAG, "Se ha hecho click en un evento no controlado: " + v.getId());
            }

        } catch (final Exception e) {
            Log.e("AACPlayerActivity", e.toString());
        }
    }


    /**
     * Muestra el botón de start y deshabilita el de stop
     */
    private void activeStartButton() {
        stopButton.setVisibility(View.VISIBLE);
        playButton.setVisibility(View.VISIBLE);

        MRAnimationUtils.setAlphaRadioButtons(context, stopButton, R.anim.button_off);
        MRAnimationUtils.setAlphaRadioButtons(context, playButton, R.anim.button_on);

        playButton.setEnabled(true);
        stopButton.setEnabled(false);

        progress.setProgress(0);
        txtRadioMessage.setText(this.getActivity().getApplicationContext().getResources().getString(R.string.radio_instrucciones));
    }

    /**
     * Muestra el botón de stop y deshabilita el de start
     */
    private void activeStopButton() {
        stopButton.setVisibility(View.VISIBLE);
        playButton.setVisibility(View.VISIBLE);

        MRAnimationUtils.setAlphaRadioButtons(context, playButton, R.anim.button_off);
        MRAnimationUtils.setAlphaRadioButtons(context, stopButton, R.anim.button_on);

        playButton.setEnabled(false);
        stopButton.setEnabled(true);

        txtRadioMessage.setText(this.getActivity().getApplicationContext().getResources().getString(R.string.radio_reproduciendo));
        progress.setProgress(0);
    }


    private void startPlaying() {
        initializeMediaPlayer();
        player.prepareAsync();
        player.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                player.start();
                playerStarted();
            }
        });
    }

    private void stopPlaying() {
        if (mWifiLock != null && mWifiLock.isHeld()) {
            mWifiLock.release();
        }

        if (player != null && player.isPlaying()) {
            player.stop();
            player = null;

            playerStopped();
        }
    }

    private void initializeMediaPlayer() {
        player = new MediaPlayer();
        try {
            player.setDataSource(Constants.STREAMING_AUDIO_URL);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                progress.setSecondaryProgress(percent);
                Log.i("Buffering", "" + percent);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
//        this.stopPlaying();
    }

    /**
     * Comrpueba que hay conexión.
     *
     * @return
     */
    private boolean thereAreNetwork() {

        boolean result = true;

        // No hay conexión, se libera el wifi
        if (!ConnectionUtils.hasConnection(context)) {
            Log.w(LOG_TAG, "No hay conexión. No Radio. Se libera Wifi");
            // Si no hay conexión libera el wifi.
            if (mWifiLock.isHeld()) {
                mWifiLock.release();
            }
            result = false;

            // Hay conexión wifi y se puede usar la radio (va a usar wifi
            // entonces), se obtiene la wifi
        } else if (ConnectionUtils.isWifiConnected(context) && PreferencesUtils.isUsableRadio(context)) {
            Log.i(LOG_TAG, "Hay Wifi. Se puede usar radio. Se bloquea Wifi");
            mWifiLock.acquire();
            result = true;

            // Hay conexión (Si no entra en el anterior es porque es 3G. Se usa
            // la radio
        } else if (ConnectionUtils.hasConnection(context) && PreferencesUtils.isUsableRadio(context)) {
            Log.i(LOG_TAG, "Hay Red.No debería ser wifi. Si Radio. Se libera Wifi");
            // hay conexión por lo que se bloquea el wifi
            if (mWifiLock.isHeld()) {
                mWifiLock.release();
            }
            result = true;

        } else {
            Log.w(LOG_TAG, "Hay red.Radio no es usable (pref). No debería darse.");
            // Este estado no se debería dar
            if (mWifiLock.isHeld()) {
                mWifiLock.release();
            }

            result = false;
        }

        return result;
    }


    /**
     * Empieza a mover el progress
     */
    public void playerStarted() {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                progress.setProgress(0);
                playerStarted = true;

                // Cambios en UI
                txtRadioMessage.setText(context.getResources().getString(R.string.radio_reproduciendo));
                isLoading = false;
                MRAnimationUtils.setAlphaRadioButtons(context, stopButton, R.anim.button_on);

            }
        });
    }


    /**
     * Se detiene el reproductor
     */
    public void playerStopped() {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                playerStarted = false;
            }
        });
    }


}