package com.abcs.hqbtravel.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/12.
 */

public class RestauransBean implements Serializable {
    @SerializedName("cate")
    public String cate;
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("english_name")
    public String english_name;
    @SerializedName("location")
    public String location;
    @SerializedName("star")
    public double star;
    @SerializedName("photo")
    public String photo;
    @SerializedName("been")
    public int been;
    @SerializedName("commentCount")
    public int commentCount;
    @SerializedName("distance")
    public String distance;
    @SerializedName("introduction")
    public String introduction;
    @SerializedName("range")
    public int range;
    @SerializedName("lng")
    public String lng;
    @SerializedName("lat")
    public String lat;
    @SerializedName("detailId")
    public String detailId;
    @SerializedName("touristAttractions")
    public String touristAttractions;
    @SerializedName("shops")
    public String shops;
    @SerializedName("product_id")
    public String product_id;
    @SerializedName("view")
    public String view;
    @SerializedName("price")
    public String price;
}
