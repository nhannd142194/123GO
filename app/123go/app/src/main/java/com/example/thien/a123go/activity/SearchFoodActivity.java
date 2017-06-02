package com.example.thien.a123go.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.thien.a123go.R;
import com.example.thien.a123go.helper.SearchFoodAdapter;
import com.example.thien.a123go.helper.SearchLoaderTask;
import com.example.thien.a123go.helper.SessionManager;

public class SearchFoodActivity extends AppCompatActivity {
    private SessionManager session;
    private SearchLoaderTask t;
    private TextView footer;
    private SearchFoodAdapter ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(getApplicationContext());

        FrameLayout footerLayout = (FrameLayout) getLayoutInflater().inflate(R.layout.food_footer,null);
        footer = (TextView) footerLayout.findViewById(R.id.footer);

        ListView lv = (ListView) findViewById(R.id.search_list_view);
        lv.addFooterView(footerLayout);

        ad = new SearchFoodAdapter(this.getBaseContext(), footer);
        lv.setAdapter(ad);

        t = new SearchLoaderTask(this.getBaseContext(),ad);
        footer.setText(getResources().getString(R.string.no_more_item));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.hint_food_name));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void search(String query){
        footer.setText(getResources().getString(R.string.searching));
        t.load_data(query);
        footer.setText(getResources().getString(R.string.no_more_item));
        InputMethodManager inputManager =
                (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
