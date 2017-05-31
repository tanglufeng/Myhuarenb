package com.abcs.hqbtravel.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/9/9.
 */
public class ServerDetail {

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
        @SerializedName("service")
        public ServiceEntity service;

        public static class ServiceEntity {
            @SerializedName("name")
            public String name;
            @SerializedName("assistant")
            public AssistantEntity assistant;
            @SerializedName("price")
            public double price;
            @SerializedName("chargeUnit")
            public String chargeUnit;
            @SerializedName("currency")
            public String currency;
            @SerializedName("desc")
            public String desc;
            @SerializedName("time")
            public String time;
            @SerializedName("site")
            public String site;
            @SerializedName("negotiate")
            public boolean negotiate;
            @SerializedName("banners")
            public List<String> banners;
            @SerializedName("others")
            public List<OthersEntity> others;

            public static class AssistantEntity {
                @SerializedName("avator")
                public String avator;
                @SerializedName("age")
                public int age;
                @SerializedName("name")
                public String name;
                @SerializedName("sex")
                public int sex;
            }

            public static class OthersEntity {
                @SerializedName("id")
                public String id;
                @SerializedName("name")
                public String name;
                @SerializedName("price")
                public double price;
                @SerializedName("logo")
                public String logo;
                @SerializedName("chargeUnit")
                public String chargeUnit;
                @SerializedName("currency")
                public String currency;
            }
        }
    }
}
