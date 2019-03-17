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

    String[] infoHeader = {"ID", "Name", "Percent", "Others"};
    String[][] infos;
    


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

        tb.setHeaderAdapter(new SimpleTableHeaderAdapter(this, infoHeader));
        tb.setDataAdapter(new SimpleTableDataAdapter(this, infos));

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

            infos  [i][0] = s.getId();
            infos[i][1] = s.getName();
            infos[i][2] = s.getPercent();
            infos[i][3] = s.getOthers();


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
