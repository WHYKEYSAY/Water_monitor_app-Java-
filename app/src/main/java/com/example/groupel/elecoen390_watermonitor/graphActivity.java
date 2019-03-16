package com.example.groupel.elecoen390_watermonitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Date;
import java.util.List;

public class graphActivity extends AppCompatActivity {

    //TODO: add method to get data point from local database or firebase
    private LineGraphSeries<DataPoint> series;
    GraphView graphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//display go-back arrow on top left
        Intent intent= getIntent();
        setUI();
        //TODO: add labels
        //TODO: add buttons that navigates to the detailed info
        plot();//plotting takes info from respective item clicked at tableActivity

    }

    private void plot() {
        long wavelength = 0;
        long intensity = 0;
        int datapointcount = 50;

        //Demo graph
        double x = 0;
        double y;
        for (int i = 0; i < datapointcount; i++) {
            x += 1;
            y = x;
            series.appendData(new DataPoint(x, y), true, 100);
        }

        //map the datapoints
        graphView.addSeries(series);

        // activate horizontal zooming and scrolling
        graphView.getViewport().setScalable(true);

        // activate horizontal scrolling
        graphView.getViewport().setScrollable(true);

        // activate horizontal and vertical zooming and scrolling
        graphView.getViewport().setScalableY(true);

        // activate vertical scrolling
        graphView.getViewport().setScrollableY(true);

        // set manual X bounds
        //Note that the sensor will give 50 readings
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(datapointcount);

        // set manual Y bounds
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(datapointcount);

        // custom label formatter to show units
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show um for x values
                    return super.formatLabel(value, isValueX) + " um";
                } else {
                    // show um for y values
                    return super.formatLabel(value, isValueX) + " um";
                }
            }
        });

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