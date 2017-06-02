package com.example.thien.a123go.activity;

import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.thien.a123go.R;
import com.example.thien.a123go.app.AppConfig;
import com.example.thien.a123go.app.AppController;
import com.example.thien.a123go.helper.CommentAdapter;
import com.example.thien.a123go.helper.SessionManager;
import com.example.thien.a123go.models.Comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodDetailActivity extends AppCompatActivity {
    private static final String TAG = FoodDetailActivity.class.getSimpleName();
    private TextView foodName;
    private TextView foodPrice;
    private TextView foodDiscount;
    private TextView foodDescription;
    private TextView foodRatingCount;
    private ImageView foodImage;
    private RatingBar foodRating;

    private SessionManager session;
    private ProgressDialog pDialog;
    private Dialog ratingDialog;
    private Dialog commentDialog;
    private CommentAdapter ad;

    private RatingBar ratingBar;
    private List<Comment> commentList;
    private EditText tvComment;

    private AppCompatActivity state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        foodName = (TextView) findViewById(R.id.foodName);
        foodPrice = (TextView) findViewById(R.id.foodPrice);
        foodDiscount = (TextView) findViewById(R.id.foodDiscount);
        foodDescription = (TextView) findViewById(R.id.foodDescription);
        foodRatingCount = (TextView) findViewById(R.id.foodRatingCount);
        foodImage = (ImageView) findViewById(R.id.foodImage);
        foodRating = (RatingBar) findViewById(R.id.foodRating);
        Button btnBack = (Button) findViewById(R.id.btnBack);
        Button btnRating = (Button) findViewById(R.id.btnRating);
        Button btnComment = (Button) findViewById(R.id.btnComment);
        ListView lv = (ListView) findViewById(R.id.list_view);
        ad = new CommentAdapter(this.getBaseContext());
        if (lv != null) {
            lv.setAdapter(ad);
        }
        commentList = new ArrayList<Comment>();

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getBaseContext());

        getDetail(session.getString(SessionManager.KEY_CURRENT_FOOD));

        state = this;
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    state.finish();
                }
            });
        }

        if(btnRating != null){
            btnRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ratingDialog = new Dialog(FoodDetailActivity.this);
                    ratingDialog.setContentView(R.layout.rating_dialog);
                    ratingDialog.setCancelable(true);
                    ratingBar = (RatingBar) ratingDialog.findViewById(R.id.dialog_ratingbar);
                    ratingBar.setRating(0);

                    TextView text = (TextView) ratingDialog.findViewById(R.id.rank_dialog_text1);
                    text.setText(foodName.getText());

                    Button updateButton = (Button) ratingDialog.findViewById(R.id.rank_dialog_button);
                    updateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            submitRating(Math.round(ratingBar.getRating()));
                            ratingDialog.dismiss();
                            getDetail(session.getString(SessionManager.KEY_CURRENT_FOOD));
                        }
                    });
                    ratingDialog.show();
                }
            });
        }

        if(btnComment != null){
            btnComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentDialog = new Dialog(FoodDetailActivity.this);
                    commentDialog.setContentView(R.layout.comment_dialog);
                    commentDialog.setCancelable(true);
                    tvComment = (EditText) commentDialog.findViewById(R.id.tv_comment);

                    Button updateButton = (Button) commentDialog.findViewById(R.id.comment_dialog_button);
                    updateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            submitComment(tvComment.getText().toString());
                            commentDialog.dismiss();
                            getDetail(session.getString(SessionManager.KEY_CURRENT_FOOD));

                        }
                    });
                    commentDialog.show();
                }
            });
        }
    }

    /**
     * function to verify login details in mysql db
     * */
    private void getDetail(final String id) {
        // Tag used to cancel the request
        String tag_string_req = "req_get_food_detail";

        pDialog.setMessage("Đang tải...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_FOOD_DETAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");

                    // Check for error node in json
                    if (error.compareTo("ok") == 0) {
                        // Get food
                        JSONObject food = jObj.getJSONObject("food");

                        String name = food.getString("name");
                        String price = "Giá: " + food.getString("price");
                        String discount = "Khuyến mãi: " + food.getString("discount");
                        String desciption = food.getString("description");
                        String rating_score = food.getString("rating_score");
                        String rating_count = "Số lượt đánh giá: " + food.getString("rating_count");

                        String image_str = food.getString("image");

                        Bitmap image = null;
                        if(image_str != null && !image_str.isEmpty()){
                            byte[] decodedString = Base64.decode(image_str, Base64.DEFAULT);
                            image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        }

                        foodName.setText(name);
                        foodPrice.setText(price);
                        foodDiscount.setText(discount);
                        foodDescription.setText(desciption);
                        foodRatingCount.setText(rating_count);
                        foodRating.setRating(Float.parseFloat(rating_score));

                        if(null != image){
                            foodImage.setImageBitmap(image);
                        }

                        //Get comment
                        commentList.clear();
                        JSONArray comments = jObj.getJSONArray("comments");

                        for (int i = 0; i < comments.length(); i++) {
                            JSONObject p = comments.getJSONObject(i);

                            String id = p.getString("id");
                            String comment = p.getString("comment");
                            String created_at = p.getString("created_at");
                            String user_name = p.getString("user_name");

                            commentList.add(new Comment(id, comment, user_name, created_at));
                            updateList();
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
                String json = null;

                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){
                    json = new String(response.data);
                    //Additional cases
                }

                Toast.makeText(getBaseContext(),
                        "Không thể kết nối đến server", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("food_id", id);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void submitRating(final int score) {
        // Tag used to cancel the request
        String tag_string_req = "req_food_rating";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_FOOD_RATING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");

                    // Check for error node in json
                    if (error.compareTo("ok") == 0) {
                        Toast.makeText(getBaseContext(), "Đã gửi", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getBaseContext(),
                        "Không thể kết nối đến server", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("food_id", session.getString(SessionManager.KEY_CURRENT_FOOD));
                params.put("user_id", session.getString(SessionManager.KEY_USER_ID));
                params.put("score", Integer.toString(score));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void submitComment(final String comment) {
        // Tag used to cancel the request
        String tag_string_req = "req_food_comment";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_FOOD_COMMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");

                    // Check for error node in json
                    if (error.compareTo("ok") == 0) {
                        Toast.makeText(getBaseContext(), "Đã gửi", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getBaseContext(),
                        "Không thể kết nối đến server", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("food_id", session.getString(SessionManager.KEY_CURRENT_FOOD));
                params.put("user_id", session.getString(SessionManager.KEY_USER_ID));
                params.put("comment", comment);

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

    protected void updateList() {
        ad.clear();
        for (Comment p : commentList){
            ad.add(p);
        }
        ad.notifyDataSetChanged();
    }

}
