package com.example.groupel.elecoen390_watermonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;;

public class AlarmReceiver extends BroadcastReceiver {
    public final static int REQUEST_CODE = 98765;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service_intent = new Intent(context, AlarmService.class);
        context.startService(service_intent);
    }
}
