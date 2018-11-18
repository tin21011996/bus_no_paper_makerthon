package com.example.nguyenantin.bususingqrcode;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class ProfileFragment extends Fragment {

    private TextView txt_username;
    private TextView txt_email;
    private TextView txt_money;
    private TextView checkstudent;
    private Button btn_Logout;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View profileViewFragment = inflater.inflate(R.layout.fragment_profile, container, false);

        EndUser user = SharedPrefManager.getInstance(getContext()).getUser();
        txt_username = (TextView) profileViewFragment.findViewById(R.id.txt_username);
        txt_email = (TextView) profileViewFragment.findViewById(R.id.txt_email);
        txt_money = (TextView) profileViewFragment.findViewById(R.id.txt_money);
        checkstudent = (TextView) profileViewFragment.findViewById(R.id.checkstudent);
        btn_Logout = (Button) profileViewFragment.findViewById(R.id.btn_Logout);

        txt_username.setText(user.getUsername());
        txt_email.setText(user.getEmail());
        DecimalFormat df = new DecimalFormat("###,###,###");
        btn_Logout.setEnabled(true);

        String formatted = df.format(SharedPrefManager.getInstance(getContext()).getUser().getMoney());
        txt_money.setText(formatted + " VND");

        if (user.getUsertype().equals("1")) {
            checkstudent.setText(R.string.textPrivatePeople);
        } else {
            checkstudent.setText(R.string.textPublicPeople);
        }

        btn_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Logout.setEnabled(false);
                DialogFragment dialogLogout = new AlertDialogLogout();
                dialogLogout.show(getFragmentManager(), "AlertDialogLogout");
            }
        });

        return profileViewFragment;
    }
}
