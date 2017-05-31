package com.abcs.haiwaigou.model.find;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 项目名称：com.abcs.haiwaigou.model.find
 * 作者：zds
 * 时间：2017/4/1 16:54
 */

public class QiangGouTag {

    /**
     * state : 1
     * datas : [{"time":"08:00","times":"1491350400","title":"已开抢"},{"time":"10:00","times":"1491357600","title":"已开抢"}]
     */

    @SerializedName("state")
    public int state;
    @SerializedName("datas")
    public List<DatasEntry> datas;

    public static class DatasEntry {
        /**
         * time : 08:00
         * times : 1491350400
         * title : 已开抢
         */

        @SerializedName("time")
        public String time;
        @SerializedName("times")
        public String times;
        @SerializedName("title")
        public String title;
    }
}
