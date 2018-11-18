package com.example.nguyenantin.bususingqrcode;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class AccountFragment extends Fragment {

    private EditText edit_SeriCash;
    private Button btn_addCash;
    private static EndUser user;
    private static String rollback = "false";
    private Dialog dialog;

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) throws RuntimeException {
        final View view = inflater.inflate(R.layout.fragment_account, container, false);
        // Inflate the layout for this fragment
        edit_SeriCash = (EditText) view.findViewById(R.id.edit_SeriCash);
        btn_addCash = (Button) view.findViewById(R.id.btn_addCash);
        btn_addCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new MyDialogFragment();
                dialog.show(getFragmentManager(), "MyDialogFragmentTag");
                sendPost();
                edit_SeriCash.setText("");
            }
        });
        return view;
    }
    public void sendPost() throws RuntimeException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(Port.URL_RECHARGE);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("username", SharedPrefManager.getInstance(getContext()).getUser().getUsername());
                    jsonParam.put("code", edit_SeriCash.getText().toString());

                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                    InputStream responseStream = new BufferedInputStream(conn.getInputStream());
                    BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
                    String line = "";
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((line = responseStreamReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    responseStreamReader.close();

                    String response = stringBuilder.toString();
                    JSONObject jsonResponse = new JSONObject(response);

                    JSONObject content = jsonResponse.getJSONObject("content");

                    Log.i("MSG" , jsonResponse.getString("status"));
                    if (jsonResponse.getString("status").equals("true")) {
                        user = SharedPrefManager.getInstance(getContext()).getUser();
                        user.setMoney(content.getInt("money"));
                        SharedPrefManager.getInstance(getContext()).userLogin(user);
                        HomeFragment.refresh(String.valueOf(user.getMoney()));
                    }

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}
