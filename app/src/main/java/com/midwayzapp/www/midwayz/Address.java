package com.midwayzapp.www.midwayz;


import android.content.Intent;

import android.location.Geocoder;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.style.MetricAffectingSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import android.location.LocationManager;
import android.content.Context;

import android.location.Location;


import java.io.IOException;
import java.util.List;


// TODO(Developer): Add pick 1 mode of transport Boxes
// TODO the midway... lol and what decides that
// TODO force 2nd location to be more feasible.
//TODO : add current location option
// TODO DELETE ADDRESS BUTTON
// TODO left adjust address text
//TODO blue transparency http://stackoverflow.com/questions/8193447/i-want-to-add-a-color-filter-to-the-imageview
public class Address extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {


    private static final String TAG = Address.class.getSimpleName();
    EditText Address1;
    EditText Address2;
    ImageView toLoad;
    ImageView currentLocat;
    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    private AutoCompleteTextView mAutocompleteView1;
    private AutoCompleteTextView mAutocompleteView2;
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds( //(25.430873, -128.803711) // (51.800123, -60.512695)
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
    private static final LatLngBounds BOUNDS_CONT_US = new LatLngBounds(
            new LatLng(25.430873, -128.803711),new LatLng(51.800123, -60.512695)); //TODO in future base off current locat
    public Context context;
    public Location location;
    public LatLng Add1Cord, Add2Cord, AddMCord;
    public String MAddress;
    LatLng currentlatlng;
    String currentadd;
    protected LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        getSupportActionBar().hide();
        toLoad = (ImageView) findViewById(R.id.GoButton);
        currentLocat = (ImageView) findViewById(R.id.ivCurrentLocat);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();


        //mAdapter.setBounds(BOUNDS_CONT_US);
        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        mAutocompleteView1 = (AutoCompleteTextView)
                findViewById(R.id.Add1);
        mAutocompleteView2 = (AutoCompleteTextView)
                findViewById(R.id.Add2);

        // Register a listener that receives callbacks when a suggestion has been selected
        mAutocompleteView1.setOnItemClickListener(mAutocompleteClickListener);
        mAutocompleteView2.setOnItemClickListener(mAutocompleteClickListener);



        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_CONT_US,
                null);
        mAutocompleteView1.setAdapter(mAdapter);
        mAutocompleteView2.setAdapter(mAdapter);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        currentLocat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
              //TODO CURRENT LOCATION ASYNC
            }
        });
        toLoad.setOnClickListener(new View.OnClickListener()



        {
            @Override
            public void onClick(View v) {

               // Address1 = (AutoCompleteTextView) findViewById(R.id.Add1);
                final String add1 = mAutocompleteView1.getText().toString();
               // Address2 = (AutoCompleteTextView) findViewById(R.id.Add2);
                final String add2 = mAutocompleteView2.getText().toString();

                //Variable prep
                Add1Cord = getLocationFromAddress(add1);
                Add2Cord = getLocationFromAddress(add2);
                AddMCord = GeoMidpoint(Add1Cord, Add2Cord);
                MAddress = getAddressFromLocation(AddMCord);

                Intent pushtoLoad = new Intent(getApplicationContext(), MapView.class); // change second param to loading


                pushtoLoad.putExtra("Lat 1", Add1Cord.latitude);
                pushtoLoad.putExtra("Lng 1", Add1Cord.longitude);
                pushtoLoad.putExtra("Lat 2", Add2Cord.latitude);
                pushtoLoad.putExtra("Lng 2", Add2Cord.longitude);
                pushtoLoad.putExtra("Lat M", AddMCord.latitude);
                pushtoLoad.putExtra("Lng M", AddMCord.longitude);
                pushtoLoad.putExtra("MAddress", MAddress);
                startActivity(pushtoLoad);

            }
        });

    }
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {

                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }

            final Place place = places.get(0);

            Log.i(TAG, "Place details received: " + place.getName());

            places.release();
        }
    };

    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());


        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }



    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<android.location.Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            android.location.Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
            //* 1E6
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p1;
    }

    public String getAddressFromLocation(LatLng myLatLng) {

        Geocoder coder = new Geocoder(this);
        List<android.location.Address> address;
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


    public LatLng GeoMidpoint(LatLng LocA, LatLng LocB) {

        double lat1 = LocA.latitude * Math.PI / 180;
        double lat2 = LocB.latitude * Math.PI / 180;

        double lon1 = LocA.longitude * Math.PI / 180;
        double lon2 = LocB.longitude * Math.PI / 180;

        double dlon = lon2 - lon1;

        double x = Math.cos(lat2) * Math.cos(dlon);
        double y = Math.cos(lat2) * Math.sin(dlon);

        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2),
                Math.sqrt((Math.cos(lat1) + x) * (Math.cos(lat1) + x) + (y * y)));
        double lon3 = lon1 + Math.atan2(y, Math.cos(lat1) + x);

        LatLng returner = new LatLng(lat3 * 180 / Math.PI, lon3 * 180 / Math.PI);

        return returner;
    }


    public void TripTime(LatLng Start, LatLng Finish, int Mode) {

        final TextView TestTV = (TextView) findViewById(R.id.TestText);
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


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }
}

