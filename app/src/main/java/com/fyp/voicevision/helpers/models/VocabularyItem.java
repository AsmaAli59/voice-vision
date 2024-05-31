package com.fyp.voicevision.helpers.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class VocabularyItem implements Serializable {

    private int vid;
    private String title;

    public VocabularyItem() {
    }

    public VocabularyItem(int vid, String title) {
        this.vid = vid;
        this.title = title;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NonNull
    @Override
    public String toString() {
        return "VocabularyItem {" +
                "vid='" + vid + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
