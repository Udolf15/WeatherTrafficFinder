package com.example.asifhashmi.weathermandmpsapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.example.asifhashmi.weatherandmap.MainActivity;
import com.example.asifhashmi.weatherandmap.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback ,GoogleMap.OnMapLongClickListener{

   public static String description,maxTemp,minTemp,temp,humidity,address;

    LatLng locationU;
    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
        address="";
        try {

            List<Address> addressList=geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            if(addressList!=null && addressList.size()>0){


                    if(addressList.get(0).getFeatureName()!=null){
                        address=addressList.get(0).getFeatureName()+", "+addressList.get(0).getLocality();
                    }


                    Log.i("dfs",address);


            }

            if(address==""){
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm dd:MM:yyy");
                address= simpleDateFormat.format(new Date());


            }

        } catch (IOException e) {
            Log.i("Error",e.toString());
            e.printStackTrace();

        }

        Location temp = new Location(LocationManager.GPS_PROVIDER);
     temp.setLatitude(latLng.latitude);
        temp.setLongitude(latLng.longitude);

        getWeather(temp);

        MarkerOptions marker = new MarkerOptions().position(latLng).title(address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

          mMap.addMarker(marker);

    }

    public class DownloadTask extends AsyncTask<String,Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            HttpURLConnection httpURLConnection=null;

            try {
                url=new URL(urls[0]);

                httpURLConnection=(HttpURLConnection)url.openConnection();
                InputStream in =httpURLConnection.getInputStream();
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));


                String data=reader.readLine();

                while (data!=null){
                    result +=data;
                    data=reader.readLine();
                }

                return result;
            } catch (MalformedURLException e) {
                Log.i("erroe safklsfdlkna;",e.toString());
                Log.i("Error",e.toString());
                e.printStackTrace();

            } catch (IOException e) {
                Log.i("error in the main",e.toString());
                Log.i("Error",e.toString());
                e.printStackTrace();
            }


            return null;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                Location userLastKnownLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


                locationU=new LatLng(userLastKnownLocation.getLatitude(),userLastKnownLocation.getLongitude());

                mapCenter(userLastKnownLocation,"Your Location");
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        mMap.setTrafficEnabled(true);
        Log.i("Enter","tre");
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mapCenter(location,"Your Location");


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

              Location userLastKnownLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);



            if(userLastKnownLocation!=null) {
                locationU=new LatLng(userLastKnownLocation.getLatitude(),userLastKnownLocation.getLongitude());
                mapCenter(userLastKnownLocation, "Your Location");
            }
        }

    }

    public void mapCenter(Location location ,String userLoc){

        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
         address="";
        try {

            List<Address> addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(addressList!=null && addressList.size()>0){

                if(addressList.get(0).getFeatureName()!=null){
                    address=addressList.get(0).getFeatureName()+", "+addressList.get(0).getLocality();
                }

                    Log.i("dfs",addressList.get(0).toString());


            }

            if(address==""){
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm dd:MM:yyy");
                address= simpleDateFormat.format(new Date());


            }

        } catch (IOException e) {
            e.printStackTrace();
Log.i("Error",e.toString());
        }
        locationU=new LatLng(location.getLatitude(),location.getLongitude());

        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(locationU).title(address));
       mMap.moveCamera(CameraUpdateFactory.newLatLng(locationU));

        getWeather(location);

    }

    public void getWeather(Location location)  {
        String json="";

        double lat=location.getLatitude();
        double lon=location.getLongitude();

        DownloadTask task=new DownloadTask();

        try {
            json=task.execute("http://api.openweathermap.org/data/2.5/weather?lat="+Double.toString(lat)+"&lon="+Double.toString(lon)+"&appid=5b5d3eab7f4a26c1311cc62c13ffffc1").get();

            Log.i("Weather.json",json);

        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.i("Error",e.toString());
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.i("Error",e.toString());
        }


        JSONObject jsonObject= null;
        JSONArray jsonArray;
        String mainS;
        try {
            jsonObject = new JSONObject(json);

            Log.i("obj returned",jsonObject.getString("weather"));

            jsonArray=new JSONArray(jsonObject.getString("weather"));
            String weather;
            for(int i=0;i<jsonArray.length();i++){

                weather=jsonArray.getString(i);
                JSONObject jsonWeather=new JSONObject(weather);
                Log.i("weather is : ",jsonWeather.getString("description"));
                description=jsonWeather.getString("description");



                mainS=jsonObject.getString("main");
                JSONObject jsonMain=new JSONObject(mainS);

                Log.i("Max Tmemp",jsonMain.getString("temp_max"));

                maxTemp=jsonMain.getString("temp_max");
                minTemp=jsonMain.getString("temp_min");
                temp=jsonMain.getString("temp");
                humidity=jsonMain.getString("humidity");
                MainActivity.setContents();

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("Error",e.toString());
        }

    }


}
