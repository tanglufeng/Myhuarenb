package com.abcs.hqbtravel.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/17.
 */

public class MyTickets implements Serializable{
//    product_id: "1"
//    product_price: "100"
//    cheap_price: "98"
//    img: "http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb_abc_img%2F%E7%81%AB%E7%8B%90%E6%88%AA%E5%9B%BE_2016-11-03T06-17-58.946Z1478153912817.png"
//    intro: "【经典必玩】曼谷大皇宫"


    @SerializedName("product_id")
    public String product_id;
    @SerializedName("product_price")
    public String product_price;
    @SerializedName("cheap_price")
    public String cheap_price;
    @SerializedName("img")
    public String img;
    @SerializedName("intro")
    public String intro;
}
