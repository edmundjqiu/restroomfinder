package com.edmundjqiu.data;

/**
 * @author Brian Yang
 */
public class Restroom {

    private String name;
    private double longitude;
    private double latitude;
    private int free;
    private RestroomReview[] reviews;

    public Restroom(String name, double longitude, double latitude, int free, RestroomReview[] reviews) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.free = free;
        this.reviews = reviews;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public RestroomReview[] getReviews() {
        return reviews;
    }

    public void setReviews(RestroomReview[] reviews) {
        this.reviews = reviews;
    }

}
