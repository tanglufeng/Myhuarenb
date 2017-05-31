package com.abcs.hqbtravel.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/1/11.
 */
public class SanBiDetialsPic implements Serializable {

    /**
     * timestamp : 1484050579037
     * body : {"items":[{"oss_url":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_04ad0a40e4e9b80ba3476a5753dd94a0.jpg","user_name":"大灰狼来了"},{"oss_url":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_397ac7896a7b948356b2fe21f74e74dc.jpg","user_name":"地心引力"},{"oss_url":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_9784df54fd297350f814eaa9841c0b5e.jpg","user_name":"liguos"},{"oss_url":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_f72cf5fc138a4742c612859fffd0bcf9.jpg","user_name":"地心引力"},{"oss_url":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_ecd6d2d9446ff90bcc57b28b89d6e333.jpg","user_name":"王纯杰"},{"oss_url":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_69d920ffee72a604c805a98e74e42523.jpg","user_name":"王纯杰"},{"oss_url":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_abb8e0e4f64e7b304e64709dfc6efc53.jpg","user_name":"王纯杰"},{"oss_url":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_979ec9d31ee98d3f5f1690e76de522dd.jpg","user_name":"王纯杰"},{"oss_url":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_ecd6d2d9446ff90bcc57b28b89d6e333.jpg","user_name":"王纯杰"},{"oss_url":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_69d920ffee72a604c805a98e74e42523.jpg","user_name":"王纯杰"}]}
     * result : 1
     * transactionid :
     * version : 1.0
     * info : OK
     */

    @SerializedName("timestamp")
    public String timestamp;
    @SerializedName("body")
    public BodyBean body;
    @SerializedName("result")
    public int result;
    @SerializedName("transactionid")
    public String transactionid;
    @SerializedName("version")
    public String version;
    @SerializedName("info")
    public String info;

    public static class BodyBean  implements Serializable{
        @SerializedName("items")
        public List<CommentItem> items;

        public static class CommentItem implements Serializable{
            /**
             * oss_url : http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_04ad0a40e4e9b80ba3476a5753dd94a0.jpg
             * user_name : 大灰狼来了
             */

            @SerializedName("oss_url")
            public String oss_url;
            @SerializedName("user_name")
            public String user_name;
        }
    }
}
