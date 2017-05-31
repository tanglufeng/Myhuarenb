package com.abcs.haiwaigou.local.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 项目名称：com.abcs.haiwaigou.local.beans
 * 作者：zds
 * 时间：2017/3/23 10:25
 */

public class HuoHangAddressList {

    /**
     * state : 1
     * datas : [{"address_id":"1238","member_id":"6592","true_name":"南苑餐厅","address":",GINZKEYPLATZ 10","tel_phone":null,"tel_phone_res":null,"mob_phone":"0662626400","mob_phone_res":null,"postcode":null,"is_default":"1"},{"address_id":"1973","member_id":"6592","true_name":"大棚饭店TAIPAN","address":"Moosstraße 22 A-5020 Salzburg","tel_phone":"0662626400))","tel_phone_res":null,"mob_phone":null,"mob_phone_res":null,"postcode":null,"is_default":"0"},{"address_id":"2737","member_id":"6592","true_name":"南苑餐厅","address":"SALZBURG， GINZKEYPLATZ 10","tel_phone":"0662626400","tel_phone_res":null,"mob_phone":null,"mob_phone_res":null,"postcode":null,"is_default":"0"}]
     */

    @SerializedName("state")
    public int state;
    @SerializedName("datas")
    public List<DatasEntry> datas;

    public static class DatasEntry {
        /**
         * address_id : 1238
         * member_id : 6592
         * true_name : 南苑餐厅
         * address : ,GINZKEYPLATZ 10
         * tel_phone : null
         * tel_phone_res : null
         * mob_phone : 0662626400
         * mob_phone_res : null
         * postcode : null
         * is_default : 1
         */

        @SerializedName("address_id")
        public String addressId;
        @SerializedName("member_id")
        public String memberId;
        @SerializedName("true_name")
        public String trueName;
        @SerializedName("address")
        public String address;
        @SerializedName("tel_phone")
        public String telPhone;
        @SerializedName("tel_phone_res")
        public String telPhoneRes;
        @SerializedName("mob_phone")
        public String mobPhone;
        @SerializedName("mob_phone_res")
        public String mobPhoneRes;
        @SerializedName("postcode")
        public String postcode;
        @SerializedName("is_default")
        public String isDefault;
    }
}
