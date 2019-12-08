package com.example.typingtest.Model;

import java.util.Comparator;

public class User {
    private String userId;
    private String userName;
    private int userScore;
    private int highestScore;
    private int userLetterCount;
    public User(){}
    public User(String userId, String userName, int userScore,int highestScore,int userLetterCount) {
        this.userId = userId;
        this.userName = userName;
        this.userScore = userScore;
        this.highestScore = highestScore;
        this.userLetterCount = userLetterCount;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserScore() {
        return userScore;
    }

    public int getHighestScore() {
        return highestScore;
    }
    public int getUserLetterCount(){
        return userLetterCount;
    }
    public static final Comparator<User> sortByScore = new Comparator<User>() {
        @Override
        public int compare(User o1, User o2) {
            return o2.userScore - o1.userScore;
        }
    };
}
