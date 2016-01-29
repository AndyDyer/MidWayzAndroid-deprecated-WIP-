package com.midwayzapp.www.midwayz;

/**
 * Created by andrew on 1/20/16.
 */
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class YelpBusiness {
    private String title, thumbnailUrl, phonenumber, address, ratingpic;
    private double rating;
    private ArrayList<String> category;
    private LatLng LatLong;

    public YelpBusiness() {
    }

    public YelpBusiness(String name, String thumbnailUrl, String phonenumber, String address, String reviews, String ratingpic,  double rating, LatLng Latlong,
                 ArrayList<String> category) {
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.phonenumber =phonenumber;
        this.address = address;
        this.ratingpic = ratingpic;
        this.rating = rating;
        this.category = category;
        this.LatLong = Latlong;
    }

    public String getLatLng() {
        return LatLong.toString();
    }
    public void setLatLng(double lat, double lng)
    {
        this.LatLong = new LatLng(lat,lng);
    }
    public String getRatingpic()
    {
        return ratingpic;
    }
    public void setRatingpic(String url)
    {
        this.ratingpic = url;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String name) {
        this.title = name;
    }
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
    public String getPhonenumber() {
        return phonenumber;
    }
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public ArrayList<String> getCategory() {
        return category;
    }
    public void setGenre(ArrayList<String> setCategory) {
        this.category = category;
    }

}