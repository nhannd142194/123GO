package com.example.thien.a123go.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.thien.a123go.R;
import com.example.thien.a123go.helper.SessionManager;

/**
 * Created by thien on 4/13/2017.
 */

public class AccountFragment extends Fragment {
    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;

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
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        txtName = (TextView) rootView.findViewById(R.id.name);
        txtEmail = (TextView) rootView.findViewById(R.id.email);
        btnLogout = (Button) rootView.findViewById(R.id.btnLogout);

        // session manager
        session = new SessionManager(getContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        txtName.setText(session.getString(SessionManager.KEY_USER_NAME));
        txtEmail.setText(session.getString(SessionManager.KEY_USER_EMAIL));

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        return rootView;
    }

    private void logoutUser(){
        session.setLogin(false);
        session.clearSession();

        activity.invalidateOptionsMenu();

        FragmentStatePagerAdapter adapter = session.getPageAdapter();
        adapter.notifyDataSetChanged();
    }
}