package com.midwayzapp.www.midwayz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.model.LatLng;
import android.widget.TextView;
import java.io.IOException;
import android.location.Geocoder;
import java.util.List;
import android.location.Address;
import java.lang.Math;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import com.android.volley.toolbox.Volley;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;


 public class LoadingScreen extends AppCompatActivity  {
    //http://javapapers.com/android/android-geocoding-to-get-latitude-longitude-for-an-address/

     LatLng Add1Cord = null;
     LatLng Add2Cord = null;
     LatLng AddMCord = null;


/*
     //OBJECT
     public LatLng cord;
     public int Time;
     public String Name;

     public LoadingScreen(String namer, int timer, LatLng corder)
     {
         cord = corder;
         Time = timer;
         Name = namer;
     }
  */
     private static final String TAG = "JSONRequester";

     @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        String loadadd1 = getIntent().getStringExtra("Address 1");
        String loadadd2 = getIntent().getStringExtra("Address 2");

        Add1Cord = getLocationFromAddress(loadadd1);
        Add2Cord = getLocationFromAddress(loadadd2);
/*
        LoadingScreen Cord1 = new LoadingScreen(0,Add1Cord,"Cord1");
        LoadingScreen Cord2 = new LoadingScreen(0,Add1Cord,"Cord2");
*/

         TripTime(Add1Cord,Add2Cord,3);




        //TODO Decide geo vs Midpt
        AddMCord = GeoMidpoint(Add1Cord, Add2Cord);



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

    public LatLng GeoMidpoint(LatLng LocA, LatLng LocB) {

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


//TODO Add in modeality
    public void TripTime (LatLng Start, LatLng Finish, int Mode) {

        final TextView TestTV = (TextView)findViewById(R.id.TestText);
        //JSON Request

        double StartLat = Start.latitude;
        double StartLng = Start.longitude;

        double FinishLat = Finish.latitude;
        double FinishLng = Finish.longitude;

        String StartLoc = Start.toString();
        String FinishLoc = Finish.toString();

        String gmapskey = getString(R.string.google_maps_key);
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + StartLat + "," + StartLng +
                "%20&destination=" + FinishLat + "," + FinishLng +
                "&mode=MODE&avoid=tolls&key=" + gmapskey;


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        TestTV.setText("Response is: " + response);
                        //TODO Add to a
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                TestTV.setText("That didn't work!");
            }
                });
       queue.add(stringRequest);
    }
/*
 JSONObject jsonObject = new JSONObject(response);
             JSONArray routesArray = jsonObject.getJSONArray("routes");
             JSONObject route = routesArray.getJSONObject(0);
             JSONArray legs = route.getJSONArray("legs");
             JSONObject leg = legs.getJSONObject(0);
             JSONObject durationObject = leg.getJSONObject("duration");
             duration = durationObject.getString("text");
 */

}

