package com.example.letschat.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.example.letschat.Models.User;
import com.example.letschat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class MyProfile extends AppCompatActivity {
    TextInputEditText names,email,phone,staus_text;
    ImageView myprofileimage,image_btn;
    Button update;
    FirebaseAuth mauth;
    FirebaseDatabase db;
    FirebaseStorage storage;
    ProgressDialog dialog;
    String status_retrieved="";
    Uri selected_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        mauth=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        getSupportActionBar().hide();
        myprofileimage=(ImageView)findViewById(R.id.myprofileimage);
        image_btn=(ImageView)findViewById(R.id.imagebutton);
        names=(TextInputEditText)findViewById(R.id.profile_name);
        email=(TextInputEditText)findViewById(R.id.profile_Email);
        staus_text=(TextInputEditText)findViewById(R.id.user_Status);
        phone=(TextInputEditText)findViewById(R.id.profile_mobile);
        db=FirebaseDatabase.getInstance();
        DatabaseReference docref= db.getReference("users");
        Query query=docref.orderByChild("userID").equalTo(mauth.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    String name=""+ds.child("display_name").getValue(String.class);
                    String status=""+ds.child("status").getValue(String.class);
                    String Email=""+ds.child("email").getValue(String.class);
                    String mob=""+ds.child("phone").getValue(String.class);
                    String pic=""+ds.child("profile_pic").getValue(String.class);
                    staus_text.setText(status);
                    names.setText(name);
                    email.setText(Email);
                    phone.setText(mob);
                    try {

                        Glide.with(MyProfile.this)
                                .load(pic)
                                .placeholder(R.drawable.account)
                                .into(myprofileimage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        update=(Button)findViewById(R.id.profile_button);
        dialog=new ProgressDialog(this);
        dialog.setMessage("Updating please wait...");
        dialog.setCancelable(false);

        myprofileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,10);
            }
        });
        image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,11);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                String status_text = Objects.requireNonNull(staus_text.getText()).toString();
                String name = Objects.requireNonNull(names.getText()).toString();
                String Email = Objects.requireNonNull(email.getText()).toString();
                String mob = Objects.requireNonNull(phone.getText()).toString();
                User user = new User();
                user.setStatus(status_text);
                user.setdisplay_name(name);
                user.setEmail(Email);
                user.setPhone(mob);

                if(!(Email.equals(Objects.requireNonNull(mauth.getCurrentUser()).getEmail()))) {
                    db.getReference().child("users").child(Objects.requireNonNull(mauth.getUid())).child("email").setValue(user.getEmail())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dialog.dismiss();
                                    Toast.makeText(MyProfile.this, "Email have ben updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
               if(!(name.equals(Objects.requireNonNull(mauth.getCurrentUser()).getDisplayName()))) {
                    db.getReference().child("users").child(Objects.requireNonNull(mauth.getUid())).child("display_name").setValue(user.getDisplay_name())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dialog.dismiss();
                                    Toast.makeText(MyProfile.this, "Name have ben updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                if(!(mob.equals(Objects.requireNonNull(mauth.getCurrentUser()).getPhoneNumber()))) {
               db.getReference().child("users").child(Objects.requireNonNull(mauth.getUid())).child("phone").setValue(user.getPhone())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialog.dismiss();
                                Toast.makeText(MyProfile.this, "Mobile have ben updated", Toast.LENGTH_SHORT).show();
                            }
                        });

                }
                db.getReference()
                        .child("users")
                        .child(Objects.requireNonNull(mauth.getUid()))
                        .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot sp:snapshot.getChildren()){
                                String st= sp.child("status").getValue(String.class);
                                status_retrieved=st;
                            }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                if(!(status_text.equals(status_retrieved))){
                    db.getReference().child("users").child(Objects.requireNonNull(mauth.getUid())).child("status").setValue(status_text)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dialog.dismiss();
                                    Toast.makeText(MyProfile.this, "Status have been updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                }


        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10 && resultCode==RESULT_OK && data.getData()!=null) {
            myprofileimage.setImageURI(data.getData());
            selected_image = data.getData();
            if(selected_image!=null){
                dialog.setMessage("Updating Profile photo.....");
                dialog.show();
            StorageReference reference = FirebaseStorage.getInstance().getReference().child("users").child(Objects.requireNonNull(mauth.getUid()));
            reference.putFile(selected_image).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String image_uri = uri.toString();
                                User user= new User();
                                user.setProfile_pic(image_uri);
                                db.getReference().child("users").child(Objects.requireNonNull(mauth.getCurrentUser())
                                        .getUid()).child("profile_pic")
                                        .setValue(user.getProfile_pic()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialog.dismiss();
                                        Toast.makeText(MyProfile.this,"Profile photo have been updated",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }
        }
        else if(requestCode==11 && resultCode==RESULT_OK && data.getData()!=null) {
            if (selected_image != null){
                StorageReference reference = FirebaseStorage.getInstance().getReference().child("users").child(Objects.requireNonNull(mauth.getUid()));
            reference.putFile(selected_image).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String image_uri = uri.toString();
                                User user = new User();
                                user.setProfile_pic(image_uri);
                                db.getReference().child("users").child(Objects.requireNonNull(mauth.getCurrentUser())
                                        .getUid()).child("profile_pic")
                                        .setValue(user.getProfile_pic()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialog.dismiss();
                                        Toast.makeText(MyProfile.this, "Profile photo have been updated", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(MyProfile.this, "Please wait  few second for reflecting !", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }
        }
        }
    }