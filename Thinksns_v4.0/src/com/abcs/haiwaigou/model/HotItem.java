package com.abcs.haiwaigou.model;/**
 * Created by Administrator on 2017/2/23.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * User: zds
 * Date: 2017-02-23
 * Time: 11:35
 * FIXME
 */
public class HotItem {

    /**
     * state : 1
     * datas : [{"goods_id":"101013","goods_commonid":"101099","goods_name":"英国Daniel Wellington丹尼尔惠灵顿摩登系列石英尼龙蓝白两色银边女表手表0963DW","goods_jingle":"【顺丰包邮】是来自瑞典的手表品牌，多彩的尼龙表带，轻薄的表身，干净的表盘塑造了DW腕表百搭的书卷味的气质，让众多时尚达人爱不释手；因本产品欧洲直邮，为欧洲价格直接乘以汇率得来，利润微薄，会依据欧洲汇率进行增减价格，支持欧洲正品，支持华侨帮！本产品全球联保，支持专柜验货！","goods_image":"1_05276821879565090.jpg","store_id":"1","store_name":"华人邦官方店","gc_id":"1107","gc_id_2":"1105","goods_price":"1029.00","jk_price":"783.00","rank":"1,2,3,4","goods_promotion_price":"1029.00","goods_promotion_type":"0","goods_marketprice":"1388.00","goods_salenum":"1","goods_storage":"5","goods_state":"1"},{"goods_id":"101012","goods_commonid":"101098","goods_name":"英国Daniel Wellington丹尼尔惠灵顿摩登系列石英棕黑色真牛皮皮带女表手表0922DW","goods_jingle":"【顺丰包邮】是来自瑞典的手表品牌，多彩的尼龙表带，轻薄的表身，干净的表盘塑造了DW腕表百搭的书卷味的气质，让众多时尚达人爱不释手；因本产品欧洲直邮，为欧洲价格直接乘以汇率得来，利润微薄，会依据欧洲汇率进行增减价格，支持欧洲正品，支持华侨帮！本产品全球联保，支持专柜验货！","goods_image":"1_05276817740714848.jpg","store_id":"1","store_name":"华人邦官方店","gc_id":"1107","gc_id_2":"1105","goods_price":"955.00","jk_price":"726.00","rank":"1,2,3,4","goods_promotion_price":"955.00","goods_promotion_type":"0","goods_marketprice":"1388.00","goods_salenum":"3","goods_storage":"3","goods_state":"1"},{"goods_id":"101011","goods_commonid":"101097","goods_name":"英国Daniel Wellington丹尼尔惠灵顿经典佳人系列石英棕色真牛皮带女表手表0611DW","goods_jingle":"【顺丰包邮】是来自瑞典的手表品牌，多彩的尼龙表带，轻薄的表身，干净的表盘塑造了DW腕表百搭的书卷味的气质，让众多时尚达人爱不释手；因本产品欧洲直邮，为欧洲价格直接乘以汇率得来，利润微薄，会依据欧洲汇率进行增减价格，支持欧洲正品，支持华侨帮！本产品全球联保，支持专柜验货！","goods_image":"1_05276814680093530.jpg","store_id":"1","store_name":"华人邦官方店","gc_id":"1107","gc_id_2":"1105","goods_price":"1250.00","jk_price":"950.00","rank":"1,2,3,4","goods_promotion_price":"1250.00","goods_promotion_type":"0","goods_marketprice":"1568.00","goods_salenum":"2","goods_storage":"5","goods_state":"1"},{"goods_id":"101010","goods_commonid":"101096","goods_name":"英国Daniel Wellington丹尼尔惠灵顿经典佳人系列石英尼龙蓝紫条钉女表手表0603DW","goods_jingle":"【顺丰包邮】是来自瑞典的手表品牌，多彩的尼龙表带，轻薄的表身，干净的表盘塑造了DW腕表百搭的书卷味的气质，让众多时尚达人爱不释手；因本产品欧洲直邮，为欧洲价格直接乘以汇率得来，利润微薄，会依据欧洲汇率进行增减价格，支持欧洲正品，支持华侨帮！本产品全球联保，支持专柜验货！","goods_image":"1_05276810804169586.jpg","store_id":"1","store_name":"华人邦官方店","gc_id":"1107","gc_id_2":"1105","goods_price":"1029.00","jk_price":"783.00","rank":"1,2,3,4","goods_promotion_price":"1029.00","goods_promotion_type":"0","goods_marketprice":"1599.00","goods_salenum":"8","goods_storage":"4","goods_state":"1"},{"goods_id":"101009","goods_commonid":"101095","goods_name":"英国Daniel Wellington丹尼尔惠灵顿经典佳人系列石英蓝白两色条钉女表手表0503DW","goods_jingle":"【顺丰包邮】是来自瑞典的手表品牌，多彩的尼龙表带，轻薄的表身，干净的表盘塑造了DW腕表百搭的书卷味的气质，让众多时尚达人爱不释手；因本产品欧洲直邮，为欧洲价格直接乘以汇率得来，利润微薄，会依据欧洲汇率进行增减价格，支持欧洲正品，支持华侨帮！本产品全球联保，支持专柜验货！","goods_image":"1_05276242139446922.jpg","store_id":"1","store_name":"华人邦官方店","gc_id":"1107","gc_id_2":"1105","goods_price":"1399.00","jk_price":"1065.00","rank":"1,2,3,4","goods_promotion_price":"1399.00","goods_promotion_type":"0","goods_marketprice":"1899.00","goods_salenum":"8","goods_storage":"5","goods_state":"1"},{"goods_id":"101008","goods_commonid":"101094","goods_name":"英国Daniel Wellington丹尼尔惠灵顿经典绅士系列石英红蓝白三色条钉男表手表0212DW","goods_jingle":"【顺丰包邮】是来自瑞典的手表品牌，多彩的尼龙表带，轻薄的表身，干净的表盘塑造了DW腕表百搭的书卷味的气质，让众多时尚达人爱不释手；因本产品欧洲直邮，为欧洲价格直接乘以汇率得来，利润微薄，会依据欧洲汇率进行增减价格，支持欧洲正品，支持华侨帮！本产品全球联保，支持专柜验货！","goods_image":"1_05400557663323058.jpg","store_id":"1","store_name":"华人邦官方店","gc_id":"1107","gc_id_2":"1105","goods_price":"1399.00","jk_price":"1065.00","rank":"1,2,3,4","goods_promotion_price":"1399.00","goods_promotion_type":"0","goods_marketprice":"4398.00","goods_salenum":"11","goods_storage":"3","goods_state":"1"},{"goods_id":"101007","goods_commonid":"101093","goods_name":"英国Daniel Wellington丹尼尔惠灵顿经典魅力系列石英尼龙皮表带金盘男女手表0554DW","goods_jingle":"【顺丰包邮】是来自瑞典的手表品牌，多彩的尼龙表带，轻薄的表身，干净的表盘塑造了DW腕表百搭的书卷味的气质，让众多时尚达人爱不释手；因本产品欧洲直邮，为欧洲价格直接乘以汇率得来，利润微薄，会依据欧洲汇率进行增减价格，支持欧洲正品，支持华侨帮！本产品全球联保，支持专柜验货！","goods_image":"1_05276233767509511.jpg","store_id":"1","store_name":"华人邦官方店","gc_id":"1107","gc_id_2":"1105","goods_price":"1029.00","jk_price":"783.00","rank":"1,2,3,4","goods_promotion_price":"1029.00","goods_promotion_type":"0","goods_marketprice":"1588.00","goods_salenum":"5","goods_storage":"4","goods_state":"1"},{"goods_id":"101006","goods_commonid":"101092","goods_name":"英国Daniel Wellington丹尼尔惠灵顿经典魅力系列蓝白尼龙皮表带金盘女表手表0552DW","goods_jingle":"【顺丰包邮】是来自瑞典的手表品牌，多彩的尼龙表带，轻薄的表身，干净的表盘塑造了DW腕表百搭的书卷味的气质，让众多时尚达人爱不释手；因本产品欧洲直邮，为欧洲价格直接乘以汇率得来，利润微薄，会依据欧洲汇率进行增减价格，支持欧洲正品，支持华侨帮！本产品全球联保，支持专柜验货！","goods_image":"1_05276229793240442.jpg","store_id":"1","store_name":"华人邦官方店","gc_id":"1107","gc_id_2":"1105","goods_price":"1177.00","jk_price":"895.00","rank":"1,2,3,4","goods_promotion_price":"1177.00","goods_promotion_type":"0","goods_marketprice":"1899.00","goods_salenum":"4","goods_storage":"5","goods_state":"1"},{"goods_id":"101005","goods_commonid":"101091","goods_name":"英国Daniel Wellington丹尼尔惠灵顿经典绅士系列石英蓝红白相间条钉男表手表0213DW","goods_jingle":"【顺丰包邮】是来自瑞典的手表品牌，多彩的尼龙表带，轻薄的表身，干净的表盘塑造了DW腕表百搭的书卷味的气质，让众多时尚达人爱不释手；因本产品欧洲直邮，为欧洲价格直接乘以汇率得来，利润微薄，会依据欧洲汇率进行增减价格，支持欧洲正品，支持华侨帮！本产品全球联保，支持专柜验货！","goods_image":"1_05276220764724980.jpg","store_id":"1","store_name":"华人邦官方店","gc_id":"1107","gc_id_2":"1105","goods_price":"1177.00","jk_price":"895.00","rank":"1,2,3,4","goods_promotion_price":"1177.00","goods_promotion_type":"0","goods_marketprice":"1688.00","goods_salenum":"5","goods_storage":"5","goods_state":"1"},{"goods_id":"101004","goods_commonid":"101090","goods_name":"英国Daniel Wellington 丹尼尔惠灵顿经典绅士系列石英男牛皮棕黑色皮带手表0211DW","goods_jingle":"【顺丰包邮】是来自瑞典的手表品牌，多彩的尼龙表带，轻薄的表身，干净的表盘塑造了DW腕表百搭的书卷味的气质，让众多时尚达人爱不释手；因本产品欧洲直邮，为欧洲价格直接乘以汇率得来，利润微薄，会依据欧洲汇率进行增减价格，支持欧洲正品，支持华侨帮！本产品全球联保，支持专柜验货！","goods_image":"1_05276199074920686.jpg","store_id":"1","store_name":"华人邦官方店","gc_id":"1107","gc_id_2":"1105","goods_price":"1399.00","jk_price":"1065.00","rank":"1,2,3,4","goods_promotion_price":"1399.00","goods_promotion_type":"0","goods_marketprice":"1899.00","goods_salenum":"11","goods_storage":"5","goods_state":"1"}]
     * page_count : 3
     * page : 3
     */

    @SerializedName("state")
    public int state;
    @SerializedName("page_count")
    public int pageCount;
    @SerializedName("page")
    public int page;
    @SerializedName("datas")
    public List<DatasBean> datas;

    public static class DatasBean {
        /**
         * goods_id : 101013
         * goods_commonid : 101099
         * goods_name : 英国Daniel Wellington丹尼尔惠灵顿摩登系列石英尼龙蓝白两色银边女表手表0963DW
         * goods_jingle : 【顺丰包邮】是来自瑞典的手表品牌，多彩的尼龙表带，轻薄的表身，干净的表盘塑造了DW腕表百搭的书卷味的气质，让众多时尚达人爱不释手；因本产品欧洲直邮，为欧洲价格直接乘以汇率得来，利润微薄，会依据欧洲汇率进行增减价格，支持欧洲正品，支持华侨帮！本产品全球联保，支持专柜验货！
         * goods_image : 1_05276821879565090.jpg
         * store_id : 1
         * store_name : 华人邦官方店
         * gc_id : 1107
         * gc_id_2 : 1105
         * goods_price : 1029.00
         * jk_price : 783.00
         * rank : 1,2,3,4
         * goods_promotion_price : 1029.00
         * goods_promotion_type : 0
         * goods_marketprice : 1388.00
         * goods_salenum : 1
         * goods_storage : 5
         * goods_state : 1
         */

        @SerializedName("goods_id")
        public String goodsId;
        @SerializedName("goods_commonid")
        public String goodsCommonid;
        @SerializedName("goods_name")
        public String goodsName;
        @SerializedName("goods_jingle")
        public String goodsJingle;
        @SerializedName("goods_image")
        public String goodsImage;
        @SerializedName("store_id")
        public String storeId;
        @SerializedName("store_name")
        public String storeName;
        @SerializedName("gc_id")
        public String gcId;
        @SerializedName("gc_id_2")
        public String gcId2;
        @SerializedName("goods_price")
        public double goodsPrice;
        @SerializedName("jk_price")
        public double jkPrice;
        @SerializedName("rank")
        public String rank;
        @SerializedName("goods_promotion_price")
        public double goodsPromotionPrice;
        @SerializedName("goods_promotion_type")
        public String goodsPromotionType;
        @SerializedName("goods_marketprice")
        public double goodsMarketprice;
        @SerializedName("goods_salenum")
        public String goodsSalenum;
        @SerializedName("goods_storage")
        public String goodsStorage;
        @SerializedName("goods_state")
        public String goodsState;
    }
}
