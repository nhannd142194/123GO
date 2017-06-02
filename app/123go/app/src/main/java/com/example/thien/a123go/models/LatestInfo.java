package com.example.thien.a123go.models;

import android.graphics.Bitmap;

/**
 * Created by thien on 5/1/2017.
 */

public class LatestInfo {
    public final String id;
    public final String title;
    public final String created_at;
    public final String content;
    public final int type;
    public final Bitmap image;

    public LatestInfo(String id, String title, String created_at, String content,int type, Bitmap image){
        this.id = id;
        this.title = title;
        this.created_at = created_at;
        this.content = content;
        this.type = type;
        this.image = image;
    }

    @Override
    public String toString(){
        return "title: " + title + ", created: " + created_at + ", content: " + content;
    }
}
