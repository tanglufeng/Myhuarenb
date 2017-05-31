package com.abcs.hqbtravel.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/17.
 */

public class Adults implements Serializable {
//
//    id: null
//    name: "Bkk48 Massage"
//    photo: "http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb_abc_img%2FQQ%E6%88%AA%E5%9B%BE201611161210171479269593848.png"
//    cate: null
//    star: 4.5
//    introduction: "BTS Phrom Phong站附近，被称为情色按摩激战区。在这个地区，有着许许多多的情色按摩店。今天要介绍的这间BKK48 Massage虽然也算是在这一带，不过所在的位置却稍微的距离车站一些距离，算是在这个情色按摩激战区的外围地区。 BKK48 Massage这间店位于SUkhumvit Soi 22，而这条巷子就在BTS Asok和Phrom Phong站之间(距离Phrom Phong比较近一点)。因此要前往这间店，还是建议从BTS Phrom Phong的方向走过来。转进Soi 22之后没多久，左边会出现一条小巷子，这巷子里有之前介绍过的口交店Bit Style，再往里面走一点点就可以看到BKK48 Massage了。 BKK48 Massage这个名称，很明显的是受到日本偶像团体AKB48的启发。可惜的是这间店的装潢与摆设完全没有AKB48的风采，从外观来看就看得出来只是一栋相当有历史的建筑物。门口虽然有稍微装潢过，不过走进店内立刻就可以感受到建筑物老旧的感觉。 建筑物老旧，服务的房间也不算整洁。在这间店里，至少就有两次在房间内看到大蟑螂出没。一次是和小姐冲完澡，走出淋浴区时看到有只蟑螂停在眼前；另一次则是和小姐正在趴趴趴的时候，突然听到房间内传出有『物体』在飞的声音，小姐把灯打开发现是只蟑螂从房间的一角飞到另一边传出的声音。 BKK48 Massage并不算是一间大店，因此小姐的数量相较于激战区的某些大店来说要少了些。虽然说官网上看起来好像有二、三十位的小姐，不过实际上当各位来到这间店的时候，通常能挑选的小姐数量不会很多，有的时候甚至可能只有一两位小姐可以挑选。因为店不大，房间不是很多，所以每天上班的小姐数量相对也比较少。 小姐的数量虽然不是很多，不过这间店的小姐素质个人认为算是中上程度。店里几位红牌的小姐，应该转到那几间比较大的知名情色按摩店也会有不错的业绩。事实上，的确也有几位这间店里的小姐后来转到Sukhumvit Soi 24/1以及Soi 26的某几间店去。所以若是在这间店里遇上自己喜欢的小姐，记得要留连络的方式免得小姐转店失去了连络。 BKK48 Massage的收费比起传统的情色按摩店要稍微贵一点点。以我们常见的两小时油压加特别服务的状况来看，这间店的收费是2400泰铢，比起部份的店贵了100泰铢。一个半小时以及一小时的收费分别是2300泰铢和2200泰铢。而且这间店的泰式、精油以及乳液的按摩价格完全相通，因此在这间店最好是选择两小时的精油或是乳液按摩比较好。 除了以上的费用外，BKK48 Massage还有一些额外的收费项目，象是捏蛋蛋、小姐穿Cosplay服装、使用跳蛋、按摩棒等等项目。不过个人觉得这些项目都是噱头，不值得为了这些项目额外花费。至于外卖的部份，这间店虽然也提供了小姐上门的按摩服务，不过收费不算便宜所以也不推荐各位使用。 由于BKK48 Massage位于Sukhumvit Soi 22的巷内，距离BTS Phrom Phong站有一段距离，因此这间店的客人并没有非常多。大多数以情色按摩为目标的同好，大多是以Sukumvit Soi 24、Soi 24/1以及Soi 26的店为目标。如果你不怕在大太阳底下走路，也想要多试几间不一样的情色按摩，那么您可以来这间店试试；若您懒得走那么晚，那么这间店错过其实也没有什么损失。"
//    range: 0
//    been: 112
//    distance: ">999km"
//    lng: "13.7314394000"
//    lat: "100.5655124000"
//    product_id: "-1"

    @SerializedName("english_name")
    public String english_name;
    @SerializedName("commentCount")
    public int commentCount;
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("photo")
    public String photo;
    @SerializedName("cate")
    public String cate;
    @SerializedName("star")
    public double star;
    @SerializedName("introduction")
    public String introduction;
    @SerializedName("range")
    public int range;
    @SerializedName("been")
    public int been;
    @SerializedName("distance")
    public String distance;
    @SerializedName("lng")
    public String lng;
    @SerializedName("lat")
    public String lat;
    @SerializedName("product_id")
    public String productId;
    @SerializedName("price")
    public String price;

}
