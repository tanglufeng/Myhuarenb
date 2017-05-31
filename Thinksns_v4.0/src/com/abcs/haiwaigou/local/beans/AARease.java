package com.abcs.haiwaigou.local.beans;/**
 * Created by Administrator on 2016/12/22.
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * User: zds
 * Date: 2016-12-22
 * Time: 11:34
 * FIXME
 */
public class AARease implements Serializable {
//
//    "category_id":88101,
//            "name":"上奥地利州4020区"

    @SerializedName("category_id")
    public String categoryId;
    @SerializedName("name")
    public String name;
}
