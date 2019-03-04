package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Pop extends Activity  {

    private EditText Fname, Lname, Email, Phone, pwd1, pwd2;
    private Button regbtn;
    private TextView reg;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popwindow);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.8));
        // Make us non-modal, so that others can receive touch events.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        // ...but notify us that it happened.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

        // Note that flag changes must happen *before* the content view is set.
        setContentView(R.layout.popwindow);

        setupUI();
        firebaseAuth = FirebaseAuth.getInstance();
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){
                    String email = Email.getText().toString().trim();
                    String password = pwd1.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Pop.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Pop.this, MainActivity.class));
                            }else {
                                Toast.makeText(Pop.this, "Registration Failed", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // If we've received a touch notification that the user has touched
        // outside the app, finish the activity.
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
            finish();
            return true;
        }

        // Delegate everything else to Activity.
        return super.onTouchEvent(event);
    }

    private void setupUI(){
        Fname = (EditText)findViewById(R.id.Fname);
        Lname = (EditText)findViewById(R.id.Lname);
        Email = (EditText)findViewById(R.id.Email);
        Phone = (EditText)findViewById(R.id.Phone);
        pwd1 = (EditText)findViewById(R.id.pwd1);
        pwd2 = (EditText)findViewById(R.id.pwd2);
        regbtn = (Button)findViewById(R.id.regbtn);
        reg = (TextView) findViewById(R.id.reg);
    }

    private Boolean validate(){
        Boolean result = false;

        String firstname= Fname.getText().toString();
        String lastname= Lname.getText().toString();
        String password1=pwd1.getText().toString();
        String password2=pwd2.getText().toString();
        String email=Email.getText().toString();
        String phone=Phone.getText().toString();

        if(firstname.isEmpty()||lastname.isEmpty()||email.isEmpty() || password1.isEmpty() || password2.isEmpty()||phone.isEmpty()){
            Toast.makeText(this,"Please Enter All The Details", Toast.LENGTH_SHORT).show();
        }else {
            result= true;
        }
        return result;

    }
}
