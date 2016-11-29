package edu.unt.transportation.bustrackingsystem;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Satyanarayana on 11/23/2016.
 */
public class NotificationReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("NotificationReceiver", "onReceive");
        intent.setComponent(new ComponentName(context,LocationSharingService.class));
        context.stopService(intent);
    }
}
