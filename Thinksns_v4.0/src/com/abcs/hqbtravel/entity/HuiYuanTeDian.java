package com.abcs.hqbtravel.entity;/**
 * Created by Administrator on 2017/1/5.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * User: zds
 * Date: 2017-01-05
 * Time: 12:15
 * FIXME
 */
public class HuiYuanTeDian {

    /**
     * vip_td_list : [{"id":"3","td_id":"3","city_id":"55","img":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_a71c7674f7f0adf6a714611e6a84e931.png","td_name":"高尔夫","intro":"简介","td_cn_name":"您的高尔夫记忆","td_en_name":"Golf Trip","td_desc":null,"td_desc_img":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_5f76ec89e02ff77466354ecf14378eb9.jpg","jump_id":"62","goods_id":"0","state":"1","type":"1.2","sort":"1"},{"id":"6","td_id":"6","city_id":"55","img":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_1268cf1b8324bc07627adcbcf88f237b.png","td_name":"私人管家","intro":null,"td_cn_name":"伴您留下美好回忆","td_en_name":"Local Guider","td_desc":null,"td_desc_img":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_4ab180c3ba37d3368e63a52332dee7b8.jpg","jump_id":"64","goods_id":"0","state":"1","type":"1.2","sort":"4"},{"id":"9","td_id":"9","city_id":"55","img":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_7492a1b79a10b239c2fc3fbd3e180842.png","td_name":"贴身服务","intro":null,"td_cn_name":"随时随地,替您解决一切问题","td_en_name":"Private Housekeeper","td_desc":null,"td_desc_img":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_60468058603eb0e5521cb64aeb2aa429.jpg","jump_id":"67","goods_id":"0","state":"1","type":"1.2","sort":"7"},{"id":"10","td_id":"10","city_id":"55","img":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_8573d2aab266f5d3ed590e2aaa8db116.png","td_name":"政要通道","intro":null,"td_cn_name":"您的尊贵机场特殊通道","td_en_name":"Airport CIP Termminal","td_desc":null,"td_desc_img":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_adc6cceb085b219bb12178bc77c20c2c.jpg","jump_id":"63","goods_id":"0","state":"1","type":"1.2","sort":"8"}]
     * state : 1
     */

    @SerializedName("state")
    public int state;
    @SerializedName("vip_td_list")
    public List<VipTdListBean> vipTdList;

    public static class VipTdListBean {
        /**
         * id : 3
         * td_id : 3
         * city_id : 55
         * img : http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_a71c7674f7f0adf6a714611e6a84e931.png
         * td_name : 高尔夫
         * intro : 简介
         * td_cn_name : 您的高尔夫记忆
         * td_en_name : Golf Trip
         * td_desc : null
         * td_desc_img : http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_5f76ec89e02ff77466354ecf14378eb9.jpg
         * jump_id : 62
         * goods_id : 0
         * state : 1
         * type : 1.2
         * sort : 1
         */

        @SerializedName("id")
        public String id;
        @SerializedName("td_id")
        public String tdId;
        @SerializedName("city_id")
        public String cityId;
        @SerializedName("img")
        public String img;
        @SerializedName("td_name")
        public String tdName;
        @SerializedName("intro")
        public String intro;
        @SerializedName("td_cn_name")
        public String tdCnName;
        @SerializedName("td_en_name")
        public String tdEnName;
        @SerializedName("td_desc")
        public Object tdDesc;
        @SerializedName("td_desc_img")
        public String tdDescImg;
        @SerializedName("jump_id")
        public String jumpId;
        @SerializedName("goods_id")
        public String goodsId;
        @SerializedName("state")
        public String state;
        @SerializedName("type")
        public String type;
        @SerializedName("sort")
        public String sort;
    }
}
