package com.example.letschat.Activities;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.letschat.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class phoneActivity extends AppCompatActivity {
    TextInputEditText mobile;
    Button newac;
    Button btn;
    FirebaseAuth mAuth;
    ProgressBar pbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_phone);
        mobile= findViewById(R.id.mobile);
        btn= findViewById(R.id.verifyb);
        newac= findViewById(R.id.createAccount);

        pbar= findViewById(R.id.progressBar);
        pbar.setVisibility(View.GONE);
        btn.setOnClickListener(v -> {
            pbar.setVisibility(View.VISIBLE);
            String mob=mobile.getText().toString().trim();
            if (mob.isEmpty() || mob.length() < 10) {
                mobile.setError("Enter valid Mobile number");
                mobile.requestFocus();
                return;
            }
            Intent intent = new Intent(phoneActivity.this,verifyphoneactivity.class);
            intent.putExtra("Phone", mob);
            startActivity(intent);
            finish();
        });
        newac.setOnClickListener(v -> {
            pbar.setVisibility(View.VISIBLE);
            startActivity(new Intent(phoneActivity.this,SignupActivity.class));
        });
    }

}