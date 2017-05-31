package com.abcs.hqbtravel.wedgt;

import java.util.List;

public class ShaiXuanBean {

    /**
     * timestamp : 1484976579916
     * body : [{"areaId":1,"area_name":"暹罗广场"},{"areaId":2,"area_name":"大皇宫/考山路"},{"areaId":3,"area_name":"素坤逸路"},{"areaId":4,"area_name":"是隆路"},{"areaId":5,"area_name":"水门"},{"areaId":6,"area_name":"曼谷近郊"},{"areaId":7,"area_name":"无线/齐隆"},{"areaId":8,"area_name":"乍都节周末市集"},{"areaId":9,"area_name":"廊曼机场/国际展览区"},{"areaId":10,"area_name":"中国城"},{"areaId":11,"area_name":"湄南河畔"},{"areaId":12,"area_name":"拉差达能路"}]
     * result : 1
     * transactionid :
     * version : 1.0
     * info : OK
     */

    public String timestamp;
    public int result;
    public String transactionid;
    public String version;
    public String info;
    public List<BodyBean> body;

    public static class BodyBean {
        /**
         * areaId : 1
         * area_name : 暹罗广场
         */

        public int areaId;
        public String area_name;

        @Override
        public String toString() {
            return "BodyBean{" +
                    "areaId=" + areaId +
                    ", area_name='" + area_name + '\'' +
                    '}';
        }
    }


}
