package com.edmundjqiu.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Brian on 9/21/2014.
 */
public class RestroomReview implements Serializable {
    private String reviewer;
    private String date;
    private int rating;
    private String content;

    public RestroomReview(String reviewer, String date, int rating, String content) {
        this.reviewer = reviewer;
        this.date = date;
        this.rating = rating;
        this.content = content;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
