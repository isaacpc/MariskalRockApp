package com.isaacpc.mariskalrock.activity;

import com.isaacpc.mariskalrock.R;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.isaacpc.mariskalrock.log.Log;

public class AboutActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.about);

        final TextView aboutText = (TextView) findViewById(R.id.about);
        aboutText.setMovementMethod(LinkMovementMethod.getInstance());

        String versionName;
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (final NameNotFoundException e) {
            versionName = null;
            Log.w(this.getClass().toString(), "VersionName not found.");
        }

        final TextView versionText = (TextView) findViewById(R.id.versionName);
        versionText.setText(versionName);

    }
}
