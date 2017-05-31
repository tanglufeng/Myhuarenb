package com.abcs.huaqiaobang.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhou on 2016/4/15.
 */
public class Rate implements Parcelable {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShorthandName() {
        return shorthandName;
    }

    public void setShorthandName(String shorthandName) {
        this.shorthandName = shorthandName;
    }

    public String getCountryUrl() {
        return countryUrl;
    }

    public void setCountryUrl(String countryUrl) {
        this.countryUrl = countryUrl;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    private String name;
    private String shorthandName;
    private String countryUrl;
    private String inPut = "";

    public String getInPut() {
        return inPut;
    }

    public void setInPut(String inPut) {
        this.inPut = inPut;
    }

    private double rate;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.shorthandName);
        dest.writeString(this.countryUrl);
        dest.writeDouble(this.rate);
    }

    public Rate() {
    }

    protected Rate(Parcel in) {
        this.name = in.readString();
        this.shorthandName = in.readString();
        this.countryUrl = in.readString();
        this.rate = in.readDouble();
    }

    public static final Parcelable.Creator<Rate> CREATOR = new Parcelable.Creator<Rate>() {
        @Override
        public Rate createFromParcel(Parcel source) {
            return new Rate(source);
        }

        @Override
        public Rate[] newArray(int size) {
            return new Rate[size];
        }
    };
}
