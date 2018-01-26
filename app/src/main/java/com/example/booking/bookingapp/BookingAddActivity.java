package com.example.booking.bookingapp;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class BookingAddActivity extends AppCompatActivity {

    Spinner spKelas, spJam;
    Button btnBooking;
    CalendarView calendarView;
    EditText keterangan;
    String curDate;
    private ArrayList<String> listKelas, listJam;
    HashMap<Integer,String> spinnerMap_kelas = new HashMap<Integer, String>();
    HashMap<Integer,String> spinnerMap_jam = new HashMap<Integer, String>();
    SimpleDateFormat df;
    Bundle extras;

    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_add);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Tambah Booking Kelas");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_login));

        init();
        getdata_jam();
        getdata_kelas();

        extras = getIntent().getExtras();
        if (extras != null) {
            calendarView.setDate(getLongAsDate(extras.getString("tgl")), true, true);
            getSupportActionBar().setTitle("Edit Booking Kelas");
            btnBooking.setText("Ubah Booking");
            keterangan.setText(extras.getString("ket"));
        }
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
                booking();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public long getLongAsDate(String tgl) {
        int year = 0, month = 0, date = 0;
        year = Integer.parseInt(tgl.substring(0,4));
        month = Integer.parseInt(tgl.substring(5,7));
        date = Integer.parseInt(tgl.substring(8,10));
        //return Log.d("pesan", String.valueOf(date));
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, date);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.YEAR, year);
        return calendar.getTimeInMillis();
    }

    private void init(){
        spKelas = (Spinner)findViewById(R.id.spKelas);
        spJam = (Spinner)findViewById(R.id.spJam);
        calendarView = (CalendarView)findViewById(R.id.calendarView);
        btnBooking = (Button)findViewById(R.id.btnBooking);
        df = new SimpleDateFormat("yyyy-MM-dd");
        curDate = df.format(calendarView.getDate());
        keterangan = (EditText)findViewById(R.id.keterangan);

        sharedPrefManager = new SharedPrefManager(this);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                int d = dayOfMonth, m = month, y = year;
                curDate =String.valueOf(y) +"-"+ String.valueOf(m + 1) +"-"+ String.valueOf(d);
            }
        });
        /*btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                booking();
            }
        });*/
    }

    public void getdata_kelas(){
        StringRequest stringRequest = new StringRequest(sharedPrefManager.getRuangUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    listKelas = new ArrayList<String>();

                    for(int i=0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        listKelas.add(jsonObject.getString("nama"));
                        spinnerMap_kelas.put(i, jsonObject.getString("kode"));
                    }
                    spKelas.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                            R.layout.spinner_layout, listKelas));

                    if (extras != null) {
                        String nama_ruangan = extras.getString("nama_ruangan");
                        spKelas.setSelection(((ArrayAdapter<String>)spKelas.getAdapter()).getPosition(nama_ruangan));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BookingAddActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public void getdata_jam(){
        StringRequest stringRequest = new StringRequest(sharedPrefManager.getJamUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    listJam = new ArrayList<String>();
                    for(int i=0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        listJam.add(jsonObject.getString("jam"));

                        spinnerMap_jam.put(i, jsonObject.getString("id"));
                    }
                    spJam.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                            R.layout.spinner_layout, listJam));

                    if (extras != null) {
                        String jam = extras.getString("jam");
                        spJam.setSelection(((ArrayAdapter<String>)spJam.getAdapter()).getPosition(jam));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BookingAddActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void booking(){

        final String idKelas = spinnerMap_kelas.get(spKelas.getSelectedItemPosition());
        final String idJam = spinnerMap_jam.get(spJam.getSelectedItemPosition());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, sharedPrefManager.getTambahBookingUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("full")){
                    Snackbar mySnackbar = Snackbar.make(findViewById(R.id.layoutBooking), "Maaf, Ruangan penuh!", Snackbar.LENGTH_SHORT);
                    mySnackbar.show();
                }else if(response.equals("success")){
                    //Snackbar mySnackbar = Snackbar.make(findViewById(R.id.layoutBooking), "Success", Snackbar.LENGTH_SHORT);
                    //mySnackbar.show();
                    //super.onBackPressed();
                    //BookingAddActivity.super.onBackPressed();
                    Intent i = new Intent(BookingAddActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Snackbar mySnackbar = Snackbar.make(findViewById(R.id.layoutBooking), "Error", Snackbar.LENGTH_SHORT);
                    mySnackbar.show();
                }
               //Log.d("pesan", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("kode_pembooking", "ede");
                params.put("jabatan", "mahasiswa");
                params.put("combo_kelas_add", idKelas);
                params.put("jam_add_kode", idJam);
                params.put("tanggal_add", curDate);
                params.put("ket",  keterangan.getText().toString());
                params.put("nama", "dimas");

                if(extras != null){
                    params.put("option", "update");
                    params.put("id", extras.getString("id"));
                }
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);

    }

}
