package com.example.groupel.elecoen390_watermonitor;

import android.app.Dialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.anastr.speedviewlib.PointerSpeedometer;
import com.github.anastr.speedviewlib.SpeedView;

import java.util.Random;

public class Meter extends AppCompatActivity {
    private Dialog waterDialog;
    private ImageView closeBad, closeGood, closeOk, start;
    private TextView titleBad, titleGood, titleOk;
    private int x=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter);
        waterDialog = new Dialog(this);

        start=(ImageView)findViewById(R.id.btn_start);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SpeedView speedometer = findViewById(R.id.speedView);

                speedometer.setWithTremble(false);
                Random rand = new Random();
                x = rand.nextInt(101);
                speedometer.speedTo(x);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(x<30)

                        {
                            ShowBadPopup();
                        }
                        else if(x>=30 && x<80)

                        {

                            ShowOkPopup();
                        }
                        else

                        {
                            ShowGoodPopup();
                        }
                    }
                },2500);



            }

        });
    }
    public void ShowBadPopup(){
        waterDialog.setContentView(R.layout.popup_window_bad);
        closeBad=(ImageView)waterDialog.findViewById(R.id.closePopupBad);
        titleBad =(TextView) waterDialog.findViewById(R.id.titleBad);

        closeBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waterDialog.dismiss();
            }
        });

        waterDialog.show();

    }
    public void ShowOkPopup(){
        waterDialog.setContentView(R.layout.popup_window_ok);
        closeOk=(ImageView)waterDialog.findViewById(R.id.closePopupBad);
        titleOk =(TextView) waterDialog.findViewById(R.id.titleOk);

        closeOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waterDialog.dismiss();
            }
        });

        waterDialog.show();

    }
    public void ShowGoodPopup(){
        waterDialog.setContentView(R.layout.popup_window_good);
        closeGood=(ImageView)waterDialog.findViewById(R.id.closePopupBad);
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

