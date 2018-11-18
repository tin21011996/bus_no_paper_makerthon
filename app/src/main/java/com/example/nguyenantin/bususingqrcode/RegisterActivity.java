package com.example.nguyenantin.bususingqrcode;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class RegisterActivity extends AppCompatActivity {

    private Button btn_Register;
    private String flag_Student = "0";
    private EditText edit_Email, edit_Username, edit_Password;
    private CheckBox checkStudent;
    @Override
    protected void onCreate(Bundle savedInstanceState) throws RuntimeException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
//        getWindow().setAttributes(lp);

        edit_Username = (EditText) findViewById(R.id.edit_Username);
        edit_Password = (EditText) findViewById(R.id.edit_Password);
        edit_Email = (EditText) findViewById(R.id.edit_Email);
        checkStudent = (CheckBox) findViewById(R.id.checkStudent);
        btn_Register = (Button) findViewById(R.id.btn_Signup);

        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPost();
            }
        });

        edit_Email.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    if (checkStudent(edit_Email.getText().toString().trim())) {
                        checkStudent.setChecked(true);
                        flag_Student = "1";
                    } else {
                        checkStudent.setChecked(false);
                        flag_Student = "0";
                    }
                    return true;
                }
                return true;
            }
        });

    }

    private boolean checkStudent(String m_email) {
        if (m_email.contains(".edu.vn")) {
            return true;
        }
        else return false;
    }

    public void sendPost() throws RuntimeException{
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(Port.URL_REGISTER);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                        conn.setRequestProperty("Accept", "application/json");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);

                        JSONObject jsonParam = new JSONObject();

                        jsonParam.put("username", edit_Username.getText().toString().trim());
                        jsonParam.put("password", edit_Password.getText().toString().trim());
                        jsonParam.put("email", edit_Email.getText().toString().trim());
                        jsonParam.put("usertype", flag_Student);

                        Log.i("JSON", jsonParam.toString());
                        DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                        //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                        os.writeBytes(jsonParam.toString());

                        os.flush();
                        os.close();

                        Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                        Log.i("MSG", conn.getResponseMessage());

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

                        Log.i("MSG", jsonResponse.getString("status"));
                        if (jsonResponse.getString("status").equals("true")) {
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        }

                        conn.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}

