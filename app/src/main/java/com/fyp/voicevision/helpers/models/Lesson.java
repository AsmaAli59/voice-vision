package com.fyp.voicevision.helpers.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Lesson implements Serializable {

    private String lid, cid, title, courseType, levelType, videoUrl;
    private long createDate;

    public Lesson() {
    }

    public Lesson(String lid, String cid, String title, String courseType, String levelType, String videoUrl, long createDate) {
        this.lid = lid;
        this.cid = cid;
        this.title = title;
        this.courseType = courseType;
        this.levelType = levelType;
        this.videoUrl = videoUrl;
        this.createDate = createDate;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
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

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getLevelType() {
        return levelType;
    }

    public void setLevelType(String levelType) {
        this.levelType = levelType;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    @NonNull
    @Override
    public String toString() {
        return "Lesson{" +
                "lid='" + lid + '\'' +
                ", cid='" + cid + '\'' +
                ", title='" + title + '\'' +
                ", courseType='" + courseType + '\'' +
                ", levelType='" + levelType + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}