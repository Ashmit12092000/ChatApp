package com.example.letschat.Activities;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.letschat.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class splash_screen extends AppCompatActivity {
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAuth= FirebaseAuth.getInstance();
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                if(mAuth.getCurrentUser()!=null) {
                    Intent i = new Intent(splash_screen.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else{
                    Intent i = new Intent(splash_screen.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 5000);
    }
}