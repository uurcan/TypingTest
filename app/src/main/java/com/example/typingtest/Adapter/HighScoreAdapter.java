package com.example.typingtest.Adapter;

import android.content.Context;
import android.content.res.Resources;
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
        String userNickText = context.getString(R.string.nickname)+ " " + userList.get(position).getUserName();
        String userLetterText = context.getString(R.string.correct_letter_count)+ " " + userList.get(position).getUserLetterCount();
        String userWPMText = context.getString(R.string.typing_speed)+ " " + userList.get(position).getUserScore() + " WPM";
        holder.textViewUserNick.setText(userNickText);
        holder.textViewUserScore.setText(userLetterText);
        holder.textViewUserLetter.setText(userWPMText);
        switch (position){
            case 0:
                holder.imgUserScore.setImageResource(R.drawable.gold_medal);
                break;
            case 1:
                holder.imgUserScore.setImageResource(R.drawable.silver_medal);
                break;
            case 2:
                holder.imgUserScore.setImageResource(R.drawable.bronze_medal);
                break;
                default:
                    holder.imgUserScore.setImageResource(R.drawable.star);
                    break;
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewUserNick,textViewUserScore,textViewUserLetter;
        private ImageView imgUserScore;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserLetter = itemView.findViewById(R.id.txtCorrectLetterCount);
            textViewUserNick = itemView.findViewById(R.id.txtHighScoreNick);
            textViewUserScore = itemView.findViewById(R.id.txtHighScoreScore);
            imgUserScore = itemView.findViewById(R.id.imgHighScoreImg);
        }
    }
}
