package com.example.booking.bookingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;

public class SettingIPActivity extends AppCompatActivity {
    SharedPrefManager sharedPrefManager;
    TextView txtIP, txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_ip);

        sharedPrefManager = new SharedPrefManager(this);

        getSupportActionBar().setTitle("Edit IP");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_login));


        Button btnTest = (Button)findViewById(R.id.setting_btnTest);
        txtIP = (TextView)findViewById(R.id.txtIP);
        txtResult = (TextView)findViewById(R.id.setting_txtResult);

        txtResult.setText(sharedPrefManager.getSPIP(""));

        btnTest.setOnClickListener(new View.OnClickListener() {
            //sharedPrefManager.getSPIP("")
            @Override
            public void onClick(View view) {
                sharedPrefManager.saveSPString(SharedPrefManager.SP_IP, txtIP.getText().toString());

//isConnectedToServer("http://192.168.56.1/dashboard/", 10)
                /*if( isConnectedToServer() ){
                   Log.d("hasil","connected");
               }else{
                   Log.d("hasil","unreachable");
               }*/
               //Snackbar mySnackbar = Snackbar.make(findViewById(R.id.layoutMain), conn ? "Connected" : "The server unreachable", Snackbar.LENGTH_SHORT);
               //mySnackbar.show();
            }
        });
    }


    public boolean checkInternet(){
        boolean connectStatus = true;
        ConnectivityManager ConnectionManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true ) {
            connectStatus = true;
        }
        else {
            connectStatus = false;
        }
        return  connectStatus;
    }

    public Boolean isConnectedToServer() {
        try{
            URL myUrl = new URL("http://192.168.56.1/");
            HttpURLConnection urlc = (HttpURLConnection) myUrl.openConnection();
            //URLConnection connection = myUrl.openConnection();
            //connection.setConnectTimeout(timeout);
            urlc.connect();
            urlc.getResponseCode();
            return true;
        } catch (Exception e) {
            // Handle your exceptions
            return false;
        }
    }

    AlertDialog b;
    AlertDialog.Builder dialogBuilder;

    public void ShowProgressDialog() {
        dialogBuilder = new AlertDialog.Builder(SettingIPActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
                editIP();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editIP() {
        sharedPrefManager.saveSPString(SharedPrefManager.SP_IP, txtIP.getText().toString());
        txtResult.setText(sharedPrefManager.getSPIP(""));
    }
}
