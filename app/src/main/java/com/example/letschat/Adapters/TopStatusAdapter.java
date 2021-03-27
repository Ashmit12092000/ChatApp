/*package com.example.letschat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.letschat.Models.UserStaus;
import com.example.letschat.R;
import com.example.letschat.databinding.ItemStatusBinding;

import java.util.ArrayList;

public class TopStatusAdapter  extends RecyclerView.Adapter<TopStatusAdapter.TopStatusViewHodler>{

    Context context;
    ArrayList<UserStaus> statuses;

    public TopStatusAdapter(Context context, ArrayList<UserStaus> statuses) {
        this.context = context;
        this.statuses = statuses;
    }

    @NonNull
    @Override
    public TopStatusViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_status,parent,false);

        return new TopStatusViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopStatusViewHodler holder, int position) {
        UserStaus status = statuses.get(position);
    }

    @Override
    public int getItemCount() {
        return statuses.size();
    }

    public class TopStatusViewHodler extends RecyclerView.ViewHolder {
        ItemStatusBinding binding;
        public TopStatusViewHodler(@NonNull View itemView) {
            super(itemView);
            binding=ItemStatusBinding.bind(itemView);
        }
    }
}*/
