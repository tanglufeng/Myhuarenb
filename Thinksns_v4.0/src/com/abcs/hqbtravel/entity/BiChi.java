package com.abcs.hqbtravel.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/9/9.
 */
public class BiChi {


    /**
     * cates : [{"id":"4142","name":"旺角小食街","star":4.315,"photo":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4142.jpg","been":3159,"distance":"28km","introduction":"旺角街头小食的集中地，有卤水出名的得奖热店\u201c肥姐小食店\u201d、 传统点心\u201c万家烧饼皇\u201d，还有小食档\u201c佳记\u201d卖煎酿三宝、咖喱鱼蛋、臭豆腐、炸鱿鱼须、章鱼小丸子等等。 \r\n\r\n招牌菜：生肠、墨鱼、煎酿三宝、咖喱鱼蛋等各色小食。","range":28,"cates":["粤菜港点"]},{"id":"4143","name":"翠华餐厅(中环威灵顿店)","star":4.145,"photo":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4143.jpg","been":4313,"distance":"31km","introduction":"香港著名的24小时连锁茶餐厅，无论是名人明星还是平民百姓都会\u201c来翠华说天光\u201d，称得上是香港饭堂，半夜来填肚的话还可以 看到在兰桂坊刚high完来这里醒酒的各路人马。咖喱鱼蛋、奶油猪、猪仔包和鱼汤粉面都很有名，其余菜色不是特别出挑，但胜在齐全。 \r\n\r\n招牌菜：奶油猪、猪仔包、奶油猪仔包。","range":31,"cates":["粤菜港点"]},{"id":"4144","name":"澳洲牛奶公司","star":4.18,"photo":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4144.jpg","been":2837,"distance":"29km","introduction":"澳洲牛奶公司的炒蛋是一个传奇，单靠这一道厚蛋三文治便可以打败所有香港的平民食肆。该餐厅也以上菜速度快而闻名。传说中的炒蛋，加了牛奶的蛋汁，刚好炒到生与熟的边界，犹如燕窝，嫩滑多汁。 还有炖蛋、炖蛋白、煎双蛋和鸡蛋三明治都一直备受推崇。\r\n推荐：炒蛋多士、蛋白炖鲜牛奶、厚多士、杏汁炖蛋；炒蛋、多士、炒蛋多士、燉奶、蛋治、茶餐、燉蛋","range":29,"cates":["快餐简餐"]},{"id":"4145","name":"庙街","star":4.03,"photo":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4145.jpg","been":3858,"distance":"29km","introduction":"大排档、算命、路边摊(售卖玉器、手工艺品、茶具、古董等)、麻雀馆、电影里龙蛇混集的复杂背景、粤剧折子戏等，是庙街的关键词。每到晚上，庙街就会热闹起来，体验香港越夜越精彩的生活。这里可以说是香港最著名和必到的地道美食街，保留着户外大排档的特色：折凳、圆桌、大炉头。著名餐厅包括美都冰室等。\r\n\r\n庙街分南北两段，庙街因邻近天后庙而有此一称呼，是香港久享盛名的夜市，又因设立不少专卖男性衣物的摊位，因此有男人街之称。每天下午五时后街侧开始摆满摊档，售卖货品琳琳种种，价钱便宜，另外满街都是特色小食的摊档，提供地道香港美食如海鲜、火锅等。入夜后更有意想不到的摊档营业，如街头中国戏曲表演、占卜、算命、气功、卖药等等，沿着围墙有许许多多算命道士，不论是面相或手相、八字流年、婚姻命名等，什么都可以算。这里是充满香港地道文化味道的夜市，香港很多电影曾多次以庙街为题材及在此取景拍成电影。\r\n\r\n招牌菜：避风塘炒蟹、豉椒炒蚬、椒盐濑尿虾、煲仔饭、拿渣面、蚝饼、鲜鱿炒西兰花。","range":29,"cates":["中餐"]},{"id":"4146","name":"九记牛腩","star":4.37,"photo":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4146.jpg","been":1442,"distance":"30km","introduction":"香港人喜欢吃牛腩面，痴迷程度不亚于兰州人爱拉面、杭州人爱生煎。牛腩面中，肉的品质及部位最重要，已开业六十多年的九记在经过不断的钻研后，发现坑腩(也就是牛肋骨附近的肉)最适合用来佐牛腩面，因为牛腩少油脂又有咬劲绝佳肉质，不但口感一级棒，也最能吸收独家炮制的秘方。\r\n\r\n招牌菜：牛筋伊面、咖喱牛筋腩、金牌上汤。","range":30,"cates":["粤菜港点"]},{"id":"4147","name":"港澳义顺牛奶公司（旺角店）","star":4.21,"photo":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4147.jpg","been":903,"distance":"28km","introduction":"很普通的茶餐厅，但出名的奶制品却让人流连忘返。双皮奶既可热饮也可冷食，啜一口，纯正的奶香在齿间弥散开来，让人感觉幸福得飞起；姜撞奶也不错，香醇嫩滑。除了甜品还有一些小点可选，闲暇时不妨来喝喝下午茶。以奶类的传统港式甜品为招牌，甜品都用白瓷碗装，环境复古设有卡位，澳门也有分店。 \n\n招牌菜：双皮奶、姜汁撞奶。","range":28,"cates":["甜点","粤菜港点"]},{"id":"4148","name":"兰芳园(中环总店)","star":4.095,"photo":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4148.jpg","been":998,"distance":"31km","introduction":"要喝奶茶还是得兰芳园，虽说很多茶餐厅的丝袜奶茶也做得挺好但是这里毕竟是丝袜奶茶的始祖，开业已经57年。话说\u201c不到长城非好汉\u201d，到了香港还是要喝一杯兰芳园的丝袜奶茶才算圆满。除奶茶外，兰芳园供应传统茶餐厅食品如多士、三文治等，其他食品包括葱油鸡扒捞出前一丁面、奶油猪仔包、猪扒包、番茄薯仔汤通粉等。每日吸引不少名人、中环上班族及海外旅客慕名光顾，其门外常见排队等候的人龙。店铺比较隐蔽，要好好找。\r\n\r\n招牌菜：丝袜奶茶、西多、葱油鸡排捞丁、猪扒包、牛脷包。","range":31,"cates":["粤菜港点"]},{"id":"4149","name":"翠华餐厅（尖沙咀店）","star":4.18,"photo":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4149.jpg","been":669,"distance":"30km","introduction":"香港著名的24小时连锁茶餐厅，无论是名人明星还是平民百姓都光顾，称得上是香港饭堂，半夜来填肚的话还可以 看到在兰桂坊刚high完来这里醒酒的各路人马。咖喱鱼蛋、奶油 猪、猪仔包和鱼汤粉面都很有名，其余菜色不是特别出挑，但胜在 齐全。 招牌菜：奶油猪、猪仔包、奶油猪仔包。","range":30,"cates":["粤菜港点"]},{"id":"4150","name":"添好运点心专门店(中环)","star":4.28,"photo":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4150.jpg","been":482,"distance":"31km","introduction":"全球最便宜的米其林星级餐厅，主厨是原四季酒店龙景轩点心部的原主厨培哥，点心水准很好价格又平民。旺角的是原店，现在在中环、北角和深水埔都有分店。\n\n招牌菜：古法糯米雞、布拉腸粉、杞子桂花糕、晶瑩鮮蝦餃、酥皮焗叉燒包。","range":31,"cates":["粤菜港点"]},{"id":"4151","name":"珍妮家手工曲奇(尖沙咀店)","star":4.18,"photo":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4151.jpg","been":540,"distance":"30km","introduction":"好吃好评价的自制饼干蛋糕店新开尖沙咀店哟！新开了店铺！地址是荃湾杨屋道1号荃新天地G31号铺，在上海婆婆的转角对面。","range":30,"cates":["面包糕点","食品店/零食店"]}]
     * next : 2
     * pageCount : 1
     */

    @SerializedName("body")
    public BodyBean body;
    /**
     * body : {"cates":[{"id":"4142","name":"旺角小食街","star":4.315,"photo":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4142.jpg","been":3159,"distance":"28km","introduction":"旺角街头小食的集中地，有卤水出名的得奖热店\u201c肥姐小食店\u201d、 传统点心\u201c万家烧饼皇\u201d，还有小食档\u201c佳记\u201d卖煎酿三宝、咖喱鱼蛋、臭豆腐、炸鱿鱼须、章鱼小丸子等等。 \r\n\r\n招牌菜：生肠、墨鱼、煎酿三宝、咖喱鱼蛋等各色小食。","range":28,"cates":["粤菜港点"]},{"id":"4143","name":"翠华餐厅(中环威灵顿店)","star":4.145,"photo":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4143.jpg","been":4313,"distance":"31km","introduction":"香港著名的24小时连锁茶餐厅，无论是名人明星还是平民百姓都会\u201c来翠华说天光\u201d，称得上是香港饭堂，半夜来填肚的话还可以 看到在兰桂坊刚high完来这里醒酒的各路人马。咖喱鱼蛋、奶油猪、猪仔包和鱼汤粉面都很有名，其余菜色不是特别出挑，但胜在齐全。 \r\n\r\n招牌菜：奶油猪、猪仔包、奶油猪仔包。","range":31,"cates":["粤菜港点"]},{"id":"4144","name":"澳洲牛奶公司","star":4.18,"photo":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4144.jpg","been":2837,"distance":"29km","introduction":"澳洲牛奶公司的炒蛋是一个传奇，单靠这一道厚蛋三文治便可以打败所有香港的平民食肆。该餐厅也以上菜速度快而闻名。传说中的炒蛋，加了牛奶的蛋汁，刚好炒到生与熟的边界，犹如燕窝，嫩滑多汁。 还有炖蛋、炖蛋白、煎双蛋和鸡蛋三明治都一直备受推崇。\r\n推荐：炒蛋多士、蛋白炖鲜牛奶、厚多士、杏汁炖蛋；炒蛋、多士、炒蛋多士、燉奶、蛋治、茶餐、燉蛋","range":29,"cates":["快餐简餐"]},{"id":"4145","name":"庙街","star":4.03,"photo":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4145.jpg","been":3858,"distance":"29km","introduction":"大排档、算命、路边摊(售卖玉器、手工艺品、茶具、古董等)、麻雀馆、电影里龙蛇混集的复杂背景、粤剧折子戏等，是庙街的关键词。每到晚上，庙街就会热闹起来，体验香港越夜越精彩的生活。这里可以说是香港最著名和必到的地道美食街，保留着户外大排档的特色：折凳、圆桌、大炉头。著名餐厅包括美都冰室等。\r\n\r\n庙街分南北两段，庙街因邻近天后庙而有此一称呼，是香港久享盛名的夜市，又因设立不少专卖男性衣物的摊位，因此有男人街之称。每天下午五时后街侧开始摆满摊档，售卖货品琳琳种种，价钱便宜，另外满街都是特色小食的摊档，提供地道香港美食如海鲜、火锅等。入夜后更有意想不到的摊档营业，如街头中国戏曲表演、占卜、算命、气功、卖药等等，沿着围墙有许许多多算命道士，不论是面相或手相、八字流年、婚姻命名等，什么都可以算。这里是充满香港地道文化味道的夜市，香港很多电影曾多次以庙街为题材及在此取景拍成电影。\r\n\r\n招牌菜：避风塘炒蟹、豉椒炒蚬、椒盐濑尿虾、煲仔饭、拿渣面、蚝饼、鲜鱿炒西兰花。","range":29,"cates":["中餐"]},{"id":"4146","name":"九记牛腩","star":4.37,"photo":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4146.jpg","been":1442,"distance":"30km","introduction":"香港人喜欢吃牛腩面，痴迷程度不亚于兰州人爱拉面、杭州人爱生煎。牛腩面中，肉的品质及部位最重要，已开业六十多年的九记在经过不断的钻研后，发现坑腩(也就是牛肋骨附近的肉)最适合用来佐牛腩面，因为牛腩少油脂又有咬劲绝佳肉质，不但口感一级棒，也最能吸收独家炮制的秘方。\r\n\r\n招牌菜：牛筋伊面、咖喱牛筋腩、金牌上汤。","range":30,"cates":["粤菜港点"]},{"id":"4147","name":"港澳义顺牛奶公司（旺角店）","star":4.21,"photo":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4147.jpg","been":903,"distance":"28km","introduction":"很普通的茶餐厅，但出名的奶制品却让人流连忘返。双皮奶既可热饮也可冷食，啜一口，纯正的奶香在齿间弥散开来，让人感觉幸福得飞起；姜撞奶也不错，香醇嫩滑。除了甜品还有一些小点可选，闲暇时不妨来喝喝下午茶。以奶类的传统港式甜品为招牌，甜品都用白瓷碗装，环境复古设有卡位，澳门也有分店。 \n\n招牌菜：双皮奶、姜汁撞奶。","range":28,"cates":["甜点","粤菜港点"]},{"id":"4148","name":"兰芳园(中环总店)","star":4.095,"photo":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4148.jpg","been":998,"distance":"31km","introduction":"要喝奶茶还是得兰芳园，虽说很多茶餐厅的丝袜奶茶也做得挺好但是这里毕竟是丝袜奶茶的始祖，开业已经57年。话说\u201c不到长城非好汉\u201d，到了香港还是要喝一杯兰芳园的丝袜奶茶才算圆满。除奶茶外，兰芳园供应传统茶餐厅食品如多士、三文治等，其他食品包括葱油鸡扒捞出前一丁面、奶油猪仔包、猪扒包、番茄薯仔汤通粉等。每日吸引不少名人、中环上班族及海外旅客慕名光顾，其门外常见排队等候的人龙。店铺比较隐蔽，要好好找。\r\n\r\n招牌菜：丝袜奶茶、西多、葱油鸡排捞丁、猪扒包、牛脷包。","range":31,"cates":["粤菜港点"]},{"id":"4149","name":"翠华餐厅（尖沙咀店）","star":4.18,"photo":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4149.jpg","been":669,"distance":"30km","introduction":"香港著名的24小时连锁茶餐厅，无论是名人明星还是平民百姓都光顾，称得上是香港饭堂，半夜来填肚的话还可以 看到在兰桂坊刚high完来这里醒酒的各路人马。咖喱鱼蛋、奶油 猪、猪仔包和鱼汤粉面都很有名，其余菜色不是特别出挑，但胜在 齐全。 招牌菜：奶油猪、猪仔包、奶油猪仔包。","range":30,"cates":["粤菜港点"]},{"id":"4150","name":"添好运点心专门店(中环)","star":4.28,"photo":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4150.jpg","been":482,"distance":"31km","introduction":"全球最便宜的米其林星级餐厅，主厨是原四季酒店龙景轩点心部的原主厨培哥，点心水准很好价格又平民。旺角的是原店，现在在中环、北角和深水埔都有分店。\n\n招牌菜：古法糯米雞、布拉腸粉、杞子桂花糕、晶瑩鮮蝦餃、酥皮焗叉燒包。","range":31,"cates":["粤菜港点"]},{"id":"4151","name":"珍妮家手工曲奇(尖沙咀店)","star":4.18,"photo":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4151.jpg","been":540,"distance":"30km","introduction":"好吃好评价的自制饼干蛋糕店新开尖沙咀店哟！新开了店铺！地址是荃湾杨屋道1号荃新天地G31号铺，在上海婆婆的转角对面。","range":30,"cates":["面包糕点","食品店/零食店"]}],"next":2,"pageCount":1}
     * result : 1
     * timestamp : 1478761021980
     * version : 1.0
     * transactionid : null
     */

    @SerializedName("result")
    public int result;
    @SerializedName("timestamp")
    public String timestamp;
    @SerializedName("version")
    public String version;
    @SerializedName("transactionid")
    public String transactionid;

    public static class BodyBean {
        @SerializedName("next")
        public int next;
        @SerializedName("pageCount")
        public int pageCount;
        /**
         * id : 4142
         * name : 旺角小食街
         * star : 4.315
         * photo : http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4142.jpg
         * been : 3159
         * distance : 28km
         * introduction : 旺角街头小食的集中地，有卤水出名的得奖热店“肥姐小食店”、 传统点心“万家烧饼皇”，还有小食档“佳记”卖煎酿三宝、咖喱鱼蛋、臭豆腐、炸鱿鱼须、章鱼小丸子等等。 

         招牌菜：生肠、墨鱼、煎酿三宝、咖喱鱼蛋等各色小食。
         * range : 28
         * cates : ["粤菜港点"]
         */

        @SerializedName("cates")
        public List<RestauransBean> cates;
//        public List<CatesBean> cates;

//        public static class CatesBean {
//            @SerializedName("id")
//            public String id;
//            @SerializedName("name")
//            public String name;
//            @SerializedName("star")
//            public double star;
//            @SerializedName("photo")
//            public String photo;
//            @SerializedName("been")
//            public int been;
//            @SerializedName("distance")
//            public String distance;
//            @SerializedName("introduction")
//            public String introduction;
//            @SerializedName("range")
//            public int range;
//            @SerializedName("cate")
//            public String cates;
//        }
    }
}
