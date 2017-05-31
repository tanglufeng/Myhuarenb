package com.abcs.haiwaigou.local.beans;/**
 * Created by Administrator on 2016/12/19.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * User: zds
 * Date: 2016-12-19
 * Time: 18:03
 * FIXME
 */
public class RestauDetisl {


    /**
     * status : 1
     * msg : [{"title":"金阁饭店Goldene Pagode","ads":"Adalbert-Stifter-Platz 2 A-4020 Linz","contact":"0732-771031"},{"title":"City Wok","ads":"Blumauerstraße 9 A-4020 Linz","contact":"0732-944228"},{"title":"华府饭店Palast","ads":"Eisenhandstraße 30 A-4020 Linz","contact":"0732-76880"},{"title":"Restaurant Qi","ads":"Gruberstr.21 A-4020 Linz","contact":"0732-770555"},{"title":"长城饭店China Mauer","ads":"Guertelstraße 30 A-4020 Linz","contact":"0732-601168"},{"title":"Wok&More","ads":"Hamoderstrasse 6A A-4020 Linz","contact":"0732-331679"},{"title":"金山饭店Kim San","ads":"Hauptplatz 4 A-4020 Linz","contact":"0732-771497"},{"title":"台湾饭店Schatzinsel Tai Wai","ads":"Klammstraße 3 A-4020 Linz","contact":"0732-7732840"},{"title":"星城饭店Singapore","ads":"Klosterstraße 1 A-4020 Linz","contact":"0732-782201"},{"title":"莲花饭店Lotus","ads":"Landstraße 11 A-4020 Linz","contact":"0732-771533"},{"title":"蒙古烤肉Rest.Mongole BBQ","ads":"Landstraße 12 A-4020 Linz","contact":""},{"title":"日本餐馆SAKURA","ads":"Landstraße 1 A-4020 Linz","contact":"0732-772639"},{"title":"KONTAKI","ads":"Landstraße 17-25 A-4020 Linz","contact":"0732-779858"},{"title":"Akakiko Passage Center 3.Stock","ads":"Landstraße 17-25 A-4020 Linz","contact":"057-333170"},{"title":"Tokyo Running Sushi","ads":"Landstraße 66 A-4020 Linz","contact":"07327-81554"},{"title":"KungFu","ads":"Landstraße 72-75 A-4020 Linz","contact":"0732-890555"},{"title":"中华阁China Court","ads":"Landwiedstraße 43 A-4020 Linz","contact":"07067-6384"},{"title":"金台北饭店King Tai Bei","ads":"Marienstraße 11 A-4020 Linz","contact":"0732-782822"},{"title":"Xu.Wok&More","ads":"Mozartstraße 7 A-4020 Linz","contact":"0732-234372"},{"title":"佳佳饭店Jin Lu","ads":"Pfarrplatz 11 A-4020 Linz","contact":"0732-788787"},{"title":"中华饭店","ads":"Rainerstraße 20 A-4020 Linz","contact":"0732-665880"},{"title":"府成拉面Fu Cheng","ads":"Untere Donaulände 16A A-4020 Linz","contact":"0732-775650"},{"title":"千鹤日本料理","ads":"Untere Donaulände 21-25 A-4020 Linz","contact":"0732-772779"},{"title":"武昌饭店Wu Chang","ads":"Wienerstraße 4 A-4020 Linz","contact":"0732-650380"}]
     */

    @SerializedName("status")
    public String status;
    @SerializedName("msg")
    public List<MsgBean> msg;

    public static class MsgBean {
        /**
         "pubTime": 1479795813,
         "id": 59493,
         "title": "蓬莱阁饭店Formosa PaVillon ",
         "ads": "Bauernmarkt 14 A-1010 Wien",
         "contact": "01-5353319,01-5359998"
         */

        @SerializedName("pubTime")
        public long pubTime;
        @SerializedName("id")
        public int id;
        @SerializedName("title")
        public String title;
        @SerializedName("ads")
        public String ads;
        @SerializedName("contact")
        public String contact;
    }
}
