package com.abcs.haiwaigou.local.beans;

import java.io.Serializable;
import java.util.List;

public class City2 implements Serializable {

    /**
     * timestamp : 1479901467057
     * result : 1
     * body : {"data":[{"area_id":12,"countrys":[{"citys":[{"city_id":41,"catename_en":"WIEN","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1562.jpg","cate_name":"维也纳（首都）"},{"city_id":6561,"catename_en":"LINZ","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1567.jpg","cate_name":"林茨"},{"city_id":6575,"catename_en":"SALZBURG","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1563.jpg","cate_name":"萨尔茨堡"},{"city_id":6547,"catename_en":"GRAZ","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1570.jpg","cate_name":"格拉茨"}],"country_id":198,"country_name":"奥地利"},{"citys":[{"city_id":6930,"catename_en":"Warsaw","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1740.jpg","cate_name":"华沙 （首都）"},{"city_id":9806,"catename_en":"Poznan","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1744.jpg","cate_name":"波兹南"},{"city_id":9805,"catename_en":"Wroclaw","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1743.jpg","cate_name":"弗罗茨瓦夫"},{"city_id":6957,"catename_en":"Gdansk","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1742.jpg","cate_name":"格但斯克"},{"city_id":6926,"catename_en":"Gdynia","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1747.jpg","cate_name":"格丁尼亚"},{"city_id":6936,"catename_en":"Krakow","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1741.jpg","cate_name":"克拉科夫"},{"city_id":6943,"catename_en":"Lublin","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1753.jpg","cate_name":"卢布林"},{"city_id":6953,"catename_en":"Sopot","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1745.jpg","cate_name":"索波特"},{"city_id":100388,"catename_en":"Chorzow","oss_url":"https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=764027112,1132867827&fm=58","cate_name":"霍茹夫"}],"country_id":427,"country_name":"波兰"}],"area_cn_name":"欧洲"}]}
     * transactionid : null
     * version : 1.0
     */

    public String timestamp;
    public int result;
    public BodyBean body;
    public Object transactionid;
    public String version;

    public static class BodyBean {
        public List<DataBean> data;
        public List<HotCityEntry> hotCity;

        public static class DataBean {
            /**
             * area_id : 12
             * countrys : [{"citys":[{"city_id":41,"catename_en":"WIEN","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1562.jpg","cate_name":"维也纳（首都）"},{"city_id":6561,"catename_en":"LINZ","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1567.jpg","cate_name":"林茨"},{"city_id":6575,"catename_en":"SALZBURG","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1563.jpg","cate_name":"萨尔茨堡"},{"city_id":6547,"catename_en":"GRAZ","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1570.jpg","cate_name":"格拉茨"}],"country_id":198,"country_name":"奥地利"},{"citys":[{"city_id":6930,"catename_en":"Warsaw","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1740.jpg","cate_name":"华沙 （首都）"},{"city_id":9806,"catename_en":"Poznan","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1744.jpg","cate_name":"波兹南"},{"city_id":9805,"catename_en":"Wroclaw","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1743.jpg","cate_name":"弗罗茨瓦夫"},{"city_id":6957,"catename_en":"Gdansk","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1742.jpg","cate_name":"格但斯克"},{"city_id":6926,"catename_en":"Gdynia","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1747.jpg","cate_name":"格丁尼亚"},{"city_id":6936,"catename_en":"Krakow","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1741.jpg","cate_name":"克拉科夫"},{"city_id":6943,"catename_en":"Lublin","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1753.jpg","cate_name":"卢布林"},{"city_id":6953,"catename_en":"Sopot","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1745.jpg","cate_name":"索波特"},{"city_id":100388,"catename_en":"Chorzow","oss_url":"https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=764027112,1132867827&fm=58","cate_name":"霍茹夫"}],"country_id":427,"country_name":"波兰"}]
             * area_cn_name : 欧洲
             */

            public int area_id;
            public String area_cn_name;
            public List<CountrysBean> countrys;

            public static class CountrysBean {
                /**
                 * citys : [{"city_id":41,"catename_en":"WIEN","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1562.jpg","cate_name":"维也纳（首都）"},{"city_id":6561,"catename_en":"LINZ","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1567.jpg","cate_name":"林茨"},{"city_id":6575,"catename_en":"SALZBURG","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1563.jpg","cate_name":"萨尔茨堡"},{"city_id":6547,"catename_en":"GRAZ","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1570.jpg","cate_name":"格拉茨"}]
                 * country_id : 198
                 * country_name : 奥地利
                 * national_flag : tubaio
                 */

                public int country_id;
                public String country_name;
                public String national_flag;
                public List<CitysBean> citys;

                public static class CitysBean {
                    /**
                     * city_id : 41
                     * catename_en : WIEN
                     * oss_url : http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1562.jpg
                     * cate_name : 维也纳（首都）
                     */

                    public int city_id;
                    public String catename_en;
                    public String oss_url;
                    public String cate_name;
                }
            }

            @Override
            public String toString() {
                return "DataBean{" +
                        "area_id=" + area_id +
                        ", area_cn_name='" + area_cn_name + '\'' +
                        ", countrys=" + countrys +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "BodyBean{" +
                    "data=" + data +
                    '}';
        }
    }
}
