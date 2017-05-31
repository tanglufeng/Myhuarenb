package com.abcs.haiwaigou.local.beans;/**
 * Created by Administrator on 2016/12/27.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * User: zds
 * Date: 2016-12-27
 * Time: 14:49
 * FIXME
 */
public class HireFindNoQu {


    /**
     * status : 1
     * msg : [{"pubTime":1479550904,"contactMan":"","id":58541,"title":"NOODLE KING","views":0,"ads":"Lassallestraße 36 A-1020 Wien","contact":"0676-9314868"},{"pubTime":1479550903,"contactMan":"","id":58537,"title":"天赐饭店 Gedeihen","views":0,"ads":"Engerthstraße 237 A-1020 Wien","contact":"01-7283715"},{"pubTime":1479550903,"contactMan":"","id":58538,"title":"Tokori","views":0,"ads":"Franz-Hochedlinger-Gasse 2 A-1020 Wien","contact":"01-2148940"},{"pubTime":1479550903,"contactMan":"","id":58539,"title":"大象饭店 Elefant","views":0,"ads":"Glockengasse 24 A-1020 Wien","contact":"01-2143998"},{"pubTime":1479550902,"contactMan":"","id":58535,"title":"Happy Noodles","views":0,"ads":"Bahnhof wien-Nord B A-1020 Wien","contact":"0676-84430820"},{"pubTime":1479550902,"contactMan":"","id":58536,"title":"欣欣饭店Zum Goldenen Panda","views":0,"ads":"Engerthstraße 159 A-1020 Wien","contact":"01-2120201"},{"pubTime":1479550901,"contactMan":"","id":58533,"title":"IKO","views":0,"ads":"Wipplingerstraße 6 A-1010 Wien","contact":"01-8904200"},{"pubTime":1479550901,"contactMan":"","id":58534,"title":"Wasabi Asia Grillhaus","views":0,"ads":"Ausstellungstraße 23 A-1020 Wien","contact":"01-9253317"},{"pubTime":1479550900,"contactMan":"","id":58530,"title":"Bento Box","views":0,"ads":"Vorlaufstraße 2 A-1010 Wien","contact":"01-5354387"},{"pubTime":1479550900,"contactMan":"","id":58531,"title":"赤阪饭店AKASAKA","views":0,"ads":"Vorlaufstraße 5 A-1010 Wien","contact":"01-5339656"}]
     */

    @SerializedName("status")
    public String status;
    @SerializedName("msg")
    public List<MsgBean> msg;

    public static class MsgBean {
        /**
         * pubTime : 1479550904
         * contactMan :
         * id : 58541
         * title : NOODLE KING
         * views : 0
         * ads : Lassallestraße 36 A-1020 Wien
         * contact : 0676-9314868
         */

        @SerializedName("pubTime")
        public long pubTime;
        @SerializedName("contactMan")
        public String contactMan;
        @SerializedName("id")
        public int id;
        @SerializedName("title")
        public String title;
        @SerializedName("views")
        public int views;
        @SerializedName("ads")
        public String ads;
        @SerializedName("contact")
        public String contact;
    }
}
