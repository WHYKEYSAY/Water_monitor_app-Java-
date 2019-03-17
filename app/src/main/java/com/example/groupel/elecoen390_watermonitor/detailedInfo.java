package com.example.groupel.elecoen390_watermonitor;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.listeners.TableDataClickListener;

public class detailedInfo extends AppCompatActivity {

    String[] spaceProbeHeaders = {"ID", "Name", "Percent", "Others"};
    String[][] spaceProbes;
    


    private Dialog waterDialog;
    private ImageView closeBad, closeGood, closeOk, start;
    private TextView titleBad, titleGood, titleOk;
    private Button history , detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_info);
        waterDialog = new Dialog(this);

        final TableView<String[]> tb = (TableView<String[]>) findViewById(R.id.tableView);
        tb.setColumnCount(4);
        tb.setHeaderBackgroundColor(Color.parseColor("#233ED8"));

        populateData();

        tb.setHeaderAdapter(new SimpleTableHeaderAdapter(this, spaceProbeHeaders));
        tb.setDataAdapter(new SimpleTableDataAdapter(this, spaceProbes));

        tb.addDataClickListener(new TableDataClickListener<String[]>() {
            @Override

            public void onDataClicked(int rowIndex, String[] clickedData) {
                if (rowIndex == 0) {

                    ShowGoodPopup();
                }
            }
        });

    }

    private void populateData() {
        Spaceprobe spaceprobe = new Spaceprobe();
        ArrayList<Spaceprobe> spaceprobeList = new ArrayList<>();


        spaceprobe.setId("1");
        spaceprobe.setName("Cyanide");
        spaceprobe.setPropellant("80%");
        spaceprobe.setDestination("xxxx");
        spaceprobeList.add(spaceprobe);

        spaceprobe = new Spaceprobe();
        spaceprobe.setId("2");
        spaceprobe.setName("Nitrates");
        spaceprobe.setPropellant("15%");
        spaceprobe.setDestination("xxxx");
        spaceprobeList.add(spaceprobe);

        spaceprobe = new Spaceprobe();
        spaceprobe.setId("3");
        spaceprobe.setName("Organic");
        spaceprobe.setPropellant("20%");
        spaceprobe.setDestination("xxx");
        spaceprobeList.add(spaceprobe);



        spaceProbes = new String[spaceprobeList.size()][4];

        for (int i = 0; i < spaceprobeList.size(); i++) {

            Spaceprobe s = spaceprobeList.get(i);

            spaceProbes[i][0] = s.getId();
            spaceProbes[i][1] = s.getName();
            spaceProbes[i][2] = s.getPropellant();
            spaceProbes[i][3] = s.getDestination();


        }

    }
    public  void ShowGoodPopup(){
        waterDialog.setContentView(R.layout.popup_window_good);
        closeGood=(ImageView)waterDialog.findViewById(R.id.closePopupGood);
        titleGood =(TextView) waterDialog.findViewById(R.id.titleGood);


        closeGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waterDialog.dismiss();
            }
        });

        waterDialog.show();

    }
}
