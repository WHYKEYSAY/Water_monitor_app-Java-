package com.example.groupel.elecoen390_watermonitor;

import android.os.Bundle;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.anastr.speedviewlib.SpeedView;

import java.util.Random;

public class MainMenu extends AppCompatActivity {

    private ImageView meter;
    ImageView bgapp;
    LinearLayout textsplash, texthome, menus;
    Animation frombottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);


        bgapp = (ImageView) findViewById(R.id.bgapp);

        textsplash = (LinearLayout) findViewById(R.id.textsplash);
        texthome = (LinearLayout) findViewById(R.id.texthome);
        menus = (LinearLayout) findViewById(R.id.menus);



                bgapp.animate().translationY(-1900).setDuration(800).setStartDelay(1300);
                textsplash.animate().translationY(140).alpha(0).setDuration(800).setStartDelay(1300);

                texthome.startAnimation(frombottom);
                menus.startAnimation(frombottom);



        meter =(ImageView)findViewById(R.id.meter_layout);
        meter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

               Intent intent = new Intent(MainMenu.this, Meter.class);
                startActivity(intent);

            }
        });


    }

    }
