package com.abcs.haiwaigou.model;/**
 * Created by Administrator on 2016/11/26.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * User: zds
 * Date: 2016-11-26
 * Time: 15:34
 * FIXME
 */
public class DuDao {

    /**
     * code : 200
     * datas : [{"id":"1","img":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_8c8f83c0280670306a1b1642d1e74830.png","description":"中秋购物卡","flag":"1","descs":"购物卡","sort":"1","state":"0"}]
     */

    @SerializedName("code")
    public int code;
    @SerializedName("datas")
    public List<DatasBean> datas;

    public static class DatasBean {
        /**
         * id : 1
         * img : http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_8c8f83c0280670306a1b1642d1e74830.png
         * description : 中秋购物卡
         * flag : 1
         * descs : 购物卡
         * sort : 1
         * state : 0
         */

        @SerializedName("id")
        public String id;
        @SerializedName("img")
        public String img;
        @SerializedName("description")
        public String description;
        @SerializedName("flag")
        public String flag;
        @SerializedName("descs")
        public String descs;
        @SerializedName("sort")
        public String sort;
        @SerializedName("state")
        public String state;
    }
}
