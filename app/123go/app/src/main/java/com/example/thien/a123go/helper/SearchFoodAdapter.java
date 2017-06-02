package com.example.thien.a123go.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thien.a123go.R;
import com.example.thien.a123go.activity.SearchFoodActivity;
import com.example.thien.a123go.models.Food;

/**
 * Created by thien on 4/18/2017.
 */

public class SearchFoodAdapter extends ArrayAdapter<Food> {
    private static final String TAG = SearchFoodActivity.class.getSimpleName();
    private final TextView mFooter;

    public SearchFoodAdapter(Context context, TextView footer) {
        super(context, android.R.layout.two_line_list_item);
        mFooter = footer;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Food p = getItem(position);
        TextView t1;
        TextView t2;
        TextView t3;
        ImageView i1;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_item,parent,false);
            t1 = (TextView) convertView.findViewById(R.id.text1);
            t2 = (TextView) convertView.findViewById(R.id.text2);
            t3 = (TextView) convertView.findViewById(R.id.text3);
            i1 = (ImageView) convertView.findViewById(R.id.menuItemImage);
            convertView.setTag(new Holder(t1,t2,t3, i1));
        }

        Holder h = (Holder) convertView.getTag();

        h.t1.setText(p.name);
        h.t2.setText("Giá: " + String.valueOf(p.price));
        h.t3.setText(p.description);

        if (null != p.image) {
            h.i1.setImageBitmap(p.image);
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
