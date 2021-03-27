package com.example.letschat.Activities;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.letschat.R;
import com.google.android.gms.tasks.*;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.google.android.material.snackbar.Snackbar.*;

public class verifyphoneactivity extends AppCompatActivity {
    TextInputEditText code,otp;
    Button verifybtn;
    private String mVerificationId;
    FirebaseAuth mAuth;
    ProgressBar pbar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_verifyphoneactivity);
        mAuth = FirebaseAuth.getInstance();
        Objects.requireNonNull(getSupportActionBar()).hide();
        code= findViewById(R.id.verifycode);
        verifybtn= findViewById(R.id.verifybtn);
        verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String codes = Objects.requireNonNull(code.getText()).toString();

                if (codes.isEmpty() || codes.length() < 6) {
                    code.setError("Wrong OTP...");
                    code.requestFocus();
                    return;
                }
                pbar.setVisibility(View.VISIBLE);
                verifyCode(codes);
            }
        });
        pbar= findViewById(R.id.progressBar2);
        pbar.setVisibility(View.GONE);
        Intent intent = getIntent();
        String mobile = intent.getStringExtra("Phone");
        verifyVerificationCode(mobile);
    }
    private void verifyVerificationCode(String mobile) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91"+ mobile)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
        }
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                pbar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }
        @Override
        public void onVerificationFailed( FirebaseException e) {
            Toast.makeText(verifyphoneactivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
    private void verifyCode(String codeByUser) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, codeByUser);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(verifyphoneactivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String uid= mAuth.getUid();
                        if (task.isSuccessful()) {
                            pbar.setVisibility(View.VISIBLE);
                            isuseralreadypresent();
                        } else {
                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = make(findViewById(R.id.parent), message, LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(verifyphoneactivity.this,MainActivity.class));
            finish();
        }
    }
    public void isuseralreadypresent(){
        String mob = getIntent().getStringExtra("Phone");
        String mail = getIntent().getStringExtra("Email");
        Task<DataSnapshot> db= FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getUid())
        .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DataSnapshot snapshot = task.getResult();
                        assert snapshot != null;
                        if(snapshot.exists()){
                            Intent i = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else{
                            Intent intent = new Intent(getApplicationContext(),Afterphoneloginuserdetail.class);
                            intent.putExtra("mail",mail);
                            intent.putExtra("phone",mob);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

    }


}