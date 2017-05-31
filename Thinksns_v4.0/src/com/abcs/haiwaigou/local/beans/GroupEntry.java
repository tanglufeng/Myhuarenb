package com.abcs.haiwaigou.local.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 项目名称：com.abcs.haiwaigou.local.beans
 * 作者：zds
 * 时间：2017/5/9 17:52
 */

public class GroupEntry {
    /**
     * en_first_letter : A
     * city : [{"cate_name":"阿姆斯特丹","catename_en":"Amsterdam","city_id":42},{"cate_name":"奥克兰","catename_en":"Auckland","city_id":66}]
     */

    @SerializedName("en_first_letter")
    public String enFirstLetter;
    @SerializedName("city")
    public List<CityEntry> city;

    public static class CityEntry {
        /**
         * cate_name : 阿姆斯特丹
         * catename_en : Amsterdam
         * city_id : 42
         */

        @SerializedName("cate_name")
        public String cateName;
        @SerializedName("catename_en")
        public String catenameEn;
        @SerializedName("city_id")
        public int cityId;
    }
}
