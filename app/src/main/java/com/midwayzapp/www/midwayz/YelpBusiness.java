package com.midwayzapp.www.midwayz;

/**
 * Created by andrew on 1/20/16.
 */
import java.util.ArrayList;

public class YelpBusiness {
    private String title, thumbnailUrl, phonenumber, address,  reviews;
    private double rating;
    private ArrayList<String> category;

    public YelpBusiness() {
    }

    public YelpBusiness(String name, String thumbnailUrl, String phonenumber, String address, String reviews, double rating,
                 ArrayList<String> category) {
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.phonenumber =phonenumber;
        this.address = address;
        this.reviews = reviews;
        this.rating = rating;
        this.category = category;

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

    public String getaddress() {
        return address;
    }

    public void setaddress(String address) {
        this.address = address;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public ArrayList<String> getGenre() {
        return category;
    }

    public void setGenre(ArrayList<String> category) {
        this.category = category;
    }

}