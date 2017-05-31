package com.abcs.hqbtravel.wedgt;


import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

/**
 * Created by Administrator on 2016/9/29.
 */
public class Config {

    /**百度地图AK**/
    public static final String BAIDU_AK = "5Dw6oUDCIiA3yKZTG05hfm8DuvHZEt8L";
    /**百度地图安全码 mcode**/
    public static final String BAIDU_MCODE = "00:17:47:EB:D0:0E:9F:5A:7B:BF:F5:B8:C6:50:22:3F:82:89:7B:52;com.abcs.huaqiaobang";
    /**获取各大洲**/
    public static final String GET_AREA = TLUrl.getInstance().URL_bind_base+"/hrq/City/getArea.json";
    /**获取各国家**/
    public static final String GET_COUNTRY = "https://japi.tuling.me/hrq/City/getCountryListByAreaId.json?areaId=";
//    public static final String GET_COUNTRY = "https://japi.tuling.me/hrq/City/getCountryListByAreaId.json?areaId=";   //旧版的
}
