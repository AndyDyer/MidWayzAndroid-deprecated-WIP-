package com.midwayzapp.www.midwayz;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;


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




public class Address extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {



    private static final String TAG = Address.class.getSimpleName();
    EditText Address1;
    EditText Address2;
    Button toLoad;



    protected GoogleApiClient mGoogleApiClient;

    private PlaceAutocompleteAdapter mAdapter;

    private AutoCompleteTextView mAutocompleteView1;
    private AutoCompleteTextView mAutocompleteView2;



    private static final LatLngBounds BOUNDS_CONT_US= new LatLngBounds(
             new LatLng(23.362429, -72.421875), new LatLng(47.129951, -127.265625));










    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        toLoad = (Button) findViewById(R.id.GoButton);





        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this) //TODO Check this clientID
                .addApi(Places.GEO_DATA_API)
                .build();


        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        mAutocompleteView1 = (AutoCompleteTextView)
                findViewById(R.id.Add1);
        mAutocompleteView2 = (AutoCompleteTextView)
                findViewById(R.id.Add2);

        // Register a listener that receives callbacks when a suggestion has been selected
        mAutocompleteView1.setOnItemClickListener(mAutocompleteClickListener);
        mAutocompleteView2.setOnItemClickListener(mAutocompleteClickListener);  //TODO added



        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_CONT_US,
                null);
        mAutocompleteView1.setAdapter(mAdapter);
        mAutocompleteView2.setAdapter(mAdapter);



        toLoad.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

               // Address1 = (AutoCompleteTextView) findViewById(R.id.Add1);
                final String add1 = mAutocompleteView1.getText().toString();
               // Address2 = (AutoCompleteTextView) findViewById(R.id.Add2);
                final String add2 = mAutocompleteView2.getText().toString();

                Intent pushtoLoad = new Intent(getApplicationContext(), LoadingScreen.class); // change second param to loading
                pushtoLoad.putExtra("Address 1", add1);
                pushtoLoad.putExtra("Address 2", add2);
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

            Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);
/*
            // Format details of the place for display and show it in a TextView.
            mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
                    place.getId(), place.getAddress(), place.getPhoneNumber(),
                    place.getWebsiteUri()));

            // Display the third party attributions if set.
            final CharSequence thirdPartyAttribution = places.getAttributions();
            if (thirdPartyAttribution == null) {
                mPlaceDetailsAttribution.setVisibility(View.GONE);
            } else {
                mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
                mPlaceDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
            }

*/
            Log.i(TAG, "Place details received: " + place.getName());

            places.release();
        }
    };
    /*
    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }
    */
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

}

