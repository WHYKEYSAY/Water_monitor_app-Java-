package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class MainActivity extends AppCompatActivity {

    protected static final String TAG = "MainActivity";

    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    CardView cardView1,cardView2;
    EditText Email, pwd1;
    TextView login, Register;
    ImageView icon;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "The onCreate() event");
        setupUI();

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        FirebaseUser user = firebaseAuth.getCurrentUser();



    }

    protected void setupUI() {
        Email = (EditText) findViewById(R.id.Email);
        pwd1 = (EditText) findViewById(R.id.pwd1);
        cardView1 = (CardView) findViewById(R.id.cardView1);
        cardView2 = (CardView) findViewById(R.id.cardView2);
        login = (TextView) findViewById(R.id.login);
        Register = (TextView) findViewById(R.id.signup);
        icon = (ImageView) findViewById(R.id.icon);


        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                validate(Email.getText().toString(),pwd1.getText().toString());

            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Pop.class));
            }
        });
    }

    private void validate(String email, String password) {

        progressDialog.setMessage("Make sure you sign up first");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,"Login Successful", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(MainActivity.this, Status.class));
                }else {
                    Toast.makeText(MainActivity.this,"Login Failed", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, MainActivity.class));

                }
            }
        });

     }



}

