package com.example.thien.a123go.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.thien.a123go.R;
import com.example.thien.a123go.app.AppConfig;
import com.example.thien.a123go.app.AppController;
import com.example.thien.a123go.helper.SessionManager;
import com.example.thien.a123go.models.Counter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ManageCounterActivity extends AppCompatActivity {
    private static final String TAG = ManageCounterActivity.class.getSimpleName();
    private int PICK_IMAGE_REQUEST = 1;
    private Button btnSubmit;
    private EditText inputName;
    private EditText inputAddress;
    private EditText inputPhone;
    private EditText inputDescription;
    private Button btnChooseImage;
    private ImageView imageView;
    private ProgressDialog pDialog;
    private SessionManager session;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_counter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Session manager
        session = new SessionManager(getBaseContext());

        if(session.isLoggedIn()){
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        inputName = (EditText) findViewById(R.id.counterName);
        inputAddress = (EditText) findViewById(R.id.counterAddress);
        inputPhone = (EditText) findViewById(R.id.counterPhone);
        inputDescription = (EditText) findViewById(R.id.counterDescription);
        btnChooseImage = (Button) findViewById(R.id.btnChooseImage);
        btnSubmit = (Button) findViewById(R.id.btnSubmitCounter);
        imageView = (ImageView) findViewById(R.id.counterImageView);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        if (session.isLoggedIn()){
            Counter currentCounter = session.getCounter();
            inputName.setText(currentCounter.name);
            inputAddress.setText(currentCounter.address);
            inputPhone.setText(currentCounter.phone);
            inputDescription.setText(currentCounter.description);
        }

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputName.getText().toString().trim();
                String address = inputAddress.getText().toString().trim();
                String phone = inputPhone.getText().toString().trim();
                String description = inputDescription.getText().toString().trim();

                if (!name.isEmpty() && !address.isEmpty() && !phone.isEmpty() && !description.isEmpty()) {
                    submitCounter(name, address, phone, description);
                } else {
                    Toast.makeText(getBaseContext(),
                            "Please enter your details!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void submitCounter(final String name, final String address, final String phone, final String description) {
        // Tag used to cancel the request
        String tag_string_req = "req_submit_counter";

        pDialog.setMessage(getResources().getString(R.string.uploading));
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SUBMIT_COUNTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Add food Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");
                    if (error.compareTo("ok") == 0) {
                        startActivity(new Intent(getBaseContext(), HomeActivity.class));
                    } else {
                        String errorMsg = jObj.getString("msg");
                        Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Upload Error: " + error.getMessage());

                String json = null;
                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){
                    json = new String(response.data);
                    //Additional cases
                }

                Toast.makeText(getBaseContext(),
                        "Không thể kết nối đến server", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("address", address);
                params.put("phone", phone);
                params.put("description", description);
                params.put("owner_id", session.getString(SessionManager.KEY_USER_ID));

                if(null != bitmap){
                    params.put("image", getStringImage(bitmap));
                }

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
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
