package com.abcs.haiwaigou.local.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 项目名称：com.abcs.haiwaigou.local.beans
 * 作者：zds
 * 时间：2017/3/13 17:59
 */

public class SSXaun {

    /**
     * state : 1
     * datas : [{"id":"1","gc_id":"1161","gc_name":"餐馆用品","c_en_name":null,"img":"http://www.huaqiaobang.com/data/upload/mobile/special/s9/s9_de439c021c3fb02670ec4f7f6a955f08.png","parent_id":"0"},{"id":"2","gc_id":"1158","gc_name":"干货零食","c_en_name":null,"img":"http://www.huaqiaobang.com/data/upload/mobile/special/s9/s9_cf03462c6447b302333cd6240aa37c54.png","parent_id":"0"},{"id":"3","gc_id":"1159","gc_name":"罐头食品","c_en_name":null,"img":"http://www.huaqiaobang.com/data/upload/mobile/special/s9/s9_f46302be49ddb96a294292048e223620.png","parent_id":"0"},{"id":"4","gc_id":"1153","gc_name":"海鲜海产","c_en_name":null,"img":"http://www.huaqiaobang.com/data/upload/mobile/special/s9/s9_d4659be6d2241eabda80f2096793fc81.png","parent_id":"0"},{"id":"5","gc_id":"1155","gc_name":"精美点心","c_en_name":null,"img":"http://www.huaqiaobang.com/data/upload/mobile/special/s9/s9_6498bf1be8b31667d2382815f8e9d8c9.png","parent_id":"0"},{"id":"6","gc_id":"1160","gc_name":"酒水饮料","c_en_name":null,"img":"http://www.huaqiaobang.com/data/upload/mobile/special/s9/s9_dea15202a0584059e8c0665165b60e35.png","parent_id":"0"},{"id":"7","gc_id":"1154","gc_name":"蔬菜瓜果","c_en_name":null,"img":"http://www.huaqiaobang.com/data/upload/mobile/special/s9/s9_02a96d43318b23231c8479a523951e48.png","parent_id":"0"},{"id":"8","gc_id":"1157","gc_name":"调味大全","c_en_name":null,"img":"http://www.huaqiaobang.com/data/upload/mobile/special/s9/s9_12c8290b28b59fff64cab592f0489e43.png","parent_id":"0"},{"id":"9","gc_id":"1156","gc_name":"五谷杂粮","c_en_name":null,"img":"http://www.huaqiaobang.com/data/upload/mobile/special/s9/s9_bd588afca584ff3911d64c0a087d48a9.png","parent_id":"0"},{"id":"10","gc_id":"1152","gc_name":"新鲜肉类","c_en_name":null,"img":"http://www.huaqiaobang.com/data/upload/mobile/special/s9/s9_415363d240ee9a9a98533cf264f62b94.png","parent_id":"0"}]
     */

    @SerializedName("state")
    public int state;
    @SerializedName("datas")
    public List<DatasEntry> datas;

    public static class DatasEntry {
        /**
         * id : 1
         * gc_id : 1161
         * gc_name : 餐馆用品
         * c_en_name : null
         * img : http://www.huaqiaobang.com/data/upload/mobile/special/s9/s9_de439c021c3fb02670ec4f7f6a955f08.png
         * parent_id : 0
         */

        @SerializedName("id")
        public String id;
        @SerializedName("gc_id")
        public String gcId;
        @SerializedName("gc_name")
        public String gcName;
        @SerializedName("c_en_name")
        public String cEnName;
        @SerializedName("img")
        public String img;
        @SerializedName("parent_id")
        public String parentId;
    }
}
