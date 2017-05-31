package com.abcs.hqbtravel.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/9.
 */

public class FujinSanBi implements Serializable {



    @SerializedName("body")
    public BodyBean body;
    @SerializedName("result")
    public int result;
    @SerializedName("timestamp")
    public String timestamp;
    @SerializedName("version")
    public String version;
    @SerializedName("transactionid")
    public String transactionid;

    public static class BodyBean implements Serializable {


        @SerializedName("touristAttractionCount")
        public int touristAttractionCount;
        @SerializedName("bannerCount")
        public int bannerCount;
        @SerializedName("shopCount")
        public int shopCount;
        @SerializedName("restaurantCount")
        public int restaurantCount;
        @SerializedName("highEstTemperature")
        public String highEstTemperature;
        @SerializedName("local_time")
        public String localTime;
        @SerializedName("touristAttractions")
        public List<TouristAttractionsBean> touristAttractions;
        @SerializedName("banners")
        public List<BannersBean> banners;
        @SerializedName("shops")
        public List<ShopBean> shops;
        @SerializedName("restaurants")
        public List<RestauransBean> restaurants;
        @SerializedName("adults")
        public List<Adults> adults;

    }
}
