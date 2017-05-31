package com.abcs.haiwaigou.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 项目名称：com.abcs.haiwaigou.model
 * 作者：zds
 * 时间：2017/4/11 16:27
 */

public class HuoHMyOrder {

    /**
     * state : 1
     * datas : [{"order_id":"5085","order_sn":"9000000000520101","pay_sn":"480545240953767765","store_id":"11","buyer_id":"10765","buyer_name":"admin13","add_time":"1491896953","payment_time":"0","goods_amount":"7.70","order_amount":"7.70","at_price":"8.47","order_state":"20","delete_state":"0","order_district":"1","shipping_fee":"0.00","extend_order_goods":[{"goods_id":"102961","goods_name":"亚洲面粉","goods_en_name":"Asia Mehl   10x1Kg/Pa","specification":"Pa","rank":"1,2,3,4","goods_serial":"002","goods_image":"11_05412748840706557.jpg","goods_state":"1","tax_rate":"b","g_orgin":"35","goods_price":"7.70","goods_storage":"9652","goods_num":"1.0"}],"tax_arr":{"netto_a":"0.00","netto_b":"7.70","mwst_a":"0.00","mwst_b":"0.77","brutto_a":"0","brutto_b":"8.47"},"deliverActivity":[{"types":"2","activity_name":"每单赠送鱼泉榨菜","activity_type":"2","img":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_6c8b40686eccd6443ad5cb8a570b4c7f.png","value_1":"每单赠送鱼泉榨菜","value_2":"8","goods_id":"0"}]},{"order_id":"5017","order_sn":"9000000000513301","pay_sn":"800545226825404765","store_id":"11","buyer_id":"10765","buyer_name":"admin13","add_time":"1491882825","payment_time":"0","goods_amount":"211.17","order_amount":"211.17","at_price":"232.29","order_state":"20","delete_state":"0","order_district":"1","shipping_fee":"0.00","extend_order_goods":[{"goods_id":"103629","goods_name":"巴西鸡肉12kg（sadia）","goods_en_name":"Huhnerf leisch (Sadia) 2 kgX6pa","specification":"Knt","rank":"2,3,4","goods_serial":"914","goods_image":"11_05433736061705613.jpg","goods_state":"1","tax_rate":"b","g_orgin":"35","goods_price":"39.30","goods_storage":"9832","goods_num":"3.0"},{"goods_id":"103358","goods_name":"鸭子","goods_en_name":"Enten 6x2.0kg/knt","specification":"Knt","rank":"1,2,3,4","goods_serial":"910","goods_image":"11_05435358339056275.jpg","goods_state":"1","tax_rate":"b","g_orgin":"35","goods_price":"31.09","goods_storage":"9852","goods_num":"3.0"}],"tax_arr":{"netto_a":"0.00","netto_b":"211.17","mwst_a":"0.00","mwst_b":"21.12","brutto_a":"0","brutto_b":"232.29"},"deliverActivity":[{"types":"2","activity_name":"每单赠送鱼泉榨菜","activity_type":"2","img":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_6c8b40686eccd6443ad5cb8a570b4c7f.png","value_1":"每单赠送鱼泉榨菜","value_2":"8","goods_id":"0"}]}]
     * page_count : 5
     */

    @SerializedName("state")
    public int state;
    @SerializedName("page_count")
    public int pageCount;
    @SerializedName("datas")
    public List<DatasEntry> datas;

    public static class DatasEntry {
        /**
         * order_id : 5085
         * order_sn : 9000000000520101
         * pay_sn : 480545240953767765
         * store_id : 11
         * buyer_id : 10765
         * buyer_name : admin13
         * add_time : 1491896953
         * payment_time : 0
         * goods_amount : 7.70
         * order_amount : 7.70
         * at_price : 8.47
         * order_state : 20
         * delete_state : 0
         * order_district : 1
         * shipping_fee : 0.00
         * extend_order_goods : [{"goods_id":"102961","goods_name":"亚洲面粉","goods_en_name":"Asia Mehl   10x1Kg/Pa","specification":"Pa","rank":"1,2,3,4","goods_serial":"002","goods_image":"11_05412748840706557.jpg","goods_state":"1","tax_rate":"b","g_orgin":"35","goods_price":"7.70","goods_storage":"9652","goods_num":"1.0"}]
         * tax_arr : {"netto_a":"0.00","netto_b":"7.70","mwst_a":"0.00","mwst_b":"0.77","brutto_a":"0","brutto_b":"8.47"}
         * deliverActivity : [{"types":"2","activity_name":"每单赠送鱼泉榨菜","activity_type":"2","img":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_6c8b40686eccd6443ad5cb8a570b4c7f.png","value_1":"每单赠送鱼泉榨菜","value_2":"8","goods_id":"0"}]
         */

        @SerializedName("order_id")
        public String orderId;
        @SerializedName("order_sn")
        public String orderSn;
        @SerializedName("pay_sn")
        public String paySn;
        @SerializedName("store_id")
        public String storeId;
        @SerializedName("buyer_id")
        public String buyerId;
        @SerializedName("buyer_name")
        public String buyerName;
        @SerializedName("add_time")
        public long addTime;
        @SerializedName("payment_time")
        public String paymentTime;
        @SerializedName("goods_amount")
        public String goodsAmount;
        @SerializedName("order_amount")
        public String orderAmount;
        @SerializedName("at_price")
        public String atPrice;
        @SerializedName("native_invoice")
        public String native_invoice;
        @SerializedName("order_message")
        public String order_message;
        @SerializedName("order_state")
        public String orderState;
        @SerializedName("delete_state")
        public String deleteState;
        @SerializedName("order_district")
        public String orderDistrict;
        @SerializedName("shipping_fee")
        public String shippingFee;
        @SerializedName("tax_arr")
        public TaxArrEntry taxArr;
        @SerializedName("extend_order_goods")
        public List<ExtendOrderGoodsEntry> extendOrderGoods;
        @SerializedName("deliverActivity")
        public List<DeliverActivityEntry> deliverActivity;

    }
}
