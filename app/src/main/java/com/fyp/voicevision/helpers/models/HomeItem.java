package com.fyp.voicevision.helpers.models;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

public class HomeItem {

    private int id;
    private String title;
    private String body;
    @ColorInt
    private int colorId;
    @DrawableRes
    private int drawableId;

    public HomeItem() {
    }

    public HomeItem(int id, String title, String body, int colorId, int drawableId) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.colorId = colorId;
        this.drawableId = drawableId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    @NonNull
    @Override
    public String toString() {
        return "HomeItem {" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", colorId=" + colorId +
                ", drawableId=" + drawableId +
                '}';
    }
}
