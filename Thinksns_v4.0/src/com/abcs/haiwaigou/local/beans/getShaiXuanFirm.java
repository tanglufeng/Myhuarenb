package com.abcs.haiwaigou.local.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 项目名称：com.abcs.haiwaigou.local.beans
 * 作者：zds
 * 时间：2017/3/16 17:16
 */

public class getShaiXuanFirm
{

    /**
     * state : 1
     * datas : {"activity_list":[{"activity_id":"1","activity_name":"满35减7，满55减11（买单立享）","img":null},{"activity_id":"2","activity_name":"新用户下单立减","img":null},{"activity_id":"3","activity_name":"9折专区只限今天","img":null},{"activity_id":"4","activity_name":"满100元代93元","img":null}],"tag_list":[{"tag_id":"1","tag_name":"新鲜到货","img":null},{"tag_id":"2","tag_name":"泰国产","img":null}]}
     */

    @SerializedName("state")
    public int state;
    @SerializedName("datas")
    public DatasBean datas;

    public static class DatasBean {
        @SerializedName("activity_list")
        public List<ActivityListBean> activityList;
        @SerializedName("tag_list")
        public List<TagListBean> tagList;

        public static class ActivityListBean {
            /**
             * activity_id : 1
             * activity_name : 满35减7，满55减11（买单立享）
             * img : null
             */

            @SerializedName("activity_id")
            public String activityId;
            @SerializedName("activity_name")
            public String activityName;
            @SerializedName("img")
            public String img;
        }

        public static class TagListBean {
            /**
             * tag_id : 1
             * tag_name : 新鲜到货
             * img : null
             */

            @SerializedName("tag_id")
            public String tagId;
            @SerializedName("tag_name")
            public String tagName;
            @SerializedName("img")
            public String img;
        }
    }
}
