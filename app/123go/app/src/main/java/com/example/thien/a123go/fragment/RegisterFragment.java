package com.example.thien.a123go.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
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

public class RegisterFragment extends Fragment {
    private static final String TAG = RegisterFragment.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private CheckBox cbIsOwner;
    private ProgressDialog pDialog;
    private SessionManager session;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        inputFullName = (EditText) rootView.findViewById(R.id.name);
        inputEmail = (EditText) rootView.findViewById(R.id.email);
        inputPassword = (EditText) rootView.findViewById(R.id.password);
        cbIsOwner = (CheckBox) rootView.findViewById(R.id.cbIsOwner);
        btnRegister = (Button) rootView.findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) rootView.findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getContext());

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                Boolean isOwner = cbIsOwner.isChecked();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    registerUser(name, email, password, isOwner);
                } else {
                    Toast.makeText(getContext(),
                            "Please enter your details!", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                session.setRegistering(false);
                session.notifyUpdatePage();
            }
        });

        return rootView;
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String name, final String email,
                              final String password, final Boolean isOwner) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SIGNUP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");
                    if (error.compareTo("ok") == 0) {
                        String name = jObj.getString("name");
                        String email = jObj.getString("email");
                        String id = jObj.getString("id");

                        session.setString(SessionManager.KEY_USER_ID, id);

                        if(isOwner){
                            startActivity(new Intent(getContext(), ManageCounterActivity.class));
                            Toast.makeText(getContext(), "Đăng ký thành công! Xin hãy nhập thông tin cửa hàng.", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getContext(), "Đăng ký thành công!", Toast.LENGTH_LONG).show();
                        }

                        session.setRegistering(false);
                        session.notifyUpdatePage();
                    } else {
                        String errorMsg = jObj.getString("msg");
                        Toast.makeText(getContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        "Tài khoản đã tồn tại", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("user_type", isOwner.toString());

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