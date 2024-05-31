package com.fyp.voicevision.helpers.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class CourseItem implements Serializable {

    private String cid, title, levelType;
    private boolean isLocked;
    private long createdDate;

    public CourseItem() {
    }

    public CourseItem(String cid, String title, String levelType, boolean isLocked, long createdDate) {
        this.cid = cid;
        this.title = title;
        this.levelType = levelType;
        this.isLocked = isLocked;
        this.createdDate = createdDate;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLevelType() {
        return levelType;
    }

    public void setLevelType(String levelType) {
        this.levelType = levelType;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
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
        return "Course{" +
                "cid='" + cid + '\'' +
                ", title='" + title + '\'' +
                ", levelType='" + levelType + '\'' +
                ", isLocked='" + isLocked + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
