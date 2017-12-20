package com.hexaenna.drchella.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hexaenna.drchella.Model.MapDetails;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.api.MapInterface;
import com.hexaenna.drchella.utils.Config;
import com.hexaenna.drchella.utils.Constants;
import com.hexaenna.drchella.utils.NotificationUtils;


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
    String cityAddressCity,cityAddress,pinCity;
    ProgressBar proMapView;
    BroadcastReceiver mRegistrationBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("fromCity") != null) {
            cityAddressCity = bundle.getString("fromCity");
            cityAddress();
        }

        proMapView = (ProgressBar) findViewById(R.id.proMapView);
        proMapView.setVisibility(View.GONE);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);



                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");
                    String title = intent.getStringExtra("title");

                    if (!intent.getStringExtra("from").equals("tips")) {
                        HomeActivity homeActivity = new HomeActivity();
                        homeActivity.showAlert(MapsActivity.this, title, message);
                    }



                }
            }
        };
    }

    public void cityAddress()
    {
        if (cityAddressCity.equals("1"))
        {
            cityAddress = getString(R.string.map_chennai_address);
            pinCity = getString(R.string.chennai_hospital);
        }else if (cityAddressCity.equals("2"))
        {
            cityAddress = getString(R.string.map_erode_address);
            pinCity = getString(R.string.erode_hospital);
        }else if (cityAddressCity.equals("3"))
        {
            cityAddress = getString(R.string.map_coimbatore_address);
            pinCity = getString(R.string.coimbatore_hospital);
        }else if (cityAddressCity.equals("4"))
        {
            cityAddress = getString(R.string.map_namakkal_address);
            pinCity = getString(R.string.namakkal_hospital);
        }else if (cityAddressCity.equals("5"))
        {
            cityAddress = getString(R.string.map_mayiladudurai_address);
            pinCity = getString(R.string.mayiladu_hospital);
        }else if (cityAddressCity.equals("6"))
        {
            cityAddress = getString(R.string.map_kolidam_address);
            pinCity = getString(R.string.kollidam_hospital);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        build_retrofit_and_get_response(cityAddress);
        // Add a marker in Sydney and move the camera

    }


    private void moveToCurrentLocation(LatLng currentLocation) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 20));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 1000, null);


    }

    private void build_retrofit_and_get_response(String address) {

        String url = "http://maps.googleapis.com/";
        proMapView.setVisibility(View.VISIBLE);
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
                            mMap.addMarker(new MarkerOptions().position(latLng).title(pinCity)).showInfoWindow();
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
//                            mMap.setMyLocationEnabled(true);
//                            mMap.setBuildingsEnabled(true);
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            moveToCurrentLocation(latLng);
                            proMapView.setVisibility(View.GONE);
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

           }
       });
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        mRegistrationBroadcastReceiver=null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }
}
