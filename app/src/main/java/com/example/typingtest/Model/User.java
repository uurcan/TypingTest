package com.example.typingtest.Model;

import java.util.Comparator;

public class User {
    private String userId;
    private String userName;
    private int userScore;
    private int userLetterCount;
    private boolean singleWord;
    public User(){}
    public User(String userId, String userName, int userScore,int userLetterCount,boolean singleWord) {
        this.userId = userId;
        this.userName = userName;
        this.userScore = userScore;
        this.userLetterCount = userLetterCount;
        this.singleWord = singleWord;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserScore() {
        return userScore;
    }

    public int getUserLetterCount(){
        return userLetterCount;
    }

    public boolean isSingleWord() {
        return singleWord;
    }

    public static final Comparator<User> sortByScore = new Comparator<User>() {
        @Override
        public int compare(User o1, User o2) {
            return o2.userScore - o1.userScore;
        }
    };
}
