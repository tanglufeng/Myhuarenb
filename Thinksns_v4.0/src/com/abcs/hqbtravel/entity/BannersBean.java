package com.abcs.hqbtravel.entity;/**
 * Created by Administrator on 2016/11/21.
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * User: zds
 * Date: 2016-11-21
 * Time: 12:25
 * FIXME
 */
public class BannersBean implements Serializable {
    @SerializedName("id")
    public String id;
    @SerializedName("url")
    public String url;

    public BannersBean(String url) {
        this.url = url;
    }
}
