package com.abcs.hqbtravel.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class BiWanDetials {



    @SerializedName("body")
    public BodyBean body;
    @SerializedName("result")
    public int result;
    @SerializedName("timestamp")
    public String timestamp;
    @SerializedName("version")
    public String version;
    @SerializedName("transactionid")
    public Object transactionid;

    public static class BodyBean {


        @SerializedName("ifCollection")
        public int ifCollection;
        @SerializedName("id")
        public String id;
        @SerializedName("noteCount")
        public int noteCount;
        @SerializedName("detail")
        public DetailBean detail;
        @SerializedName("comments")
        public int comments;
        @SerializedName("name")
        public String name;
        @SerializedName("star")
        public double star;
        @SerializedName("type")
        public int type;
        @SerializedName("shops")
        public List<ShopBean> shops;
        @SerializedName("restaurants")
        public List<RestauransBean> restaurants;
        @SerializedName("touristAttractions")
        public List<TouristAttractionsBean> touristAttractions;
        @SerializedName("tickets")
        public List<MyTickets> tickets;
        @SerializedName("tags")
        public List<String> tags;
        @SerializedName("banners")
        public List<BannersBean> banners;

        public static class DetailBean {
            /**
             * cn_name :
             * en_name :
             * local_name :
             * voice_url :
             * id : 35258
             * detail_id : 1207769
             * introduction : 在泰国水疗集团 Oasis Spa 来一趟感官享受之旅，体验以泰北的兰纳风情为主题的Thai Lanna Spa Experience 疗程，让身心焕发新生活力，获得健康新感受。
             当您走进任何一个位置的 Oasis Spa，您都将会把城市生活的喧嚣，工作的压力抛之脑后。
             Oasis Spa在泰国拥有众多水疗中心分馆，这里的每种特色疗程服务都是由受过严格培训的理疗师为您提供的。在她们的悉心呵护下，您的身体进会被带入一个完全放松宁静的新境界。
             我们诚挚的邀请您前来体验曾荣获 “亚洲水疗大奖 — 年度最佳水疗中心奖”的 Oasis Spa 的过人之处。
             * lat : 18.7952345000
             * lng : 98.9355737000
             * address : 清迈素贴山山脚
             * phone :
             * site :
             * open_time :
             * way_to :
             * tips : 每个房间都备有独立的保险柜和首饰盒，在进行Spa疗程之前请先将戒指、项链等首饰摘下，放在首饰盒中，贵重物品可以自设保险箱密码并存放在内。 在Spa正式开始之前，服务员会为您提供问卷表，让您选择。如果您身体有特别需要注意的地方也请通知工作人员，如：怀孕，经期，腰间盘突出等。
             * price : 0
             * chinese_name :
             * smy_audio_id : -1
             */

            @SerializedName("cn_name")
            public String cnName;
            @SerializedName("en_name")
            public String enName;
            @SerializedName("local_name")
            public String localName;
            @SerializedName("voice_url")
            public String voiceUrl;
            @SerializedName("id")
            public String id;
            @SerializedName("detail_id")
            public String detailId;
            @SerializedName("introduction")
            public String introduction;
            @SerializedName("lat")
            public String lat;
            @SerializedName("lng")
            public String lng;
            @SerializedName("address")
            public String address;
            @SerializedName("phone")
            public String phone;
            @SerializedName("site")
            public String site;
            @SerializedName("open_time")
            public String openTime;
            @SerializedName("way_to")
            public String wayTo;
            @SerializedName("tips")
            public String tips;
            @SerializedName("price")
            public String price;
            @SerializedName("chinese_name")
            public String chineseName;
            @SerializedName("smy_audio_id")
            public String smyAudioId;
        }


    }
}
