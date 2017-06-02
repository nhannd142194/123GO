package com.example.thien.a123go.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thien.a123go.R;
import com.example.thien.a123go.fragment.NewsFragment;
import com.example.thien.a123go.models.News;

/**
 * Created by thien on 4/18/2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {
    private static final String TAG = NewsFragment.class.getSimpleName();
    private boolean mHasMoreItems;
    private final int mPageSize;
    private final TextView mFooter;

    public NewsAdapter(Context context, int pageSize, TextView footer) {
        super(context, android.R.layout.two_line_list_item);
        mPageSize = pageSize;
        mFooter = footer;
        mHasMoreItems = true;
    }

    public void setmHasMoreItems(boolean b){
        mHasMoreItems = b;
        if(!b){
            mFooter.setText(getContext().getResources().getString(R.string.no_more_item));
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(position == getCount() - 1 && mHasMoreItems){
            NewsLoaderTask t = new NewsLoaderTask(position + 1, position + 1 + mPageSize, getContext(),this);
            t.load_data();
            mFooter.setText(getContext().getResources().getString(R.string.uploading));
        }


        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_item,parent,false);

            TextView t1 = (TextView) convertView.findViewById(R.id.text1);
            TextView t2 = (TextView) convertView.findViewById(R.id.text2);
            TextView t3 = (TextView) convertView.findViewById(R.id.text3);
            ImageView i1 = (ImageView) convertView.findViewById(R.id.newsImage);

            convertView.setTag(new Holder(t1,t2,t3, i1));

        }


        News p = getItem(position);
        Holder h = (Holder) convertView.getTag();

        h.t1.setText(p.title);
        h.t2.setText(p.created_at);
        h.t3.setText(p.content);

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
