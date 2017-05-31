package com.abcs.hqbtravel.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/9.
 */
public class DaoHang2 {


    @SerializedName("body")
    public BodyEntity body;
    @SerializedName("result")
    public int result;
    @SerializedName("timestamp")
    public String timestamp;
    @SerializedName("version")
    public String version;
    @SerializedName("transactionid")
    public String transactionid;

    public static class BodyEntity {
        @SerializedName("next")
        public int next;
        @SerializedName("pageCount")
        public int pageCount;
        @SerializedName("cityName")
        public String cityName;
        @SerializedName("highEstTemperature")
        public String highEstTemperature;
        @SerializedName("minimumTemperature")
        public String minimumTemperature;
        @SerializedName("cityPhoto")
        public String cityPhoto;
        @SerializedName("items")
        public List<ItemsEntity> items;

        public static class ItemsEntity implements Serializable {
            @SerializedName("logo")
            public String logo;
            @SerializedName("name")
            public String name;
            @SerializedName("been")
            public int been;
            @SerializedName("id")
            public String id;
            @SerializedName("voiceCount")
            public String voiceCount;
            @SerializedName("voiceSize")
            public String voiceSize;
            @SerializedName("audioUrl")
            public String audioUrl;
            @SerializedName("sightEnName")
            public String sightEnName;
        }
    }
}
