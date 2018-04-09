package com.example.asifhashmi.weatherandmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.asifhashmi.weathermandmpsapp.MapsActivity;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
   public static List<LatLng> locations=new ArrayList<LatLng>();

   static TextView desc,temp,maxTemp,minTemp,humidity,address;
   static Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        desc=(TextView)findViewById(R.id.desc);
        humidity=(TextView)findViewById(R.id.humidity);
        maxTemp=(TextView)findViewById(R.id.maxTemp);
        minTemp=(TextView)findViewById(R.id.minTemp);
        temp=(TextView)findViewById(R.id.temp);
        button=(Button)findViewById(R.id.toMap);
        address=(TextView)findViewById(R.id.address);
        address.setVisibility(View.INVISIBLE);
        desc.setVisibility(View.INVISIBLE);
        humidity.setVisibility(View.INVISIBLE);
        maxTemp.setVisibility(View.INVISIBLE);
        minTemp.setVisibility(View.INVISIBLE);
        temp.setVisibility(View.INVISIBLE);

    }

    public void toMapFunc(View view){

        Intent intent=new Intent(getApplicationContext(),MapsActivity.class);
        startActivity(intent);

    }

    public static void setContents(){

        double maxTempD=Double.parseDouble(MapsActivity.maxTemp);
        double tempD=Double.parseDouble(MapsActivity.temp);
        double minTempD=Double.parseDouble(MapsActivity.minTemp);

        maxTempD-=273.15;
        tempD-=273.15;
        minTempD-=273.15;

        desc.setText("Weather : "+MapsActivity.description);
        humidity.setText("Humidity : "+MapsActivity.humidity+" %");
        temp.setText("Temp : "+String.format("%.2f", tempD)+"°C");
        maxTemp.setText("Max Temp : "+String.format("%.2f", maxTempD)+" °C");
        minTemp.setText("Min Temp : "+String.format("%.2f", minTempD)+" °C");
        address.setText("Location : "+MapsActivity.address);
        desc.setVisibility(View.VISIBLE);
        humidity.setVisibility(View.VISIBLE);
        maxTemp.setVisibility(View.VISIBLE);
        minTemp.setVisibility(View.VISIBLE);
        temp.setVisibility(View.VISIBLE);
        address.setVisibility(View.VISIBLE);
        button.setText("To maps");

    }
}
