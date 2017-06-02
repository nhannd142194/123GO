package com.example.thien.a123go.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.thien.a123go.R;
import com.example.thien.a123go.activity.FoodDetailActivity;
import com.example.thien.a123go.models.Comment;

/**
 * Created by thien on 5/20/2017.
 */

public class CommentAdapter extends ArrayAdapter<Comment> {
    private static final String TAG = FoodDetailActivity.class.getSimpleName();

    public CommentAdapter(Context context) {
        super(context, android.R.layout.two_line_list_item);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Comment p = getItem(position);
        TextView t1;
        TextView t2;
        TextView t3;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comment_item,parent,false);
            t1 = (TextView) convertView.findViewById(R.id.text1);
            t2 = (TextView) convertView.findViewById(R.id.text2);
            t3 = (TextView) convertView.findViewById(R.id.text3);
            convertView.setTag(new Holder(t1,t2,t3));
        }

        Holder h = (Holder) convertView.getTag();
        if (p != null) {
            h.t1.setText(p.user_name);
            h.t2.setText(p.created_at);
            h.t3.setText(p.comment);
        }
        return convertView;
    }

    private static class Holder{
        private final TextView t1;
        private final TextView t2;
        private final TextView t3;

        private Holder(TextView t1, TextView t2, TextView t3) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
        }
    }
}
