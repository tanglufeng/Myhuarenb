package com.abcs.haiwaigou.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 作者：Administrator on 2017/3/9 15:07.
 */

public class BenDiSearch {

    /**
     * state : 1
     * datas : [{"goods_id":"103437","goods_name":"CP Ente CP Ente","goods_en_name":"CP Ente","goods_price":"86.36","goods_serial":"999A","goods_image":"11_05412507472525810.jpg","district_id":"2","goods_storage":"100"},{"goods_id":"103436","goods_name":"黄箱北京烤鸭 Rote Peking Ente 18st/10kg","goods_en_name":"Rote Peking Ente 18st/10kg","goods_price":"89.90","goods_serial":"999","goods_image":"11_05412507548420058.jpg","district_id":"2","goods_storage":"98"},{"goods_id":"103435","goods_name":"(绿箱）中国烤鸭 Peking Enten in Gr0n karton l0kg/knt","goods_en_name":"Peking Enten in Gr0n karton l0kg/knt","goods_price":"84.00","goods_serial":"998","goods_image":"11_05412507628440801.jpg","district_id":"2","goods_storage":"100"},{"goods_id":"103434","goods_name":"盐水鸭 Gesalzen Ente ","goods_en_name":"Gesalzen Ente ","goods_price":"13.64","goods_serial":"997","goods_image":"11_05412508491598859.jpg","district_id":"2","goods_storage":"100"},{"goods_id":"103433","goods_name":"(白箱）中国烤鸭 Peking Enten in weiss Karton l0Kg/Knt","goods_en_name":"Peking Enten in weiss Karton l0Kg/Knt","goods_price":"77.27","goods_serial":"996","goods_image":"11_05412507701538060.jpg","district_id":"2","goods_storage":"100"},{"goods_id":"103432","goods_name":"HU烤鸭 Ungarische Grillente ","goods_en_name":"Ungarische Grillente ","goods_price":"70.00","goods_serial":"995","goods_image":"11_05412507773654179.jpg","district_id":"2","goods_storage":"100"}]
     * page_count : 23
     * msg : 查询成功
     */

    @SerializedName("state")
    public int state;
    @SerializedName("page_count")
    public int pageCount;
    @SerializedName("msg")
    public String msg;
    @SerializedName("datas")
    public List<DatasBean> datas;

    public static class DatasBean {
        /**
         * goods_id : 103437
         * goods_name : CP Ente CP Ente
         * goods_en_name : CP Ente
         * goods_price : 86.36
         * goods_serial : 999A
         * goods_image : 11_05412507472525810.jpg
         * district_id : 2
         * goods_storage : 100
         */

        @SerializedName("goods_id")
        public String goodsId;
        @SerializedName("goods_name")
        public String goodsName;
        @SerializedName("goods_en_name")
        public String goodsEnName;
        @SerializedName("goods_price")
        public String goodsPrice;
        @SerializedName("goods_serial")
        public String goodsSerial;
        @SerializedName("goods_image")
        public String goodsImage;
        @SerializedName("district_id")
        public String districtId;
        @SerializedName("goods_storage")
        public String goodsStorage;
    }
}
