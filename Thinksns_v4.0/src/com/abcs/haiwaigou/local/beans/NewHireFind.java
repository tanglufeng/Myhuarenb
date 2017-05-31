package com.abcs.haiwaigou.local.beans;

import com.abcs.haiwaigou.model.GGAds;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class NewHireFind {


    /**
     * status : 1
     * msg : [{"count":158,"qyer_city_id":41,"img":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1562.jpg","areas":[{"category_id":88101,"name":"上奥地利州4020区"},{"category_id":88102,"name":"上奥地利州4030区"},{"category_id":88103,"name":"上奥地利州4040区"},{"category_id":88104,"name":"上奥地利州4050区"},{"category_id":88105,"name":"上奥地利州4060区"},{"category_id":88106,"name":"上奥地利州4070区"},{"category_id":88107,"name":"上奥地利州4080区"},{"category_id":88108,"name":"上奥地利州4100区"},{"category_id":88109,"name":"上奥地利州4200区"},{"category_id":88110,"name":"上奥地利州4300区"},{"category_id":88111,"name":"上奥地利州4400区"},{"category_id":88112,"name":"上奥地利州4500区"},{"category_id":88113,"name":"上奥地利州4600区"},{"category_id":88114,"name":"上奥地利州4700区"},{"category_id":88115,"name":"上奥地利州4800区"},{"category_id":88116,"name":"上奥地利州4900区"},{"category_id":88117,"name":"下奥地利州2000"},{"category_id":88118,"name":"下奥地利州2100"},{"category_id":88119,"name":"下奥地利州2200"},{"category_id":88120,"name":"下奥地利州2300"},{"category_id":88121,"name":"下奥地利州2400"},{"category_id":88122,"name":"下奥地利州2500"},{"category_id":88123,"name":"下奥地利州2600"},{"category_id":88124,"name":"下奥地利州2700"},{"category_id":88125,"name":"下奥地利州3000"},{"category_id":88126,"name":"下奥地利州3100"},{"category_id":88127,"name":"下奥地利州3200"},{"category_id":88128,"name":"下奥地利州3300"},{"category_id":88129,"name":"下奥地利州3400"},{"category_id":88130,"name":"下奥地利州3500"},{"category_id":88131,"name":"下奥地利州3600"},{"category_id":88132,"name":"下奥地利州3700-3800"},{"category_id":88133,"name":"下奥地利州3900"},{"category_id":88134,"name":"伏拉贝尔州6700"},{"category_id":88135,"name":"伏拉贝尔州6800"},{"category_id":88136,"name":"伏拉贝尔州6900"},{"category_id":88137,"name":"克恩滕州9020"},{"category_id":88138,"name":"克恩滕州9100-9200"},{"category_id":88139,"name":"克恩滕州9300-9400"},{"category_id":88140,"name":"克恩滕州9500-9600"},{"category_id":88141,"name":"克恩滕州9800-9900"},{"category_id":88142,"name":"布尔根兰州7000"},{"category_id":88143,"name":"布尔根兰州7100-7200"},{"category_id":88144,"name":"布尔根兰州7300-7400"},{"category_id":88145,"name":"布尔根兰州7500"},{"category_id":88146,"name":"施蒂利亚州8010"},{"category_id":88147,"name":"施蒂利亚州8020"},{"category_id":88148,"name":"施蒂利亚州8040"},{"category_id":88149,"name":"施蒂利亚州8050"},{"category_id":88150,"name":"施蒂利亚州8060"},{"category_id":88151,"name":"施蒂利亚州8100-8200"},{"category_id":88152,"name":"施蒂利亚州8300-8400"},{"category_id":88153,"name":"施蒂利亚州8500-8600"},{"category_id":88154,"name":"施蒂利亚州8700"},{"category_id":88155,"name":"施蒂利亚州8800-8900"},{"category_id":88156,"name":"维也纳10区"},{"category_id":88157,"name":"维也纳12区"},{"category_id":88158,"name":"维也纳13区"},{"category_id":88159,"name":"维也纳14区"},{"category_id":88160,"name":"维也纳15区"},{"category_id":88161,"name":"维也纳16区"},{"category_id":88162,"name":"维也纳17区"},{"category_id":88163,"name":"维也纳18区"},{"category_id":88164,"name":"维也纳19区"},{"category_id":88165,"name":"维也纳1区"},{"category_id":88166,"name":"维也纳20区"},{"category_id":88167,"name":"维也纳21区"},{"category_id":88168,"name":"维也纳22区"},{"category_id":88169,"name":"维也纳23区"},{"category_id":88170,"name":"维也纳2区"},{"category_id":88171,"name":"维也纳3区"},{"category_id":88172,"name":"维也纳4区"},{"category_id":88173,"name":"维也纳5区"},{"category_id":88174,"name":"维也纳6区"},{"category_id":88175,"name":"维也纳7区"},{"category_id":88176,"name":"维也纳8区"},{"category_id":88177,"name":"维也纳9区"},{"category_id":88178,"name":"萨尔斯堡州5020区"},{"category_id":88179,"name":"萨尔斯堡州5060-5070区"},{"category_id":88180,"name":"萨尔斯堡州5080区"},{"category_id":88181,"name":"萨尔斯堡州5100-5200区"},{"category_id":88182,"name":"萨尔斯堡州5300-5400区"},{"category_id":88183,"name":"萨尔斯堡州5500-5600区"},{"category_id":88184,"name":"萨尔斯堡州5700区"},{"category_id":88185,"name":"蒂罗尔州6020区"},{"category_id":88186,"name":"蒂罗尔州6060-6100区"},{"category_id":88187,"name":"蒂罗尔州6200-6300区"},{"category_id":88188,"name":"蒂罗尔州6400-6600区"}],"short_name":"奥地利首都","intro":"音乐之都","cate_name":"维也纳（首都）"},{"count":0,"qyer_city_id":6561,"img":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1567.jpg","areas":[],"short_name":"上奥地利州首府","intro":"多瑙河上游最大河港","cate_name":"林茨"},{"count":0,"qyer_city_id":6575,"img":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1563.jpg","areas":[],"short_name":"萨尔茨堡州的首府","intro":"莫扎特的故乡","cate_name":"萨尔茨堡"},{"count":0,"qyer_city_id":6547,"img":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1570.jpg","areas":[],"short_name":"施泰尔马克州首府","intro":"奥地利第二大城市","cate_name":"格拉茨"}]
     */

    @SerializedName("status")
    public String status;
    @SerializedName("ads")
    public List<GGAds> ads;
    @SerializedName("msg")
    public List<MsgBean> msg;

    public static class MsgBean implements Serializable{
        /**
         * count : 158
         * qyer_city_id : 41
         * img : http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1562.jpg
         * areas : [{"category_id":88101,"name":"上奥地利州4020区"},{"category_id":88102,"name":"上奥地利州4030区"},{"category_id":88103,"name":"上奥地利州4040区"},{"category_id":88104,"name":"上奥地利州4050区"},{"category_id":88105,"name":"上奥地利州4060区"},{"category_id":88106,"name":"上奥地利州4070区"},{"category_id":88107,"name":"上奥地利州4080区"},{"category_id":88108,"name":"上奥地利州4100区"},{"category_id":88109,"name":"上奥地利州4200区"},{"category_id":88110,"name":"上奥地利州4300区"},{"category_id":88111,"name":"上奥地利州4400区"},{"category_id":88112,"name":"上奥地利州4500区"},{"category_id":88113,"name":"上奥地利州4600区"},{"category_id":88114,"name":"上奥地利州4700区"},{"category_id":88115,"name":"上奥地利州4800区"},{"category_id":88116,"name":"上奥地利州4900区"},{"category_id":88117,"name":"下奥地利州2000"},{"category_id":88118,"name":"下奥地利州2100"},{"category_id":88119,"name":"下奥地利州2200"},{"category_id":88120,"name":"下奥地利州2300"},{"category_id":88121,"name":"下奥地利州2400"},{"category_id":88122,"name":"下奥地利州2500"},{"category_id":88123,"name":"下奥地利州2600"},{"category_id":88124,"name":"下奥地利州2700"},{"category_id":88125,"name":"下奥地利州3000"},{"category_id":88126,"name":"下奥地利州3100"},{"category_id":88127,"name":"下奥地利州3200"},{"category_id":88128,"name":"下奥地利州3300"},{"category_id":88129,"name":"下奥地利州3400"},{"category_id":88130,"name":"下奥地利州3500"},{"category_id":88131,"name":"下奥地利州3600"},{"category_id":88132,"name":"下奥地利州3700-3800"},{"category_id":88133,"name":"下奥地利州3900"},{"category_id":88134,"name":"伏拉贝尔州6700"},{"category_id":88135,"name":"伏拉贝尔州6800"},{"category_id":88136,"name":"伏拉贝尔州6900"},{"category_id":88137,"name":"克恩滕州9020"},{"category_id":88138,"name":"克恩滕州9100-9200"},{"category_id":88139,"name":"克恩滕州9300-9400"},{"category_id":88140,"name":"克恩滕州9500-9600"},{"category_id":88141,"name":"克恩滕州9800-9900"},{"category_id":88142,"name":"布尔根兰州7000"},{"category_id":88143,"name":"布尔根兰州7100-7200"},{"category_id":88144,"name":"布尔根兰州7300-7400"},{"category_id":88145,"name":"布尔根兰州7500"},{"category_id":88146,"name":"施蒂利亚州8010"},{"category_id":88147,"name":"施蒂利亚州8020"},{"category_id":88148,"name":"施蒂利亚州8040"},{"category_id":88149,"name":"施蒂利亚州8050"},{"category_id":88150,"name":"施蒂利亚州8060"},{"category_id":88151,"name":"施蒂利亚州8100-8200"},{"category_id":88152,"name":"施蒂利亚州8300-8400"},{"category_id":88153,"name":"施蒂利亚州8500-8600"},{"category_id":88154,"name":"施蒂利亚州8700"},{"category_id":88155,"name":"施蒂利亚州8800-8900"},{"category_id":88156,"name":"维也纳10区"},{"category_id":88157,"name":"维也纳12区"},{"category_id":88158,"name":"维也纳13区"},{"category_id":88159,"name":"维也纳14区"},{"category_id":88160,"name":"维也纳15区"},{"category_id":88161,"name":"维也纳16区"},{"category_id":88162,"name":"维也纳17区"},{"category_id":88163,"name":"维也纳18区"},{"category_id":88164,"name":"维也纳19区"},{"category_id":88165,"name":"维也纳1区"},{"category_id":88166,"name":"维也纳20区"},{"category_id":88167,"name":"维也纳21区"},{"category_id":88168,"name":"维也纳22区"},{"category_id":88169,"name":"维也纳23区"},{"category_id":88170,"name":"维也纳2区"},{"category_id":88171,"name":"维也纳3区"},{"category_id":88172,"name":"维也纳4区"},{"category_id":88173,"name":"维也纳5区"},{"category_id":88174,"name":"维也纳6区"},{"category_id":88175,"name":"维也纳7区"},{"category_id":88176,"name":"维也纳8区"},{"category_id":88177,"name":"维也纳9区"},{"category_id":88178,"name":"萨尔斯堡州5020区"},{"category_id":88179,"name":"萨尔斯堡州5060-5070区"},{"category_id":88180,"name":"萨尔斯堡州5080区"},{"category_id":88181,"name":"萨尔斯堡州5100-5200区"},{"category_id":88182,"name":"萨尔斯堡州5300-5400区"},{"category_id":88183,"name":"萨尔斯堡州5500-5600区"},{"category_id":88184,"name":"萨尔斯堡州5700区"},{"category_id":88185,"name":"蒂罗尔州6020区"},{"category_id":88186,"name":"蒂罗尔州6060-6100区"},{"category_id":88187,"name":"蒂罗尔州6200-6300区"},{"category_id":88188,"name":"蒂罗尔州6400-6600区"}]
         * short_name : 奥地利首都
         * intro : 音乐之都
         * cate_name : 维也纳（首都）
         */

        @SerializedName("count")
        public int count;
        @SerializedName("qyer_city_id")
        public int qyerCityId;
        @SerializedName("img")
        public String img;
        @SerializedName("short_name")
        public String shortName;
        @SerializedName("intro")
        public String intro;
        @SerializedName("cate_name")
        public String cateName;
        @SerializedName("areas")
        public List<AARease> areas;
    }
}
