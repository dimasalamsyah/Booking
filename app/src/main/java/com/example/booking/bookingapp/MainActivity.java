package com.example.booking.bookingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    private ArrayList<String> listBooking, listBooking_detail;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPrefManager = new SharedPrefManager(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Booking Kelas - " + sharedPrefManager.getSPNama());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                //Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
                startActivity(new Intent(MainActivity.this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
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
                        listBooking.put("id", jsonObject.getString("id"));
                        listBooking.put("nama_ruangan", jsonObject.getString("nama_ruangan"));
                        listBooking.put("jam", jsonObject.getString("jam"));
                        listBooking.put("tgl", jsonObject.getString("tgl"));
                        listBooking.put("ket", jsonObject.getString("ket"));
                        listBookings.add(listBooking);
                    }

                    final SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, listBookings, R.layout.list_view_layout,
                            new String[]{"id", "nama_ruangan", "jam", "tgl", "ket"},
                            new int[]{0, R.id.judul, R.id.subtitle, R.id.tgltitle, 0});

                    listView.setAdapter(adapter);
                    listView.setSelected(true);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                            final CharSequence[] dialogitem = {"Detail", "Edit", "Hapus"};
                            AlertDialog.Builder builder = new AlertDialog.Builder( MainActivity.this );
                            builder.setTitle("Pilihan");
                            final HashMap<String, Object> obj = (HashMap<String, Object>) adapter.getItem(position);
                            builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    switch(item){
                                        case 0 :
                                            String det_nama_ruangan = (String) obj.get("nama_ruangan");
                                            String det_jam = (String) obj.get("jam");
                                            String det_tgl = (String) obj.get("tgl");
                                            String det_ket = (String) obj.get("ket");
                                            Intent det_intent = new Intent(MainActivity.this, BookingDetailActivity.class);
                                            det_intent.putExtra("nama_ruangan",det_nama_ruangan .toString());
                                            det_intent.putExtra("jam",det_jam .toString());
                                            det_intent.putExtra("tgl",det_tgl .toString());
                                            det_intent.putExtra("ket",det_ket .toString());
                                            startActivity(det_intent);
                                            break;
                                        case 1 :
                                            String nama_ruangan = (String) obj.get("nama_ruangan");
                                            String jam = (String) obj.get("jam");
                                            String tgl = (String) obj.get("tgl");
                                            String id = (String) obj.get("id");
                                            String ket = (String) obj.get("ket");
                                            Intent intent = new Intent(MainActivity.this, BookingAddActivity.class);
                                            intent.putExtra("nama_ruangan",nama_ruangan.toString());
                                            intent.putExtra("jam",jam.toString());
                                            intent.putExtra("tgl",tgl.toString());
                                            intent.putExtra("id",id.toString());
                                            intent.putExtra("ket",ket.toString());
                                            startActivity(intent);
                                            break;
                                        case 2 :
                                            AlertDialog.Builder builder;
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                                            } else {
                                                builder = new AlertDialog.Builder(MainActivity.this);
                                            }
                                            builder.setTitle("Warning!")
                                                    .setMessage("Yakin ingin menghapus?")
                                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            String id = (String) obj.get("id");
                                                            hapus(id);
                                                        }
                                                    })
                                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            // do nothing
                                                        }
                                                    })
                                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                                    .show();
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

    private void hapus(final String id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.HAPUS_BOOKING_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("success")){
                    Snackbar mySnackbar = Snackbar.make(findViewById(R.id.layoutMain), "Success", Snackbar.LENGTH_SHORT);
                    mySnackbar.show();
                    refreshList();
                }else{
                    Snackbar mySnackbar = Snackbar.make(findViewById(R.id.layoutMain), "Error", Snackbar.LENGTH_SHORT);
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
                params.put("id", id);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);

    }

}
