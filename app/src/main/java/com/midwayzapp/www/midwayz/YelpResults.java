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



public class YelpResults extends Activity {

    private static final String TAG = YelpResults.class.getSimpleName();
    private static final String url = null;

    // private ProgressDialog pDialog;
    private ArrayList<YelpBusiness> yelpList = new ArrayList<YelpBusiness>();



    public YelpAdapter adapter;
    public  ArrayList<YelpBusiness> arrayOfbiz;
    public ListView listView;


    //to clear adapter.clear();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yelp_results);


        // Construct the data source
        ArrayList<YelpBusiness> arrayOfbiz = new ArrayList<YelpBusiness>();
        // Create the adapter to convert the array to views
        YelpAdapter adapter = new YelpAdapter(this, arrayOfbiz);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.yelpListView);
        listView.setAdapter(adapter);

        AsyncTask task = new YelpASync().execute();

      if (task.getStatus() == AsyncTask.Status.FINISHED )
      {
          adapter.addAll(yelpList);
      }



        //TODO draw yelplist to  List adapter. https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView

    }
    /*
     this.title = name;
        this.hours = hours;
        this.thumbnailUrl = thumbnailUrl;
        this.phonenumber =phonenumber;
        this.address = address;
        this.ratingpic = ratingpic;
        this.rating = rating;
        this.category = category;
        this.LatLong = Latlong;
     */


    public void processJson(String jsonStuff) throws JSONException {

        JSONObject firstObject = new JSONObject(jsonStuff);
        JSONArray jsonArray = firstObject.getJSONArray("businesses");

        for (int i = 0; i < jsonArray.length(); i++)
        {

            JSONObject objectInArray = jsonArray.getJSONObject(i);



            YelpBusiness yelp = new YelpBusiness();
            yelp.setTitle(objectInArray.getString("name"));
          //  yelp.setRating(objectInArray.getString("review_count"));

            Log.v(TAG, " PROCESSJSON: Business Name: " + objectInArray.getString("name"));
            Log.v(TAG, " IsADDED: " + yelp.getTitle());

           // yelp.setThumbnailUrl(objectInArray.getString("image_url"));
           // yelp.setRatingpic(objectInArray.getString("rating_img_url_small"));
           // yelp.setPhonenumber(objectInArray.getString("display_phone"));

            // yelp.setRating(((Number) obj.get("rating")).doubleValue()); //TODO Change this to # of ratings. http://stackoverflow.com/questions/6697147/json-iterate-through-jsonarray
           //
            /*
            JSONObject structure = (JSONObject) obj.get("location");
            yelp.setLatLng(structure.getDouble("latitude"),structure.getDouble("longitude"));
            Log.v(TAG,"LATLNG:" + yelp.getLatLng().toString());
            */


            //TODO Lat and Lng + Genre Verification
            yelpList.add(yelp);

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




    public class YelpASync extends AsyncTask<Void, Void, String> {

        String consumerKey = "QKodnM4cvgney-eyscrl6g";
        String consumerSecret = "voK2W34WBrqReDvd8PZ_9WWFZ-k";
        String token = "hONXwy3QeD-Fd-uWQY0cuMeJOvTRTZZ5";
        String tokenSecret = "t-wyckuw6OvUktL8XAyPA4zckT4";


        final Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);

        protected String doInBackground(Void... params) {
           // String response = yelp.searchForBusinessesByLocation("restaurant", "cll=" + "42.3600,-71.0568"); //
            String response = yelp.searchForBusinessesByLocation("restaurant", "1 Faneuil Hall Sq, Boston, MA 02109"); //TODO Take LatLng and ReverseGeocode lol fuck me.
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

