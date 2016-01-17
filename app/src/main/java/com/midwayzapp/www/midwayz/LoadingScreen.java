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
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import android.widget.Toast;
import android.app.ListActivity;

 public class LoadingScreen extends AppCompatActivity  {
    //http://javapapers.com/android/android-geocoding-to-get-latitude-longitude-for-an-address/
    private RequestQueue mRequestQueue;
     LatLng Add1Cord = null;
     LatLng Add2Cord = null;
     LatLng AddMCord = null;
     ArrayList<String> values;
     ArrayAdapter<String> adapter;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
         values = new ArrayList<String>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        String loadadd1 = getIntent().getStringExtra("Address 1");
        String loadadd2 = getIntent().getStringExtra("Address 2");

        Add1Cord = getLocationFromAddress(loadadd1);
        Add2Cord = getLocationFromAddress(loadadd2);


        TripTime(Add1Cord,Add2Cord, 1);


        //TODO Decide geo vs Midpt

        AddMCord = GeoMidpoint(Add1Cord, Add2Cord);



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

     public RequestQueue getReqQueue() {
         if (mRequestQueue == null) {
             mRequestQueue = Volley.newRequestQueue(getApplicationContext());
         }

         return mRequestQueue;
     }

     public <T> void addToReqQueue(Request<T> req, String tag) {

         getReqQueue().add(req);
     }

     public <T> void addToReqQueue(Request<T> req) {

         getReqQueue().add(req);
     }

     public void cancelPendingReq(Object tag) {
         if (mRequestQueue != null) {
             mRequestQueue.cancelAll(tag);
         }
     }

    private void TripTime (LatLng Start, LatLng Finish, int Mode)
    {
    //JSON Request
    String StartLoc = Start.toString();
        String FinishLoc = Finish.toString();
        String gmapskey = getString(R.string.google_maps_key);
//https://maps.googleapis.com/maps/api/directions/json?origin=STARTLOCAT%20&destination=ENDLOCAT&mode=MODE&avoid=highways&key=GOOGLE KEY

        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + StartLoc +
                "%20&destination="+ FinishLoc +
                "&mode=MODE&avoid=tolls&key=" + gmapskey;


        JsonArrayRequest jreq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jo = response.getJSONObject(i);
                                String name = jo.getString("text");
                                values.add(name);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

    }


}

