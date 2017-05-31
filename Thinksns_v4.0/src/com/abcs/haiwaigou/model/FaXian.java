package com.abcs.haiwaigou.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/3/3.
 */

public class FaXian {

    /**
     * state : 1
     * datas : [{"id":"1","title":"口令红包","type":"1","image":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_bb02f7dd5ed5f5e06c1d3313ccc50c16.png","sort":"1"},{"id":"2","title":"一元抢","type":"2","image":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_dc1474efbf3555085085be91105adef1.png","sort":"2"},{"id":"3","title":"电子礼品卡","type":"3","image":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_a81733a43ce23b6279d5ff79e367ecc5.png","sort":"3"},{"id":"4","title":"企业入口","type":"4","image":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_285291936fa3bc0e9b9f6437110951f4.png","sort":"4"}]
     */

    @SerializedName("state")
    public int state;
    @SerializedName("datas")
    public List<DatasBean> datas;

    public static class DatasBean {
        /**
         * id : 1
         * title : 口令红包
         * type : 1
         * image : http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_bb02f7dd5ed5f5e06c1d3313ccc50c16.png
         * sort : 1
         */

        @SerializedName("id")
        public String id;
        @SerializedName("title")
        public String title;
        @SerializedName("type")
        public String type;
        @SerializedName("image")
        public String image;
        @SerializedName("sort")
        public String sort;
    }
}
