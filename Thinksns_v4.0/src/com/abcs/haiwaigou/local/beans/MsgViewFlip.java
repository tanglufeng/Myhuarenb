package com.abcs.haiwaigou.local.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zjz on 2016/8/31.
 */
public class MsgViewFlip implements Parcelable{

    public String title;
    public String title2;


    public MsgViewFlip() {
    }

    protected MsgViewFlip(Parcel in) {
        title = in.readString();
        title2 = in.readString();
    }

    public static final Creator<MsgViewFlip> CREATOR = new Creator<MsgViewFlip>() {
        @Override
        public MsgViewFlip createFromParcel(Parcel in) {
            return new MsgViewFlip(in);
        }

        @Override
        public MsgViewFlip[] newArray(int size) {
            return new MsgViewFlip[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(title2);
    }
}
