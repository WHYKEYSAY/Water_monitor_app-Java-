package com.example.groupel.elecoen390_watermonitor;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class firebaseListener {
    private class cloudDB{
        public Integer counter;
        public Integer measure;
        public ArrayList<cloudMeasurement> allMeasurements;
    }
    private class cloudMeasurement{
        public int cloudID;
        public ArrayList<Integer> Vout;
        public ArrayList<Integer> position;//ignored
        public Integer read;
        public Integer timestamp;
    }

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    private static int last_measurement = 0;

    //listens to count and uses count to fetch old data
    public void initListener() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    cloudDB entireDB = new cloudDB();
                    //wait for measurement to end
                    entireDB.measure = dataSnapshot.child("measure").getValue(Integer.class);
                    if (entireDB.measure == 0) {
                        //iterate though measurements up to counter -1
                        entireDB.counter = dataSnapshot.child("counter").getValue(Integer.class);
                        while (last_measurement < entireDB.counter) {
                            //create new measurement object for every measurement# where last_measurement <= # < counter
                            DataSnapshot measurement = dataSnapshot.child("measurement" + Integer.toString(last_measurement));
                            cloudMeasurement nextMeasure = new cloudMeasurement();
                            nextMeasure.cloudID = last_measurement;//save current measurement, maybe useful later?
                            nextMeasure.read = measurement.child("read").getValue(Integer.class);
                            nextMeasure.timestamp = measurement.child("timestamp").getValue(Integer.class);
                            for (DataSnapshot Vout : measurement.child("Vout").getChildren()) {
                                nextMeasure.Vout.add(Vout.getValue(Integer.class));
                            }
                            entireDB.allMeasurements.add(nextMeasure);
                            last_measurement++;
                        }
                    }
                }
                else
                    Log.e(TAG, "Value from cloud is null.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Getting cloud DB failed.", databaseError.toException());
            }
        });
    }

    //request measurement by setting measure to 1
    public void requestMeasure(){
        reference.child("measure").setValue(1);
    }

}