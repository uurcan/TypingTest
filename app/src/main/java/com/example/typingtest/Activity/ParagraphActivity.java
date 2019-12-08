package com.example.typingtest.Activity;

import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.typingtest.Utils.Constants;
import com.example.typingtest.Model.User;
import com.example.typingtest.Utils.ParagraphInitializer;
import com.example.typingtest.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

import java.io.IOException;
import java.util.ArrayList;

public class ParagraphActivity extends AppCompatActivity implements TextWatcher {
    private ParagraphInitializer paragraphInitializer;
    private ArrayList<String> wordList;
    private TextView paragraphTextView,timerView;
    private Boolean inputClean;
    private EditText edtInput;
    public CountDownTimer countDownTimer;
    private ProgressBar progressBar;
    private int wordIndex,numberOfWords,numberOfLetters;
    private DatabaseReference databaseReference;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paragraph);
        initializeComponents();
        try {
            refreshParagraph();
        } catch (IOException e) {
            e.printStackTrace();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.USER_SCORE);
        edtInput.addTextChangedListener(this);
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //empty method
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().replace(" ","").equals(wordList.get(wordIndex))){
            inputClean = true;
            numberOfLetters += s.length();
            wordIndex++;
            progressBar.setProgress((int)(100 * (float)wordIndex/numberOfWords));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (inputClean){
            inputClean = false;
            edtInput.setText("");
            updateColor(paragraphTextView,paragraphInitializer.getNextIndex(wordIndex-1));
        }else if(!wordList.get(wordIndex).contains(s.toString().replace(" ",""))){
            undoColorChange(paragraphTextView,paragraphInitializer.getCurrentIndex(),paragraphInitializer.lookaheadIndex(wordIndex));
        }
    }
    protected void refreshParagraph() throws IOException {
        paragraphInitializer = new ParagraphInitializer(this);
        String paragraph = paragraphInitializer.getRandomParagraph();
        wordList = paragraphInitializer.getWords(paragraph);
        paragraphTextView.setText(paragraph,TextView.BufferType.SPANNABLE);
        progressBar.setProgress(0);
        inputClean = false;
        wordIndex = 0;
        numberOfWords = paragraphInitializer.getWordCount();
        countDownTimer = new CountDownTimer(60000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                timerView.setText("00:" + millisUntilFinished / 1000);
                if ((millisUntilFinished/1000) < 10)
                    timerView.setTextColor(Color.rgb(139,34,34));
            }

            @Override
            public void onFinish() {
                timerView.setText(getString(R.string.zero));
                edtInput.setEnabled(false);
                initializeResultDialog();
            }
        }.start();
    }

    private void initializeResultDialog() {
        this.setTheme(R.style.MaterialTheme);
        String speedText = getString(R.string.typing_speed) + " " + numberOfLetters / 5 + " WPM";
        SpannableString spannableString = new SpannableString(speedText);
        spannableString.setSpan(Color.RED,0,speedText.length(),0);
        //todo: spannable color change is disabled
        final BottomSheetMaterialDialog builder = new BottomSheetMaterialDialog.Builder(this)
                .setTitle("Time").setCancelable(false)
                .setMessage(getString(R.string.correct_letter) + " " + numberOfLetters + "\n" +
                                    getString(R.string.correct_word) + " " + wordIndex + "\n" +
                                    spannableString)
                .setPositiveButton("Try Again",R.drawable.try_again,new BottomSheetMaterialDialog.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        pushFirebaseValues();
                        edtInput.setText("");
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

    private void initializeComponents() {
        paragraphTextView = findViewById(R.id.txtParagraph);
        edtInput = findViewById(R.id.edtTextWriter);
        timerView = findViewById(R.id.txtTimer);
        progressBar = findViewById(R.id.typingProgress);
    }

    private void updateColor(TextView paragraphTextView, int nextIndex) {
        Spannable wordToSpan = new SpannableString(paragraphTextView.getText());
        wordToSpan.setSpan(new ForegroundColorSpan(Color.rgb(34,139,34)),0,nextIndex,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        paragraphTextView.setText(wordToSpan);
    }

    private void undoColorChange(TextView paragraphTextView, int currentIndex, int lookaheadIndex) {
        Spannable wordToSpan = new SpannableString(paragraphTextView.getText());
        wordToSpan.setSpan(new ForegroundColorSpan(Color.rgb(139,34,34)),currentIndex,lookaheadIndex,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        paragraphTextView.setText(wordToSpan);
    }
    private void pushFirebaseValues(){
        String id = databaseReference.push().getKey();
        User user = new User(id,getSharedPreferences(Constants.USER_PREFERENCE, Context.MODE_PRIVATE)
                .getString(Constants.USER_NICK,getString(R.string.OK)),(numberOfLetters/5),0,numberOfLetters);
        if (id != null)
            databaseReference.child(id).setValue(user);
    }
}
