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
        //TODO: Store last 20 test history locally
        //This list should acquire all the history from the firebase/local database
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: Make this to be able to pass table(DB) id to graph activity
                Intent intent = new Intent(getApplicationContext(),graphActivity.class);
                //Use date as key to search the measurement ID, no bad practice
                //waterMonitorHelper helper = new waterMonitorHelper(getContext());
                //SpectroMeasure sample = helper.getMeasure(date);
                //id = sample.getID();
                //intent.putExtra("measureID",id);
                startActivity(intent);
            }
        });
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
}
