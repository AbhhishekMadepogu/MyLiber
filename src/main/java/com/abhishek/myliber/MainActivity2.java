package com.abhishek.myliber;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
public class MainActivity2 extends AppCompatActivity {
    double sourcelat,sourcelong,destlat,destlong;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static ArrayList<Library1> library1s;
    String city="";
    String placetype="library";
    //////////////////////////
    private static final String API_KEY = "AIzaSyDzPiMQO0YGCarnG3xmSl3H9g_uIBvqGC0";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String TYPE_DETAILS = "/details";
    private static final String TYPE_SEARCH = "/nearbysearch";
    private static final String OUT_JSON = "/json?";
    private static final String LOG_TAG = "ListLibrary";
    /////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        library1s=new ArrayList<>();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        int radius = 9000;
        sourcelat=getIntent().getDoubleExtra("sourcelatti",0.0);
        city=getIntent().getStringExtra("cities");
        sourcelong=getIntent().getDoubleExtra("sourcelongi",0.0);
        destlat=getIntent().getDoubleExtra("destlatti",0.0);
        destlong=getIntent().getDoubleExtra("destlongi",0.0);
        ArrayList<Place> list = search(sourcelat, sourcelong, radius);
        Log.v("place","list "+list);
        for(int i = 0; i < list.size(); i++) {
            String name = list.get(i).name;
            String rating = list.get(i).rating;
            //do whatever you want to do with the data.
            //create an arraylist of names,ratings etc.
        }
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Map");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity2.this,RidedetailsActivity.class);
                i.putExtra("extralatti",destlat);
                i.putExtra("extralongi",destlong);
                i.putExtra("extralattitude",sourcelat);
                i.putExtra("extralongitude",sourcelong);
                i.putExtra("extracity",city);
                startActivity(i);
            }
        });
        recyclerView=findViewById(R.id.rv);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Fragment fragment=new MapFragment();
        Bundle bundle=new Bundle();
        bundle.putDouble("sorcelat",sourcelat);
        bundle.putDouble("sorcelong",sourcelong);
        bundle.putDouble("destlong",destlong);
        bundle.putDouble("destlat",destlat);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).commit();
        LibraryAdapter adapter=new LibraryAdapter(library1s);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
    }
    public static ArrayList<Place> search(double lat, double lng, int radius) {
        ArrayList<Place> resultList = null;
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE);
            sb.append(TYPE_SEARCH);
            sb.append(OUT_JSON);
            sb.append("location=" + String.valueOf(lat) + "," + String.valueOf(lng));
            sb.append("&radius=" + String.valueOf(radius));
            sb.append("&type=library");
            sb.append("&key=" + API_KEY);
            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("results");
            // Extract the descriptions from the results
            resultList = new ArrayList<Place>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {

                String n=predsJsonArray.getJSONObject(i).getString("name");
                Library1 l=new Library1(n);
                library1s.add(l);


            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error processing JSON results", e);
        }
        return resultList;
    }
    public static class Place {
        private String reference;
        private String name;
        private String icon;
        private String lat;
        private String lng;
        private String rating;
        public Place(){
            super();
        }
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Hello back", Toast.LENGTH_SHORT).show();

        super.onBackPressed();
    }
}