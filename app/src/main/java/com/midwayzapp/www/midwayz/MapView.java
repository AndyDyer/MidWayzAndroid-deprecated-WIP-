package com.midwayzapp.www.midwayz;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapView extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




        Intent pushtoLoad = new Intent(getApplicationContext(), YelpResults.class); // change second param to loading

        startActivity(pushtoLoad);


       // String loadadd1 = getIntent().getStringExtra("Address 1");
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
        LatLng PointA = null;
        LatLng PointB = null;
        LatLng MidPoint = null;

        PointA = new LatLng((getIntent().getDoubleExtra("Lat 1", 1)), getIntent().getDoubleExtra("Lng 1", 1));
        PointB = new LatLng((getIntent().getDoubleExtra("Lat 2", 1)), getIntent().getDoubleExtra("Lng 2", 1));
        MidPoint = new LatLng((getIntent().getDoubleExtra("Lat M", 1)), getIntent().getDoubleExtra("Lng M", 1));

        mMap.addMarker(new MarkerOptions().position(PointA).title("Point A"));
        mMap.addMarker(new MarkerOptions().position(PointB).title("Point B"));
        mMap.addMarker(new MarkerOptions().position(MidPoint).title("MidPoint").draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(MidPoint));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10)); // TODO Change to be relative to mid points.

        //

        // TODO http://developer.android.com/training/volley/request.html need to use this to establish distances in terms of time
    }
}
