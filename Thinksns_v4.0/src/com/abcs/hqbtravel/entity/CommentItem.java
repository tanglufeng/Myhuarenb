package com.abcs.hqbtravel.entity;/**
 * Created by Administrator on 2017/1/7.
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * User: zds
 * Date: 2017-01-07
 * Time: 12:12
 * FIXME
 */
public class CommentItem implements Serializable {

    @SerializedName("comment")
    public String comment;
    @SerializedName("create_time")
    public long createTime;
    @SerializedName("score")
    public int score;
    @SerializedName("u_id")
    public int uId;
    @SerializedName("name")
    public String name;
    @SerializedName("avatar")
    public String avatar;
    @SerializedName("imgs")
    public ArrayList<String> imgs;

}
