package com.example.typingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnStartGame,btnSingleWord,btnParagraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeComponents();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnParagraph:
                startParagraph();
                break;
            case R.id.btnSingleWord:
                startSingleWord();
                break;
            case R.id.btnStartGame:
                startSelection();
                break;
        }
    }
    private void initializeComponents(){
        btnStartGame = findViewById(R.id.btnStartGame);
        btnParagraph = findViewById(R.id.btnParagraph);
        btnSingleWord = findViewById(R.id.btnSingleWord);
        btnParagraph.setVisibility(View.GONE);
        btnSingleWord.setVisibility(View.GONE);
        btnStartGame.setOnClickListener(this);
        btnSingleWord.setOnClickListener(this);
        btnParagraph.setOnClickListener(this);
    }
    private void startSelection(){
        btnStartGame.setVisibility(View.GONE);
        btnSingleWord.setVisibility(View.VISIBLE);
        btnParagraph.setVisibility(View.VISIBLE);
        Animation animation = new AlphaAnimation(0, 1);
        animation.setDuration(1500);
        btnSingleWord.setAnimation(animation);
        btnParagraph.setAnimation(animation);
    }
    private void startSingleWord(){
        Intent intent = new Intent(this,SingleWordActivity.class);
        intent.putExtra("GAME_MODE","SINGLE_WORD_MODE");
        startActivity(intent);
    }
    private void startParagraph(){
        Intent intent = new Intent(this,ParagraphActivity.class);
        intent.putExtra("GAME_MODE","PARAGRAPH_MODE");
        startActivity(intent);
    }
}
