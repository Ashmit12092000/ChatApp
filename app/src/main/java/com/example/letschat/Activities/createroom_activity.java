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

public class createroom_activity extends AppCompatActivity {
    Button create_btn,share_btn;
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    TextInputEditText code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createroom_activity);
        code= findViewById(R.id.code);
        create_btn = findViewById(R.id.create_btn);
        share_btn= findViewById(R.id.share_btn);
        code.setText(randomAlphaNumeric(6));
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

        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                        .setRoom(Objects.requireNonNull(code.getText()).toString())
                        .setWelcomePageEnabled(false)
                        .build();
                JitsiMeetActivity.launch(createroom_activity.this,options);

            }
        });
        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "join the let's chat meet";
                String shareSub = " join the let's chat meet " +"\n"+
                        "code : "+ Objects.requireNonNull(code.getText()).toString()+"\n"+
                        "Or join the meet by the link Given below:"+"\n"+
                        "Meet Link: https://meet.jit.si/"+Objects.requireNonNull(code.getText()).toString();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });
    }
    public  String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}*/