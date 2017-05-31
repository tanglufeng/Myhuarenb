package com.abcs.haiwaigou.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 项目名称：com.abcs.haiwaigou.model
 * 作者：zds
 * 时间：2017/4/6 18:34
 */

public class ReXiaoItem {

    /**
     * state : 1
     * datas : [{"id":"1","ref_id":"1","title":"实时热销","descs":"测试详情","img":"图片","sort":"1","ref_type":"1","ref_param":"123"},{"id":"2","ref_id":"1","title":"每周热销","descs":null,"img":null,"sort":"2","ref_type":"1","ref_param":"1234"}]
     */

    @SerializedName("state")
    public int state;
    @SerializedName("datas")
    public List<DatasEntry> datas;

    public static class DatasEntry {
        /**
         * id : 1
         * ref_id : 1
         * title : 实时热销
         * descs : 测试详情
         * img : 图片
         * sort : 1
         * ref_type : 1
         * ref_param : 123
         */

        @SerializedName("id")
        public String id;
        @SerializedName("ref_id")
        public String refId;
        @SerializedName("title")
        public String title;
        @SerializedName("descs")
        public String descs;
        @SerializedName("img")
        public String img;
        @SerializedName("sort")
        public String sort;
        @SerializedName("ref_type")
        public String refType;
        @SerializedName("ref_param")
        public String refParam;
    }
}
