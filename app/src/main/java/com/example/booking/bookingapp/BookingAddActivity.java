package com.example.booking.bookingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookingAddActivity extends AppCompatActivity {

    Spinner spKelas, spJam;
    Button btnBooking;
    CalendarView calendarView;
    String curDate;
    private ArrayList<String> listKelas, listJam;
    HashMap<Integer,String> spinnerMap_kelas = new HashMap<Integer, String>();
    HashMap<Integer,String> spinnerMap_jam = new HashMap<Integer, String>();
    SimpleDateFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_add);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Tambah Booking Kelas");

        init();
        getdata_jam();
        getdata_kelas();
    }

    private void init(){
        spKelas = (Spinner)findViewById(R.id.spKelas);
        spJam = (Spinner)findViewById(R.id.spJam);
        calendarView = (CalendarView)findViewById(R.id.calendarView);
        btnBooking = (Button)findViewById(R.id.btnBooking);

        df = new SimpleDateFormat("yyyy-MM-dd");
        curDate = df.format(calendarView.getDate());

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                int d = dayOfMonth, m = month, y = year;
                curDate =String.valueOf(y) +"-"+ String.valueOf(m + 1) +"-"+ String.valueOf(d);
                Toast.makeText(BookingAddActivity.this, curDate, Toast.LENGTH_SHORT).show();
            }
        });
        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                booking();
            }
        });
    }

    public void getdata_kelas(){
        StringRequest stringRequest = new StringRequest(Config.RUANG_URL, new Response.Listener<String>() {
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
        StringRequest stringRequest = new StringRequest(Config.JAM_URL, new Response.Listener<String>() {
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
        /*final String curJam = spJam.getSelectedItem().toString();
        final String curKelas = spKelas.getSelectedItem().toString();*/
        final String idKelas = spinnerMap_kelas.get(spKelas.getSelectedItemPosition());
        final String idJam = spinnerMap_jam.get(spJam.getSelectedItemPosition());

        //Toast.makeText(TambahBooking.this, "hasil ="+ idKelas, Toast.LENGTH_SHORT).show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.TAMBAH_BOOKING_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(BookingAddActivity.this, response, Toast.LENGTH_SHORT).show();
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
                params.put("ket", "ini ket");
                params.put("nama", "Dimas");

                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);

    }
}
