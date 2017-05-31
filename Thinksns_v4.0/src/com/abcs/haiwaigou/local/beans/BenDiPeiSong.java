package com.abcs.haiwaigou.local.beans;/**
 * Created by Administrator on 2017/1/12.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * User: zds
 * Date: 2017-01-12
 * Time: 18:18
 * FIXME
 */
public class BenDiPeiSong {

    /**
     * state : 1
     * datas : [{"gc_id":"-1","gc_name":"我的常用"},{"gc_id":"1153","gc_name":"海鲜海产"},{"gc_id":"1154","gc_name":"蔬菜瓜果"},{"gc_id":"1152","gc_name":"新鲜肉类"},{"gc_id":"1155","gc_name":"精美点心"},{"gc_id":"1156","gc_name":"五谷杂粮"},{"gc_id":"1157","gc_name":"调味大全"},{"gc_id":"1158","gc_name":"干货零食"},{"gc_id":"1159","gc_name":"罐头食品"},{"gc_id":"1160","gc_name":"酒水饮料"},{"gc_id":"1161","gc_name":"餐馆用品"}]
     * cart_price : null
     * cart_num : null
     */

    @SerializedName("state")
    public int state;
    @SerializedName("cart_price")
    public Object cartPrice;
    @SerializedName("cart_num")
    public Object cartNum;
    @SerializedName("datas")
    public List<DatasBean> datas;

    public static class DatasBean {
        /**
         * gc_id : -1
         * gc_name : 我的常用
         */

        @SerializedName("gc_id")
        public String gcId;
        @SerializedName("gc_name")
        public String gcName;
    }
}
