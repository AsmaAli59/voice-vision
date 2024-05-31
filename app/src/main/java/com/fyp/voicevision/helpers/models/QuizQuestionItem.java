package com.fyp.voicevision.helpers.models;

import androidx.annotation.NonNull;

public class QuizQuestionItem {

    private String qid;
    private String title;
    private String option1;
    private String option2;
    private String option3;
    private boolean isPass;

    public QuizQuestionItem() {
    }

    public QuizQuestionItem(String qid, String title, String option1, String option2, String option3, boolean isPass) {
        this.qid = qid;
        this.title = title;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.isPass = isPass;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
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
        return "QuizQuestionItem{" + "qid='" + qid + '\'' + ", title='" + title + '\'' + ", option1='" + option1 + '\'' + ", option2='" + option2 + '\'' + ", option3='" + option3 + '\'' + ", isPass=" + isPass + '}';
    }
}