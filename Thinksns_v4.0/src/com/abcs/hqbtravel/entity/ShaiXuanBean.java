package com.abcs.hqbtravel.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShaiXuanBean {

    /**
     * timestamp : 1488878481983
     * body : [{"name":"美食","type":3,"mostComment":true,"filter":[{"id":1,"name":"中餐","childName":[{"id":2,"name":"私房菜"},{"id":3,"name":"家常菜"},{"id":7,"name":"本帮江浙菜"},{"id":8,"name":"蟹宴"},{"id":9,"name":"川菜"},{"id":10,"name":"海鲜"},{"id":11,"name":"粤菜"},{"id":12,"name":"湘菜"},{"id":13,"name":"江西菜"},{"id":14,"name":"东北菜"},{"id":15,"name":"贵州菜"},{"id":16,"name":"面馆"},{"id":17,"name":"台湾菜"},{"id":18,"name":"清真菜"},{"id":19,"name":"新疆菜"},{"id":20,"name":"西北菜"},{"id":21,"name":"云南菜"},{"id":22,"name":"素菜"},{"id":23,"name":"烧烤"},{"id":24,"name":"火锅"},{"id":25,"name":"自助餐"},{"id":26,"name":"小吃快餐"}]},{"id":4,"name":"西餐","childName":[{"id":5,"name":"法国菜"},{"id":6,"name":"意大利菜"},{"id":27,"name":"土耳其菜"},{"id":28,"name":"中东菜"},{"id":29,"name":"俄罗斯菜"},{"id":39,"name":"其他"}]},{"id":30,"name":"日本菜","childName":[]},{"id":31,"name":"韩国料理","childName":[{"id":32,"name":"韩国"},{"id":33,"name":"朝鲜"}]},{"id":34,"name":"东南亚菜","childName":[]},{"id":35,"name":"泰国菜","childName":[]},{"id":36,"name":"面包甜点","childName":[]},{"id":37,"name":"咖啡厅","childName":[]},{"id":38,"name":"其他","childName":[]},{"id":40,"name":"酒吧","childName":[{"id":41,"name":"酒吧"}]}]}]
     * result : 1
     * transactionid :
     * version : 1.0
     * info : OK
     */

    @SerializedName("timestamp")
    public String timestamp;
    @SerializedName("result")
    public int result;
    @SerializedName("transactionid")
    public String transactionid;
    @SerializedName("version")
    public String version;
    @SerializedName("info")
    public String info;
    @SerializedName("body")
    public List<BodyBean> body;

    public static class BodyBean {
        /**
         * name : 美食
         * type : 3
         * mostComment : true
         * filter : [{"id":1,"name":"中餐","childName":[{"id":2,"name":"私房菜"},{"id":3,"name":"家常菜"},{"id":7,"name":"本帮江浙菜"},{"id":8,"name":"蟹宴"},{"id":9,"name":"川菜"},{"id":10,"name":"海鲜"},{"id":11,"name":"粤菜"},{"id":12,"name":"湘菜"},{"id":13,"name":"江西菜"},{"id":14,"name":"东北菜"},{"id":15,"name":"贵州菜"},{"id":16,"name":"面馆"},{"id":17,"name":"台湾菜"},{"id":18,"name":"清真菜"},{"id":19,"name":"新疆菜"},{"id":20,"name":"西北菜"},{"id":21,"name":"云南菜"},{"id":22,"name":"素菜"},{"id":23,"name":"烧烤"},{"id":24,"name":"火锅"},{"id":25,"name":"自助餐"},{"id":26,"name":"小吃快餐"}]},{"id":4,"name":"西餐","childName":[{"id":5,"name":"法国菜"},{"id":6,"name":"意大利菜"},{"id":27,"name":"土耳其菜"},{"id":28,"name":"中东菜"},{"id":29,"name":"俄罗斯菜"},{"id":39,"name":"其他"}]},{"id":30,"name":"日本菜","childName":[]},{"id":31,"name":"韩国料理","childName":[{"id":32,"name":"韩国"},{"id":33,"name":"朝鲜"}]},{"id":34,"name":"东南亚菜","childName":[]},{"id":35,"name":"泰国菜","childName":[]},{"id":36,"name":"面包甜点","childName":[]},{"id":37,"name":"咖啡厅","childName":[]},{"id":38,"name":"其他","childName":[]},{"id":40,"name":"酒吧","childName":[{"id":41,"name":"酒吧"}]}]
         */

        @SerializedName("name")
        public String name;
        @SerializedName("type")
        public int type;
        @SerializedName("mostComment")
        public boolean mostComment;
        @SerializedName("filter")
        public List<FilterBean> filter;

        public static class FilterBean {
            /**
             * id : 1
             * name : 中餐
             * childName : [{"id":2,"name":"私房菜"},{"id":3,"name":"家常菜"},{"id":7,"name":"本帮江浙菜"},{"id":8,"name":"蟹宴"},{"id":9,"name":"川菜"},{"id":10,"name":"海鲜"},{"id":11,"name":"粤菜"},{"id":12,"name":"湘菜"},{"id":13,"name":"江西菜"},{"id":14,"name":"东北菜"},{"id":15,"name":"贵州菜"},{"id":16,"name":"面馆"},{"id":17,"name":"台湾菜"},{"id":18,"name":"清真菜"},{"id":19,"name":"新疆菜"},{"id":20,"name":"西北菜"},{"id":21,"name":"云南菜"},{"id":22,"name":"素菜"},{"id":23,"name":"烧烤"},{"id":24,"name":"火锅"},{"id":25,"name":"自助餐"},{"id":26,"name":"小吃快餐"}]
             */

            @SerializedName("id")
            public int id;
            @SerializedName("name")
            public String name;
            @SerializedName("childName")
            public List<ChildNameBean> childName;

            public static class ChildNameBean {
                /**
                 * id : 2
                 * name : 私房菜
                 */

                @SerializedName("id")
                public int id;
                @SerializedName("name")
                public String name;
            }
        }
    }
}
