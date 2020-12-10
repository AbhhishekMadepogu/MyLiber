package com.abhishek.myliber;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class RidedetailsActivity extends AppCompatActivity {
    TextView tvvdestination,tvvestimatedprice,tvvdistance;
    Button btnmap,btnconfirm;
    double destlatti,destlongi,sourcelatti,sourcelongi;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ridedetails);
        Toolbar toolbar = findViewById(R.id.toolbar_main1);
       setSupportActionBar(toolbar);
        tvvdestination=findViewById(R.id.tvvdestination);
        tvvestimatedprice=findViewById(R.id.tvvestimatedprice);
        tvvdistance=findViewById(R.id.tvvdistance);
        btnmap=findViewById(R.id.btnMap);
        btnconfirm=findViewById(R.id.btnConfirm);
        getSupportActionBar().setTitle("Ride Details");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        String city=getIntent().getStringExtra("extracity");
         destlatti=getIntent().getDoubleExtra("extralatti",0.0);
       destlongi=getIntent().getDoubleExtra("extralongi",0.0);
        sourcelatti=getIntent().getDoubleExtra("extralattitude",0.0);
        sourcelongi=getIntent().getDoubleExtra("extralongitude",0.0);
        float[] results=new float[1];
        Location.distanceBetween(sourcelatti,sourcelongi,destlatti,destlongi,results);
        tvvdestination.setText(city);
        float distance=results[0];
        float its= (float) Math.ceil(distance/1000);
        int it= (int) its;
        System.out.println(+sourcelatti);
        System.out.println(+sourcelongi);
        System.out.println(+destlatti);
        System.out.println(+destlongi);

        tvvdistance.setText(it +" km");
        tvvestimatedprice.setText(it +" $");
        btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(RidedetailsActivity.this,MainActivity2.class);
                i.putExtra("sourcelatti",sourcelatti);
                i.putExtra("sourcelongi",sourcelongi);
                i.putExtra("destlatti",destlatti);
                i.putExtra("destlongi",destlongi);
                i.putExtra("cities",city);
                startActivityForResult(i,0);


            }
        });


    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}