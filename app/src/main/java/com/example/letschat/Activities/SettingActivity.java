package com.example.letschat.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.example.letschat.Models.User;
import com.example.letschat.R;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.firestore.DocumentReference;

public class SettingActivity extends AppCompatActivity {
    Context context;
    FirebaseAuth mauth;
    ImageView profile,prfile_ima,logout_img;
    MaterialTextView names,status_text,profile_text,log_txt;
    FirebaseDatabase db;
    DatabaseReference docref;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().hide();
        mauth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Logging Out");
        progressDialog.setCancelable(false);
        profile=(ImageView)findViewById(R.id.profile_picture);
        logout_img=(ImageView)findViewById(R.id.logout_profile);
        log_txt=(MaterialTextView)findViewById(R.id.log_Text);
        names=(MaterialTextView)findViewById(R.id.name1);
        status_text=(MaterialTextView)findViewById(R.id.stats);
        prfile_ima=(ImageView)findViewById(R.id.profil);
        profile_text=(MaterialTextView)findViewById(R.id.myprofiletext);
        User user= new User();
        db=FirebaseDatabase.getInstance();
        docref=db.getReference("users");
        Query query= docref.orderByChild("userID").equalTo(mauth.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    String age=snapshot1.child("age").getValue(String.class);
                    String name=""+snapshot1.child("display_name").getValue(String.class);
                    String pic=""+snapshot1.child("profile_pic").getValue(String.class);
                    String status=""+snapshot1.child("status").getValue(String.class);
                    names.setText(name);
                    status_text.setText(status);
                    try{
                        Glide.with(SettingActivity.this).load(pic).placeholder(R.drawable.account)
                                .into(profile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        logout_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("Logout").setMessage("Are you sure  want to Logout?").create();
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        progressDialog.show();
                        logout();
                        finish();
                    }
                });
                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
        log_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("Logout").setMessage("Are you sure want  to Logout?").create();
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.show();
                        logout();
                        finish();
                    }
                });
                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
        profile_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this,MyProfile.class));
            }
        });

    }
    public void logout(){

        mauth.signOut();

        startActivity(new Intent(getApplicationContext(),phoneActivity.class));

        finish();
    }
}