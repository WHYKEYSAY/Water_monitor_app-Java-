package com.example.groupel.elecoen390_watermonitor;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.ArrayList;

public class AlarmService extends IntentService {
    private NotificationManagerCompat notificationManagerCompat;
    private static long alarmID;

    public AlarmService() {
        super("WaterMonitorAlarm");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //setup test and notification here
        waterMonitordbHelper dbhelper = new waterMonitordbHelper(getApplicationContext());
        ArrayList<turbidity> allTurb = dbhelper.getAllTurbidity();
        if (!allTurb.isEmpty()){
            turbidity turb = allTurb.get(allTurb.size() - 1);
            if (turb.getTurb() >= 20 && alarmID != turb.getID()){
                pushAlarmNotification();
                alarmID = turb.getID();
            }
        }
        //reset alarm
        cancelAlarm();//avoid duplicates
        startAlarmSystem();
    }
    public void pushAlarmNotification(){
        notificationManagerCompat = NotificationManagerCompat.from(this);

        Intent notifIntent = new Intent(this, Meter.class);

        PendingIntent startAppIntent = PendingIntent.getActivity(this, 0, notifIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, app.CHANNEL_ALARM_ID)
                .setSmallIcon(R.drawable.ic_announcement)
                .setContentTitle("Water Monitor")
                .setContentText("Detected issue in water.")
                .setContentIntent(startAppIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)//sound/vibration + front display
                .build();

        notificationManagerCompat.notify(1, notification);
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
