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

import com.example.typingtest.Constants;
import com.example.typingtest.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnStartGame,btnSingleWord,btnParagraph;
    final String prefName = "isFirst";

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
