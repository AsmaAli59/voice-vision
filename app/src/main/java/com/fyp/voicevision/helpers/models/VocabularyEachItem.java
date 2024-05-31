package com.fyp.voicevision.helpers.models;

import androidx.annotation.NonNull;

public class VocabularyEachItem {

    private int id, vid;
    private String titleEnglish, titleUrdu;

    public VocabularyEachItem() {
    }

    public VocabularyEachItem(int id, int vid, String titleEnglish, String titleUrdu) {
        this.id = id;
        this.vid = vid;
        this.titleEnglish = titleEnglish;
        this.titleUrdu = titleUrdu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public String getTitleEnglish() {
        return titleEnglish;
    }

    public void setTitleEnglish(String titleEnglish) {
        this.titleEnglish = titleEnglish;
    }

    public String getTitleUrdu() {
        return titleUrdu;
    }

    public void setTitleUrdu(String titleUrdu) {
        this.titleUrdu = titleUrdu;
    }

    @NonNull
    @Override
    public String toString() {
        return "VocabularyItem {" +
                "id='" + id + '\'' +
                ", vid='" + vid + '\'' +
                ", titleEnglish='" + titleEnglish + '\'' +
                ", titleUrdu='" + titleUrdu + '\'' +
                '}';
    }
}
