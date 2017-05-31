package com.abcs.hqbtravel.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/9/9.
 */
public class DaoHang {


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
        @SerializedName("pageCount")
        public int pageCount;
        @SerializedName("items")
        public List<ItemsEntity> items;

        public static class ItemsEntity {
            @SerializedName("logo")
            public String logo;
            @SerializedName("name")
            public String name;
            @SerializedName("been")
            public int been;
            @SerializedName("id")
            public String id;
        }
    }
}
