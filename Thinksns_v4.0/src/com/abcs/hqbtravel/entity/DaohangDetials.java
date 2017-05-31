package com.abcs.hqbtravel.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/9/30.
 */
public class DaohangDetials {


    @SerializedName("body")
    public BodyEntity body;
    @SerializedName("result")
    public int result;
    @SerializedName("timestamp")
    public String timestamp;
    @SerializedName("version")
    public String version;
    @SerializedName("transactionid")
    public Object transactionid;

    public static class BodyEntity {
        @SerializedName("guide")
        public GuideEntity guide;

        public static class GuideEntity {
            @SerializedName("id")
            public String id;
            @SerializedName("country_id")
            public String countryId;
            @SerializedName("city_id")
            public String cityId;
            @SerializedName("scenic_id")
            public String scenicId;
            @SerializedName("scenic_spot_id")
            public String scenicSpotId;
            @SerializedName("name")
            public String name;
            @SerializedName("short_intro")
            public String shortIntro;
            @SerializedName("intro")
            public String intro;
            @SerializedName("normal_price")
            public String normalPrice;
            @SerializedName("address")
            public String address;
            @SerializedName("en_address")
            public String enAddress;
            @SerializedName("recommend_play_time")
            public String recommendPlayTime;
            @SerializedName("business_time")
            public String businessTime;
            @SerializedName("tips")
            public String tips;
            @SerializedName("audio_url")
            public String audioUrl;
            @SerializedName("icon_url")
            public String iconUrl;
            @SerializedName("lat")
            public String lat;
            @SerializedName("lng")
            public String lng;
            @SerializedName("pic_url")
            public String picUrl;
            @SerializedName("traffic_line")
            public String trafficLine;
            @SerializedName("en_name")
            public String enName;
            @SerializedName("th_enname")
            public String thEnname;
            @SerializedName("thumnail_id")
            public String thumnailId;
            @SerializedName("can_listen")
            public String canListen;
            @SerializedName("create_time")
            public String createTime;
            @SerializedName("oss_url")
            public String ossUrl;
            @SerializedName("pictures")
            public List<PicturesEntity> pictures;

            public static class PicturesEntity {
                @SerializedName("id")
                public String id;
                @SerializedName("detail_id")
                public String detailId;
                @SerializedName("img_url")
                public String imgUrl;
                @SerializedName("create_time")
                public String createTime;
            }
        }
    }
}
