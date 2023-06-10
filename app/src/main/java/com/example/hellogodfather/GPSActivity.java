package com.example.hellogodfather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.example.hellogodfather.databinding.ActivityGpsactivityBinding;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class GPSActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener {

    private GoogleMap mMap;
    private ActivityGpsactivityBinding binding;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager mlocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGpsactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        //check the GPS is open or not
        mlocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!mlocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(GPSActivity.this, "please open GPS!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent,0);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        //check the permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(GPSActivity.this, "no GPS permission", Toast.LENGTH_SHORT).show();
            String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION};
            requestPermissions(permissions,0);
            onMapReady(googleMap);
            return;
        }

        //get the location from GPS
        String provider = LocationManager.GPS_PROVIDER;

        Location lc = mlocationManager.getLastKnownLocation(provider);
        if(lc != null){
            showLocation(lc);
        }

        //real-time update the location
        mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 8, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                showLocation(location);
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @SuppressLint("MissingPermission")
            @Override
            public void onProviderEnabled(String provider) {
                showLocation(mlocationManager.getLastKnownLocation(provider));
            }

            @Override
            public void onProviderDisabled(String provider) {
                showLocation(null);
            }
        });



    }




    //show the location in map
    private void showLocation(Location location) {
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(userLocation).title("your location here"));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10));
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        showLocation(location);
    }


}