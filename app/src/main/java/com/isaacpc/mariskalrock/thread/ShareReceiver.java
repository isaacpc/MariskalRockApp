package com.isaacpc.mariskalrock.thread;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public abstract class ShareReceiver extends BroadcastReceiver {

    public static final String PARAM_LINK = "link";

    @Override
    public void onReceive(Context ctx, Intent intent) {


        manageBroadcast(intent);
    }

    public abstract void manageBroadcast(Intent intent);
}
