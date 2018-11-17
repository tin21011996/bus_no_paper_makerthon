package com.example.nguyenantin.bususingqrcode;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

public class ProfileFragment extends Fragment {

    private TextView txt_username;
    private TextView txt_email;
    private TextView txt_money;
    private TextView checkstudent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View profileViewFragment = inflater.inflate(R.layout.fragment_profile, container, false);

        EndUser user = SharedPrefManager.getInstance(getContext()).getUser();
        txt_username = (TextView) profileViewFragment.findViewById(R.id.txt_username);
        txt_email = (TextView) profileViewFragment.findViewById(R.id.txt_email);
        txt_money = (TextView) profileViewFragment.findViewById(R.id.txt_money);
        checkstudent = (TextView) profileViewFragment.findViewById(R.id.checkstudent);

        txt_username.setText(user.getUsername());
        txt_email.setText(user.getEmail());
        txt_money.setText(String.valueOf(user.getMoney()));
        if (user.getUsertype().equals("true")) {
            checkstudent.setText(R.string.textPrivatePeople);
        } else {
            checkstudent.setText(R.string.textPublicPeople);
        }
        return profileViewFragment;
    }
}
