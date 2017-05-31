package com.abcs.hqbtravel.entity;/**
 * Created by Administrator on 2017/1/4.
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * User: zds
 * Date: 2017-01-04
 * Time: 15:11
 * FIXME
 */
public class VIPGG implements Serializable{

    /**
     * state : 1
     * datas : [{"id":"2","advert_id":"2","advert_state":"1","advert_img":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_94f058e61ec85d8c3339b94a51b8bed0.png","advert_desc":"广告1","sort":"1","flag":"1","flag_desc":"1"},{"id":"3","advert_id":"3","advert_state":"1","advert_img":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_7d10e9c67b87361b61864da6080e2aa8.png","advert_desc":"广告2","sort":"2","flag":"1","flag_desc":"1"},{"id":"4","advert_id":"4","advert_state":"1","advert_img":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_5b3791099f0b3cd40db31a67182a36bc.png","advert_desc":"广告3","sort":"3","flag":"1","flag_desc":"1"},{"id":"5","advert_id":"5","advert_state":"1","advert_img":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_4dd07dd280cd91469a7d4109d67570f9.png","advert_desc":"广告4","sort":"4","flag":"1","flag_desc":"1"}]
     */

    @SerializedName("state")
    public int state;
    @SerializedName("datas")
    public List<DatasBean> datas;

    public static class DatasBean {
        /**
         * id : 2
         * advert_id : 2
         * advert_state : 1
         * advert_img : http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_94f058e61ec85d8c3339b94a51b8bed0.png
         * advert_desc : 广告1
         * sort : 1
         * flag : 1
         * flag_desc : 1
         */

        @SerializedName("id")
        public String id;
        @SerializedName("advert_id")
        public String advertId;
        @SerializedName("advert_state")
        public String advertState;
        @SerializedName("advert_img")
        public String advertImg;
        @SerializedName("advert_desc")
        public String advertDesc;
        @SerializedName("sort")
        public String sort;
        @SerializedName("flag")
        public String flag;
        @SerializedName("flag_desc")
        public String flagDesc;
    }
}
