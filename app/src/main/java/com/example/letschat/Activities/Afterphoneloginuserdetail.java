package com.example.letschat.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.letschat.R;
import com.example.letschat.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.Objects;

public class Afterphoneloginuserdetail extends AppCompatActivity {
    ImageView profile;
    TextInputEditText age,Gender,Name,Email;
    Button next;
    ProgressBar pbar;
    FirebaseAuth mauth;
    TextInputLayout name_layout,mail_layout;
    FirebaseDatabase database;
    Uri selected_image;
    FirebaseStorage storage;
    CollectionReference creference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afterphoneloginuserdetail);
        age = findViewById(R.id.Age);
        name_layout=(TextInputLayout)findViewById(R.id.name_layout);
        mail_layout=(TextInputLayout)findViewById(R.id.email_id_layout);
        mauth = FirebaseAuth.getInstance();
        Email=findViewById(R.id.Email);
        Name=findViewById(R.id.Name);
        Gender = findViewById(R.id.gender);
        Objects.requireNonNull(getSupportActionBar()).hide();
        pbar= findViewById(R.id.pbarafter);
        pbar.setVisibility(View.GONE);
        next = findViewById(R.id.next);

        String mail = getIntent().getStringExtra("mail");
        Email.setText(mail);
        String email= Objects.requireNonNull(Email.getText()).toString();
        if(email.isEmpty()){
           Email.setVisibility(View.VISIBLE);
           Name.setVisibility(View.VISIBLE);
           mail_layout.setVisibility(View.VISIBLE);
            name_layout.setVisibility(View.VISIBLE);
        }
        else{
            mail_layout.setVisibility(View.GONE);
            name_layout.setVisibility(View.GONE);
            Email.setVisibility(View.GONE);
            Name.setVisibility(View.GONE);
        }
        database = FirebaseDatabase.getInstance();
        profile= findViewById(R.id.profile_Pic);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i,45);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbar.setVisibility(View.VISIBLE);
                String Age= Objects.requireNonNull(age.getText()).toString();
                String gender = Objects.requireNonNull(Gender.getText()).toString();
                if(Age.isEmpty()){
                    age.setError("Enter Email Address");
                    age.requestFocus();
                    pbar.setVisibility(View.GONE);
                }
                else if(gender.isEmpty()){
                    Gender.setError("Enter Name");
                    Gender.requestFocus();
                    pbar.setVisibility(View.GONE);
                }
                else if(selected_image!=null)
                    {

                        StorageReference reference= FirebaseStorage.getInstance().getReference().child("users").child(Objects.requireNonNull(mauth.getUid()));
                        reference.putFile(selected_image).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if(task.isSuccessful()){
                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String Age = age.getText().toString();
                                            String names= Objects.requireNonNull(Name.getText()).toString();
                                            String mails = Email.getText().toString();
                                            String image_uri = uri.toString();
                                            String gender = Gender.getText().toString();
                                            String uid =mauth.getUid();
                                            String phone= Objects.requireNonNull(mauth.getCurrentUser()).getPhoneNumber();
                                            User usr = new User(uid,names,image_uri,mails,phone,Age,gender,"Null");
                                            database.getReference().child("users").child(mauth.getUid())
                                                    .setValue(usr)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void a) {
                                                            startActivity(new Intent(Afterphoneloginuserdetail.this,MainActivity.class));
                                                            finish();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }
                            });
                        }
                else {

                    String names= Objects.requireNonNull(Name.getText()).toString();
                    String mail = getIntent().getStringExtra("mail");

                    String uid =mauth.getUid();
                    String phone= Objects.requireNonNull(mauth.getCurrentUser()).getPhoneNumber();
                    User usr = new User(uid,names,"No image",mail,phone,Age,gender,"Null");
                    database.getReference().child("users").child(Objects.requireNonNull(mauth.getUid()))
                            .setValue(usr)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void a) {
                                    startActivity(new Intent(Afterphoneloginuserdetail.this,MainActivity.class));
                                    finish();
                                }
                            });

                }

                }
            });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            if(data.getData()!=null){
                profile.setImageURI(data.getData());
                selected_image = data.getData();
            }
        }
    }

}