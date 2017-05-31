package com.abcs.haiwaigou.model;

import com.google.gson.annotations.SerializedName;

/**
 * 项目名称：com.abcs.haiwaigou.model
 * 作者：zds
 * 时间：2017/4/11 16:31
 */

public class ExtendOrderGoodsEntry {

    /**
     * goods_id : 102961
     * goods_name : 亚洲面粉
     * goods_en_name : Asia Mehl   10x1Kg/Pa
     * specification : Pa
     * rank : 1,2,3,4
     * goods_serial : 002
     * goods_image : 11_05412748840706557.jpg
     * goods_state : 1
     * tax_rate : b
     * g_orgin : 35
     * goods_price : 7.70
     * goods_storage : 9652
     * goods_num : 1.0
     */

    @SerializedName("goods_id")
    public String goodsId;
    @SerializedName("goods_name")
    public String goodsName;
    @SerializedName("goods_en_name")
    public String goodsEnName;
    @SerializedName("specification")
    public String specification;
    @SerializedName("rank")
    public String rank;
    @SerializedName("goods_serial")
    public String goodsSerial;
    @SerializedName("goods_image")
    public String goodsImage;
    @SerializedName("goods_state")
    public String goodsState;
    @SerializedName("tax_rate")
    public String taxRate;
    @SerializedName("g_orgin")
    public String gOrgin;
    @SerializedName("goods_price")
    public double goodsPrice;
    @SerializedName("goods_storage")
    public String goodsStorage;
    @SerializedName("goods_num")
    public String goodsNum;
}
