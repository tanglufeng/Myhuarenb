package com.abcs.hqbtravel.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/12.
 */

public class TouristAttractionsBean implements Serializable {
    @SerializedName("english_name")
    public String english_name;
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("photo")
    public String photo;
    @SerializedName("commentCount")
    public int commentCount;
    @SerializedName("cate")
    public String cate;
    @SerializedName("star")
    public double star;
    @SerializedName("introduction")
    public String introduction;
    @SerializedName("range")
    public int range;
    @SerializedName("been")
    public int been;
    @SerializedName("distance")
    public String distance;
    @SerializedName("lng")
    public String lng;
    @SerializedName("lat")
    public String lat;
    @SerializedName("product_id")
    public String product_id;
    @SerializedName("view")
    public String view;
    @SerializedName("price")
    public String price;
}
