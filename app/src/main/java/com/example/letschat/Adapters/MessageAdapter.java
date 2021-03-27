package com.example.letschat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.letschat.Models.Message;
import com.example.letschat.R;
import com.example.letschat.databinding.DeleteLayoutBinding;
import com.example.letschat.databinding.RecieverSampleChatBinding;
import com.example.letschat.databinding.SendersSampleChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter{
    Context context;
    ArrayList<Message> messages;
    String senderRoom;
    String receiverRoom;
    final int ITEM_SENT=1;
    final int ITEM_RECEIVED=2;
    public MessageAdapter(Context context, ArrayList<Message> messages,String senderRoom,String receiverRoom) {
        this.context = context;
        this.messages = messages;
        this.senderRoom = senderRoom;
        this.receiverRoom = receiverRoom;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == ITEM_SENT){
            View view= LayoutInflater.from(context).inflate(R.layout.senders_sample_chat,parent,false);
            return new SenderViewHolder(view);
        }
        else{
            View view= LayoutInflater.from(context).inflate(R.layout.reciever_sample_chat,parent,false);
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

            if(message.getMessage().equals("photo")){
                viewHolder.binding.imageView.setVisibility(View.VISIBLE);
                viewHolder.binding.message.setVisibility(View.GONE);
                Glide.with(context).load(message.getImage_uri()).into(((SenderViewHolder) holder).binding.imageView);
            }
            else if (message.getMessage().equals("camera")){
                viewHolder.binding.imageView.setVisibility(View.VISIBLE);
                viewHolder.binding.message.setVisibility(View.GONE);
                Glide.with(context).load(message.getImage_uri()).into(((SenderViewHolder) holder).binding.imageView);
            }
            else {
                viewHolder.binding.message.setText(message.getMessage());
            }
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    View view =LayoutInflater.from(context).inflate(R.layout.delete_layout,null);
                    DeleteLayoutBinding binding=DeleteLayoutBinding.bind(view);
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("Delete Message")
                            .setView(binding.getRoot())
                            .create();
                    binding.everyone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            message.setMessage("This message is removed.");
                            FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(senderRoom)
                                    .child("messages")
                                    .child(message.getMessageID()).setValue(message);

                            FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(receiverRoom)
                                    .child("messages")
                                    .child(message.getMessageID()).setValue(message);
                            dialog.dismiss();
                        }
                    });
                    binding.delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            message.setMessage("This message is removed.");
                            FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(senderRoom)
                                    .child("messages")
                                    .child(message.getMessageID()).setValue(null);
                            dialog.dismiss();
                        }
                    });
                    binding.cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    return false;
                }
            });
        }
        else {
            RecieverViewHolder viewHolder = (RecieverViewHolder)holder;
            if(message.getMessage().equals("photo")){
                viewHolder.binding.imageView2.setVisibility(View.VISIBLE);
                viewHolder.binding.recieverMessage.setVisibility(View.GONE);
                Glide.with(context).load(message.getImage_uri()).into(((RecieverViewHolder) holder).binding.imageView2);
            }
            else if (message.getMessage().equals("camera")){
                viewHolder.binding.imageView2.setVisibility(View.VISIBLE);
                viewHolder.binding.recieverMessage.setVisibility(View.GONE);
                Glide.with(context).load(message.getImage_uri()).into(((RecieverViewHolder) holder).binding.imageView2);
            }
            else {
                viewHolder.binding.recieverMessage.setText(message.getMessage());
            }
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    View view =LayoutInflater.from(context).inflate(R.layout.delete_layout,null);
                    DeleteLayoutBinding binding = null;
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("Delete Message")
                            .setView(binding.getRoot())
                            .create();
                    binding.everyone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            message.setMessage("This message is removed.");
                            FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(senderRoom)
                                    .child("messages")
                                    .child(message.getMessageID()).setValue(message);

                            FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(receiverRoom)
                                    .child("messages")
                                    .child(message.getMessageID()).setValue(message);
                            dialog.dismiss();
                        }
                    });
                    binding.delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            message.setMessage("This message is removed.");
                            FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(senderRoom)
                                    .child("messages")
                                    .child(message.getMessageID()).setValue(null);
                            dialog.dismiss();
                        }
                    });
                    binding.cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
    public  class SenderViewHolder extends RecyclerView.ViewHolder{
        SendersSampleChatBinding binding;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=SendersSampleChatBinding.bind(itemView);
        }
    }
    public  class RecieverViewHolder extends RecyclerView.ViewHolder{
        RecieverSampleChatBinding binding;
        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding =RecieverSampleChatBinding.bind(itemView);
        }
    }
}
