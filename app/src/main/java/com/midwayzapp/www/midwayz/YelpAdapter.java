package com.midwayzapp.www.midwayz;

import android.widget.ArrayAdapter;
import android.content.Context;
import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.widget.ImageView;
/**
 * Created by andrew on 2/5/16.
 */
public class YelpAdapter extends ArrayAdapter<YelpBusiness>
{
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
        ImageView ivMain = (ImageView) convertView.findViewById(R.id.ivMainPhoto);
        ImageView ivReview = (ImageView) convertView.findViewById(R.id.ivReviewPhoto);

        // Populate the data into the template view using the data object
        tvName.setText(yelpbiz.getTitle());
        //tvAddress.setText(yelpbiz.getAddress());
        //tvReviews.setText(yelpbiz.getRating());
        //ivMain.setImageBitmap(yelpbiz.GETBITMAP); http://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
        //ivMain.setReview(yelp.biz.getREVIEW);


        // Return the completed view to render on screen
        return convertView;

    }
}
