package com.example.thien.a123go.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.thien.a123go.R;
import com.example.thien.a123go.activity.NewsDetailActivity;
import com.example.thien.a123go.helper.NewsAdapter;
import com.example.thien.a123go.helper.NewsLoaderTask;
import com.example.thien.a123go.helper.SessionManager;
import com.example.thien.a123go.models.News;

/**
 * Created by thien on 4/13/2017.
 */

public class NewsFragment extends Fragment{
    private NewsLoaderTask t;
    private SessionManager session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);

        session = new SessionManager(getContext());

        FrameLayout footerLayout = (FrameLayout) inflater.inflate(R.layout.news_footer,null);
        TextView footer = (TextView) footerLayout.findViewById(R.id.footer);

        ListView lv = (ListView) rootView.findViewById(R.id.list_view);
        lv.addFooterView(footerLayout);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News item = (News)parent.getAdapter().getItem(position);
                session.setString(SessionManager.KEY_CURRENT_NEWS, item.id);
                startActivity(new Intent(getContext(), NewsDetailActivity.class));
            }
        });

        //--page size = 10--
        NewsAdapter ad = new NewsAdapter(this.getContext(),10, footer);
        lv.setAdapter(ad);

        //--load first 10 items--
        t = new NewsLoaderTask(0,10,this.getContext(),ad);
        t.load_data();

        return rootView;
    }

    public void update(){
        t.load_data();
    }
}