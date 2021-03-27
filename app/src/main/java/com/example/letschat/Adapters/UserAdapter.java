package com.example.letschat.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.letschat.Activities.MainActivity;
import com.example.letschat.Activities.chat_activity;
import com.example.letschat.Models.User;
import com.example.letschat.R;
import com.example.letschat.databinding.SampleUserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UsersViewHolder>{
    Context context;
    ArrayList<User> users;
    public UserAdapter(Context context, ArrayList<User> users){
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_user,parent,false);

        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        User user = users.get(position);
        holder.binding.username.setText(user.getDisplay_name());
        Glide.with(context).load(user.profile_pic).placeholder(R.drawable.account).into(holder.binding.profileImage);
        String sender_id= FirebaseAuth.getInstance().getUid();
        String sender_Room= sender_id+user.getUserID();
        FirebaseDatabase.getInstance().getReference()
                .child("chats")
                .child(sender_Room)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            String lastMsg = snapshot.child("lastMsg").getValue(String.class);
                            Long tim = snapshot.child("lastMsgTime").getValue(Long.class);
                            SimpleDateFormat dateFormat=new SimpleDateFormat("hh:mm a");
                            holder.binding.time.setText(dateFormat.format(new Date(tim)));
                            holder.binding.lastMessage.setText(lastMsg);
                            holder.binding.lastMessage.setVisibility(View.VISIBLE);

                        }
                        else {
                            assert holder.binding.lastMessage2 != null;
                            holder.binding.lastMessage2.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, chat_activity.class);
                intent.putExtra("name",user.getDisplay_name());
                intent.putExtra("image",user.profile_pic);
                intent.putExtra("uid",user.getUserID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        SampleUserBinding binding;
        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SampleUserBinding.bind(itemView);
        }
    }
}
