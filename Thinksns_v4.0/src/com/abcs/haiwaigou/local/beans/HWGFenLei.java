package com.abcs.haiwaigou.local.beans;/**
 * Created by Administrator on 2017/2/20.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * User: zds
 * Date: 2017-02-20
 * Time: 12:07
 * FIXME
 */
public class HWGFenLei {

    /**
     * state : 1
     * datas : [{"id":"959","f_id":"0","class_name":"营养保健","class_img_1":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_ebdc7398dc797e4947789a0087d25909.png","class_img_2":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_86da0d18bd3755a2a8c194c49ee5ff8a.png","sort":"1"},{"id":"593","f_id":"0","class_name":"休闲食品","class_img_1":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_23369ececd0ad991b2637d5d4af5333f.png","class_img_2":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_b1dc829054392675e027981ec08ff3c3.png","sort":"2"},{"id":"470","f_id":"0","class_name":"个护护肤","class_img_1":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_8a21f8b334b2d7bbbb9a8bc4607886fe.png","class_img_2":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_c3abfa067f13bd24bb39f9ca8a3cee28.png","sort":"3"},{"id":"1066","f_id":"0","class_name":"饮料酒水","class_img_1":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_9cd454cd5655699d3cbcb22152bb2e98.png","class_img_2":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_5500af9cd0bc1e861331810a5da19310.png","sort":"4"},{"id":"1089","f_id":"0","class_name":"生活用品","class_img_1":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_8fdfc4e77c453a6a27d15b4bb534597f.png","class_img_2":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_cd830d4622683297b8abd7609631aebb.png","sort":"5"}]
     */

    @SerializedName("state")
    public int state;
    @SerializedName("datas")
    public List<DatasBean> datas;

    public static class DatasBean {
        /**
         * id : 959
         * f_id : 0
         * class_name : 营养保健
         * class_img_1 : http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_ebdc7398dc797e4947789a0087d25909.png
         * class_img_2 : http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_86da0d18bd3755a2a8c194c49ee5ff8a.png
         * sort : 1
         */

        @SerializedName("id")
        public String id;
        @SerializedName("f_id")
        public String fId;
        @SerializedName("class_name")
        public String className;
        @SerializedName("class_img_1")
        public String classImg1;
        @SerializedName("class_img_2")
        public String classImg2;
        @SerializedName("sort")
        public String sort;
    }
}
