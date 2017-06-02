package com.example.thien.a123go.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.thien.a123go.activity.SearchFoodActivity;
import com.example.thien.a123go.app.AppConfig;
import com.example.thien.a123go.app.AppController;
import com.example.thien.a123go.models.Food;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thien on 4/18/2017.
 */

public class SearchLoaderTask {
    private static final String TAG = SearchFoodActivity.class.getSimpleName();
    private final Context mContext;
    private final SearchFoodAdapter mAdapter;

    private List<Food> mList;
    private SessionManager session;

    public SearchLoaderTask(Context context, SearchFoodAdapter adapter) {
        mContext = context;
        mAdapter = adapter;
        mList = new ArrayList<Food>();
        session = new SessionManager(mContext);
    }

    public void load_data(final String query) {
        try{
            mList.clear();
            String tag_string_req = "req_search_food";

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_SEARCH_FOOD, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        String error = jObj.getString("status");

                        if (error.compareTo("ok") == 0) {
                            JSONArray food_list = jObj.getJSONArray("food");
                            mList.clear();

                            //Get all food
                            for (int i = 0; i < food_list.length(); i++) {
                                JSONObject p = food_list.getJSONObject(i);

                                String id = p.getString("id");
                                String name = p.getString("name");
                                String description = p.getString("description");
                                int price = Integer.parseInt(p.getString("price"));
                                int discount = Integer.parseInt(p.getString("discount"));
                                String image_str = p.getString("image");

                                Bitmap image = null;
                                if(image_str != null && !image_str.isEmpty()){
                                    byte[] decodedString = Base64.decode(image_str, Base64.DEFAULT);
                                    image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                }

                                mList.add(new Food(id, name, description, price, discount, image));
                            }
                        } else {
                            // Error in login. Get the error message
                            String errorMsg = jObj.getString("msg");
                            Toast.makeText(mContext, errorMsg,
                                    Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                    }
                    onPostExecute();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String json = null;

                    NetworkResponse response = error.networkResponse;
                    if(response != null && response.data != null){
                        json = new String(response.data);
                        //Additional cases
                    }

                    Log.e(TAG, "Search food Error: " + error.getMessage());
                    Toast.makeText(mContext,
                            "Không thể kết nối đến server", Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("food_name", query);

                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        }catch (Exception e) {
            Log.e("EA_DEMO", "Error fetching food list", e);
        }
    }

    protected void onPostExecute() {
        mAdapter.clear();
        for (Food p : mList){
            mAdapter.add(p);
        }
        mAdapter.notifyDataSetChanged();
    }
}