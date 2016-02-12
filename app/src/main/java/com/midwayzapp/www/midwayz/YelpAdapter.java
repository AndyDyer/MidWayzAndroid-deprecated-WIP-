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
import android.content.Intent;
import com.squareup.picasso.Picasso;
import android.net.Uri;
import android.app.Activity;
/**
 * Created by andrew on 2/5/16.
 */
public class YelpAdapter extends ArrayAdapter<YelpBusiness>
{
    Context context = getContext();
    public String TAG = "YelpAdapter";
    public YelpAdapter(Context context, ArrayList<YelpBusiness> users) {
        super(context, 0, users);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final YelpBusiness yelpbiz = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.yelp_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvCat = (TextView) convertView.findViewById(R.id.tvCat);
        TextView tvReviews = (TextView) convertView.findViewById(R.id.tvReviews);
        ImageView ivMain = (ImageView) convertView.findViewById(R.id.ivMainPhoto);
        ImageView ivReview = (ImageView) convertView.findViewById(R.id.ivReviewPhoto);
/*
        ivMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               YelpResults.openURL(context,yelpbiz.getMobileurl());
            }
        });

        tvReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YelpResults.openURL(context,yelpbiz.getMobileurl());
            }
        });
*/
        //tvAddress.setText(yelpbiz.getAddress().substring(2, yelpbiz.getAddress().length() - 2));
        // Populate the data into the template view using the object's data
        tvName.setText(yelpbiz.getTitle());
        tvCat.setText(yelpbiz.getCategory().substring(3, yelpbiz.getCategory().indexOf(",")-1));

        tvReviews.setText(yelpbiz.getRating() + " reviews");

        Picasso.with(context)
                .load(yelpbiz.getThumbnailUrl().substring(0,yelpbiz.getThumbnailUrl().length()-6) +"o.jpg")
                .fit()
                .into(ivMain);
        Picasso.with(context)
                .load(yelpbiz.getRatingpic())
                .into(ivReview);

        return convertView;

    }


}