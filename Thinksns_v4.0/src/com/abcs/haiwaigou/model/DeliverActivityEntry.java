package com.abcs.haiwaigou.model;

import com.google.gson.annotations.SerializedName;

/**
 * 项目名称：com.abcs.haiwaigou.model
 * 作者：zds
 * 时间：2017/4/11 16:31
 */

public class DeliverActivityEntry {

    /**
     * types : 2
     * activity_name : 每单赠送鱼泉榨菜
     * activity_type : 2
     * img : http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_6c8b40686eccd6443ad5cb8a570b4c7f.png
     * value_1 : 每单赠送鱼泉榨菜
     * value_2 : 8
     * goods_id : 0
     */

    @SerializedName("types")
    public String types;
    @SerializedName("activity_name")
    public String activityName;
    @SerializedName("activity_type")
    public String activityType;
    @SerializedName("img")
    public String img;
    @SerializedName("value_1")
    public String value1;
    @SerializedName("value_2")
    public String value2;
    @SerializedName("goods_id")
    public String goodsId;
}
