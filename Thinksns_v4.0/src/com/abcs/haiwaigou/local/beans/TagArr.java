package com.abcs.haiwaigou.local.beans;

import com.google.gson.annotations.SerializedName;

/**
 * 项目名称：com.abcs.haiwaigou.local.beans
 * 作者：zds
 * 时间：2017/3/22 18:53
 */

public class TagArr {
    /**
     * tag_id : 1
     * tag_name : 新鲜到货
     * img : null
     */

    @SerializedName("tag_id")
    public String tagId;
    @SerializedName("tag_name")
    public String tagName;
    @SerializedName("img")
    public String img;


    @Override
    public boolean equals(Object o) {
        TagArr tagArr=(TagArr)o;
        if(tagArr.tagId.equals(this.tagId)){
            return true;
        }else {
            return false;
        }
    }
}
