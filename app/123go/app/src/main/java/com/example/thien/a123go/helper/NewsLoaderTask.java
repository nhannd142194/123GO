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
import com.example.thien.a123go.app.AppConfig;
import com.example.thien.a123go.app.AppController;
import com.example.thien.a123go.fragment.NewsFragment;
import com.example.thien.a123go.models.News;

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

public class NewsLoaderTask {
    private static final String TAG = NewsFragment.class.getSimpleName();
    private final int mFrom;
    private final int mTo;
    private final Context mContext;
    private final NewsAdapter mAdapter;

    private List<News> mList;
    private boolean mReachedLastPage;

    public NewsLoaderTask(int from, int to, Context context, NewsAdapter adapter) {
        mFrom = from;
        mTo = to;
        mContext = context;
        mAdapter = adapter;
        mReachedLastPage = false;
        mList = new ArrayList<News>();
    }

    public void load_data() {
        try{
            mList.clear();
            String tag_string_req = "req_news";

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_NEWS, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        String error = jObj.getString("status");
                        mList.clear();

                        if (error.compareTo("ok") == 0) {
                            JSONArray news = jObj.getJSONArray("news");

                            for (int i = 0; i < news.length(); i++) {
                                JSONObject p = news.getJSONObject(i);

                                String id = p.getString("id");
                                String title = p.getString("title");
                                String created_at = p.getString("created_at");
                                String content = p.getString("description");
                                String image_str = p.getString("image");

                                Bitmap image = null;
                                if(image_str != null && !image_str.isEmpty()){
                                    byte[] decodedString = Base64.decode(image_str, Base64.DEFAULT);
                                    image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                }

                                mList.add(new News(id, title, created_at, content, image));
                            }
                            mReachedLastPage = jObj.getBoolean("last_page");
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
            }){
                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("limit", Integer.toString(mTo));

                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        }catch (Exception e) {
            Log.e("EA_DEMO", "Error fetching news list", e);
        }
    }

    protected void onPostExecute() {
        mAdapter.clear();
        for (News p : mList){
            mAdapter.add(p);
        }
        mAdapter.notifyDataSetChanged();
        mAdapter.setmHasMoreItems(!mReachedLastPage);
    }
}