package com.abcs.haiwaigou.model.find;

import com.google.gson.annotations.SerializedName;

/**
 * 项目名称：com.abcs.haiwaigou.model.find
 * 作者：zds
 * 时间：2017/3/31 10:17
 */


public class SmallEntry {
    /**
     * id : 11
     * a_type : 3
     * b_id : 1
     * title : 奶粉纸尿裤会场
     * descs :
     * sort : 2
     * ref_id : 103738
     * img : http://www.huaqiaobang.com/data/upload/mobile/special/s9/s9_2bddd52d667b275bedd5e566ff366a75.jpg
     * add_time : 1490866713
     */

    @SerializedName("id")
    public String id;
    @SerializedName("a_type")
    public String aType;
    @SerializedName("b_id")
    public String bId;
    @SerializedName("title")
    public String title;
    @SerializedName("descs")
    public String descs;
    @SerializedName("sort")
    public String sort;
    @SerializedName("ref_id")
    public String refId;
    @SerializedName("img")
    public String img;
    @SerializedName("add_time")
    public String addTime;
}
