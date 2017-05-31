package com.abcs.haiwaigou.local.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 项目名称：com.abcs.haiwaigou.local.model
 * 作者：zds
 * 时间：2017/4/22 10:20
 */

public class NewHire {

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
             * content :
             * pubTime : 1479550904
             * imgUrl :
             * contactMan :
             * id : 58541
             * title : NOODLE KING
             * views : 0
             * ads : Lassallestraße 36 A-1020 Wien
             * contact : 0676-9314868
             */

            @SerializedName("content")
            public String content;
            @SerializedName("pubTime")
            public long pubTime;
            @SerializedName("imgUrl")
            public String imgUrl;
            @SerializedName("contactMan")
            public String contactMan;
            @SerializedName("id")
            public int id;
            @SerializedName("title")
            public String title;
            @SerializedName("views")
            public int views;
            @SerializedName("ads")
            public String ads;
            @SerializedName("contact")
            public String contact;
        }
    }
}
