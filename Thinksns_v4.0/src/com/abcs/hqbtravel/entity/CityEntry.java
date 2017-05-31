package com.abcs.hqbtravel.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/9/23.
 */
public class CityEntry {

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
        @SerializedName("items")
        public List<ItemsEntity> items;

        public static class ItemsEntity {
            @SerializedName("id")
            public String id;
            @SerializedName("name")
            public String name;

            public String getName() {
                return name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }


}
