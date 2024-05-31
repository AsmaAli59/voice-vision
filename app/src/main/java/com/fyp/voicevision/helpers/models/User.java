package com.fyp.voicevision.helpers.models;

import androidx.annotation.NonNull;

import com.fyp.voicevision.helpers.enums.LevelType;
import com.fyp.voicevision.helpers.enums.ModeType;

public class User {

    private String uid, fullName, email, age;
    private ModeType modeType;
    private LevelType levelType, quizLevelType;
    private int currentCourse;
    private long createdDate;

    public User() {
    }

    public User(String uid, String fullName, String email, String age, ModeType modeType, LevelType levelType, LevelType quizLevelType, int currentCourse, long createdDate) {
        this.uid = uid;
        this.fullName = fullName;
        this.email = email;
        this.age = age;
        this.modeType = modeType;
        this.levelType = levelType;
        this.quizLevelType = quizLevelType;
        this.currentCourse = currentCourse;
        this.createdDate = createdDate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public ModeType getModeType() {
        return modeType;
    }

    public void setModeType(ModeType modeType) {
        this.modeType = modeType;
    }

    public LevelType getLevelType() {
        return levelType;
    }

    public void setLevelType(LevelType levelType) {
        this.levelType = levelType;
    }

    public LevelType getQuizLevelType() {
        return quizLevelType;
    }

    public void setQuizLevelType(LevelType quizLevelType) {
        this.quizLevelType = quizLevelType;
    }

    public int getCurrentCourse() {
        return currentCourse;
    }

    public void setCurrentCourse(int currentCourse) {
        this.currentCourse = currentCourse;
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
        return "User{" +
                "uid='" + uid + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", age='" + age + '\'' +
                ", modeType=" + modeType +
                ", levelType=" + levelType +
                ", quizLevelType=" + quizLevelType +
                ", originalList=" + currentCourse +
                ", createdDate=" + createdDate +
                '}';
    }
}