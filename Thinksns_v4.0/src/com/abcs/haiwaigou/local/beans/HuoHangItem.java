package com.abcs.haiwaigou.local.beans;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：com.abcs.haiwaigou.local.beans
 * 作者：zds
 * 时间：2017/3/23 9:36
 */

public class HuoHangItem {
    /**
     * goods_id : 103546
     * goods_name : 本地测试商品
     * goods_en_name : CP Ente
     * goods_serial : 14123
     * goods_image : 11_05424015161102321.jpg
     * goods_price : 1.00
     * goods_storage : 1
     * tax_rate : 1
     * price_hide : 0
     * tax_includ : 1
     * activity_arr : [{"types":"1","activity_name":"满35减7，满55减11（买单立享）","img":null},{"types":"2","activity_name":"新用户下单立减","img":null}]
     * tag_arr : [{"tag_id":"1","tag_name":"新鲜到货","img":null},{"tag_id":"2","tag_name":"泰国产","img":null}]
     */

    @SerializedName("goods_id")
    public String goodsId;
    @SerializedName("goods_name")
    public String goodsName;
    @SerializedName("tax_rate")
    public String tax_rate;
    @SerializedName("price_hide")
    public int price_hide;
    @SerializedName("tax_includ")
    public String tax_includ;
    @SerializedName("goods_en_name")
    public String goodsEnName;
    @SerializedName("goods_serial")
    public String goodsSerial;
    @SerializedName("goods_image")
    public String goodsImage;
    @SerializedName("goods_price")
    public double goodsPrice;
    @SerializedName("goods_storage")
    public String goodsStorage;
    @SerializedName("activity_arr")
    public List<ActivityArr> activityArr=new ArrayList<>();
    @SerializedName("tag_arr")
    public List<TagArr> tagArr=new ArrayList<>();

    //本地字段，用户recyclerview 购买数量
    public int numbers;
    public HuoHangItem() {
    }
}
