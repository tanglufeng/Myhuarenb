package com.abcs.hqbtravel.entity;/**
 * Created by Administrator on 2017/1/6.
 */

import com.google.gson.annotations.SerializedName;

/**
 * User: zds
 * Date: 2017-01-06
 * Time: 17:24
 * FIXME
 */
public class IfCollect {


    /**
     * timestamp : 1483688422599
     * body : {}
     * result : 1
     * transactionid :
     * version : 1.0
     * info : OK
     */

    @SerializedName("timestamp")
    public String timestamp;
    @SerializedName("body")
    public BodyBean body;
    @SerializedName("result")
    public int result;
    @SerializedName("transactionid")
    public String transactionid;
    @SerializedName("version")
    public String version;
    @SerializedName("info")
    public String info;

    public static class BodyBean {
    }
}
