package com.midwayzapp.www.midwayz;

import android.widget.ArrayAdapter;
import android.content.Context;
import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.graphics.Bitmap;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
/**
 * Created by andrew on 2/5/16.
 */
public class YelpAdapter extends ArrayAdapter<YelpBusiness>
{
    public String TAG = "YelpAdapter";
    public YelpAdapter(Context context, ArrayList<YelpBusiness> users) {
        super(context, 0, users);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        YelpBusiness yelpbiz = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.yelp_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
        TextView tvReviews = (TextView) convertView.findViewById(R.id.tvReviews);
        TextView tvPhone =  (TextView) convertView.findViewById(R.id.tvPhone);
        ImageView ivMain = (ImageView) convertView.findViewById(R.id.ivMainPhoto);
        ImageView ivReview = (ImageView) convertView.findViewById(R.id.ivReviewPhoto);

        // Populate the data into the template view using the data object
        tvName.setText(yelpbiz.getTitle());
        tvAddress.setText(yelpbiz.getAddress().substring(2,yelpbiz.getAddress().length()-2));
        tvReviews.setText("Reviews: "+yelpbiz.getRating());
        tvPhone.setText(yelpbiz.getPhonenumber());




        // Return the completed view to render on screen
        return convertView;

    }



}
