package com.abcs.hqbtravel.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 项目名称：com.abcs.hqbtravel.entity
 * 作者：zds
 * 时间：2017/4/18 14:18
 */

public class Notice {

    /**
     * state : 1
     * datas : [{"id":"1","member_id":"1028","message_title":"测试消息","message_details":"消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情\r\n消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情","state":"1","del_state":"0","add_time":"1492485475"}]
     * page_count : 1
     */

    @SerializedName("state")
    public int state;
    @SerializedName("page_count")
    public int pageCount;
    @SerializedName("datas")
    public List<DatasEntry> datas;

    public static class DatasEntry  {
        /**
         * id : 1
         * member_id : 1028
         * message_title : 测试消息
         * message_details : 消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情
         消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情消息详情
         * state : 1
         * del_state : 0
         * add_time : 1492485475
         */

        @SerializedName("id")
        public int id;
        @SerializedName("member_id")
        public String memberId;
        @SerializedName("message_title")
        public String messageTitle;
        @SerializedName("message_details")
        public String messageDetails;
        @SerializedName("state")
        public int state;
        @SerializedName("del_state")
        public String delState;
        @SerializedName("add_time")
        public long addTime;
    }
}
