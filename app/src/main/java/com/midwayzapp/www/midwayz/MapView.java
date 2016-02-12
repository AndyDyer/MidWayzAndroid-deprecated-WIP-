package com.midwayzapp.www.midwayz;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import android.content.Intent;
import android.location.*;
import android.location.Address;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapView extends FragmentActivity implements OnMapReadyCallback,OnMarkerDragListener {

    private GoogleMap mMap;
    private GoogleMap mMap2;
    public String TAG = "MAPVIEW";

   public LatLng PointA;
    public LatLng PointB;
    public LatLng MidPoint;
    public String MidAddress;
    Button toLoad;
    public OnMarkerDragListener dragListen;
    public AsyncTask Steve;

    private ArrayList<YelpBusiness> yelpList = new ArrayList<YelpBusiness>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        toLoad = (Button) findViewById(R.id.buttonList);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




        //Intent pushtoLoad = new Intent(getApplicationContext(), YelpResults.class); // change second param to loading

       // AsyncTask task = new YelpASync().execute();

        //startActivity(pushtoLoad);


        toLoad.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {


                Intent pushtoLoad = new Intent(getApplicationContext(), YelpResults.class); // change second param to loading
                pushtoLoad.putExtra("MAddress", MidAddress);

                startActivity(pushtoLoad);

            }
        });



    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap2 = googleMap;
        MidAddress = getIntent().getStringExtra("MAddress");
        PointA = new LatLng((getIntent().getDoubleExtra("Lat 1", 1)), getIntent().getDoubleExtra("Lng 1", 1));
        PointB = new LatLng((getIntent().getDoubleExtra("Lat 2", 1)), getIntent().getDoubleExtra("Lng 2", 1));
        MidPoint = new LatLng((getIntent().getDoubleExtra("Lat M", 1)), getIntent().getDoubleExtra("Lng M", 1));



        mMap.moveCamera(CameraUpdateFactory.newLatLng(MidPoint));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
        // TODO Change to be relative to mid points.
        // TODO Make so point and A and B cannot be cleared see WILLS solution


        mMap.setOnMarkerDragListener(this);

       new YelpASync().execute();


    }
    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        MidPoint = marker.getPosition();
        MidAddress = getAddressFromLocation(MidPoint);
        mMap.clear();
        yelpList.clear();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(MidPoint));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
        new YelpASync().execute();
    }
    @Override
    public void onMarkerDrag(Marker marker) {

    }


    public void PlaceMarkers(ArrayList<YelpBusiness> mylisty) {
        mMap2.addMarker(new MarkerOptions().position(PointA).title("Point A"));
        mMap2.addMarker(new MarkerOptions().position(PointB).title("Point B"));
        mMap2.addMarker(new MarkerOptions().position(MidPoint).title("MidPoint").draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

/*  //TODO check will's to see how he did that.
        //TODO Possible option for removing markers to maintain smoothness and allow .remove as opposed to clearing principal A and B markers
        // but fuck me is this convoluted.
        Marker marker1 = mMap.addMarker(new MarkerOptions().position(yelpList.get(yelpList.size()-(yelpList.size()-1)).getLatLngT()).title(yelpList.get(52).getTitle()));
       */

        /*
        for(int i = 0; i < mylisty.size();i++)
        {
         mMap.addMarker(new MarkerOptions().position(yelpList.get(i).getLatLngT()).title(yelpList.get(i).getTitle()));

        }*/
    }


    public void processJson(String jsonStuff) throws JSONException {


        JSONObject firstObject = new JSONObject(jsonStuff);
        JSONArray jsonArray = firstObject.getJSONArray("businesses");

        for (int i = 0; i < jsonArray.length(); i++)
        {

            JSONObject objectInArray = jsonArray.getJSONObject(i);
            YelpBusiness yelp = new YelpBusiness();

            yelp.setTitle(objectInArray.getString("name"));
            yelp.setRating(objectInArray.getString("review_count"));

            Log.v(TAG, " PROCESSJSON: Business Name: " + objectInArray.getString("name"));
            Log.v(TAG, " isAddedTitle: " + yelp.getTitle());
            Log.v(TAG, " isAddedRev: " + yelp.getRating());


            yelp.setPhonenumber(objectInArray.getString("display_phone"));
            yelp.setThumbnailUrl(objectInArray.getString("image_url"));
            yelp.setRatingpic(objectInArray.getString("rating_img_url_small"));

            Log.v(TAG, " isAddedPhone: " + yelp.getPhonenumber());
            Log.v(TAG, " isAddedThumb: " + yelp.getThumbnailUrl());
            Log.v(TAG, " isAddedRating: " + yelp.getRatingpic());



            //JSONArray locArray = objectInArray.getJSONArray("location");
            JSONObject locObj = new JSONObject(objectInArray.getString("location"));

            yelp.setAddress(locObj.getString("address"));
            Log.v(TAG, " isAddedAddress: " + yelp.getAddress());

            JSONObject cordObj = new JSONObject(locObj.getString("coordinate"));

            yelp.setLatLng(cordObj.getDouble("latitude"), cordObj.getDouble("longitude"));
            Log.v(TAG, " isLatLng: " + yelp.getLatLng());


            yelpList.add(yelp);

        }

       PlaceMarkers(yelpList);

    }




    public class YelpASync extends AsyncTask<Void, Void, String> {


        String consumerKey = "QKodnM4cvgney-eyscrl6g";
        String consumerSecret = "voK2W34WBrqReDvd8PZ_9WWFZ-k";
        String token = "hONXwy3QeD-Fd-uWQY0cuMeJOvTRTZZ5";
        String tokenSecret = "t-wyckuw6OvUktL8XAyPA4zckT4";


        final Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);

        protected String doInBackground(Void... params) {
            // String response = yelp.searchForBusinessesByLocation("restaurant", "cll=" + "42.3600,-71.0568"); //
            String response = yelp.searchForBusinessesByLocation("restaurant", MidAddress); 

            return response;
        }

        protected void onProgressUpdate(Integer... progress) {
            //Null
        }

        protected void onPreExecute()
        {

        }
        protected void onPostExecute(String result) {
            try {
                processJson(result);
            } catch (JSONException e) {
                Log.v(TAG, "Failed to Parse Response");

            }



        }


    }

    public String getAddressFromLocation(LatLng myLatLng) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        String p1 = null;

        try {
            address = coder.getFromLocation(myLatLng.latitude, myLatLng.longitude, 1);
            if (address == null) {
                return null;
            }

            String myaddress = address.get(0).getAddressLine(0) + address.get(0).getLocality() + "," + address.get(0).getAdminArea();
            p1 = myaddress;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return p1;
    }



}
