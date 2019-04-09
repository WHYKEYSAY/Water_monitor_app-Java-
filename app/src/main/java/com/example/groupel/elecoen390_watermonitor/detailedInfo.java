package com.example.groupel.elecoen390_watermonitor;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.listeners.TableDataClickListener;

public class detailedInfo extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    String[] infoHeader = { "Names"};

    String[][] infos;



    private Dialog detailInfo,detailInfo1, detailInfo2;
    private ImageView  closeGood;
    private TextView titleGeneral, general, titleHealth, Healthgeneral,titleGeneral1, general1, titleHealth1, Healthgeneral1,titleGeneral2, general2, titleHealth2, Healthgeneral2;
    private Button filter;
    private NotificationManagerCompat notificationManagerCompat;
    private DrawerLayout drawer;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.nav_filter:
                Intent intent = new Intent(detailedInfo.this, FilterListView.class);
                startActivity(intent);
                break;
            case R.id.nav_alarm:
                notificationManagerCompat = NotificationManagerCompat.from(this);
                pushAlarmNotification();
                cancelAlarm();//avoid duplicates
                startAlarmSystem();
                break;
            case R.id.nav_history:
                Intent intent1 = new Intent(detailedInfo.this, TableActivity.class);
                startActivity(intent1);
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tableinfo_main);
        detailInfo = new Dialog(this);
        detailInfo1 = new Dialog(this);
        detailInfo2 = new Dialog(this);
        final TableView<String[]> tb = (TableView<String[]>) findViewById(R.id.tableView);
        tb.setColumnCount(4);
        tb.setHeaderBackgroundColor(Color.parseColor("#233ED8"));

        populateData();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView =findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        tb.setHeaderAdapter(new SimpleTableHeaderAdapter(this, infoHeader));
        tb.setDataAdapter(new SimpleTableDataAdapter(this, infos));


        tb.addDataClickListener(new TableDataClickListener<String[]>() {
            @Override

            public void onDataClicked(int rowIndex, String[] clickedData) {
                if (rowIndex == 0) {

                    ShowCyanidePopup();

                }
                else if(rowIndex == 1){
                    ShowNitratesPopup();
                }
                else{
                    ShowOrganicPopup();
                }
            }
        });

    }

    private void populateData() {


        //waterMonitordbHelper dbhelper = new waterMonitordbHelper(this) ;
          //  ArrayList<turbidity> data = dbhelper.getAllTurbidity();


        InfoTable infotable = new InfoTable();
        ArrayList<InfoTable> infotableList = new ArrayList<>();



        infotable.setName("Cyanide");
        infotableList.add(infotable);

        infotable = new InfoTable();
        infotable.setName("Nitrates");
        infotableList.add(infotable);

        infotable = new InfoTable();
        infotable.setName("Bacteria");
        infotableList.add(infotable);


        infos = new String[infotableList.size()][4];

        for (int i = 0; i < infotableList.size(); i++) {

            InfoTable s = infotableList.get(i);


            infos[i][0] = s.getName();



        }

    }

    public void ShowNitratesPopup() {
        detailInfo1.setContentView(R.layout.nitrates_popup);
        closeGood = (ImageView) detailInfo1.findViewById(R.id.closePopupGood);
        titleGeneral = (TextView) detailInfo1.findViewById(R.id.titleNitrates);
        general = (TextView) detailInfo1.findViewById(R.id.Nitrates);
        titleHealth = (TextView) detailInfo1.findViewById(R.id.titleNitrateHealth);
        Healthgeneral = (TextView) detailInfo1.findViewById(R.id.NitrateHealth);





        closeGood.setOnClickListener(new View.OnClickListener() {
           @Override
          public void onClick(View v) {
                detailInfo1.dismiss();
           }
          });

          detailInfo1.show();

         }

    public void ShowCyanidePopup() {
        detailInfo.setContentView(R.layout.cyanide_popup);
        closeGood = (ImageView) detailInfo.findViewById(R.id.closePopupGood);
        titleGeneral1 = (TextView) detailInfo.findViewById(R.id.titleCyanide);
        general1 = (TextView) detailInfo.findViewById(R.id.Cyanide);
        titleHealth1 = (TextView) detailInfo.findViewById(R.id.titleCyanideHealth);
        Healthgeneral1 = (TextView) detailInfo.findViewById(R.id.CyanideHealth);


        closeGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailInfo.dismiss();
            }
        });

        detailInfo.show();

    }
    public void ShowOrganicPopup() {
        detailInfo2.setContentView(R.layout.organic_popup);
        closeGood = (ImageView) detailInfo2.findViewById(R.id.closePopupGood);
        titleGeneral2 = (TextView) detailInfo2.findViewById(R.id.titleOrganic);
        general2 = (TextView) detailInfo2.findViewById(R.id.Organic);
        titleHealth2 = (TextView) detailInfo2.findViewById(R.id.titleOrganicHealth);
        Healthgeneral2 = (TextView) detailInfo2.findViewById(R.id.OrganicHealth);




        closeGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailInfo2.dismiss();
            }
        });

        detailInfo2.show();

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
