package com.fyp.voicevision.helpers.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class GeneralQuizItem implements Serializable {

    private String qid, tid, quizType, question, optionA, optionB, optionC, optionD;
    private int correctOption;
    private long createdDate;
    private boolean isPass;

    public GeneralQuizItem() {
    }

    public GeneralQuizItem(String qid, String tid, String quizType, String question, String optionA, String optionB, String optionC, String optionD, int correctOption, long createdDate) {
        this.qid = qid;
        this.tid = tid;
        this.quizType = quizType;
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption;
        this.createdDate = createdDate;
        this.isPass = false;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getQuizType() {
        return quizType;
    }

    public void setQuizType(String quizType) {
        this.quizType = quizType;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public int getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(int correctOption) {
        this.correctOption = correctOption;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isPass() {
        return isPass;
    }

    public void setPass(boolean pass) {
        isPass = pass;
    }

    @NonNull
    @Override
    public String toString() {
        return "GeneralQuizItem{" +
                "qid='" + qid + '\'' +
                ", tid='" + tid + '\'' +
                ", quizType='" + quizType + '\'' +
                ", question='" + question + '\'' +
                ", optionA='" + optionA + '\'' +
                ", optionB='" + optionB + '\'' +
                ", optionC='" + optionC + '\'' +
                ", optionD='" + optionD + '\'' +
                ", correctOption=" + correctOption +
                ", createdDate=" + createdDate +
                ", isPass=" + isPass +
                '}';
    }
}