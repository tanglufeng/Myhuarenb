package com.abcs.haiwaigou.model;/**
 * Created by Administrator on 2016/11/26.
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * User: zds
 * Date: 2016-11-26
 * Time: 17:16
 * FIXME
 */
public class VipBean implements Serializable{


    /**
     * code : 200
     * datas : {"vip_card":[{"id":"2","level_id":"2","level_name":"尊贵金卡","money":"500.00","continuous_time":"31536000","rebate_percent":"2.00","description":"这是外部描述","dudao":"0","introduce":"这是内部描述"},{"id":"3","level_id":"3","level_name":"奢华铂金卡","money":"1280.00","continuous_time":"31536000","rebate_percent":"3.00","description":"这是外部描述","dudao":"0","introduce":"这是内部描述"},{"id":"4","level_id":"4","level_name":"合伙人黑卡","money":"12680.00","continuous_time":"31536000","rebate_percent":"5.00","description":"这是外部描述","dudao":"0","introduce":"这是内部描述"}],"member":{"id":"1005","member_id":"1028","member_name":"cy3329520","level_id":"1","level_name":"普卡会员","add_time":"1479377343","end_time":"1510913343"}}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("datas")
    public DatasBean datas;

    public static class DatasBean implements Serializable {
        /**
         * vip_card : [{"id":"2","level_id":"2","level_name":"尊贵金卡","money":"500.00","continuous_time":"31536000","rebate_percent":"2.00","description":"这是外部描述","dudao":"0","introduce":"这是内部描述"},{"id":"3","level_id":"3","level_name":"奢华铂金卡","money":"1280.00","continuous_time":"31536000","rebate_percent":"3.00","description":"这是外部描述","dudao":"0","introduce":"这是内部描述"},{"id":"4","level_id":"4","level_name":"合伙人黑卡","money":"12680.00","continuous_time":"31536000","rebate_percent":"5.00","description":"这是外部描述","dudao":"0","introduce":"这是内部描述"}]
         * member : {"id":"1005","member_id":"1028","member_name":"cy3329520","level_id":"1","level_name":"普卡会员","add_time":"1479377343","end_time":"1510913343"}
         */

        @SerializedName("member")
        public MemberBean member;
        @SerializedName("vip_card")
        public List<VipCardBean> vipCard;

        public static class MemberBean implements Serializable{
          /*  "id":"1005",
                    "member_id":"1028",
                    "member_name":"cy3329520",
                    "total_vip_money":"1296.62",
                    "level_id":"3",
                    "level_name":"贵宾白金卡",
                    "credit":"0.00",
                    "add_time":"1481274402",
                    "end_time":"1512810402"*/

            @SerializedName("id")
            public String id;
            @SerializedName("max_desc")
            public String max_desc;
            @SerializedName("member_id")
            public String memberId;
            @SerializedName("member_name")
            public String memberName;
            @SerializedName("level_id")
            public String levelId;
            @SerializedName("total_vip_money")
            public double total_vip_money;
            @SerializedName("level_name")
            public String levelName;
            @SerializedName("add_time")
            public String addTime;
            @SerializedName("credit")
            public String credit;
            @SerializedName("end_time")
            public String endTime;
        }

        public static class VipCardBean  implements Serializable{
          /*  "id":"4",
                    "level_id":"4",
                    "level_name":"合伙人黑卡",
                    "money":"12680.00",
                    "continuous_time":"31536000",
                    "rebate_percent":"5.00",
                    "description":"私人管家响应您的一切需求",
                    "dudao":"0",
                    "introduce":"1、购买会员后立即赠送12680购物金，12680点会员升级信用点 2、消费享受黑卡会员折扣价 3、年尾按年度消费金额5%返还购物金 4、可购买会员特典商品及服务 5、充值金额可累计计入会员升级信用 ",
                    "notice":"1、用于享受会员特权及商品优惠。 2、限一卡一账户。 3、余额可转赠。"*/

            @SerializedName("id")
            public String id;
            @SerializedName("level_id")
            public String levelId;
            @SerializedName("level_name")
            public String levelName;
            @SerializedName("money")
            public double money;
            @SerializedName("continuous_time")
            public String continuousTime;
            @SerializedName("rebate_percent")
            public String rebatePercent;
            @SerializedName("description")
            public String description;
            @SerializedName("dudao")
            public String dudao;
            @SerializedName("introduce")
            public String introduce;
            @SerializedName("notice")
            public String notice;
        }
    }
}
