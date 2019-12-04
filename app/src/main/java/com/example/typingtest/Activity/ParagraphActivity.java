package com.example.typingtest.Activity;

import android.annotation.SuppressLint;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
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

import com.example.typingtest.ParagraphInitializer;
import com.example.typingtest.R;

import java.io.IOException;
import java.util.ArrayList;

public class ParagraphActivity extends AppCompatActivity {
    private ParagraphInitializer paragraphInitializer;
    private ArrayList<String> wordList;
    private TextView paragraphTextView,resultTextView,timerView;
    private Boolean inputClean;
    private EditText edtInput;
    public CountDownTimer countDownTimer;
    private ProgressBar progressBar;
    private int wordIndex,numberOfWords;

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
        edtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //empty method
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().replace(" ","").equals(wordList.get(wordIndex))){
                    inputClean = true;
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
        });
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
            }

            @Override
            public void onFinish() {
                timerView.setText(getString(R.string.zero));
                edtInput.setEnabled(false);
                StringBuilder builder = new StringBuilder("Your Score").append(wordIndex).append("wpm");
                resultTextView.setText(builder);
            }
        }.start();
    }
    private void initializeComponents() {
        paragraphTextView = findViewById(R.id.txtParagraph);
        resultTextView = findViewById(R.id.txtResult);
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
    }
}
