package com.abcs.haiwaigou.local.beans;/**
 * Created by Administrator on 2017/2/22.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * User: zds
 * Date: 2017-02-22
 * Time: 11:21
 * FIXME
 */
public class PinLeiRight {


    /**
     * datas : [{"class_2":"1164","img":null,"class_3":[{"id":"1208","class_name":"纸尿裤"},{"id":"1209","class_name":"清洁护理"}]},{"class_2":"1210","img":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_ddbc34db76bc3dbe8634e017e9d7eb5e.jpg","class_3":[{"id":"1213","class_name":"诺优能"},{"id":"1214","class_name":"美林"},{"id":"1215","class_name":"爱他美"}]},{"class_2":"1211","img":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_1630b8f7371eedc6ee6aa1d1aeb01559.jpg","class_3":[{"id":"1216","class_name":"鱼油/DHA"},{"id":"1217","class_name":"益生菌"},{"id":"1218","class_name":"钙铁锌"},{"id":"1219","class_name":"VD/维生素"},{"id":"1220","class_name":"其他"}]},{"class_2":"1212","img":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_1deab381a81817d2020409893944934b.jpg","class_3":[{"id":"1221","class_name":"铁元"},{"id":"1222","class_name":"营养片"},{"id":"1223","class_name":"海藻油"},{"id":"1224","class_name":"其他"}]}]
     * state : 1
     */

    @SerializedName("state")
    public int state;
    @SerializedName("datas")
    public List<DatasBean> datas;

    public static class DatasBean {
        /**
         * class_2 : 1164
         * img : null
         * class_3 : [{"id":"1208","class_name":"纸尿裤"},{"id":"1209","class_name":"清洁护理"}]
         */

        @SerializedName("class_2")
        public String class2;
        @SerializedName("img")
        public String img;
        @SerializedName("class_3")
        public List<Class3Bean> class3;

        public static class Class3Bean {
            /**
             * id : 1208
             * class_name : 纸尿裤
             */

            @SerializedName("id")
            public String id;
            @SerializedName("class_name")
            public String className;
        }
    }
}
