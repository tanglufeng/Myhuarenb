package com.abcs.hqbtravel.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/9/9.
 */
public class YouJiDetials {

    /**
     * nickName : 泡泡想要这里那里到处游走
     * title : 0418-0425 清迈+pai+清莱白庙黑庙+骑大象+丛林飞越 海量图 完结
     * avatar : http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_98ca2da0b20e37176373b5b804c34401.jpg
     * time : 1397750400000
     * content : q
     * tagList : [{"tagId":"17","name":"自由行"},{"tagId":"0","name":"记录"},{"tagId":"0","name":"拍摄"}]
     * read : 4
     */

    @SerializedName("body")
    public BodyBean body;
    /**
     * body : {"nickName":"泡泡想要这里那里到处游走","title":"0418-0425 清迈+pai+清莱白庙黑庙+骑大象+丛林飞越 海量图 完结","avatar":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_98ca2da0b20e37176373b5b804c34401.jpg","time":"1397750400000","content":"q","tagList":[{"tagId":"17","name":"自由行"},{"tagId":"0","name":"记录"},{"tagId":"0","name":"拍摄"}],"read":4}
     * result : 1
     * timestamp : 1478942256582
     * version : 1.0
     * transactionid : null
     */

    @SerializedName("result")
    public int result;
    @SerializedName("timestamp")
    public String timestamp;
    @SerializedName("version")
    public String version;
    @SerializedName("transactionid")
    public String transactionid;

    public static class BodyBean {
        @SerializedName("travelNoteTopCount")
        public int travelNoteTopCount;
        @SerializedName("travelNoteCommentCount")
        public int travelNoteCommentCount;
        @SerializedName("nickName")
        public String nickName;
        @SerializedName("title")
        public String title;
        @SerializedName("avatar")
        public String avatar;
        @SerializedName("time")
        public String time;
        @SerializedName("content")
        public String content;
        @SerializedName("read")
        public int read;
        /**
         * tagId : 17
         * name : 自由行
         */

        @SerializedName("tagList")
        public List<TagListBean> tagList;

        public static class TagListBean {
            @SerializedName("tagId")
            public String tagId;
            @SerializedName("name")
            public String name;
        }


        @SerializedName("poiNames")
        public List<PoiNamesBean> poiNames;

        public static class PoiNamesBean {
            @SerializedName("name")
            public String name;
            @SerializedName("anchor")
            public String anchor;


        }
    }
}
