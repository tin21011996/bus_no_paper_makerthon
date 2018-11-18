package com.example.nguyenantin.bususingqrcode;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText editUsername;
    private EditText editPassword;
    private Button btn_Login;
    private Button btn_Register;
    private String qrcode;
    private Intent intent;
    private String flagLogin = "false";
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, BottomNavigationBarActivity.class));
        }

        editUsername = (EditText) findViewById(R.id.edit_Username);
        editPassword = (EditText) findViewById(R.id.edit_Password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        editPassword.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });

        editPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btn_Login.performClick();
                    return true;
                }
                return false;
            }
        });

        btn_Login = (Button) findViewById(R.id.btn_Login);

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editUsername.getText().toString().length()<=0) {
                    Toast.makeText(LoginActivity.this, "Vui lòng điền tên đăng nhập", Toast.LENGTH_SHORT).show();
                } else if (editPassword.getText().toString().length()<=0) {
                    Toast.makeText(LoginActivity.this, "Vui lòng điền mật khẩu", Toast.LENGTH_SHORT).show();
                } else {
//                    loginAccount();
                    sendPost();
                }
            }
        });

        btn_Register = (Button) findViewById(R.id.btn_Signup);

        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void sendPost() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(Port.URL_SIGNIN);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("username", editUsername.getText().toString().trim());
                    jsonParam.put("password", editPassword.getText().toString().trim());

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

                    Log.i("MSG" , jsonResponse.getString("status"));
                    flagLogin = jsonResponse.getString("status");
                    JSONObject content = jsonResponse.getJSONObject("content");
                    qrcode  = content.getString("qrcode");
                    Log.i("MSG" , content.getString("qrcode"));
                    conn.disconnect();
                    if (qrcode!=null) {
                        EndUser user  = new EndUser(editUsername.getText().toString().trim(), content.getString("usertype"), content.getString("email"), content.getString("qrcode"), content.getInt("money"));
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                        intent = new Intent( LoginActivity.this, BottomNavigationBarActivity.class);
                        finish();
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}
