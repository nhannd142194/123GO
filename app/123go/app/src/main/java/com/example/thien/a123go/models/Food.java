package com.example.thien.a123go.models;

import android.graphics.Bitmap;

/**
 * Created by thien on 5/1/2017.
 */

public class Food {
    public final String id;
    public final String name;
    public final String description;
    public final int price;
    public final int discount;
    public final Bitmap image;

    public Food(String id, String name, String description, int price, int discount, Bitmap image){
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.image = image;
    }
}
