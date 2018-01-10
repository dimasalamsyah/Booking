package com.example.booking.bookingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    private ArrayList<String> listBooking, listBooking_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BookingAddActivity.class);
                startActivity(intent);
            }
        });

        listView = (ListView)findViewById(R.id.listView);
        refreshList();
    }


    public void refreshList(){
        StringRequest stringRequest = new StringRequest(Config.BOOKING_LIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    listBooking = new ArrayList<String>();
                    listBooking_detail = new ArrayList<String>();

                    List<HashMap<String, String>> listBookings = new ArrayList<HashMap<String, String>>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> listBooking = new HashMap<String, String>();
                        listBooking.put("title", jsonObject.getString("nama_ruangan"));
                        listBooking.put("subtile", jsonObject.getString("jam"));
                        listBooking.put("tgl", jsonObject.getString("tgl"));
                        listBookings.add(listBooking);
                    }

                    SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, listBookings, R.layout.list_view_layout,
                            new String[]{"title", "subtile", "tgl"},
                            new int[]{R.id.judul,
                                    R.id.subtitle, R.id.tgltitle});

                    listView.setAdapter(adapter);
                    listView.setSelected(true);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            final CharSequence[] dialogitem = {"Detail", "Edit", "Hapus"};
                            AlertDialog.Builder builder = new AlertDialog.Builder( MainActivity.this );
                            builder.setTitle("Pilihan");
                            builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    switch(item){
                                        case 0 :
                                            break;
                                        case 1 :
                                            break;
                                        case 2 :
                                            break;
                                    }
                                }
                            });
                            builder.create().show();
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "server trouble", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue( MainActivity.this );
        requestQueue.add(stringRequest);
    }

}
