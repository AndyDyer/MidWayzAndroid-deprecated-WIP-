package com.midwayzapp.www.midwayz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.maps.model.LatLng;
import android.widget.TextView;
import java.io.IOException;
import android.location.Geocoder;
import java.util.List;
import android.location.Address;
import java.lang.Math;



public class LoadingScreen extends AppCompatActivity {
    //http://javapapers.com/android/android-geocoding-to-get-latitude-longitude-for-an-address/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        LatLng Add1Cord = null;
        LatLng Add2Cord = null;
        LatLng AddMCord = null;

        String loadadd1 = getIntent().getStringExtra("Address 1");
        String loadadd2 = getIntent().getStringExtra("Address 2");

        Add1Cord = getLocationFromAddress(loadadd1);
        Add2Cord = getLocationFromAddress(loadadd2);


        //TODO Decide geo vs Midpt

        AddMCord = GeoMidpoint(Add1Cord,Add2Cord);

        TextView myAwesomeTextView = (TextView) findViewById(R.id.TestText);
        myAwesomeTextView.setText(AddMCord.toString());

        //final String add1 = TestTex.getText().toString();

        Intent pushtoLoad = new Intent(getApplicationContext(), MapView.class); // change second param to loading
        pushtoLoad.putExtra("Lat 1", Add1Cord.latitude);
        pushtoLoad.putExtra("Lng 1", Add1Cord.longitude);
        pushtoLoad.putExtra("Lat 2", Add2Cord.latitude);
        pushtoLoad.putExtra("Lng 2", Add2Cord.longitude);
        pushtoLoad.putExtra("Lat M", AddMCord.latitude);
        pushtoLoad.putExtra("Lng M", AddMCord.longitude);

        startActivity(pushtoLoad);



        //TODO Make the Lat Lng into individual longs to send info over Also decide when to use geo vs midpt.TM .

    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
            //* 1E6
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p1;
    }

    LatLng GeoMidpoint(LatLng LocA, LatLng LocB) {

        double lat1 = LocA.latitude * Math.PI / 180;
        double lat2 = LocB.latitude * Math.PI / 180;

        double lon1 = LocA.longitude * Math.PI / 180;
        double lon2 = LocB.longitude * Math.PI / 180;

        double dlon = lon2 -lon1;

        double x = Math.cos(lat2) * Math.cos(dlon);
        double y = Math.cos(lat2) * Math.sin(dlon);

        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2),
                Math.sqrt((Math.cos(lat1) + x) * (Math.cos(lat1) + x) + (y * y)));
        double lon3 = lon1 + Math.atan2(y, Math.cos(lat1) + x);

        LatLng returner = new LatLng (lat3 * 180 / Math.PI,lon3 * 180 / Math.PI);

        return returner;
    }

}

