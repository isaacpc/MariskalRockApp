package com.isaacpc.mariskalrock.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.TextView;

import com.isaacpc.mariskalrock.MariskalRockApplication;
import com.isaacpc.mariskalrock.R;
import com.isaacpc.mariskalrock.log.Log;

public class SplashScreenActivity extends Activity {
    protected boolean _active = true;
    private final int SPLASH_DISPLAY_LENGHT = 2500;
    protected MariskalRockApplication appState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_screen);

        // sets screen orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        showVersionNumber();

        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // Obtain the sharedPreference, default to true if not available
        final boolean isSplashEnabled = sp.getBoolean("isSplashEnabled", true);

        if (isSplashEnabled) {
            splashWithoutAnimation();
        } else {
            gotoApp();
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    private void showVersionNumber() {

        String versionName;
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (final NameNotFoundException e) {
            versionName = null;
            Log.w(this.getClass().toString(), "VersionName not found");
        }

        final TextView versionText = (TextView) findViewById(R.id.versionName);
        versionText.setText("v." + versionName);
    }

    private void splashWithoutAnimation() {
    /*
     * New Handler to start the Menu-Activity and close this Splash-Screen
	 * after some seconds.
	 */
        new Handler().postDelayed(new Runnable() {
            public void run() {
                gotoApp();
            }
        }, SPLASH_DISPLAY_LENGHT);
    }

    private void gotoApp() {
        final Intent mainIntent = new Intent(SplashScreenActivity.this, FragmentTabsActivity.class);
        SplashScreenActivity.this.startActivity(mainIntent);
        SplashScreenActivity.this.finish();
    }
}
