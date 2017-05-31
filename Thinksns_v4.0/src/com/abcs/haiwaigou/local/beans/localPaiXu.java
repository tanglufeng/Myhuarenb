package com.abcs.haiwaigou.local.beans;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：com.abcs.haiwaigou.local.beans
 * 作者：zds
 * 时间：2017/3/22 18:07
 */

public class localPaiXu {

    @SerializedName("state")
    public int state;
    @SerializedName("datas")
    public List<DatasEntry> datas;

    public static class DatasEntry {

        @SerializedName("goods_letter")
        public String goodsLetter;
        @SerializedName("goods_list")
        public List<HuoHangItem> goodsList=new ArrayList<>();
    }
}
