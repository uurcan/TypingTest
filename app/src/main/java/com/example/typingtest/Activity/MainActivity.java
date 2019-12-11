package com.example.typingtest.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;

import com.example.typingtest.Utils.Constants;
import com.example.typingtest.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnStartGame,btnSingleWord,btnParagraph,btnHighScore,btnHParagraph,btnHSingleWord;
    final String prefName = Constants.IS_FIRST_LOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeComponents();
        initializeSharedPreferences();
    }

    private void initializeSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(prefName, Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean(Constants.PREF_FIRST_TIME,true)){
            initializeDialog();
            sharedPreferences.edit().putBoolean(Constants.PREF_FIRST_TIME,false).apply();
        }
    }

    private void initializeDialog() {
        final EditText input = new EditText(this);
        AlertDialog builder = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.nickname))
                .setView(input)
                .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = getSharedPreferences(Constants.USER_PREFERENCE,Context.MODE_PRIVATE).edit();
                        editor.putString(Constants.USER_NICK,input.getText().toString());
                        editor.apply();
                    }
                }).create();
        builder.show();
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
            case R.id.btnHighScore:
                startHighScoreSelection();
                break;
            case R.id.btnHighScoreParagraph:
                startHighScoreParagraph();
                break;
            case R.id.btnHighScoreSingleWord:
                startHighScoreSingleWord();
                break;
        }
    }

    private void startHighScoreSingleWord() {
        Intent intent = new Intent(this,HighScoreActivity.class);
        intent.putExtra(Constants.GAME_TYPE,Constants.SINGLE_WORD);
        startActivity(intent);
    }

    private void startHighScoreParagraph() {
        Intent intent = new Intent(this,HighScoreActivity.class);
        intent.putExtra(Constants.GAME_TYPE,Constants.PARAGRAPH);
        startActivity(intent);
    }

    private void initializeComponents(){
        btnStartGame = findViewById(R.id.btnStartGame);
        btnParagraph = findViewById(R.id.btnParagraph);
        btnSingleWord = findViewById(R.id.btnSingleWord);
        btnHighScore = findViewById(R.id.btnHighScore);
        btnHParagraph = findViewById(R.id.btnHighScoreParagraph);
        btnHSingleWord = findViewById(R.id.btnHighScoreSingleWord);
        btnParagraph.setVisibility(View.GONE);
        btnSingleWord.setVisibility(View.GONE);
        btnHParagraph.setVisibility(View.GONE);
        btnHSingleWord.setVisibility(View.GONE);
        btnHighScore.setOnClickListener(this);
        btnStartGame.setOnClickListener(this);
        btnSingleWord.setOnClickListener(this);
        btnParagraph.setOnClickListener(this);
        btnHParagraph.setOnClickListener(this);
        btnHSingleWord.setOnClickListener(this);
    }
    private void startSelection(){
        btnStartGame.setVisibility(View.GONE);
        btnHighScore.setVisibility(View.GONE);
        btnSingleWord.setVisibility(View.VISIBLE);
        btnParagraph.setVisibility(View.VISIBLE);
        Animation animation = new AlphaAnimation(0, 1);
        animation.setDuration(1500);
        btnSingleWord.setAnimation(animation);
        btnParagraph.setAnimation(animation);
    }
    private void startSingleWord(){
        startActivity(new Intent(this,SingleWordActivity.class));
    }
    private void startParagraph(){
        startActivity(new Intent(this,ParagraphActivity.class));
    }
    private void startHighScoreSelection() {
        btnStartGame.setVisibility(View.GONE);
        btnHighScore.setVisibility(View.GONE);
        btnHParagraph.setVisibility(View.VISIBLE);
        btnHSingleWord.setVisibility(View.VISIBLE);
        Animation animation = new AlphaAnimation(0,1);
        animation.setDuration(1500);
        btnHParagraph.setAnimation(animation);
        btnHSingleWord.setAnimation(animation);
    }
}
