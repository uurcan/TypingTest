package com.example.typingtest.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.typingtest.Model.User;
import com.example.typingtest.R;
import com.example.typingtest.Utils.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SingleWordActivity extends AppCompatActivity {
    public CountDownTimer countDownTimer;
    private TextView textCurrentScore,textTimeRemaining,textCurrentWord;
    private ArrayList<String> sequence,wordList;
    private EditText edtUserInput;
    private Random random = new Random();
    private String currentWord;
    private int score,numberOfLetters = 0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_word);
        textCurrentScore = findViewById(R.id.textCurrentCount);
        textTimeRemaining = findViewById(R.id.textRemainingTime);
        textCurrentWord = findViewById(R.id.textRandomWords);
        edtUserInput = findViewById(R.id.edtUserInput);
        try {
            extractRandomWords();
        } catch (IOException e) {
            e.printStackTrace();
        }
        generateSequence();
        countDownTimer = new CountDownTimer(60000,1000){

            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                textTimeRemaining.setText("00:" + millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                textTimeRemaining.setText(getString(R.string.zero));
                edtUserInput.setEnabled(false);
                textCurrentWord.setText("-----");
                initializeResultDialog();
            }
        }.start();
        currentWord = sequence.get(0);
        textCurrentWord.setText(currentWord);
        edtUserInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //empty method
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CharSequence textView = textCurrentWord.getText();
                CharSequence editText = s.toString();
                if (textView.equals(editText)){
                    score++;
                    currentWord = sequence.get(score);
                    textCurrentWord.setText(currentWord);
                    edtUserInput.setText("");
                    numberOfLetters += s.length();
                    String result = getString(R.string.score) + " " + score;
                    textCurrentScore.setText(result);
                }
                else if(textView.length() <= editText.length())
                    textCurrentWord.setTextColor(Color.RED);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //empty method
            }
        });
    }

    private void generateSequence() {
        sequence = new ArrayList<>();
        for (int i = 0;i<100;i++)
            sequence.add(wordList.get(random.nextInt(wordList.size())));
    }
    private void initializeResultDialog(){
        this.setTheme(R.style.MaterialTheme);
        String speedText = getString(R.string.typing_speed) + " " + numberOfLetters / 5 + " WPM";
        SpannableString spannableString = new SpannableString(speedText);
        spannableString.setSpan(Color.RED,0,speedText.length(),0);
        //todo: spannable color change is disabled
        final BottomSheetMaterialDialog builder = new BottomSheetMaterialDialog.Builder(this)
                .setTitle("Time").setCancelable(false)
                .setMessage(getString(R.string.correct_letter) + " " + numberOfLetters + "\n" +
                        getString(R.string.correct_word) + "\n" + spannableString)
                .setPositiveButton("Try Again",R.drawable.try_again,new BottomSheetMaterialDialog.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        pushFirebaseValues();
                        edtUserInput.setText("");
                        recreate();
                    }
                })
                .setNegativeButton("Main Menu",R.drawable.main_menu,new BottomSheetMaterialDialog.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        pushFirebaseValues();
                        setTheme(R.style.AppTheme);
                        finishAffinity();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }).build();
        builder.show();
    }

    private void pushFirebaseValues() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.USER_SCORE);
        String id = databaseReference.push().getKey();
        User user = new User(id,getSharedPreferences(Constants.USER_PREFERENCE,Context.MODE_PRIVATE).getString(Constants.USER_NICK,getString(R.string.OK)),
                numberOfLetters/5,0,numberOfLetters,true);
        if (id!=null){
            databaseReference.child(id).setValue(user);
        }
    }

    private void extractRandomWords() throws IOException {
        AssetManager assetManager = getAssets();
        InputStream inputStream = assetManager.open("words.txt");
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        wordList = new ArrayList<>();
        while ((line = in.readLine()) != null){
            String word = line.trim();
            wordList.add(word);
        }
    }
}
