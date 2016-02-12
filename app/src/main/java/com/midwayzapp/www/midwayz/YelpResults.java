package com.midwayzapp.www.midwayz;


import android.os.AsyncTask;
import android.os.Bundle;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import android.os.AsyncTask.Status;
import java.util.List;

import android.util.Log;
import android.app.Activity;
import android.net.Uri;
import android.content.Intent;
import android.content.Context;


public class YelpResults extends Activity {

    private static final String TAG = YelpResults.class.getSimpleName();
    private static final String url = null;


    private ArrayList<YelpBusiness> yelpList = new ArrayList<YelpBusiness>();
    public String MidAddress;



    public YelpAdapter adapter;
    public  ArrayList<YelpBusiness> arrayOfbiz;
    public ListView listView;


    //to clear adapter.clear();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yelp_results);

        MidAddress = getIntent().getStringExtra("MAddress");

/*
        // Construct the data source
        ArrayList<YelpBusiness> arrayOfbiz = new ArrayList<YelpBusiness>();
        // Create the adapter to convert the array to views
        YelpAdapter adapter = new YelpAdapter(this, arrayOfbiz);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.yelpListView);
        listView.setAdapter(adapter);
*/

        arrayOfbiz = new ArrayList<YelpBusiness>();
        adapter = new YelpAdapter(this,arrayOfbiz);
        listView = (ListView) findViewById(R.id.yelpListView);
        listView.setAdapter(adapter);

        AsyncTask task = new YelpASync().execute();

      if (task.getStatus() == AsyncTask.Status.FINISHED )
      {
          adapter.addAll(yelpList);
      }



        //TODO BITMAP

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
            yelp.setRatingpic(objectInArray.getString("rating_img_url_small")); // _small
            yelp.setMobileurl(objectInArray.getString("mobile_url"));

            Log.v(TAG, " isAddedPhone: " + yelp.getPhonenumber());
            Log.v(TAG, " isAddedThumb: " + yelp.getThumbnailUrl());
            Log.v(TAG, " isAddedRating: " + yelp.getRatingpic());

            //JSONObject catObj = new JSONObject((objectInArray.getString("categories")));
            yelp.setCategory(objectInArray.getString("categories"));

            Log.v(TAG, " CATEGORIES CATEGORIES: " + yelp.getCategory());

            //JSONArray locArray = objectInArray.getJSONArray("location");
            JSONObject locObj = new JSONObject(objectInArray.getString("location"));

            yelp.setAddress(locObj.getString("address"));
            Log.v(TAG, " isAddedAddress: " + yelp.getAddress());

            JSONObject cordObj = new JSONObject(locObj.getString("coordinate"));

            yelp.setLatLng(cordObj.getDouble("latitude"), cordObj.getDouble("longitude"));
            Log.v(TAG, " isLatLng: " + yelp.getLatLng());


            yelpList.add(yelp);

        }

        adapter.addAll(yelpList);

    }




    public class YelpASync extends AsyncTask<Void, Void, String> {

        String consumerKey = "QKodnM4cvgney-eyscrl6g";
        String consumerSecret = "voK2W34WBrqReDvd8PZ_9WWFZ-k";
        String token = "hONXwy3QeD-Fd-uWQY0cuMeJOvTRTZZ5";
        String tokenSecret = "t-wyckuw6OvUktL8XAyPA4zckT4";


        final Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);

        protected String doInBackground(Void... params) {
           // String response = yelp.searchForBusinessesByLocation("restaurant", "cll=" + "42.3600,-71.0568"); //
            String response = yelp.searchForBusinessesByLocation("restaurant", MidAddress); //TODO Take LatLng and ReverseGeocode lol fuck me.
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
    public void TestPopulation()
    {
        for (int i = 0; i < 10; i++) {
            YelpBusiness yelp = new YelpBusiness();
            yelp.setTitle("Name" + i);
            yelp.setPhonenumber("949" + i);
            yelp.setAddress(i + "Lane");
            yelpList.add(yelp);
        }
    }
    public static void openURL(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

}

