package com.midwayzapp.www.midwayz;


import android.widget.ArrayAdapter;
import android.content.Context;
import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

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

        //Hidden Stuff
        final ImageView buttonShare = (ImageView) convertView.findViewById(R.id.buttonShare);
        final TextView tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
        final TextView tvPointA = (TextView) convertView.findViewById(R.id.tvPointA);
        final TextView tvPointB = (TextView) convertView.findViewById(R.id.tvPointB);

        buttonShare.setVisibility(View.GONE);
        tvAddress.setVisibility(View.GONE);
        tvPointA.setVisibility(View.GONE);
        tvPointB.setVisibility(View.GONE);

        tvAddress.setText(yelpbiz.getAddress());
        /*
        tvPointA.setText();
        tvPointB.setText("FIX ME mins" + "from point B.");
        */
        ivMain.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if (buttonShare.getVisibility() == View.VISIBLE)
                {
                    buttonShare.setVisibility(View.GONE);
                    tvAddress.setVisibility(View.GONE);
                    tvPointA.setVisibility(View.GONE);
                    tvPointB.setVisibility(View.GONE);
                }
                else
                {
                    buttonShare.setVisibility(View.VISIBLE);
                    tvAddress.setVisibility(View.VISIBLE);
                    tvPointA.setVisibility(View.VISIBLE);
                    tvPointB.setVisibility(View.VISIBLE);
                }

            }
        });

        buttonShare.setOnClickListener(new View.OnClickListener(){
            @Override
             public void onClick(View v){


                String shareput = "Let's meet at " + yelpbiz.getTitle() +
                        "\n" + yelpbiz.getAddress() + "\n \n \n"
                        + yelpbiz.getMobileurl();

               YelpResults.ShareText(context, shareput);

            }
        });

        ivReview.setOnClickListener(new View.OnClickListener() {
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

        //
        // Populate the data into the template view using the object's data
        tvName.setText(yelpbiz.getTitle());
        if (yelpbiz.getCategory()!= null)
        {
            tvCat.setText(yelpbiz.getCategory().substring(3, yelpbiz.getCategory().indexOf(",") - 1));
        }


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
