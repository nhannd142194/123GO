package com.example.thien.a123go.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.thien.a123go.R;
import com.example.thien.a123go.fragment.AccountFragment;
import com.example.thien.a123go.fragment.FoodFragment;
import com.example.thien.a123go.fragment.LoginFragment;
import com.example.thien.a123go.fragment.NewsFragment;
import com.example.thien.a123go.fragment.RegisterFragment;
import com.example.thien.a123go.helper.SessionManager;

public class HomeActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        session = new SessionManager(getApplicationContext());
        session.setPageAdapter(mSectionsPagerAdapter);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new OnPageChange(mSectionsPagerAdapter));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuItem btn_manage_menu = menu.findItem(R.id.action_manage_menu);
        MenuItem btn_manage_counter = menu.findItem(R.id.action_manage_counter);
        if(session.isOwner()){
            btn_manage_menu.setVisible(true);
            btn_manage_counter.setVisible(true);
        }
        else{
            btn_manage_menu.setVisible(false);
            btn_manage_counter.setVisible(false);
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_manage_menu:
                startActivity(new Intent(this, ManageMenuActivity.class));
                break;
            case R.id.action_manage_counter:
                startActivity(new Intent(this, ManageCounterActivity.class));
                break;
            case R.id.action_search_food:
                startActivity(new Intent(this, SearchFoodActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private NewsFragment newsFragment;
        private FoodFragment foodFragment;
        private AccountFragment accountFragment;
        private LoginFragment loginFragment;
        private RegisterFragment registerFragment;

        private SessionManager session;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

            newsFragment = new NewsFragment();
            foodFragment = new FoodFragment();
            accountFragment = new AccountFragment();
            loginFragment = new LoginFragment();
            registerFragment = new RegisterFragment();

            // Session manager
            session = new SessionManager(getApplicationContext());
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return newsFragment;
                case 1:
                    return foodFragment;
                case 2:
                    return getAccountFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public int getItemPosition(Object object) {
            // Causes adapter to reload all Fragments when
            // notifyDataSetChanged is called
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.tab_title_1);
                case 1:
                    return getResources().getString(R.string.tab_title_2);
                case 2:
                    return getResources().getString(R.string.tab_title_3);
            }
            return null;
        }

        private Fragment getAccountFragment(){
            // Check if user is already logged in or not
            if (session.isLoggedIn()) {
                return accountFragment;
            }
            else if(session.isRegistering()){
                return registerFragment;
            }
            return loginFragment;
        }
    }

    public class OnPageChange implements ViewPager.OnPageChangeListener{
        private SectionsPagerAdapter adapter;

        public OnPageChange(SectionsPagerAdapter a){
            this.adapter = a;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position){
                case 0 :
                    ((NewsFragment)adapter.getItem(position)).update();
                    break;
                case 1:
                    ((FoodFragment)adapter.getItem(position)).update();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
