package com.abcs.haiwaigou.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 项目名称：com.abcs.haiwaigou.model
 * 作者：zds
 * 时间：2017/4/7 19:00
 */

public class HDong2 {

    /**
     * state : 1
     * datas : [{"title":"精选特卖","list":[{"many_id":"1494","plate":"58","flag":"8","type":"5","depict":"103478","image":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_692846107cb3fd07b6590334a44e06f8.jpg","many_image":"","desc_ribe":"","title":"比利时蜂蜜","sort":"0","advert_sort":"0","advert_goods":"","other":"0"},{"many_id":"1534","plate":"58","flag":"8","type":"1","depict":"泰国茉莉香米泰国原粮","image":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_2d15e844824428ad31ef5185c63332fd.jpg","many_image":"","desc_ribe":"泰国元实茉莉香米选用泰国纯天然大米，得天独厚的充足日照加上高原气候带来的巨大温差，使香米在充足的光合作用下充分沉淀，很大程度提升芳香成分与甜度。淡淡清香透人心扉，阵阵清香渺渺炊烟让你重回宁静自然。","title":"泰国茉莉香米专场","sort":null,"advert_sort":"0","advert_goods":"","other":"0"}]},{"title":"每周上新","list":[{"many_id":"1527","plate":"58","flag":"2","type":"1","depict":"意大利Trudi特鲁迪","image":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_bf60f4b129b05d7b051a02299936ae1c.jpg","many_image":"","desc_ribe":"","title":"特鲁迪专区","sort":null,"advert_sort":"0","advert_goods":"","other":"0"},{"many_id":"1525","plate":"58","flag":"2","type":"1","depict":"葡萄牙Oreo奥利奥","image":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_97859bdd9d7cbe209608bc1889654805.jpg","many_image":"","desc_ribe":"","title":"奥利奥专区","sort":null,"advert_sort":"0","advert_goods":"","other":"0"},{"many_id":"1526","plate":"58","flag":"2","type":"1","depict":"德国Schwarzkopf施华蔻","image":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_188cf58baa4bdbda5f93460dad3b413d.jpg","many_image":"","desc_ribe":"","title":"施华蔻洗发液专区","sort":null,"advert_sort":"0","advert_goods":"","other":"0"}]}]
     */

    @SerializedName("state")
    public int state;
    @SerializedName("datas")
    public List<DatasEntry> datas;

    public static class DatasEntry {
        /**
         * title : 精选特卖
         * list : [{"many_id":"1494","plate":"58","flag":"8","type":"5","depict":"103478","image":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_692846107cb3fd07b6590334a44e06f8.jpg","many_image":"","desc_ribe":"","title":"比利时蜂蜜","sort":"0","advert_sort":"0","advert_goods":"","other":"0"},{"many_id":"1534","plate":"58","flag":"8","type":"1","depict":"泰国茉莉香米泰国原粮","image":"http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_2d15e844824428ad31ef5185c63332fd.jpg","many_image":"","desc_ribe":"泰国元实茉莉香米选用泰国纯天然大米，得天独厚的充足日照加上高原气候带来的巨大温差，使香米在充足的光合作用下充分沉淀，很大程度提升芳香成分与甜度。淡淡清香透人心扉，阵阵清香渺渺炊烟让你重回宁静自然。","title":"泰国茉莉香米专场","sort":null,"advert_sort":"0","advert_goods":"","other":"0"}]
         */

        @SerializedName("title")
        public String title;
        @SerializedName("list")
        public List<HDong2Item> list;

    }
}
