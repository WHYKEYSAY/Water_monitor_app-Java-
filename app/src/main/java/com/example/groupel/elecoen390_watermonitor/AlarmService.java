package com.example.groupel.elecoen390_watermonitor;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;

public class AlarmService extends IntentService {

    public AlarmService() {
        super("WaterMonitorAlarm");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //setup test and notification here
        //reset alarm
        cancelAlarm();//avoid duplicates
        startAlarmSystem();
    }

    private void cancelAlarm(){
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                AlarmReceiver.REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pendingIntent);
    }

    private void startAlarmSystem(){
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                AlarmReceiver.REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarm.setWindow(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                    AlarmManager.INTERVAL_FIFTEEN_MINUTES + 5 * 60 * 1000,//5 min interval to allow for battery optimisation
                    pendingIntent);
        }
        else{
            alarm.set(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                    pendingIntent);
        }
    }
}
