package com.abcs.haiwaigou.local.beans;/**
 * Created by Administrator on 2017/1/12.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * User: zds
 * Date: 2017-01-12
 * Time: 18:37
 * FIXME
 */
public class HWGWanLeDeTials {

    /**
     * status : 1
     * goods_list : [{"goods_id":"101268","goods_name":"曼谷金东尼人妖秀门票·普通座位 （成人）","goods_price":"37.00","jk_price":"35.00","bjk_price":"35.00","hk_price":"35.00","rank":"1,2,3,4","goods_promotion_price":"37.00","goods_promotion_type":"0","goods_marketprice":"39.00","goods_storage":"100","goods_image":"1_05336608691955408.jpg","city_id":"55"},{"goods_id":"101266","goods_name":"曼谷金东尼人妖秀门票·普通座位 （儿童）","goods_price":"31.00","jk_price":"30.00","bjk_price":"30.00","hk_price":"30.00","rank":"1,2,3,4","goods_promotion_price":"31.00","goods_promotion_type":"0","goods_marketprice":"33.00","goods_storage":"100","goods_image":"1_05336576166794427.jpg","city_id":"55"},{"goods_id":"101271","goods_name":"曼谷金东尼人妖秀门票·VIP座位 （儿童）","goods_price":"33.00","jk_price":"32.00","bjk_price":"32.00","hk_price":"32.00","rank":"1,2,3,4","goods_promotion_price":"33.00","goods_promotion_type":"0","goods_marketprice":"35.00","goods_storage":"100","goods_image":"1_05336610907819624.jpg","city_id":"55"},{"goods_id":"101273","goods_name":"曼谷金东尼人妖秀门票·VIP座位 （成人）","goods_price":"44.00","jk_price":"41.00","bjk_price":"41.00","hk_price":"41.00","rank":"1,2,3,4","goods_promotion_price":"44.00","goods_promotion_type":"0","goods_marketprice":"46.00","goods_storage":"100","goods_image":"1_05336613081496084.jpg","city_id":"55"},{"goods_id":"101274","goods_name":"曼谷 曼波人妖表演门票（普通座）（儿童）","goods_price":"45.00","jk_price":"42.00","bjk_price":"42.00","hk_price":"42.00","rank":"1,2,3,4","goods_promotion_price":"45.00","goods_promotion_type":"0","goods_marketprice":"47.00","goods_storage":"100","goods_image":"1_05336619819634236.jpg","city_id":"55"},{"goods_id":"101276","goods_name":"曼谷 曼波人妖表演门票（普通座）（成人）","goods_price":"54.00","jk_price":"51.00","bjk_price":"51.00","hk_price":"51.00","rank":"1,2,3,4","goods_promotion_price":"54.00","goods_promotion_type":"0","goods_marketprice":"57.00","goods_storage":"100","goods_image":"1_05336632436797820.jpg","city_id":"55"},{"goods_id":"101277","goods_name":"曼谷 曼波人妖表演门票（VIP座）（儿童）","goods_price":"54.00","jk_price":"51.00","bjk_price":"51.00","hk_price":"51.00","rank":"1,2,3,4","goods_promotion_price":"54.00","goods_promotion_type":"0","goods_marketprice":"57.00","goods_storage":"100","goods_image":"1_05336636693846317.jpg","city_id":"55"},{"goods_id":"101278","goods_name":"曼波人妖表演门票（VIP座）（成人）","goods_price":"63.00","jk_price":"59.00","bjk_price":"59.00","hk_price":"59.00","rank":"1,2,3,4","goods_promotion_price":"63.00","goods_promotion_type":"0","goods_marketprice":"66.00","goods_storage":"100","goods_image":"1_05336638699774077.jpg","city_id":"55"},{"goods_id":"101281","goods_name":"曼谷克里普索人妖秀门票（儿童）","goods_price":"114.00","jk_price":"108.00","bjk_price":"108.00","hk_price":"108.00","rank":"1,2,3,4","goods_promotion_price":"114.00","goods_promotion_type":"0","goods_marketprice":"120.00","goods_storage":"100","goods_image":"1_05336644179616562.jpg","city_id":"55"},{"goods_id":"101285","goods_name":"曼谷克里普索人妖秀门票（成人）","goods_price":"171.00","jk_price":"162.00","bjk_price":"162.00","hk_price":"162.00","rank":"1,2,3,4","goods_promotion_price":"171.00","goods_promotion_type":"0","goods_marketprice":"180.00","goods_storage":"100","goods_image":"1_05336655882113742.jpg","city_id":"55"}]
     * page_count : 4
     */

    @SerializedName("status")
    public int status;
    @SerializedName("page_count")
    public int pageCount;
    @SerializedName("goods_list")
    public List<GoodsListBean> goodsList;

    public static class GoodsListBean {
        /**
         * goods_id : 101268
         * goods_name : 曼谷金东尼人妖秀门票·普通座位 （成人）
         * goods_price : 37.00
         * jk_price : 35.00
         * bjk_price : 35.00
         * hk_price : 35.00
         * rank : 1,2,3,4
         * goods_promotion_price : 37.00
         * goods_promotion_type : 0
         * goods_marketprice : 39.00
         * goods_storage : 100
         * goods_image : 1_05336608691955408.jpg
         * city_id : 55
         */

        @SerializedName("goods_id")
        public String goodsId;
        @SerializedName("goods_name")
        public String goodsName;
        @SerializedName("goods_price")
        public String goodsPrice;
        @SerializedName("jk_price")
        public String jkPrice;
        @SerializedName("bjk_price")
        public String bjkPrice;
        @SerializedName("hk_price")
        public String hkPrice;
        @SerializedName("rank")
        public String rank;
        @SerializedName("goods_promotion_price")
        public String goodsPromotionPrice;
        @SerializedName("goods_promotion_type")
        public String goodsPromotionType;
        @SerializedName("goods_marketprice")
        public String goodsMarketprice;
        @SerializedName("goods_storage")
        public String goodsStorage;
        @SerializedName("goods_image")
        public String goodsImage;
        @SerializedName("city_id")
        public String cityId;
    }
}
