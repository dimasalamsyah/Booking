package com.example.booking.bookingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

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
            @Override
            public void onClick(View view) {
                sharedPrefManager.getSPIP("");
            }
        });
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
