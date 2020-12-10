package com.abhishek.myliber;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

import java.util.ArrayList;
import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener{
    public Double sorcelatti,sorcelongi,detlatti,detlongi;
    private GeoApiContext geoApiContext = null;
    private ArrayList<PolylineData> polylinesData = new ArrayList<>();
    private GoogleMap mGoogleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_map,container,false);
        sorcelatti=this.getArguments().getDouble("sorcelat");
        sorcelongi=this.getArguments().getDouble("sorcelong");
        detlatti=this.getArguments().getDouble("destlat");
        detlongi=this.getArguments().getDouble("destlong");
        if(geoApiContext == null){
            geoApiContext = new GeoApiContext.Builder()
                    .apiKey("AIzaSyDzPiMQO0YGCarnG3xmSl3H9g_uIBvqGC0")
                    .build();
        }
        SupportMapFragment supportMapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this::onMapReady);
        /*supportMapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(final GoogleMap googleMap) {

            }
        });*/


        return view;
    }

    private void calculateDirections(){
        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                detlatti,
                detlongi
        );
        DirectionsApiRequest directions = new DirectionsApiRequest(geoApiContext);

        directions.alternatives(true);
        directions.origin(
                new com.google.maps.model.LatLng(
                        sorcelatti,
                        sorcelongi
                )
        );
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                //ResultSet
                addPolylinesToMap(result);

            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }
    private void addPolylinesToMap(final DirectionsResult result){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if(polylinesData.size() > 0){
                    for(PolylineData mPolylineData : polylinesData){
                        mPolylineData.getPolyline().remove();
                    }
                    polylinesData.clear();
                    polylinesData = new ArrayList<>();
                }
                for(DirectionsRoute route: result.routes){
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                    List<LatLng> newDecodedPath = new ArrayList<>();

                    // This loops through all the LatLng coordinates of ONE polyline.
                    for(com.google.maps.model.LatLng latLng: decodedPath){

                        newDecodedPath.add(new LatLng(
                                latLng.lat,
                                latLng.lng
                        ));
                    }
                    Polyline polyline = mGoogleMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    polyline.setColor(ContextCompat.getColor(getContext(), R.color.colorGrey));
                    polyline.setClickable(true);
                    polylinesData.add(new PolylineData(polyline,route.legs[0]));

                }
            }
        });
    }
    private void updateCameraPosition(GoogleMap googleMap) {
        Location startingLocation = new Location("starting point");
        startingLocation.setLatitude(sorcelatti); // location is current location
        startingLocation.setLongitude(sorcelongi);

        //Get the target location
        Location endingLocation = new Location("ending point");
        endingLocation.setLatitude(detlatti);
        endingLocation.setLongitude(detlongi);

        //Find the Bearing from current location to next location
        float targetBearing = startingLocation.bearingTo(endingLocation);
        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(new LatLng(sorcelatti,sorcelongi))
                        .bearing(targetBearing)
                        .tilt(90)
                        .zoom(googleMap.getCameraPosition().zoom)
                        .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        for(PolylineData polylineData: polylinesData){
            if(polyline.getId().equals(polylineData.getPolyline().getId())){
                polylineData.getPolyline().setColor(ContextCompat.getColor(getContext(), R.color.colorBlue));
                polylineData.getPolyline().setZIndex(1);
                Log.v("polyline",""+polylineData.getLeg().distance);
                String distance = ""+polylineData.getLeg().distance;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(distance);
                stringBuilder.deleteCharAt(distance.length()-1);
                stringBuilder.deleteCharAt(distance.length()-2);
                stringBuilder.deleteCharAt(distance.length()-3);
                distance = stringBuilder.toString();
            }
            else{
                polylineData.getPolyline().setColor(ContextCompat.getColor(getContext(), R.color.colorGrey));
                polylineData.getPolyline().setZIndex(0);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setOnPolylineClickListener(this);
        mGoogleMap = googleMap;
        LatLng coordinates = new LatLng(detlatti, detlongi);
        googleMap.addMarker(new MarkerOptions().position(coordinates).title("Liber"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15));
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            return;
        }
        googleMap.setMyLocationEnabled(true);
        updateCameraPosition(googleMap);
        calculateDirections();
    }
}