package com.abcs.hqbtravel.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/9.
 */
public class YouJi  implements Serializable{

    private static final long serialVersionUID = 1L;

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
            @SerializedName("id")
            public String id;
            @SerializedName("photo")
            public String photo;
            @SerializedName("title")
            public String title;
            @SerializedName("time")
            public String time;
            @SerializedName("author")
            public String author;
            @SerializedName("avatar")
            public String avatar;
            @SerializedName("read")
            public int read;
            @SerializedName("taglist")
            public List<TaglistEntity> taglist;

            public static class TaglistEntity {
                @SerializedName("tagId")
                public String tagId;
                @SerializedName("name")
                public String name;
            }
        }
    }
}
