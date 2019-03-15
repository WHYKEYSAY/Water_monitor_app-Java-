package com.example.groupel.elecoen390_watermonitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

public class graphActivity extends AppCompatActivity {

    //TODO: add method to get data point from local database or firebase
    private LineGraphSeries<DataPoint> series;
    GraphView graphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Intent intent= getIntent();
        setUI();
        //TODO: add labels
        //TODO: add buttons that navigates to the detailed info
        plot();

    }

    private void plot() {
        long wavelength = 0;
        long intensity = 0;
        //Demo graph  y=sin(x)
        double x = 0;
        double y;
        int datapointcount = 50;
        for (int i = 0; i < datapointcount; i++) {
            x += 0.1;
            y = Math.sin(x);
            series.appendData(new DataPoint(x, y), true, 100);
        }
        graphView.addSeries(series);


    }

    private void setUI() {
        graphView = (GraphView) findViewById(R.id.graph_IntensityVSwavelength);
        series = new LineGraphSeries<>();

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