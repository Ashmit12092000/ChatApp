package com.example.letschat.Activities;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.letschat.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {
    TextInputEditText name,email,phone;
    ImageView profile;
    MaterialTextView logphone;
    Button btn;
    String valid_mail="btech";
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        name= findViewById(R.id.Name);
        Objects.requireNonNull(getSupportActionBar()).hide();
        email= findViewById(R.id.Email);
        phone= findViewById(R.id.mobile);
        btn= findViewById(R.id.button);

        logphone= findViewById(R.id.logphone);
        logphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,phoneActivity.class));
            }
        });
        mAuth=FirebaseAuth.getInstance();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name =Objects.requireNonNull(name.getText()).toString();
                String emails= Objects.requireNonNull(email.getText()).toString();
                String mob= Objects.requireNonNull(phone.getText()).toString();
                if(Name.isEmpty()){
                    name.setError("Please Enter Name");
                    name.requestFocus();
                }
                else if(emails.isEmpty()){
                    email.setError("Please Enter Email");
                    email.requestFocus();
                }
                else if(mob.isEmpty()){
                    phone.setError("Please Enter mobile");
                    phone.requestFocus();
                }
                else if((!(emails.isEmpty()) && !(Name.isEmpty()) && !(mob.isEmpty()))){
                    Intent intent = new Intent(SignupActivity.this,verifyphoneactivity.class);
                    intent.putExtra("Name",Name);
                    intent.putExtra("Email",emails);
                    intent.putExtra("Phone",mob);
                    startActivity(intent);
                }
                else{
                    email.setError("Something Went  Wrong");
                    email.requestFocus();
                    Toast.makeText(SignupActivity.this,"This app is for  Btech Students only!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(SignupActivity.this,MainActivity.class));
            finish();
        }
    }
}