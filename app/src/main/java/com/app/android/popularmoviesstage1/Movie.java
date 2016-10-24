package com.app.android.popularmoviesstage1;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private String mTitle;
    private String mThumbnail;
    private String mOverview;
    private double mRating;
    private String mDate;

    public Movie(String mTitle, String mThumbnail, String mOverview, double mRating, String mDate) {
        this.mTitle = mTitle;
        this.mThumbnail = mThumbnail;
        this.mOverview = mOverview;
        this.mRating = mRating;
        this.mDate = mDate;
    }

    protected Movie(Parcel in) {
        mTitle = in.readString();
        mThumbnail = in.readString();
        mOverview = in.readString();
        mRating = in.readDouble();
        mDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return mTitle;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public String getOverview() {
        return mOverview;
    }

    public double getRating() {
        return mRating;
    }

    public String getDate() {
        return mDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mThumbnail);
        parcel.writeString(mOverview);
        parcel.writeDouble(mRating);
        parcel.writeString(mDate);
    }
}
