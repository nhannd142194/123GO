package com.example.thien.a123go.models;

import android.graphics.Bitmap;

/**
 * Created by thien on 4/18/2017.
 */

public class News {
    public final String id;
    public final String title;
    public final String created_at;
    public final String content;
    public final Bitmap image;

    public News(String id, String title, String created_at, String content, Bitmap image){
        this.id = id;
        this.title = title;
        this.created_at = created_at;
        this.content = content;
        this.image = image;
    }

    @Override
    public String toString(){
        return "title: " + title + ", created: " + created_at + ", content: " + content;
    }
}
