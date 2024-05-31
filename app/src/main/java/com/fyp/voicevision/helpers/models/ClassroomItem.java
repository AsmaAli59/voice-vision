package com.fyp.voicevision.helpers.models;

import androidx.annotation.NonNull;

public class ClassroomItem {

    private String cid;
    private String title;
    private boolean isLocked;

    public ClassroomItem() {
    }

    public ClassroomItem(String cid, String title, boolean isLocked) {
        this.cid = cid;
        this.title = title;
        this.isLocked = isLocked;
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

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    @NonNull
    @Override
    public String toString() {
        return "ClassroomItem {" +
                "cid='" + cid + '\'' +
                ", title='" + title + '\'' +
                ", isLocked=" + isLocked +
                '}';
    }
}
