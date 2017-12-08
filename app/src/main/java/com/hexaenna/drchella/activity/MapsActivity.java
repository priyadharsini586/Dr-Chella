package com.hexaenna.drchella.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;
import com.hexaenna.drchella.Model.MapDetails;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.api.MapInterface;
import com.hexaenna.drchella.utils.Constants;


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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        build_retrofit_and_get_response(getString(R.string.coimbatore_hospital_address));
        // Add a marker in Sydney and move the camera

    }


    private void moveToCurrentLocation(LatLng currentLocation) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 20));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(20), 2000, null);


    }

    private void build_retrofit_and_get_response(String address) {

        String url = "http://maps.googleapis.com/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MapInterface service = retrofit.create(MapInterface.class);

        Call<MapDetails> call = service.getLanLont(address, "true");

        call.enqueue(new Callback<MapDetails>() {
            @Override
            public void onResponse(Call<MapDetails> call, Response<MapDetails> response) {

                if (response.isSuccessful()) {
                    MapDetails mapDetails = response.body();
                    if (mapDetails != null) {
                        ArrayList<MapDetails.MapResults> mapResultses = mapDetails.getResults();
                        if (mapResultses.size() != 0) {
                            MapDetails.MapResults mapResults = mapResultses.get(0);
                            MapDetails.Geometry geometry = mapResults.getGeometry();
                            MapDetails.LocationLat locationLat = geometry.getLocation();
                            double lat = locationLat.getLat();
                            double lon = locationLat.getLng();
                            LatLng latLng = new LatLng(lat, lon);
                            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney")).showInfoWindow();
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            mMap.setMyLocationEnabled(true);
                            mMap.setBuildingsEnabled(true);
                            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                            moveToCurrentLocation(latLng);
                        }
                  }

              }
             /*  LatLng sydney = getLocationFromAddress(getApplicationContext(),"U.R.C. Hospital,Surampattivalasu,Erode,Tamil Nadu");

               mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
               mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
               moveToCurrentLocation(sydney);*/
           }

           @Override
           public void onFailure(Call<MapDetails> call, Throwable t) {
               Log.e("failure","fail");
           }
       });
    }


}
