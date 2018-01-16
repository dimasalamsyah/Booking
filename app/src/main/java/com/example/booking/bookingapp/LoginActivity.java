package com.example.booking.bookingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText login_username, login_password;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        sharedPrefManager = new SharedPrefManager(this);

        if (sharedPrefManager.getSPSudahLogin()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin();
            }
        });
    }

    private void init(){
        btnLogin = (Button)findViewById(R.id.login_btnLogin);
        login_username = (EditText)findViewById(R.id.login_username);
        login_password = (EditText)findViewById(R.id.login_password);
    }

    AlertDialog b;
    AlertDialog.Builder dialogBuilder;

    public void ShowProgressDialog() {
        dialogBuilder = new AlertDialog.Builder(LoginActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View dialogView = inflater.inflate(R.layout.loading, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        b = dialogBuilder.create();
        b.show();
    }
    public void HideProgressDialog(){
        b.dismiss();
    }

    public void doLogin(){
        final String username = login_username.getText().toString().trim();
        final String password = login_password.getText().toString().trim();

        if(username.equals("*config")){
            Intent intent = new Intent(LoginActivity.this, SettingIPActivity.class);
            startActivity(intent);
        }else{
            Log.d("Hasil", sharedPrefManager.getLoginUrl());

            ShowProgressDialog();
            //Creating a string request
            StringRequest stringRequest = new StringRequest(Request.Method.POST, sharedPrefManager.getLoginUrl(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            //If we are getting success from server
                            if (response.contains("success")) {
                                HideProgressDialog();
                                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, true);
                                sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA, username);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                //Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                            } else {
                                //Displaying an error message on toast
                                HideProgressDialog();
                                Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //You can handle error here if you want
                            Toast.makeText(LoginActivity.this, "The server unreachable", Toast.LENGTH_LONG).show();
                            HideProgressDialog();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request
                    params.put("username", username);
                    params.put("password", password);

                    //returning parameter
                    return params;
                }
            };

            //Adding the string request to the queue
            Volley.newRequestQueue(this).add(stringRequest);
        }
    }
}
