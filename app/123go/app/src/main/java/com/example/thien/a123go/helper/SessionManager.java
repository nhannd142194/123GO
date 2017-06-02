package com.example.thien.a123go.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.thien.a123go.models.Counter;

/**
 * Created by thien on 4/10/2017.
 */

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    public static final String PREF_NAME = "AndroidHiveLogin";

    public static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public static final String KEY_IS_REGISTERING = "isRegistering";

    public static final String KEY_USER_ID = "userID";

    public static final String KEY_USER_NAME = "userName";

    public static final String KEY_USER_EMAIL = "userEmail";

    public static final String KEY_USER_TOKEN = "userToken";

    public static final String KEY_COUNTER_ID = "counterID";
    public static final String KEY_COUNTER_NAME = "counterName";
    public static final String KEY_COUNTER_ADDRESS = "counterAddress";
    public static final String KEY_COUNTER_PHONE = "counterPhone";
    public static final String KEY_COUNTER_DESCRIPTION = "counterDescription";

    public static final String KEY_IS_OWNER = "isOwner";

    public static final String KEY_CURRENT_FOOD = "currentFood";
    public static final String KEY_CURRENT_NEWS = "currentNews";
    public static final String KEY_CURRENT_COUNTER = "currentCounter";

    public static FragmentStatePagerAdapter adapter;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setString(String key, String data){
        editor.putString(key, data);
        editor.commit();
        Log.d(TAG, "Session modified!");
    }

    public void setBoolean(String key, Boolean data){
        editor.putBoolean(key, data);
        editor.commit();
        Log.d(TAG, "Session modified!");
    }

    public String getString(String key){
        return pref.getString(key, "");
    }

    public void clearSession(){
        editor.clear();
        editor.commit();
    }

    public void setPageAdapter(FragmentStatePagerAdapter a){
        adapter = a;
    }

    public FragmentStatePagerAdapter getPageAdapter(){
        return adapter;
    }

    public void notifyUpdatePage(){
        adapter.notifyDataSetChanged();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }
    public boolean isOwner(){
        return pref.getBoolean(KEY_IS_OWNER, false);
    }

    public void setRegistering(boolean isRegistering) {
        if(isLoggedIn()){
            isRegistering = false;
        }
        editor.putBoolean(KEY_IS_REGISTERING, isRegistering);

        // commit changes
        editor.commit();

        Log.d(TAG, "User register session modified!");
    }

    public boolean isRegistering(){
        return pref.getBoolean(KEY_IS_REGISTERING, false);
    }

    public void setCounter(String id, String name, String address, String phone, String description){
        setString(KEY_COUNTER_ID, id);
        setString(KEY_COUNTER_NAME, name);
        setString(KEY_COUNTER_ADDRESS, address);
        setString(KEY_COUNTER_PHONE, phone);
        setString(KEY_COUNTER_DESCRIPTION, description);
    }

    public Counter getCounter(){
        String name = getString(KEY_COUNTER_NAME);
        String address = getString(KEY_COUNTER_ADDRESS);
        String phone = getString(KEY_COUNTER_PHONE);
        String description = getString(KEY_COUNTER_DESCRIPTION);
        return new Counter(name, address, phone, description, null);
    }
}
