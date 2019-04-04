package com.example.groupel.elecoen390_watermonitor;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class detailedInfo extends AppCompatActivity {

    String[] infoHeader = {"ID", "Name", "Concentration", "Others"};
    String[][] infos;
    String[] datas;


    private Dialog detailInfo,detailInfo1, detailInfo2;
    private ImageView  closeGood;
    private TextView titleGeneral, general, titleHealth, Healthgeneral,titleGeneral1, general1, titleHealth1, Healthgeneral1,titleGeneral2, general2, titleHealth2, Healthgeneral2;
    private Button filter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_info);
        detailInfo = new Dialog(this);
        detailInfo1 = new Dialog(this);
        detailInfo2 = new Dialog(this);
        final TableView<String[]> tb = (TableView<String[]>) findViewById(R.id.tableView);
        tb.setColumnCount(4);
        tb.setHeaderBackgroundColor(Color.parseColor("#233ED8"));

        populateData();


        filter = findViewById(R.id.filter_btn);
        filter.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(detailedInfo.this, FilterListView.class);
                startActivity(intent);

            }
        });


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


        infotable.setId("1");
        infotable.setName("Cyanide");
        infotable.setPercent("80%");
        infotable.setOthers("xxxx");
        infotableList.add(infotable);

        infotable = new InfoTable();
        infotable.setId("2");
        infotable.setName("Nitrates");
        infotable.setPercent("15%");
        infotable.setOthers("xxxx");
        infotableList.add(infotable);

        infotable = new InfoTable();
        infotable.setId("3");
        infotable.setName("Organic");
        infotable.setPercent("20%");
        infotable.setOthers("xxx");
        infotableList.add(infotable);


        infos = new String[infotableList.size()][4];

        for (int i = 0; i < infotableList.size(); i++) {

            InfoTable s = infotableList.get(i);

            infos[i][0] = s.getId();
            infos[i][1] = s.getName();
            infos[i][2] = s.getPercent();
            infos[i][3] = s.getOthers();


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
    }
