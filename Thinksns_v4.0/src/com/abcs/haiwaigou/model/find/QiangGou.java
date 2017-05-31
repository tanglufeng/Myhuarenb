package com.abcs.haiwaigou.model.find;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 项目名称：com.abcs.haiwaigou.model.find
 * 作者：zds
 * 时间：2017/4/1 16:13
 */

public class QiangGou {

    /**
     * state : 1
     * datas : {"goods_list":[{"id":"16","goods_id":"103473","times":"1490925600","goods_name":"奥地利Umathum乌玛通蓝法兰基胥2014红酒750ml精品葡萄酒","goods_price":"239.00","goods_marketprice":"0.00","goods_num":"0","goods_image":"http://www.huaqiaobang.com/data/upload/shop/store/goods/1/1_05415282096271752.jpg","other_img":"http://www.huaqiaobang.com/data/upload/shop/store/goods/1/1_05415282096271752.jpg,112"}],"img":"http://www.huaqiaobang.com/data/upload/shop/store/goods/1/1_05415282096271752.jpg","ref_id":"112"}
     */

    @SerializedName("state")
    public int state;
    @SerializedName("datas")
    public DatasEntry datas;

    public static class DatasEntry {
        /**
         * goods_list : [{"id":"16","goods_id":"103473","times":"1490925600","goods_name":"奥地利Umathum乌玛通蓝法兰基胥2014红酒750ml精品葡萄酒","goods_price":"239.00","goods_marketprice":"0.00","goods_num":"0","goods_image":"http://www.huaqiaobang.com/data/upload/shop/store/goods/1/1_05415282096271752.jpg","other_img":"http://www.huaqiaobang.com/data/upload/shop/store/goods/1/1_05415282096271752.jpg,112"}]
         * img : http://www.huaqiaobang.com/data/upload/shop/store/goods/1/1_05415282096271752.jpg
         * ref_id : 112
         */

        @SerializedName("img")
        public String img;
        @SerializedName("ref_id")
        public String refId;
        @SerializedName("goods_list")
        public List<GoodsListEntry> goodsList;

        public static class GoodsListEntry {
            /**
             * id : 16
             * goods_id : 103473
             * times : 1490925600
             * goods_name : 奥地利Umathum乌玛通蓝法兰基胥2014红酒750ml精品葡萄酒
             * goods_price : 239.00
             * goods_marketprice : 0.00
             * goods_num : 0
             * goods_image : http://www.huaqiaobang.com/data/upload/shop/store/goods/1/1_05415282096271752.jpg
             * other_img : http://www.huaqiaobang.com/data/upload/shop/store/goods/1/1_05415282096271752.jpg,112
             */

            @SerializedName("id")
            public String id;
            @SerializedName("goods_id")
            public String goodsId;
            @SerializedName("times")
            public String times;
            @SerializedName("goods_name")
            public String goodsName;
            @SerializedName("goods_price")
            public String goodsPrice;
            @SerializedName("goods_marketprice")
            public String goodsMarketprice;
            @SerializedName("goods_num")
            public String goodsNum;
            @SerializedName("goods_image")
            public String goodsImage;
            @SerializedName("other_img")
            public String otherImg;
            @SerializedName("max_num")
            public String max_num;
        }
    }
}
