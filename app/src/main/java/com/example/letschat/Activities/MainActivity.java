package com.example.letschat.Activities;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
//import com.example.letschat.Adapters.TopStatusAdapter;
import com.example.letschat.Fragments.Calls_fragment;
import com.example.letschat.Fragments.Status_fragment;
import com.example.letschat.Fragments.chat_fragment;
import com.example.letschat.Models.UserStaus;
import com.example.letschat.R;
import com.example.letschat.Models.User;
import com.example.letschat.Adapters.UserAdapter;
import com.example.letschat.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Button logout;
    FirebaseAuth mauth;
    FirebaseFirestore db;
    DocumentReference reference;
    ArrayList<User> users;
    FirebaseDatabase database;
    //TopStatusAdapter statusAdapter;
    ArrayList<UserStaus> userStaus;
    Toolbar toolbar;
    UserAdapter useradapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mauth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        toolbar=(androidx.appcompat.widget.Toolbar)findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        BottomNavigationView bottomNavigationView =findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        FragmentTransaction chat_Transaction = getSupportFragmentManager().beginTransaction();
        chat_Transaction.replace(R.id.fragment_container,new chat_fragment());
        chat_Transaction.commit();
        users = new ArrayList<>();
        useradapter = new UserAdapter(this, users);

        userStaus=new ArrayList<>();


    }
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.CHATS:
                    FragmentTransaction chat_Transaction = getSupportFragmentManager().beginTransaction();
                    chat_Transaction.replace(R.id.fragment_container,new chat_fragment());
                    chat_Transaction.commit();
                    break;
                case R.id.STATUS:
                    FragmentTransaction status_Transaction = getSupportFragmentManager().beginTransaction();
                    status_Transaction.replace(R.id.fragment_container,new Status_fragment());
                    status_Transaction.commit();
                    break;
                case R.id.CALLS:
                    FragmentTransaction calls_Transaction = getSupportFragmentManager().beginTransaction();
                    calls_Transaction.replace(R.id.fragment_container,new Calls_fragment());
                    calls_Transaction.commit();
                    break;
            }
            return  true;
        }

    };
    @Override
    protected void onResume() {
        super.onResume();
        String currentuid=FirebaseAuth.getInstance().getUid();
        FirebaseDatabase database =FirebaseDatabase.getInstance();
        database.getReference().child("status").child(currentuid).setValue("Online");
    }

    @Override
    protected void onPause() {
        String currentuid=FirebaseAuth.getInstance().getUid();
        FirebaseDatabase database =FirebaseDatabase.getInstance();
        database.getReference().child("status").child(currentuid).setValue("");
        super.onPause();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logouts:
                mauth.signOut();
                startActivity(new Intent(MainActivity.this,phoneActivity.class));
                finish();
                break;
            case R.id.group:
                startActivity(new Intent(MainActivity.this,GroupActivity.class));
                break;
            case R.id.setting:
                startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}