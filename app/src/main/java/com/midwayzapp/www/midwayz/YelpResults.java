package com.midwayzapp.www.midwayz;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.app.Activity;



public class YelpResults extends Activity {

    private static final String TAG = YelpResults.class.getSimpleName();

    // Movies json url
    private static final String url = null;

    // private ProgressDialog pDialog;
    private List<YelpBusiness> yelpList = new ArrayList<YelpBusiness>();
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yelp_results);

        new YelpASync().execute(null,null,null);

        //TODO draw yelplist to  List adapter.

    }


    public List<YelpBusiness> processJson(String jsonStuff) throws JSONException {
        JSONObject obj = new JSONObject(jsonStuff);
        JSONArray businesses = obj.getJSONArray("businesses");
        ArrayList<YelpBusiness> businessObjs = new ArrayList<YelpBusiness>(businesses.length());
        for (int i = 0; i < businesses.length(); i++) {
            YelpBusiness yelp = new YelpBusiness();
            yelp.setTitle(obj.getString("name"));
            Log.v(TAG, " PROCESSJSON: Business Name: " + obj.getString("name"));
            yelp.setThumbnailUrl( obj.getString("image_url"));
            yelp.setRating(obj.getDouble("rating"));
            yelp.setRatingpic(obj.getString("rating_img_url_small"));
            yelp.setRating(((Number) obj.get("rating")).doubleValue());
            yelp.setPhonenumber("display_phone");
            /*
            JSONObject structure = (JSONObject) obj.get("location");
            yelp.setLatLng(structure.getDouble("latitude"),structure.getDouble("longitude"));
            Log.v(TAG,"LATLNG:" + yelp.getLatLng().toString());
            */


            //TODO Lat and Lng + Genre Verification
            yelpList.add(yelp);

        }
        return businessObjs;
    }


    public class YelpASync extends AsyncTask<Void, Void, String> {

        String consumerKey = "QKodnM4cvgney-eyscrl6g";
        String consumerSecret = "voK2W34WBrqReDvd8PZ_9WWFZ-k";
        String token = "hONXwy3QeD-Fd-uWQY0cuMeJOvTRTZZ5";
        String tokenSecret = "t-wyckuw6OvUktL8XAyPA4zckT4";


        final Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);

        protected String doInBackground(Void... params) {
            String response = yelp.searchForBusinessesByLocation("restaurant", "cll=" + "37.7833,-122.4167"); //TODO Add in LATLNGTerms into  ASYNCcall
            Log.v(TAG, "Response:" + response);
            return response;
        }

        protected void onProgressUpdate(Integer... progress) {
            //Null
        }

        protected void onPostExecute(String result) {
            try {
                processJson(result);
            } catch (JSONException e) {
                Log.v(TAG, "Failed to Parse Response");
            }
        }


    }
}

