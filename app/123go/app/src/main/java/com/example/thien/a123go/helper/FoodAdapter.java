package com.example.thien.a123go.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thien.a123go.R;
import com.example.thien.a123go.activity.ManageMenuActivity;
import com.example.thien.a123go.models.LatestInfo;

/**
 * Created by thien on 4/18/2017.
 */

public class FoodAdapter extends ArrayAdapter<LatestInfo> {
    private static final String TAG = ManageMenuActivity.class.getSimpleName();
    private final TextView mFooter;

    public FoodAdapter(Context context, TextView footer) {
        super(context, android.R.layout.two_line_list_item);
        mFooter = footer;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LatestInfo p = getItem(position);
        TextView t1;
        TextView t2;
        TextView t3;
        ImageView i1;
        if(p.type == 2){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_header,parent,false);
            t1 = (TextView) convertView.findViewById(R.id.textSeparator);
            convertView.setTag(new Holder(t1, null, null, null));
        }
        else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.food_item,parent,false);
            t1 = (TextView) convertView.findViewById(R.id.text1);
            t2 = (TextView) convertView.findViewById(R.id.text2);
            t3 = (TextView) convertView.findViewById(R.id.text3);
            i1 = (ImageView) convertView.findViewById(R.id.foodImage);
            convertView.setTag(new Holder(t1,t2,t3,i1));
        }

        Holder h = (Holder) convertView.getTag();
        if(p.type == 2){
            h.t1.setText(p.title);
        }
        else {
            h.t1.setText(p.title);
            h.t2.setText(p.created_at);
            h.t3.setText(p.content);
            if (null != p.image) {
                h.i1.setImageBitmap(p.image);
            }

        }
        return convertView;
    }

    private static class Holder{
        public final TextView t1;
        public final TextView t2;
        public final TextView t3;
        public final ImageView i1;

        private Holder(TextView t1, TextView t2, TextView t3, ImageView i1) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.i1 = i1;
        }
    }
}
