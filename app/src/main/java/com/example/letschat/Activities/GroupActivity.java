package com.example.letschat.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.letschat.Adapters.GroupMessageAdapter;
import com.example.letschat.Adapters.MessageAdapter;
import com.example.letschat.Models.Message;
import com.example.letschat.Models.User;
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

public class GroupActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 100;
    ImageView send_btn;
    EditText chat_message;
    RecyclerView recyclerView;
    ImageView camera;
    ArrayList<Message> messages;
    FirebaseStorage storage;
    FirebaseAuth mauth;
    String reciever_uid;
    GroupMessageAdapter groupmessageAdapter;

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
        setContentView(R.layout.activity_group);
        User user= new User();
        chat_message= findViewById(R.id.gchat_message);
        send_btn=(ImageView)findViewById(R.id.gsend);
        senderRoom=FirebaseAuth.getInstance().getUid();
        recieverRoom=user.getUserID();
        storage=FirebaseStorage.getInstance();
        attachment=(ImageView)findViewById(R.id.gattachment);
        name_user=(MaterialTextView)findViewById(R.id.gname_user);
        profile_pic=(ImageView)findViewById(R.id.gprofile_user);
        toolbar=(Toolbar) findViewById(R.id.gtoolbar);
        recyclerView=(RecyclerView)findViewById(R.id.grecycler_Views);
        setSupportActionBar(toolbar);
        camera=(ImageView)findViewById(R.id.gcamera);
        back=(ImageView)findViewById(R.id.gback);
        mauth=FirebaseAuth.getInstance();
        messages=new ArrayList<Message>();

        groupmessageAdapter= new GroupMessageAdapter(this,messages);
        recyclerView.setAdapter(groupmessageAdapter);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        sender_uid= FirebaseAuth.getInstance().getUid();
        FirebaseDatabase db =FirebaseDatabase.getInstance();
        name_user.setText("World Chat");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,41);
            }
        });
        db.getReference()
                .child("Random Group Chats")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            Message message = snapshot1.getValue(Message.class);
                            messages.add(message);
                        }
                        groupmessageAdapter.notifyDataSetChanged();
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
                FirebaseDatabase db =FirebaseDatabase.getInstance();
                db.getReference().child("Random Group Chats")
                        .push()
                        .setValue(message)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermission();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==41 && resultCode==RESULT_OK && data.getData()!=null){

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
                                String image_uri = uri.toString();
                                String message_text = chat_message.getText().toString();
                                Date date = new Date();
                                Message message = new Message(sender_uid, message_text, date.getTime());
                                message.setMessage("gphoto");
                                message.setImage_uri(image_uri);
                                chat_message.setText("");
                                FirebaseDatabase db = FirebaseDatabase.getInstance();
                                String Key = db.getReference().push().getKey();

                                //HashMap<String, Object> lastMsgObj = new HashMap<>();
                                //lastMsgObj.put("lastMsg", message.getMessage());
                                //lastMsgObj.put("lastMsgTime", date.getTime());

                                //db.getReference().child("chats").child(senderRoom).updateChildren(lastMsgObj);
                               // db.getReference().child("chats").child(recieverRoom).updateChildren(lastMsgObj);
                                //assert Key != null;
                                db.getReference().child("Random Group Chats")
                                        .push()
                                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                });
                            }

                        });
                    }
                }
            });

        }
        else if(requestCode==103 && resultCode ==RESULT_OK){
            Message message = new Message();
            message.setMessage("gcamera");
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
                            message.setMessage("gcamera");
                            message.setImage_uri(image_uri);
                            FirebaseDatabase db = FirebaseDatabase.getInstance();
                            String Key = db.getReference().push().getKey();
                            //HashMap<String, Object> lastMsgObj = new HashMap<>();
                           // lastMsgObj.put("lastMsg", message.getMessage());
                            //lastMsgObj.put("lastMsgTime", date.getTime());

                            //db.getReference().child("chats").child(senderRoom).updateChildren(lastMsgObj);
                            //db.getReference().child("chats").child(recieverRoom).updateChildren(lastMsgObj);


                            db.getReference().child("Random Group Chats")
                                    .push()
                                    .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

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
        startActivityForResult(in, 103);
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