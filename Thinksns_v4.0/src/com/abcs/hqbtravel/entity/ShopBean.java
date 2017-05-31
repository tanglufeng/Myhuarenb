package com.abcs.hqbtravel.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/12.
 */

public class ShopBean implements Serializable {

//    "id": "7067",
//            "name": "暹罗广场",
//            "star": 5.0,
//            "distance": "5.8km",
//            "photo": "http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb_abc_img%2F51479351738887.jpg",
//            "introduction": "Siam Center 暹罗中心周围有着网络般的小巷，它们被总称为Siam Square 暹罗广场。位于Siam轻轨站周边，从考山路可坐15，47，79，532路到达这里。这里是曼谷最繁华地段，集中了大型商场，有central world（世贸中心），MBK，沙炎中心等，就像北京的王府井和西单。\nSiam Square 暹罗广场 上挤满了相互独立的商店和货摊，主要出售音乐用品，书籍，饰品，服装，绝大部分商品是为了满足泰国前卫的年轻人。拉玛一世路在广场旁，这里是泰国电影工业的一个陈列窗，有三家豪华电影院。旁边是国家体育场，这里是泰国足球和其它一些项目的主要举办场地。\n虽然地处市中心，但这一带的小巷中，也有很多小旅馆，双人间价格在750-1000铢左右，条件不错，胜在交通方便。有很多喜欢购物的游客一般住在这里。",
//            "range": 6,
//            "been": 1564,
//            "lng": "100.5017651000",
//            "lat": "13.7563309000",
//            "detailId": "1207204",
//            "cate": "购物中心",
//            "product_id": "-1"

    @SerializedName("english_name")
    public String english_name;
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("star")
    public double star;
    @SerializedName("distance")
    public String distance;
    @SerializedName("photo")
    public String photo;
    @SerializedName("introduction")
    public String introduction;
    @SerializedName("range")
    public int range;
    @SerializedName("been")
    public int been;
    @SerializedName("commentCount")
    public int commentCount;
    @SerializedName("lng")
    public String lng;
    @SerializedName("lat")
    public String lat;
    @SerializedName("detailId")
    public String detailId;
    @SerializedName("cate")
    public String cate;
    @SerializedName("product_id")
    public String productId;
    @SerializedName("view")
    public String view;
    @SerializedName("price")
    public String price;
}
