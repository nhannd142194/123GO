package com.example.thien.a123go.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.thien.a123go.R;
import com.example.thien.a123go.app.AppConfig;
import com.example.thien.a123go.app.AppController;
import com.example.thien.a123go.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewsDetailActivity extends AppCompatActivity {
    private static final String TAG = NewsDetailActivity.class.getSimpleName();
    private TextView newsTitle;
    private TextView newsCreatedAt;
    private TextView newsDescription;
    private TextView newsBody;
    private ImageView newsImage;

    private SessionManager session;
    private ProgressDialog pDialog;

    private AppCompatActivity state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        newsTitle = (TextView) findViewById(R.id.newsTitle);
        newsCreatedAt = (TextView) findViewById(R.id.newsCreatedAt);
        newsDescription = (TextView) findViewById(R.id.newsDescription);
        newsBody = (TextView) findViewById(R.id.newsBody);
        newsImage = (ImageView) findViewById(R.id.newsImage);
        Button btnBack = (Button) findViewById(R.id.btnNewsBack);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getBaseContext());

        getDetail(session.getString(SessionManager.KEY_CURRENT_NEWS));

        state = this;
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state.finish();
            }
        });
    }

    /**
     * function to verify login details in mysql db
     * */
    private void getDetail(final String id) {
        // Tag used to cancel the request
        String tag_string_req = "req_get_news_detail";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_NEWS_DETAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");

                    // Check for error node in json
                    if (error.compareTo("ok") == 0) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);
                        JSONObject food = jObj.getJSONObject("news");

                        String title = food.getString("title");
                        String created_at = food.getString("created_at");
                        String description = food.getString("description");
                        String body = food.getString("body");

                        String image_str = food.getString("image");

                        Bitmap image = null;
                        if(image_str != null && !image_str.isEmpty()){
                            byte[] decodedString = Base64.decode(image_str, Base64.DEFAULT);
                            image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        }

                        newsTitle.setText(title);
                        newsCreatedAt.setText(created_at);
                        newsDescription.setText(description);
                        newsBody.setText(body);

                        if(null != image){
                            newsImage.setImageBitmap(image);
                        }
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("msg");
                        Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getBaseContext(),
                        "Không thể kết nối đến server", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("news_id", id);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
