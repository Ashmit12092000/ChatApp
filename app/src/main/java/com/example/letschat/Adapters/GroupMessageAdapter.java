package com.example.letschat.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.letschat.Models.Message;
import com.example.letschat.Models.User;
import com.example.letschat.R;
import com.example.letschat.databinding.DeleteLayoutBinding;
import com.example.letschat.databinding.GroupRecieverChatBinding;
import com.example.letschat.databinding.GroupSenderChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;


public class GroupMessageAdapter extends RecyclerView.Adapter{
    Context context;
    ArrayList<Message> messages;
    final int ITEM_SENT=1;
    final int ITEM_RECEIVED=2;
    public GroupMessageAdapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == ITEM_SENT){
            View view= LayoutInflater.from(context).inflate(R.layout.group_sender_chat,parent,false);
            return new SenderViewHolder(view);
        }
        else{
            View view= LayoutInflater.from(context).inflate(R.layout.group_reciever_chat,parent,false);
            return new RecieverViewHolder(view);
        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        Message message =messages.get(position);
        if(FirebaseAuth.getInstance().getUid().equals(message.getSenderID())){
            return  ITEM_SENT;
        }
        else {
            return ITEM_RECEIVED;
        }
    }
    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.setIsRecyclable(false);

        if(holder.getClass()==SenderViewHolder.class){
            SenderViewHolder viewHolder = (SenderViewHolder)holder;

            if(message.getMessage().equals("gphoto")){
                viewHolder.binding.imageView.setVisibility(View.VISIBLE);
                viewHolder.binding.message.setVisibility(View.GONE);
                Glide.with(context).load(message.getImage_uri()).into(((SenderViewHolder) holder).binding.imageView);
            }
            else if (message.getMessage().equals("gcamera")){
                viewHolder.binding.imageView.setVisibility(View.VISIBLE);
                viewHolder.binding.message.setVisibility(View.GONE);
                Glide.with(context).load(message.getImage_uri()).into(((SenderViewHolder) holder).binding.imageView);
            }
            else {
                viewHolder.binding.message.setText(message.getMessage());
                FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(message.getSenderID())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()) {
                                    User user = snapshot.getValue(User.class);
                                    viewHolder.binding.name.setText(user.getDisplay_name());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }


        }
        else {
            RecieverViewHolder viewHolder = (RecieverViewHolder)holder;
            if(message.getMessage().equals("gphoto")){
                viewHolder.binding.imageView2.setVisibility(View.VISIBLE);
                viewHolder.binding.recieverMessage.setVisibility(View.GONE);
                Glide.with(context).load(message.getImage_uri()).into(((RecieverViewHolder) holder).binding.imageView2);
            }
            else if (message.getMessage().equals("gcamera")){
                viewHolder.binding.imageView2.setVisibility(View.VISIBLE);
                viewHolder.binding.recieverMessage.setVisibility(View.GONE);
                Glide.with(context).load(message.getImage_uri()).into(((RecieverViewHolder) holder).binding.imageView2);
            }
            else {
                viewHolder.binding.recieverMessage.setText(message.getMessage());
                FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(message.getSenderID())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()) {
                                    User user = snapshot.getValue(User.class);
                                    viewHolder.binding.name.setText("@"+user.getDisplay_name());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        }
    }
    @Override
    public int getItemCount() {
        return messages.size();
    }
    public  class SenderViewHolder extends RecyclerView.ViewHolder{
        GroupSenderChatBinding binding;
        LinearLayout msgLayout;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=GroupSenderChatBinding.bind(itemView);
            msgLayout=(LinearLayout)itemView.findViewById(R.id.senderlinearLayout);

        }
    }
    public  class RecieverViewHolder extends RecyclerView.ViewHolder{
        GroupRecieverChatBinding binding;
        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding =GroupRecieverChatBinding.bind(itemView);
        }
    }
}
