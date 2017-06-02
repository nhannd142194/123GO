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
import com.example.thien.a123go.R;
import com.example.thien.a123go.app.AppConfig;
import com.example.thien.a123go.app.AppController;
import com.example.thien.a123go.fragment.FoodFragment;
import com.example.thien.a123go.models.LatestInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thien on 4/18/2017.
 */

public class FoodLoaderTask {
    private static final String TAG = FoodFragment.class.getSimpleName();
    private final Context mContext;
    private final FoodAdapter mAdapter;

    private List<LatestInfo> mList;

    public FoodLoaderTask(Context context, FoodAdapter adapter) {
        mContext = context;
        mAdapter = adapter;
        mList = new ArrayList<LatestInfo>();
    }

    public void load_data() {
        try{
            mList.clear();
            String tag_string_req = "req_news";

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_LATEST_FOOD, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        String error = jObj.getString("status");

                        mList.clear();

                        if (error.compareTo("ok") == 0) {
                            JSONArray food_list = jObj.getJSONArray("food");
                            JSONArray counter_list = jObj.getJSONArray("counter");

                            //Add header
                            mList.add(new LatestInfo("0", mContext.getResources().getString(R.string.latest_food), "", "", 2, null));

                            //Get all food
                            for (int i = 0; i < food_list.length(); i++) {
                                JSONObject p = food_list.getJSONObject(i);

                                String id = p.getString("id");
                                String title = p.getString("name");
                                String created_at = p.getString("created_at");
                                String content = p.getString("description");
                                String image_str = p.getString("image");

                                Bitmap image = null;
                                if(image_str != null && !image_str.isEmpty()){
                                    byte[] decodedString = Base64.decode(image_str, Base64.DEFAULT);
                                    image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                }

                                mList.add(new LatestInfo(id, title, created_at, content, 0, image));
                            }

                            //Add header
                            mList.add(new LatestInfo("0", mContext.getResources().getString(R.string.latest_counter), "", "", 2, null));

                            //Get all counter
                            for (int i = 0; i < counter_list.length(); i++) {
                                JSONObject p = counter_list.getJSONObject(i);

                                String id = p.getString("id");
                                String title = p.getString("name");
                                String created_at = p.getString("created_at");
                                String content = p.getString("description");
                                String image_str = p.getString("image");

                                Bitmap image = null;
                                if(image_str != null && !image_str.isEmpty()){
                                    byte[] decodedString = Base64.decode(image_str, Base64.DEFAULT);
                                    image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                }

                                mList.add(new LatestInfo(id, title, created_at, content, 1, image));
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

                    Log.e(TAG, "Get news Error: " + error.getMessage());
                    Toast.makeText(mContext,
                            "Không thể kết nối đến server", Toast.LENGTH_LONG).show();
                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        }catch (Exception e) {
            Log.e("EA_DEMO", "Error fetching food list", e);
        }
    }

    protected void onPostExecute() {
        mAdapter.clear();
        for (LatestInfo p : mList){
            mAdapter.add(p);
        }
        mAdapter.notifyDataSetChanged();
    }
}