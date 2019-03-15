package com.example.groupel.elecoen390_watermonitor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class TableActivity extends AppCompatActivity {

    TextView text_tur;
    TextView text_date;
    ListView listView;
    ArrayList<String> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        setUI();
    }

    private void setUI() {


        text_tur = (TextView) findViewById(R.id.title_turbidity);
        text_date = (TextView) findViewById(R.id.title_testDateAndtime);
        listView = (ListView) findViewById(R.id.history);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
        //TODO:  Display text "No History" if nothing is retrieved from the database/firebase
        //This list should acquire all the history from the firebase/local database
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


}
