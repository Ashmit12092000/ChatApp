/*package com.example.letschat.Activities;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.letschat.R;
import com.google.android.material.textfield.TextInputEditText;
import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class join_room_activity extends AppCompatActivity {
    Button join_btn,create_btn;
    TextInputEditText code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_room_activity);
        code= findViewById(R.id.code);
        join_btn = findViewById(R.id.join_btn);
        create_btn= findViewById(R.id.creates_btn);
        URL url ;
        try {
            url=new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions defaultOptions = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(url)
                    .build();
            JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                        .setRoom(Objects.requireNonNull(code.getText()).toString())
                        .setWelcomePageEnabled(false)
                        .build();
                JitsiMeetActivity.launch(join_room_activity.this,options);

            }
        });
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),createroom_activity.class));
            }
        });
    }
}*/