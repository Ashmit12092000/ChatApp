package com.example.letschat.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.letschat.Adapters.MessageAdapter;
import com.example.letschat.Models.Message;
import com.example.letschat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class chat_activity extends AppCompatActivity {

    public static final int REQUEST_CODE = 101;
    ImageView send_btn;
    EditText chat_message;
    RecyclerView recyclerView;
    ImageView camera;

    ArrayList<Message> messages;
    FirebaseStorage storage;
    FirebaseAuth mauth;
    String receiver_uid;
    MessageAdapter messageAdapter;

    MaterialTextView name_user;
    MaterialTextView status_user;
    ImageView profile_pic;
    String sender_uid;
    ImageView attachment;
    ImageView back;
    androidx.appcompat.widget.Toolbar toolbar;
    String senderRoom,recieverRoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_activity);
        send_btn= findViewById(R.id.send);
        mauth= FirebaseAuth.getInstance();
        profile_pic=(ImageView)findViewById(R.id.profile_user);

        sender_uid= FirebaseAuth.getInstance().getUid();
        receiver_uid = getIntent().getStringExtra("uid");
        senderRoom=sender_uid+ receiver_uid;
        recieverRoom= receiver_uid +sender_uid;
        camera=(ImageView)findViewById(R.id.camera);
        storage=FirebaseStorage.getInstance();
        toolbar=(androidx.appcompat.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        name_user=(MaterialTextView)findViewById(R.id.name_user);
        attachment= findViewById(R.id.attachment);
        String mesg="uploading image...";
        recyclerView = findViewById(R.id.recycler_Views);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final Handler handler = new Handler();
        chat_message= findViewById(R.id.chat_message);
        chat_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Date date =new Date();
                db.getReference().child("status").child(sender_uid).setValue("Typing...");
                //db.getReference().child("chats").child(reciever_uid).child("lastMsg").setValue("Typing....");
                //db.getReference().child("chats").child(reciever_uid).child("lastMsgTime").setValue(date.getTime());
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(StopTyping,1000);
            }
            final Runnable StopTyping=new Runnable() {
                @Override
                public void run() {
                    db.getReference().child("status").child(sender_uid).setValue("Online");
                }
            };
        });
        back=(ImageView)findViewById(R.id.back);
        status_user = (MaterialTextView)findViewById(R.id.status_user);
        messages=new ArrayList<Message>();
        messageAdapter= new MessageAdapter(this,messages,senderRoom,recieverRoom);
        recyclerView.setAdapter(messageAdapter);
        String name = getIntent().getStringExtra("name");
        String profile = getIntent().getStringExtra("image");

        name_user.setText(name);
        Glide.with(chat_activity.this).load(profile)
                .placeholder(R.drawable.account)
                .into(profile_pic);
        //getSupportActionBar().setTitle(name);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db.getReference().child("is_seen").child(receiver_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final LayoutInflater factory = getLayoutInflater();

                final View textEntryView = factory.inflate(R.layout.senders_sample_chat, null);

                ImageView delivered_msg = (ImageView) textEntryView.findViewById(R.id.seen);
                ImageView seen_msg=(ImageView)textEntryView.findViewById(R.id.seen2);
                if(snapshot.exists()){
                    String is_seen=snapshot.getValue(String.class);
                    if(!(is_seen.isEmpty())){
                        seen_msg.setVisibility(View.VISIBLE);
                    }
                    else{
                        delivered_msg.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        db.getReference().child("status").child(receiver_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String status = snapshot.getValue(String.class);
                    if(!(status.isEmpty())){
                        status_user.setText(status);
                        status_user.setVisibility(View.VISIBLE);
                    }
                    else{
                        status_user.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        db.getReference()
                .child("chats")
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            Message message = snapshot1.getValue(Message.class);
                            message.setMessageID(snapshot1.getKey());
                            messages.add(message);
                        }
                        messageAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message_text=chat_message.getText().toString();
                Date date= new Date();
                Message message=new Message(sender_uid,message_text,date.getTime());
                chat_message.setText("");
                FirebaseDatabase db= FirebaseDatabase.getInstance();
                String Key = db.getReference().push().getKey();

                HashMap<String, Object> lastMsgObj = new HashMap<>();
                lastMsgObj.put("lastMsg", message.getMessage());
                lastMsgObj.put("lastMsgTime", date.getTime());
                db.getReference().child("chats").child(senderRoom).updateChildren(lastMsgObj);
                db.getReference().child("chats").child(recieverRoom).updateChildren(lastMsgObj);
                assert Key != null;
                db.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .child(Key)
                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        db.getReference().child("chats")
                                .child(recieverRoom)
                                .child("messages")
                                .child(Key)
                                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });
            }
        });
        attachment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,40);
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermission();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

    }

    private void askPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, REQUEST_CODE);

        }
        else{
            openCamera();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults.length<0){
                openCamera();
            }
            else{
                Toast.makeText(this,"Camera Pemrission are required To click the photo",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void openCamera() {
        Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(in, 102);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        if(requestCode ==40 && resultCode ==RESULT_OK && data.getData()!=null){
            Uri uri = data.getData();
                Calendar calendar = Calendar.getInstance();
                StorageReference reference = storage.getReference().child("chats").child(calendar.getTimeInMillis() + "");
                reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    senderRoom = sender_uid + receiver_uid;
                                    recieverRoom = receiver_uid + sender_uid;
                                    String image_uri = uri.toString();
                                    String message_text = chat_message.getText().toString();
                                    Date date = new Date();
                                    Message message = new Message(sender_uid, message_text, date.getTime());
                                    message.setMessage("photo");
                                    message.setImage_uri(image_uri);
                                    chat_message.setText("");
                                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                                    String Key = db.getReference().push().getKey();

                                    HashMap<String, Object> lastMsgObj = new HashMap<>();
                                    lastMsgObj.put("lastMsg", message.getMessage());
                                    lastMsgObj.put("lastMsgTime", date.getTime());

                                    db.getReference().child("chats").child(senderRoom).updateChildren(lastMsgObj);
                                    db.getReference().child("chats").child(recieverRoom).updateChildren(lastMsgObj);
                                    assert Key != null;
                                    db.getReference().child("chats")
                                            .child(senderRoom)
                                            .child("messages")
                                            .child(Key)
                                            .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            db.getReference().child("chats")
                                                    .child(recieverRoom)
                                                    .child("messages")
                                                    .child(Key)
                                                    .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            });
                                        }
                                    });
                                }

                            });
                        }
                    }
                });

        }
        else if(requestCode==102 && resultCode ==RESULT_OK){
            Message message = new Message();
            message.setMessage("camera");
            Bitmap image=(Bitmap)data.getExtras().get("data");
            message.setSetbitMapImage_uri(image);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,100,stream);
            StorageReference image_Ref= storage.getReference().child("chats/"+mauth.getUid());
            byte[] b = stream.toByteArray();
            image_Ref.putBytes(b).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String image_uri = uri.toString();
                            String message_text = chat_message.getText().toString();
                            Date date = new Date();
                            Message message = new Message(sender_uid, message_text, date.getTime());
                            message.setMessage("camera");
                            message.setImage_uri(image_uri);
                            FirebaseDatabase db = FirebaseDatabase.getInstance();
                            String Key = db.getReference().push().getKey();
                            HashMap<String, Object> lastMsgObj = new HashMap<>();
                            lastMsgObj.put("lastMsg", message.getMessage());
                            lastMsgObj.put("lastMsgTime", date.getTime());

                            db.getReference().child("chats").child(senderRoom).updateChildren(lastMsgObj);
                            db.getReference().child("chats").child(recieverRoom).updateChildren(lastMsgObj);


                            db.getReference().child("chats")
                                    .child(senderRoom)
                                    .child("messages")
                                    .child(Key)
                                    .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    db.getReference().child("chats")
                                            .child(recieverRoom)
                                            .child("messages")
                                            .child(Key)
                                            .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    });
                                }
                            });

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Photo not sent",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        String currentuid=FirebaseAuth.getInstance().getUid();
        FirebaseDatabase database =FirebaseDatabase.getInstance();
        database.getReference().child("status").child(currentuid).setValue("Online");
        database.getReference().child("is_seen").child(currentuid).setValue("Seen");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_chat_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.Report:
                Toast.makeText(getApplicationContext(),"This feature is coming soon",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}