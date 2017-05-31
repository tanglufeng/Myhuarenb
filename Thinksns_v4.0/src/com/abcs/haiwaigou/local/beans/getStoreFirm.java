package com.abcs.haiwaigou.local.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 项目名称：com.abcs.haiwaigou.local.beans
 * 作者：zds
 * 时间：2017/3/16 14:25
 */

public class getStoreFirm {

    /**
     * state : 1
     * datas : [{"native":" 仟惠仁（维也纳）","store_list":[{"store_name":"哈肯吃咯恶魔","address_id":"2873","district_id":"1","district_name":" 仟惠仁（维也纳）","is_default":"1"},{"store_name":"125836991","address_id":"2874","district_id":"1","district_name":" 仟惠仁（维也纳）","is_default":"0"}]},{"native":" 仟惠仁（格拉茨）","store_list":[{"store_name":"黑啊夸","address_id":"2847","district_id":"2","district_name":" 仟惠仁（格拉茨）","is_default":"0"}]}]
     */

    @SerializedName("state")
    public int state;
    @SerializedName("datas")
    public List<DatasEntry> datas;

    public static class DatasEntry {
        /**
         * native :  仟惠仁（维也纳）
         * store_list : [{"store_name":"哈肯吃咯恶魔","address_id":"2873","district_id":"1","district_name":" 仟惠仁（维也纳）","is_default":"1"},{"store_name":"125836991","address_id":"2874","district_id":"1","district_name":" 仟惠仁（维也纳）","is_default":"0"}]
         */

        @SerializedName("native")
        public String nativeX;
        @SerializedName("store_list")
        public List<StoreListEntry> storeList;

        public static class StoreListEntry {
            /**
             * store_name : 哈肯吃咯恶魔
             * address_id : 2873
             * district_id : 1
             * district_name :  仟惠仁（维也纳）
             * is_default : 1
             */

            @SerializedName("store_name")
            public String storeName;
            @SerializedName("address_id")
            public String addressId;
            @SerializedName("district_id")
            public String districtId;
            @SerializedName("district_name")
            public String districtName;
            @SerializedName("is_default")
            public String isDefault;
        }
    }
}
