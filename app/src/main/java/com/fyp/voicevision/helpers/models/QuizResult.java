package com.fyp.voicevision.helpers.models;

import androidx.annotation.NonNull;

public class QuizResult {

    private String qr_id, uid, username, userEmail, noOfQuestions, correctAnswers, scoresPercentage, quizType, levelType, quizContent;
    private long createdDate;

    public QuizResult() {
    }

    public QuizResult(String qr_id, String uid, String username, String userEmail, String noOfQuestions, String correctAnswers, String scoresPercentage, String quizType, String levelType, String quizContent, long createdDate) {
        this.qr_id = qr_id;
        this.uid = uid;
        this.username = username;
        this.userEmail = userEmail;
        this.noOfQuestions = noOfQuestions;
        this.correctAnswers = correctAnswers;
        this.scoresPercentage = scoresPercentage;
        this.quizType = quizType;
        this.levelType = levelType;
        this.quizContent = quizContent;
        this.createdDate = createdDate;
    }

    public String getQr_id() {
        return qr_id;
    }

    public void setQr_id(String qr_id) {
        this.qr_id = qr_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getNoOfQuestions() {
        return noOfQuestions;
    }

    public void setNoOfQuestions(String noOfQuestions) {
        this.noOfQuestions = noOfQuestions;
    }

    public String getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(String correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public String getScoresPercentage() {
        return scoresPercentage;
    }

    public void setScoresPercentage(String scoresPercentage) {
        this.scoresPercentage = scoresPercentage;
    }

    public String getQuizType() {
        return quizType;
    }

    public void setQuizType(String quizType) {
        this.quizType = quizType;
    }

    public String getLevelType() {
        return levelType;
    }

    public void setLevelType(String levelType) {
        this.levelType = levelType;
    }

    public String getQuizContent() {
        return quizContent;
    }

    public void setQuizContent(String quizContent) {
        this.quizContent = quizContent;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    @NonNull
    @Override
    public String toString() {
        return "QuizResult{" +
                "qr_id='" + qr_id + '\'' +
                ", uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", noOfQuestions='" + noOfQuestions + '\'' +
                ", correctAnswers='" + correctAnswers + '\'' +
                ", scoresPercentage='" + scoresPercentage + '\'' +
                ", quizType='" + quizType + '\'' +
                ", levelType='" + levelType + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}