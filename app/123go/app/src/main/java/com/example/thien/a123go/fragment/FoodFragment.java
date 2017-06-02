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

import com.example.thien.a123go.activity.CounterDetailActivity;
import com.example.thien.a123go.activity.FoodDetailActivity;
import com.example.thien.a123go.R;
import com.example.thien.a123go.helper.FoodAdapter;
import com.example.thien.a123go.helper.FoodLoaderTask;
import com.example.thien.a123go.helper.SessionManager;
import com.example.thien.a123go.models.LatestInfo;

/**
 * Created by thien on 4/13/2017.
 */

public class FoodFragment extends Fragment {
    private FoodLoaderTask t;
    private SessionManager session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food, container, false);

        FrameLayout footerLayout = (FrameLayout) inflater.inflate(R.layout.food_footer,null);
        TextView footer = (TextView) footerLayout.findViewById(R.id.footer);

        session = new SessionManager(getContext());

        ListView lv = (ListView) rootView.findViewById(R.id.list_view);
        lv.addFooterView(footerLayout);

        FoodAdapter ad = new FoodAdapter(this.getContext(),footer);
        lv.setAdapter(ad);

        t = new FoodLoaderTask(this.getContext(),ad);
        t.load_data();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LatestInfo item = (LatestInfo)parent.getAdapter().getItem(position);
                if (item.type == 0){
                    session.setString(SessionManager.KEY_CURRENT_FOOD, item.id);
                    startActivity(new Intent(getContext(), FoodDetailActivity.class));
                }

                if (item.type == 1){
                    session.setString(SessionManager.KEY_CURRENT_COUNTER, item.id);
                    startActivity(new Intent(getContext(), CounterDetailActivity.class));
                }
            }
        });

        return rootView;
    }

    public void update(){
        t.load_data();
    }
}