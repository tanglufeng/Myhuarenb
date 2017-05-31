package com.abcs.haiwaigou.local.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 项目名称：com.abcs.haiwaigou.local.model
 * 作者：zds
 * 时间：2017/4/25 11:12
 */

public class MoreNewsInfo {


    @SerializedName("status")
    public int status;
    @SerializedName("msg")
    public List<MsgEntry> msg;

    public static class MsgEntry {

        @SerializedName("group_time")
        public String groupTime;
        @SerializedName("date")
        public List<DateEntry> date;

        public static class DateEntry {
            /**
             * contactMan :
             * content :
             * id : 68391
             * typeName : 招工
             * createTime : 1493086779
             * title : 请有经验的铁板或寿司师傅和跑堂
             * views : 0
             * cityName : 维也纳（首都）
             * ads :
             * listTypeId : 1010
             * contact :
             * country : 奥地利
             */

            @SerializedName("contactMan")
            public String contactMan;
            @SerializedName("content")
            public String content;
            @SerializedName("id")
            public long id;
            @SerializedName("typeName")
            public String typeName;
            @SerializedName("createTime")
            public long createTime;
            @SerializedName("title")
            public String title;
            @SerializedName("views")
            public int views;
            @SerializedName("cityName")
            public String cityName;
            @SerializedName("ads")
            public String ads;
            @SerializedName("listTypeId")
            public String listTypeId;
            @SerializedName("contact")
            public String contact;
            @SerializedName("country")
            public String country;
        }
    }
}
