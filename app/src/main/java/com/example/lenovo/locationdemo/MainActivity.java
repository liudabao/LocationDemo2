package com.example.lenovo.locationdemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button bt1;
    TextView tv;
    LocationManager manager;
    List<String> providerList;
    String provider;
    Location location;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        initView();
    }

    private void initView() {
        tv = (TextView) findViewById(R.id.textView);
        bt1 = (Button) findViewById(R.id.button);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.e("Location", "gps provider ");
            // location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            provider = LocationManager.GPS_PROVIDER;
        } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Log.e("Location", "net provider");
            //location=manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            provider = LocationManager.NETWORK_PROVIDER;
        } else {

            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPosition();
            }
        });

    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.e("Location2", provider);
            show(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void getPosition() {

        // providerList = manager.getProviders(true);
        /*if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            Log.e("Location", "No provider to use");
        }*/

        // if (location != null) {
        //     Log.e("Location",  "show");
        //     show(location);
        //  }
        Log.e("Location", provider);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION }, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
        manager.requestLocationUpdates(provider, 1000, 1, locationListener);
        Log.e("Location1",  provider);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                          String permissions[], int[] grantResults){
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void show(Location location){
        String position = "latitude is " + location.getLatitude() + "\n" + "longitude is " + location.getLongitude();
        tv.setText(position);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (manager != null) {
            manager.removeUpdates(locationListener);
        }
    }



}
