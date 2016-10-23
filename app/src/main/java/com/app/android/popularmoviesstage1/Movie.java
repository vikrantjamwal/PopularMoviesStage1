package com.app.android.popularmoviesstage1;

public class Movie {

    private String mTitle;
    private String mThumbnail;
    private String mOverview;
    private int mRating;
    private String mDate;

    public Movie(String mTitle, String mThumbnail, String mOverview, int mRating, String mDate) {
        this.mTitle = mTitle;
        this.mThumbnail = mThumbnail;
        this.mOverview = mOverview;
        this.mRating = mRating;
        this.mDate = mDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public String getOverview() {
        return mOverview;
    }

    public int getRating() {
        return mRating;
    }

    public String getDate() {
        return mDate;
    }
}
