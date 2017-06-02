package com.example.thien.a123go.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.thien.a123go.R;
import com.example.thien.a123go.helper.MenuAdapter;
import com.example.thien.a123go.helper.MenuLoaderTask;
import com.example.thien.a123go.helper.SessionManager;
import com.example.thien.a123go.models.Food;

public class ManageMenuActivity extends AppCompatActivity {
    private SessionManager session;
    private MenuLoaderTask t;
    private TextView footer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(getApplicationContext());

        FrameLayout footerLayout = (FrameLayout) getLayoutInflater().inflate(R.layout.food_footer,null);
        footer = (TextView) footerLayout.findViewById(R.id.footer);

        ListView lv = (ListView) findViewById(R.id.list_view);
        lv.addFooterView(footerLayout);

        MenuAdapter ad = new MenuAdapter(this.getBaseContext(), footer);
        lv.setAdapter(ad);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Food item = (Food)parent.getAdapter().getItem(position);
                session.setString(SessionManager.KEY_CURRENT_FOOD, item.id);
                startActivity(new Intent(getBaseContext(), FoodDetailActivity.class));
            }
        });

        footer.setText(getResources().getString(R.string.loading));
        t = new MenuLoaderTask(this.getBaseContext(),ad);
        t.load_data();
        footer.setText(getResources().getString(R.string.no_more_item));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_add_item:
                startActivity(new Intent(this, AddFoodActivity.class));
                break;
            case R.id.action_refresh:
                footer.setText(getResources().getString(R.string.loading));
                t.load_data();
                footer.setText(getResources().getString(R.string.no_more_item));
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
