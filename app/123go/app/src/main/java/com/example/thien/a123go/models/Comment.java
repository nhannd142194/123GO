package com.example.thien.a123go.models;

/**
 * Created by thien on 5/1/2017.
 */

public class Comment {
    public String id;
    public String comment;
    public String user_name;
    public String created_at;

    public Comment(String id, String comment, String user_name, String created_at){
        this.id = id;
        this.comment = comment;
        this.user_name = user_name;
        this.created_at = created_at;
    }
}
