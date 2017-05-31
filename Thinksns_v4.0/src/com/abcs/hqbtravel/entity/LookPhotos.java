package com.abcs.hqbtravel.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/11/9.
 */

public class LookPhotos {


    /**
     * body : [{"logo":"http://pic1.qyer.com/album/user/370/89/Qk5VShMOaA/index/w800"},{"logo":"http://pic4.qyer.com/album/user/213/10/Q0hWQxoFYw/index/w800"},{"logo":"http://pic.qyer.com/album/user/213/10/Q0hWQxoGYg/index/w800"},{"logo":"http://pic.qyer.com/album/163/6e/1857093/index/w800"},{"logo":"http://pic3.qyer.com/album/169/e3/1857082/index/w800"}]
     * result : 1
     * timestamp : 1478674739286
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
    public Object transactionid;
    /**
     * logo : http://pic1.qyer.com/album/user/370/89/Qk5VShMOaA/index/w800
     */

    @SerializedName("body")
    public List<BodyBean> body;

    public static class BodyBean {
        @SerializedName("logo")
        public String logo;
    }
}
