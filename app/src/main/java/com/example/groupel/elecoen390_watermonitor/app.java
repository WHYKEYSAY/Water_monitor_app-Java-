package com.example.groupel.elecoen390_watermonitor;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class app extends Application {
    public static final String CHANNEL_ALARM_ID = "alarmnotificationchannel";

    @Override
    public void onCreate(){
        super.onCreate();

        createNotificationchannels();
    }

    private void createNotificationchannels(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channelAlarm = new NotificationChannel(
                    CHANNEL_ALARM_ID,
                    "alarm channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channelAlarm);
        }
    }
}
