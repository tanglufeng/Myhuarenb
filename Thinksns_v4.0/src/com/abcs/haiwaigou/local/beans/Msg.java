package com.abcs.haiwaigou.local.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by zjz on 2016/8/31.
 */
public class Msg implements Parcelable{
    private String id;
    private long createTime;
    private String title;
    private String cityName;
    private String country;
    private String typeName;
    private String imgUrl;
    private String listTypeId;
    private int ids;

    public Msg() {
    }

    protected Msg(Parcel in) {
        id = in.readString();
        createTime = in.readLong();
        title = in.readString();
        cityName = in.readString();
        country = in.readString();
        typeName = in.readString();
        imgUrl = in.readString();
        listTypeId = in.readString();
        ids = in.readInt();
    }

    public static final Creator<Msg> CREATOR = new Creator<Msg>() {
        @Override
        public Msg createFromParcel(Parcel in) {
            return new Msg(in);
        }

        @Override
        public Msg[] newArray(int size) {
            return new Msg[size];
        }
    };

    public int getIds() {
        return ids;
    }

    public void setIds(int ids) {
        this.ids = ids;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getListTypeId() {
        return listTypeId;
    }

    public void setListTypeId(String listTypeId) {
        this.listTypeId = listTypeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeLong(createTime);
        dest.writeString(title);
        dest.writeString(cityName);
        dest.writeString(country);
        dest.writeString(typeName);
        dest.writeString(imgUrl);
        dest.writeString(listTypeId);
        dest.writeInt(ids);
    }
}
