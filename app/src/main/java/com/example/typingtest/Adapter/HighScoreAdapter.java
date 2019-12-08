package com.example.typingtest.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.typingtest.Model.User;
import com.example.typingtest.R;

import java.util.List;

public class HighScoreAdapter extends RecyclerView.Adapter<HighScoreAdapter.ViewHolder> {
    private List<User> userList;
    private Context context;

    public HighScoreAdapter(Context context,List<User> userList) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public HighScoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_high_score_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HighScoreAdapter.ViewHolder holder, int position) {
        holder.textViewUserNick.setText(userList.get(position).getUserName());
        holder.textViewUserScore.setText(String.valueOf(userList.get(position).getUserScore()));
        if (position == 0){
            holder.imgUserScore.setImageResource(R.drawable.main_menu);
        }else {
            holder.imgUserScore.setImageResource(R.drawable.try_again);
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewUserNick,textViewUserScore;
        private ImageView imgUserScore;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserNick = itemView.findViewById(R.id.txtHighScoreNick);
            textViewUserScore = itemView.findViewById(R.id.txtHighScoreScore);
            imgUserScore = itemView.findViewById(R.id.imgHighScoreImg);
        }
    }
}
