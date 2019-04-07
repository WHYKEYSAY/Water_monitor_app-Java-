package com.example.groupel.elecoen390_watermonitor;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.anastr.speedviewlib.SpeedView;

import java.util.Random;

public class Meter extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NotificationManagerCompat notificationManagerCompat;
    private Dialog waterDialog;
    private ImageView closeBad, closeGood, closeOk;
    private TextView titleBad, titleGood, titleOk;
    private Button history, detail, alarmTest,test;
    private int x=0;
    private DrawerLayout drawer;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.nav_filter:
                Intent intent = new Intent(Meter.this, FilterListView.class);
                startActivity(intent);
                break;
            case R.id.nav_alarm:
                notificationManagerCompat = NotificationManagerCompat.from(this);
                pushAlarmNotification();
                cancelAlarm();//avoid duplicates
                startAlarmSystem();
                break;
            case R.id.nav_history:
                Intent intent1 = new Intent(Meter.this, TableActivity.class);
                startActivity(intent1);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_main);
        waterDialog = new Dialog(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView =findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);







        test = findViewById(R.id.test_btn);
        test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SpeedView speedometer = findViewById(R.id.speedView);

                speedometer.setWithTremble(false);
                Random rand = new Random();
                x = rand.nextInt(101);
                speedometer.speedTo(x);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(x<30) {
                            ShowBadPopup();
                        }
                        else if(x>=30 && x<80) {
                            ShowOkPopup();
                        }
                        else {
                            ShowGoodPopup();
                        }
                    }
                },2500);
            }
        });
        /*
        history = findViewById(R.id.history_button);
        history.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(Meter.this, TableActivity.class);
                startActivity(intent);
            }
        });
*/
       // notificationManagerCompat = NotificationManagerCompat.from(this);
        //alarmTest = findViewById(R.id.alarmTest_button);
        //alarmTest.setOnClickListener(new View.OnClickListener(){
           // public void onClick(View v) {
             //   pushAlarmNotification();
          //  }
      //  });

       // cancelAlarm();//avoid duplicates
      //  startAlarmSystem();
   }








    public void pushAlarmNotification(){
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

    public void ShowBadPopup(){
        waterDialog.setContentView(R.layout.popup_window_bad);
        closeBad=(ImageView)waterDialog.findViewById(R.id.closePopupBad);
        titleBad =(TextView) waterDialog.findViewById(R.id.titleBad);

        detail = (Button)waterDialog.findViewById(R.id.detailedBtnBad);
        detail.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(Meter.this, detailedInfo.class);
                startActivity(intent);
            }
        });

        closeBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waterDialog.dismiss();
            }
        });

        waterDialog.show();
    }
    public void ShowOkPopup(){
        waterDialog.setContentView(R.layout.popup_window_ok);
        closeOk=(ImageView)waterDialog.findViewById(R.id.closePopupOk);
        titleOk =(TextView) waterDialog.findViewById(R.id.titleOk);

        detail = (Button)waterDialog.findViewById(R.id.detailedBtnOk);
        detail.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(Meter.this, detailedInfo.class);
                startActivity(intent);
            }
        });
        closeOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waterDialog.dismiss();
            }
        });

        waterDialog.show();

    }
    public  void ShowGoodPopup(){
        waterDialog.setContentView(R.layout.popup_window_good);
        closeGood=(ImageView)waterDialog.findViewById(R.id.closePopupGood);
        titleGood =(TextView) waterDialog.findViewById(R.id.titleGood);

        detail = (Button)waterDialog.findViewById(R.id.detailedBtnGood);
        detail.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(Meter.this, detailedInfo.class);
                startActivity(intent);
            }
        });
        closeGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waterDialog.dismiss();
            }
        });

        waterDialog.show();

    }

    //default app methods
    protected void onStart() {//after the OnCreate() is called, this function will be called.
        super.onStart();
    }

    protected void onResume() {//This function will be called either when onStart() is called
        // or the activity is resume from function onPause().
        super.onResume();
    }

    protected void onStop() {//This function is called when the app stops running.
        // The data-saving process is also defined inside this function
        super.onStop();
    }

    protected void onPause() {//When another activity is taking priority,
        // this function is called and activity is on hold.
        super.onPause();
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

