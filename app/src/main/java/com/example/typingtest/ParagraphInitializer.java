package com.example.typingtest;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ParagraphInitializer {
    private Context context;
    private ArrayList<String> wordList;
    public int totalLineCount;
    private int currentIndex;
    private String paragraph;
    private AssetManager assetManager;
    private Random random;
    private BufferedReader bufferedReader;
    private InputStream inputStream;

    public ParagraphInitializer(Context context) throws IOException {
        this.context = context;
        totalLineCount = currentIndex = 0;
        paragraph = "Default Paragraph";
        assetManager = context.getAssets();
        random = new Random();
        inputStream = assetManager.open("paragraphs.txt");
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        while (bufferedReader.readLine()!= null)
            totalLineCount++;
    }
    public String getRandomParagraph(){
        int lineIndex = random.nextInt(totalLineCount);
        try {
            inputStream = assetManager.open("paragraphs.txt");
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            for (int i = 0; i < lineIndex -1; i++){
                bufferedReader.readLine();
            }paragraph = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        wordList = getWords(paragraph);
        return  paragraph;
    }
    public ArrayList<String> getWords(String paragraph) {
        return new ArrayList<>(Arrays.asList(paragraph.split(" ")));
    }
    public int getWordCount(){
        return wordList.size();
    }
    public int getNextIndex(int wordNumber){
        currentIndex += wordList.get(wordNumber).length()+1;
        return currentIndex;
    }
    public int getCurrentIndex(){
        return currentIndex;
    }
    public int lookaheadIndex(int wordNumber){
        return  currentIndex + wordList.get(wordNumber).length()+1;
    }
}
