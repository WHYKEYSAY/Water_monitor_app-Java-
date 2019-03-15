package com.example.groupel.elecoen390_watermonitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class TableActivity extends AppCompatActivity {

    TextView text_title_line;
    ListView listView;
    ArrayList<String> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        setUI();
        //Demo listview item
        //TODO: Display an arrow on the rightmost of a line
        String item = String.format("%-20s %15s","4","2019-03-15");
        arrayList.add(item);
    }

    private void setUI() {
        text_title_line = (TextView) findViewById(R.id.title_line);
        String title = String.format("%-20s %15s","TURBIDITY LEVEL[NTU]","DATE");
        text_title_line.setText(title);
        listView = (ListView) findViewById(R.id.history);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
        //TODO:  Display text "No History" if nothing is retrieved from the database/firebase
        //This list should acquire all the history from the firebase/local database
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: Make this to be able to pass table(DB) id to graph activity
                Intent intent = new Intent(view.getContext(),graphActivity.class);
                startActivity(intent);
            }
        });
    }


}
