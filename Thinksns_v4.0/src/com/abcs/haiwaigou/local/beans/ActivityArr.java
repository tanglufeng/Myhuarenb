package com.abcs.haiwaigou.local.beans;

import com.google.gson.annotations.SerializedName;

/**
 * 项目名称：com.abcs.haiwaigou.local.beans
 * 作者：zds
 * 时间：2017/3/22 18:52
 */

public class ActivityArr {
    /**
     * types : 1
     * activity_name : 满35减7，满55减11（买单立享）
     * img : null
     */

    @SerializedName("types")
    public String types;
    @SerializedName("activity_name")
    public String activityName;
    @SerializedName("img")
    public String img;
}
