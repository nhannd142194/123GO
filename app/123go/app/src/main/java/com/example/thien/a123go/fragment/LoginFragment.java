package com.example.thien.a123go.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.thien.a123go.R;
import com.example.thien.a123go.activity.ManageCounterActivity;
import com.example.thien.a123go.app.AppConfig;
import com.example.thien.a123go.app.AppController;
import com.example.thien.a123go.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thien on 4/13/2017.
 */

public class LoginFragment extends Fragment {
    private static final String TAG = LoginFragment.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private CheckBox cbIsOwner;
    private ProgressDialog pDialog;
    private SessionManager session;

    private AppCompatActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (AppCompatActivity) context;
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        inputEmail = (EditText) rootView.findViewById(R.id.email);
        inputPassword = (EditText) rootView.findViewById(R.id.password);
        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) rootView.findViewById(R.id.btnLinkToRegisterScreen);
        cbIsOwner = (CheckBox) rootView.findViewById(R.id.cbIsOwner);

        // Progress dialog
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            session.notifyUpdatePage();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                Boolean isOwner = cbIsOwner.isChecked();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password, isOwner);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                session.setRegistering(true);
                session.notifyUpdatePage();
            }
        });

        return rootView;
    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email, final String password, final Boolean isOwner) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {
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

                        String token = jObj.getString("token");
                        String name = jObj.getString("name");
                        String email = jObj.getString("email");
                        String id = jObj.getString("id");
                        if(isOwner){
                            JSONObject counter = jObj.getJSONObject("counter");
                            String counter_id = counter.getString("id");
                            String counter_name = counter.getString("name");
                            String counter_address = counter.getString("address");
                            String counter_phone = counter.getString("phone");
                            String counter_description = counter.getString("description");
                            session.setCounter(counter_id, counter_name, counter_address, counter_phone, counter_description);
                        }

                        session.setString(SessionManager.KEY_USER_ID, id);
                        session.setString(SessionManager.KEY_USER_NAME, name);
                        session.setString(SessionManager.KEY_USER_EMAIL, email);
                        session.setString(SessionManager.KEY_USER_TOKEN, token);
                        session.setBoolean(SessionManager.KEY_IS_OWNER, isOwner);
                        session.notifyUpdatePage();

                        activity.invalidateOptionsMenu();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("msg");

                        if(errorMsg.equals("No counter founded")){
                            session.setString(SessionManager.KEY_USER_ID, jObj.getString("user_id"));
                            startActivity(new Intent(getContext(), ManageCounterActivity.class));
                            Toast.makeText(getContext(), "Xin hãy nhập thông tin cửa hàng", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        "Không thể kết nối đến server", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("is_owner", isOwner.toString());

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