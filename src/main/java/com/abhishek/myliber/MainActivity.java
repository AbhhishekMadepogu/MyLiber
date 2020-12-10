package com.abhishek.myliber;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Button btnSearch;
    EditText edtPC;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public static SqliteHelper sqliteHelper;
    double lat,longi,destlongi,destlatti ;
    String city;
    ProgressDialog loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSearch=findViewById(R.id.btnSearch);
        drawerLayout = findViewById(R.id.drawerLayout);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        NavigationView navigationView = findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        navigationView.setNavigationItemSelectedListener(this);
        
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null)
                {
                    lat = location.getLatitude();
                    longi= location.getLongitude();
                    Toast.makeText(MainActivity.this, ""+lat+""+longi, Toast.LENGTH_SHORT).show();
                    System.out.println(+lat+""+longi);
                }

                });

            }
            else{
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

            }
        }
        sqliteHelper=new SqliteHelper(this,"Latlong.sqlite",null,1);
        sqliteHelper.querydata("CREATE TABLE IF NOT EXISTS LATLONG (lattitude VARCHAR, longitude VARCHAR)");


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         if(TextUtils.isEmpty(edtPC.getText().toString())){
             Toast.makeText(MainActivity.this, "Postal Code should not be empty", Toast.LENGTH_SHORT).show();
         }
         else{
             String lattitude = Double.toString(lat), longitude=Double.toString(longi);
            // GeoLocation geolocation=new GeoLocation();
             final  Geocoder geocoder=new Geocoder(MainActivity.this);
             String geocode=edtPC.getText().toString();
                 try {

                     List<Address> addresses=geocoder.getFromLocationName(geocode,1);
                     if(addresses!=null&&addresses.size()>0){
                         Address address=addresses.get(0);
                          destlongi=address.getLongitude();
                          destlatti=address.getLatitude();
                          city=address.getLocality();


                         Toast.makeText(MainActivity.this, ""+city, Toast.LENGTH_SHORT).show();
                         StartIntent();
                     }
                     else {
                         Toast.makeText(MainActivity.this, "Unable to geocode", Toast.LENGTH_SHORT).show();
                     }
                 } catch (IOException e) {
                     e.printStackTrace();
                 }




             try {
                 sqliteHelper.insertLatlong(lattitude,longitude);
                 //Toast.makeText(MainActivity.this, "Lattitude and Longitude saved into database", Toast.LENGTH_SHORT).show();


             }

             catch (Exception e){


             }
         }
               
            }
        });

        edtPC=findViewById(R.id.txtPS);
        edtPC.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String PostalStr=edtPC.getText().toString();
                if(!isPostalCodeValid(PostalStr)) {
                    btnSearch.setVisibility(View.GONE);
                    edtPC.setError("Invalid Postal Code");
                }
                else {
                    btnSearch.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void StartIntent() {
        Intent i = new Intent(getApplicationContext(), RidedetailsActivity.class);
        i.putExtra("extralongi",destlongi);
        i.putExtra("extralatti",destlatti);
        i.putExtra("extracity",city);
        i.putExtra("extralattitude",lat);
        i.putExtra("extralongitude",longi);

        startActivityForResult(i,0);
    }


    private boolean isPostalCodeValid(String postalStr) {
        String regex = "^(?!.*[DFIOQU])[A-VXY][0-9][A-Z] ?[0-9][A-Z][0-9]$";


        Pattern pattern = Pattern.compile(regex);


        Matcher matcher = pattern.matcher(postalStr);
        System.out.println(matcher.matches());

        return matcher.matches();
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        actionBarDrawerToggle.syncState();

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);

    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (actionBarDrawerToggle.onOptionsItemSelected(item))
//        {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item){
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            setTitle("Profile Page");
            Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(i);


        } else if (id == R.id.nav_payment) {

            setTitle("Payment Page");
            Intent i = new Intent(getApplicationContext(), PaymentActivity.class);
            startActivity(i);


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }






}