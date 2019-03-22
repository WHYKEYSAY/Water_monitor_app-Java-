package com.example.groupel.elecoen390_watermonitor;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class cloudListener {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    final DatabaseReference measure = myRef.child("measure");
    final DatabaseReference position = myRef.child("position");
    final DatabaseReference Vout = myRef.child("Vout");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vout.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("file", "Value is: " + value);
                //Do this
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("file", "Failed to read value.", error.toException());
            }
        });

}}
