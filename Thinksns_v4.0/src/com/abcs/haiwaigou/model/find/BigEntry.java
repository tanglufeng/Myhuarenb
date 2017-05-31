package com.abcs.haiwaigou.model.find;

import com.google.gson.annotations.SerializedName;

/**
 * 项目名称：com.abcs.haiwaigou.model.find
 * 作者：zds
 * 时间：2017/3/31 10:16
 */

public class BigEntry {
    /**
     * id : 10
     * a_type : 3
     * b_id : 1
     * title : 奶粉纸尿裤会场
     * descs :
     * sort : 1
     * ref_id : 103738
     * img : http://www.huaqiaobang.com/data/upload/mobile/special/s9/s9_7868b82f069253ae114c9fb983665877.jpg
     * add_time : 1490866679
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
