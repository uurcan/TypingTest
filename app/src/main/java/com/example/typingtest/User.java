package com.example.typingtest;

public class User {
    private String userId;
    private String userName;
    private int userScore;
    private int highestScore;

    public User(String userId, String userName, int userScore,int highestScore) {
        this.userId = userId;
        this.userName = userName;
        this.userScore = userScore;
        this.highestScore = highestScore;
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
}
