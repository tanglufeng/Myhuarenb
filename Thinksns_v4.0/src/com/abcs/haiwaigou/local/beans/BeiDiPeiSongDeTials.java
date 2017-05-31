package com.abcs.haiwaigou.local.beans;/**
 * Created by Administrator on 2017/1/12.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * User: zds
 * Date: 2017-01-12
 * Time: 18:37
 * FIXME
 */
public class BeiDiPeiSongDeTials {
    /**
     * state : 1
     * datas : [{"goods_id":"103546","goods_name":"本地测试商品","goods_en_name":"CP Ente","goods_serial":"14123","goods_image":"11_05424015161102321.jpg","goods_price":"1.00","goods_storage":"1","activity_arr":[{"types":"1","activity_name":"满35减7，满55减11（买单立享）","img":null},{"types":"2","activity_name":"新用户下单立减","img":null}],"tag_arr":[{"tag_id":"1","tag_name":"新鲜到货","img":null},{"tag_id":"2","tag_name":"泰国产","img":null}]}]
     * page_count : 1
     */

    @SerializedName("state")
    public int state;
    @SerializedName("page_count")
    public int pageCount;
    @SerializedName("datas")
    public List<HuoHangItem> datas;
}

