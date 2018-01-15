package com.example.booking.bookingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class BookingDetailActivity extends AppCompatActivity {
    TextView ruang, jam, tgl, ket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Detail Booking Kelas");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_login));

        ruang = (TextView)findViewById(R.id.detail_ruang);
        jam = (TextView)findViewById(R.id.detail_jam);
        tgl = (TextView)findViewById(R.id.detail_tgl);
        ket = (TextView)findViewById(R.id.detail_ket);

        Bundle extras;
        extras = getIntent().getExtras();
        ruang.setText(extras.getString("nama_ruangan"));
        jam.setText(extras.getString("jam"));
        tgl.setText(extras.getString("tgl"));
        ket.setText(extras.getString("ket"));

    }

}
