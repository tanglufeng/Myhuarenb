package com.abcs.hqbtravel.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/9/9.
 */
public class ZhuLi {

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
        @SerializedName("next")
        public int next;
        @SerializedName("services")
        public List<ServicesEntity> services;

        public static class ServicesEntity {
            @SerializedName("id")
            public String id;
            @SerializedName("logo")
            public String logo;
            @SerializedName("type")
            public String type;
            @SerializedName("name")
            public String name;
            @SerializedName("assistants")
            public AssistantsEntity assistants;
            @SerializedName("comments")
            public int comments;
            @SerializedName("price")
            public double price;
            @SerializedName("chargeUnit")
            public String chargeUnit;
            @SerializedName("currency")
            public String currency;

            public static class AssistantsEntity {
                @SerializedName("avator")
                public String avator;
                @SerializedName("age")
                public int age;
                @SerializedName("name")
                public String name;
                @SerializedName("sex")
                public int sex;
            }
        }
    }
}
