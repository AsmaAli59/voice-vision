package com.fyp.voicevision.helpers.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class QuizTopicItem implements Serializable {

    private String tid, topicName;
    private long createdDate;

    public QuizTopicItem() {
    }

    public QuizTopicItem(String tid, String topicName, long createdDate) {
        this.tid = tid;
        this.topicName = topicName;
        this.createdDate = createdDate;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
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
        return "QuizTopicItem{" +
                "tid='" + tid + '\'' +
                ", topicName='" + topicName + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}