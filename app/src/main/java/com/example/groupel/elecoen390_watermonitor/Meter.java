package com.example.groupel.elecoen390_watermonitor;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class Meter extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NotificationManagerCompat notificationManagerCompat;
    private Dialog waterDialog;
    private ImageView closeBad, closeGood, closeOk;
    private TextView titleBad, titleGood, titleOk;
    private Button history, detail, alarmTest,test;
    private int x=0;
    private DrawerLayout drawer;
    private static int read_count;//keep track of entry on cloud

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

        SharedPreferences sharedPref = getApplicationContext()
                .getSharedPreferences(getString(R.string.saved_count), Context.MODE_PRIVATE);
        read_count = sharedPref.getInt("key_count", 0);//if never saved (new install), it's 0
        startCloudListener();

        waterDialog = new Dialog(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView =findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setupWaterInfoCard();

        test = findViewById(R.id.test_btn);
        test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //request measurement
                FirebaseDatabase.getInstance().getReference("measure").setValue(1);//set 'measure' on cloud
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
      cancelAlarm();//avoid duplicates
      startAlarmSystem();
   }

   private void setupWaterInfoCard(){
       CardView message = findViewById(R.id.current_status);
       TextView warning = findViewById(R.id.titleBad);
       ImageView icon = findViewById(R.id.quality_icon);

       waterMonitordbHelper dbhelper = new waterMonitordbHelper(getApplicationContext());
       ArrayList<turbidity> allTurb = dbhelper.getAllTurbidity();

       if (!allTurb.isEmpty()) {
           Integer last_turb = allTurb.get(allTurb.size() - 1).getTurb();
           if (last_turb >= 20) {//value is in NTU
               message.setCardBackgroundColor(Color.RED);
               warning.setText("Your Water is Bad! \nAdvise: Avoid drinking");
               icon.setImageResource(R.drawable.ic_bad_black_24dp);
           } else if (last_turb >= 5 && x < 20) {
               message.setCardBackgroundColor(Color.YELLOW);
               warning.setText("Your Water is OK! \nAdvise: Boil before drinking");
               icon.setImageResource(R.drawable.ic_ok_black_24dp);
           } else {
               message.setCardBackgroundColor(Color.GREEN);
               warning.setText("Your Water is Good!");
               icon.setImageResource(R.drawable.ic_good_black_24dp);
           }
       }
       detail = (Button)findViewById(R.id.detailedBtn);
       detail.setOnClickListener(new View.OnClickListener(){
           public void onClick(View v) {
               Intent intent = new Intent(Meter.this, detailedInfo.class);
               startActivity(intent);
           }
       });
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

    public void startCloudListener(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference allref = database.getReference();

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer measure_flag;
                measure_flag = dataSnapshot.child("measure").getValue(Integer.class);
                if (measure_flag == 0){//wait for measurement to end
                    int i = 1;//online count starts at 1 if this runs
                    for (DataSnapshot t_list : dataSnapshot.child("turbs").getChildren()) { //go through all measurements
                        if(i <= read_count){//skip old values
                            i++;
                            continue;
                        }
                        waterMonitordbHelper localDB = new waterMonitordbHelper(getApplicationContext());
                        turbidity turb = new turbidity();
                        spectroMeasure new_meas = new spectroMeasure();
                        new_meas.setDate(new java.sql.Date(new java.util.Date().getTime()));
                        turb.setMeasurementID(localDB.createSpectroMeasure(new_meas));
                        turb.setDate(new java.sql.Date(new java.util.Date().getTime()));
                        Integer sensor_out = t_list.getValue(Integer.class);
                        Log.d(TAG, "Saved some data to local DB: " + sensor_out);
                        //based on empirical characterisation: y = 0.0028x^2 - 6.8856x + 4068.6
                        turb.setTurb((int) (0.0028 * sensor_out.floatValue() * sensor_out.floatValue()
                                - 6.8856 * sensor_out.floatValue()
                                + 4068.6));
                        localDB.createTur(turb);
                    }
                    Integer online_count = dataSnapshot.child("counter").getValue(Integer.class);
                    read_count = online_count;
                    getApplicationContext().getSharedPreferences(getString(R.string.saved_count), Context.MODE_PRIVATE)
                            .edit().putInt("key_count", read_count).commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Error: Firebase listener failed", databaseError.toException());
            }
        };
        allref.addValueEventListener(listener);
    }
}

