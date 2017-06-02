package com.example.thien.a123go.models;

/**
 * Created by thien on 5/1/2017.
 */

public class Counter {
    public final String name;
    public final String address;
    public final String phone;
    public final String description;
    public final String image;

    public Counter(String name,String address, String phone, String description, String image){
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.description = description;
        this.image = image;
    }
}
