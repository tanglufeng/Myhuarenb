package com.abcs.haiwaigou.model;/**
 * Created by Administrator on 2016/12/27.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * User: zds
 * Date: 2016-12-27
 * Time: 17:01
 * FIXME
 */
public class QuHao {

    @SerializedName("timestamp")
    public String timestamp;
    @SerializedName("result")
    public int result;
    @SerializedName("transactionid")
    public String transactionid;
    @SerializedName("version")
    public String version;
    @SerializedName("info")
    public String info;
    @SerializedName("body")
    public List<BodyBean> body;

    public static class BodyBean {


        @SerializedName("first_letter")
        public String firstLetter;
        @SerializedName("countrys")
        public List<CountrysBean> countrys;

        public static class CountrysBean {
            /**
             * id : 276
             * country : 奥地利
             * mobile_prefix : +43
             * area : 欧洲
             * sort : 0
             * first_letter : A
             */

            @SerializedName("id")
            public int id;
            @SerializedName("country")
            public String country;
            @SerializedName("mobile_prefix")
            public String mobilePrefix;
            @SerializedName("area")
            public String area;
            @SerializedName("sort")
            public int sort;
            @SerializedName("first_letter")
            public String firstLetter;
        }
    }
}
