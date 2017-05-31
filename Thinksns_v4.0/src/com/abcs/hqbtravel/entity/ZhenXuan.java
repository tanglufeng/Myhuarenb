package com.abcs.hqbtravel.entity;/**
 * Created by Administrator on 2017/2/28.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * User: zds
 * Date: 2017-02-28
 * Time: 17:54
 * FIXME
 */
public class ZhenXuan {

    /**
     * result : 1
     * body : [{"id":10,"title":"美国L2R夏令营2017","html_url":"http://c.eqxiu.com/s/QpZFS1GY?eqrcode=1&from=groupmessage&isappinstalled=0","img":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb_abc_img%2F%E7%BE%8E%E5%9B%BDL2R%E5%A4%8F%E4%BB%A4%E8%90%A520171488272479826.png"},{"id":11,"title":"希伯来智慧与财富探秘","html_url":"http://b.eqxiu.com/s/RNtbjsMX?from=singlemessage&isappinstalled=0","img":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb_abc_img%2F%E5%B8%8C%E4%BC%AF%E6%9D%A5%E6%99%BA%E6%85%A7%E4%B8%8E%E8%B4%A2%E5%AF%8C%E6%8E%A2%E7%A7%981488272481577.png"},{"id":9,"title":"\tHola,西班牙艺术夏令营","html_url":"http://url.cn/45QzcIa","img":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb_abc_img%2FHola%2C%E8%A5%BF%E7%8F%AD%E7%89%99%E8%89%BA%E6%9C%AF%E5%A4%8F%E4%BB%A4%E8%90%A51488272477728.png"},{"id":12,"title":"寻访财富与智慧的以色列","html_url":"http://url.cn/45Qwa6y","img":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb_abc_img%2F%E5%AF%BB%E8%AE%BF%E8%B4%A2%E5%AF%8C%E4%B8%8E%E6%99%BA%E6%85%A7%E7%9A%84%E4%BB%A5%E8%89%B2%E5%88%971488272483044.png"}]
     * info : success!
     */

    @SerializedName("result")
    public int result;
    @SerializedName("info")
    public String info;
    @SerializedName("body")
    public List<BodyEntry> body;

    public static class BodyEntry {
        /**
         * id : 10
         * title : 美国L2R夏令营2017
         * html_url : http://c.eqxiu.com/s/QpZFS1GY?eqrcode=1&from=groupmessage&isappinstalled=0
         * img : http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb_abc_img%2F%E7%BE%8E%E5%9B%BDL2R%E5%A4%8F%E4%BB%A4%E8%90%A520171488272479826.png
         */

        @SerializedName("id")
        public int id;
        @SerializedName("title")
        public String title;
        @SerializedName("html_url")
        public String htmlUrl;
        @SerializedName("img")
        public String img;
    }
}
