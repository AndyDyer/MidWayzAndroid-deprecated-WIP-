package com.midwayzapp.www.midwayz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.maps.model.LatLng;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.InputStream;
import java.net.URLConnection;
import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.lang.StringBuilder;



public class LoadingScreen extends AppCompatActivity {
//http://javapapers.com/android/android-geocoding-to-get-latitude-longitude-for-an-address/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        LatLng Add1Cord = null;
        LatLng Add2Cord = null;

        String loadadd1 = getIntent().getStringExtra("Address 1");
        String loadadd2 = getIntent().getStringExtra("Address 2");

        Add1Cord = getLatLongFromAddress(loadadd1);
        Add2Cord = getLatLongFromAddress(loadadd2);

        TextView Tester = (TextView)
                findViewById(R.id.TestText);
        Tester.setText(Add1Cord.toString());

    }


    //TODO ASync Task

    public static LatLng getLatLongFromAddress(String youraddress)
    {

        HttpURLConnection connection;
        String uri = "http://maps.google.com/maps/api/geocode/json?address=" + youraddress + "&sensor=false" ;
        LatLng returner = null;
        try {
            URL url = new URL(uri); //TODO
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            StringBuilder stringBuilder = new StringBuilder();


            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(stringBuilder.toString());

                double lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng");

                double lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat");
                returner = new LatLng(lat, lng);

            }
            catch (JSONException e)
            {
                e.printStackTrace();

            }
        }



        catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
        catch (IOException e)
            {
                e.printStackTrace();
            }



    return returner;
    }

    LatLng Midpoint(String address1, String address2 )
    {


        return null;
    }

}
