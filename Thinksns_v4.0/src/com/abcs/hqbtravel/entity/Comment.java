package com.abcs.hqbtravel.entity;/**
 * Created by Administrator on 2017/1/7.
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * User: zds
 * Date: 2017-01-07
 * Time: 12:03
 * FIXME
 */
public class Comment implements Serializable {

    /**
     * timestamp : 1483753515616
     * body : {"items":[{"comment":"28、夜的草原是这么宁静而安详，只有漫流的溪水声引起你对这大自然的退思。\n\n29、浪花是海上的奇景，可她更像一位舞蹈家，她能使人抛开烦恼，尽情地欣赏。\n\n30、阳光被层层叠叠的树叶过滤，漏到他身上变成了淡淡的圆圆的轻轻摇曳的光晕。\n\n31、忽然，迎面升起一轮红日，洒下的道道金光，就像条条金鞭，驱赶着飞云流雾。\n\n32、骄阳的两道光柱穿过房间，宛如两条透明的金带，内中闪耀着星星点点的尘埃。\n\n33、杉树枝头的芽簇已经颇为肥壮，嫩嫩的，映着天色闪闪发亮，你说春天还会远吗？\n\n34、就算你留恋开放在水中娇艳的水仙，别忘了山谷里寂寞的角落里，野百合也有春天\n\n35、深秋的太阳像被罩上橘红色灯罩，放射出柔和的光线，照得身上脸上，暖烘烘的。\n\n36、这时候正是早上八九点钟，明亮的阳光在树叶上涂了一圈又一圈金色银色的光环。\n\n37、金灿灿的阳光倾泻下来，注进万顷碧波，使单调而平静的海面而变得有些色彩了。\n\n38、太阳慢慢地透过云霞，露出了早已胀得通红的脸庞，像一个害羞的小娘张望着大地。\n\n39、阳光透过淡薄的云层，照耀着白茫茫的大地，反射出银色的光芒，耀得人眼睛发花。\n\n40、海面上跃出一轮红日，鲜艳夺目，海空顿时洒满了金辉，海面由墨蓝一变而为湛蓝。\n\n41、金灿灿的朝晖，渐渐染红了东方的天际，高高的黄山主峰被灿烂的云霞染成一片绯红。\n\n42、太阳像个老大老大的火球，光线灼人，公路被烈日烤得发烫，脚踏下去一步一串白烟。\n\n43、七月，透蓝的天空，悬着火球似的太阳，云彩好似被太阳烧化了，也消失得无影无踪。\n\n44、红艳艳的太阳光在山尖上时，雾气像幕布一样拉开了，城市渐渐地显现在金色的阳光","imgs":["http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_4a521b3ed49f5019261eb0ba31e4f5c9.jpg","http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_69d662508e1f59f310ee6c6902b47846.jpg","http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_73b27cd289abd925e6148952b4b54d1c.jpg"],"create_time":1483704825,"score":5,"u_id":11803,"name":"王纯杰","avatar":"http://qhcdn.vlahao.com/avatar/0246a5d3b5064a3db2f588617ff254ba.jpg"},{"comment":"28、夜的草原是这么宁静而安详，只有漫流的溪水声引起你对这大自然的退思。\n\n29、浪花是海上的奇景，可她更像一位舞蹈家，她能使人抛开烦恼，尽情地欣赏。\n\n30、阳光被层层叠叠的树叶过滤，漏到他身上变成了淡淡的圆圆的轻轻摇曳的光晕。\n\n31、忽然，迎面升起一轮红日，洒下的道道金光，就像条条金鞭，驱赶着飞云流雾。\n\n32、骄阳的两道光柱穿过房间，宛如两条透明的金带，内中闪耀着星星点点的尘埃。\n\n33、杉树枝头的芽簇已经颇为肥壮，嫩嫩的，映着天色闪闪发亮，你说春天还会远吗？\n\n34、就算你留恋开放在水中娇艳的水仙，别忘了山谷里寂寞的角落里，野百合也有春天\n\n35、深秋的太阳像被罩上橘红色灯罩，放射出柔和的光线，照得身上脸上，暖烘烘的。\n\n36、这时候正是早上八九点钟，明亮的阳光在树叶上涂了一圈又一圈金色银色的光环。\n\n37、金灿灿的阳光倾泻下来，注进万顷碧波，使单调而平静的海面而变得有些色彩了。\n\n38、太阳慢慢地透过云霞，露出了早已胀得通红的脸庞，像一个害羞的小娘张望着大地。\n\n39、阳光透过淡薄的云层，照耀着白茫茫的大地，反射出银色的光芒，耀得人眼睛发花。\n\n40、海面上跃出一轮红日，鲜艳夺目，海空顿时洒满了金辉，海面由墨蓝一变而为湛蓝。\n\n41、金灿灿的朝晖，渐渐染红了东方的天际，高高的黄山主峰被灿烂的云霞染成一片绯红。\n\n42、太阳像个老大老大的火球，光线灼人，公路被烈日烤得发烫，脚踏下去一步一串白烟。\n\n43、七月，透蓝的天空，悬着火球似的太阳，云彩好似被太阳烧化了，也消失得无影无踪。\n\n44、红艳艳的太阳光在山尖上时，雾气像幕布一样拉开了，城市渐渐地显现在金色的阳光","imgs":["http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_c31c1cf7da6e2b6b74d951a2610a5e84.jpg","http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_13514d1bc8dbda6622515bef562bf0e4.jpg","http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_1238171cd3c84bd8e880b166782a6d66.jpg"],"create_time":1483704782,"score":5,"u_id":11803,"name":"王纯杰","avatar":"http://qhcdn.vlahao.com/avatar/0246a5d3b5064a3db2f588617ff254ba.jpg"},{"comment":"饿就跟高跟冯绍峰哇吃好的 v 个会议预计发售的","imgs":["http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_04dbb4c81b76575bbbbf8c7b872752f1.jpg"],"create_time":1483697685,"score":5,"u_id":11803,"name":"王纯杰","avatar":"http://qhcdn.vlahao.com/avatar/0246a5d3b5064a3db2f588617ff254ba.jpg"},{"comment":"","imgs":[""],"create_time":1483687503,"score":5,"u_id":11803,"name":"王纯杰","avatar":"http://qhcdn.vlahao.com/avatar/0246a5d3b5064a3db2f588617ff254ba.jpg"},{"comment":"","imgs":[""],"create_time":1483687337,"score":5,"u_id":11803,"name":"王纯杰","avatar":"http://qhcdn.vlahao.com/avatar/0246a5d3b5064a3db2f588617ff254ba.jpg"},{"comment":"","imgs":[""],"create_time":1483687242,"score":5,"u_id":11803,"name":"王纯杰","avatar":"http://qhcdn.vlahao.com/avatar/0246a5d3b5064a3db2f588617ff254ba.jpg"},{"comment":"","imgs":[""],"create_time":1483687165,"score":5,"u_id":11803,"name":"王纯杰","avatar":"http://qhcdn.vlahao.com/avatar/0246a5d3b5064a3db2f588617ff254ba.jpg"},{"comment":"","imgs":[""],"create_time":1483687165,"score":5,"u_id":11803,"name":"王纯杰","avatar":"http://qhcdn.vlahao.com/avatar/0246a5d3b5064a3db2f588617ff254ba.jpg"},{"comment":"","imgs":[""],"create_time":1483687159,"score":5,"u_id":11803,"name":"王纯杰","avatar":"http://qhcdn.vlahao.com/avatar/0246a5d3b5064a3db2f588617ff254ba.jpg"},{"comment":"","imgs":[""],"create_time":1483687097,"score":5,"u_id":11803,"name":"王纯杰","avatar":"http://qhcdn.vlahao.com/avatar/0246a5d3b5064a3db2f588617ff254ba.jpg"}],"score":{"count":14,"percentage":["100.0%","0.0%","0.0%","0.0%","0.0%"],"scoreFloat":"10.0"}}
     * result : 1
     * transactionid :
     * version : 1.0
     * info : OK
     */

    @SerializedName("timestamp")
    public String timestamp;
    @SerializedName("body")
    public BodyBean body;
    @SerializedName("result")
    public int result;
    @SerializedName("transactionid")
    public String transactionid;
    @SerializedName("version")
    public String version;
    @SerializedName("info")
    public String info;

    public static class BodyBean implements Serializable {
        /**
         * items : [{"comment":"28、夜的草原是这么宁静而安详，只有漫流的溪水声引起你对这大自然的退思。\n\n29、浪花是海上的奇景，可她更像一位舞蹈家，她能使人抛开烦恼，尽情地欣赏。\n\n30、阳光被层层叠叠的树叶过滤，漏到他身上变成了淡淡的圆圆的轻轻摇曳的光晕。\n\n31、忽然，迎面升起一轮红日，洒下的道道金光，就像条条金鞭，驱赶着飞云流雾。\n\n32、骄阳的两道光柱穿过房间，宛如两条透明的金带，内中闪耀着星星点点的尘埃。\n\n33、杉树枝头的芽簇已经颇为肥壮，嫩嫩的，映着天色闪闪发亮，你说春天还会远吗？\n\n34、就算你留恋开放在水中娇艳的水仙，别忘了山谷里寂寞的角落里，野百合也有春天\n\n35、深秋的太阳像被罩上橘红色灯罩，放射出柔和的光线，照得身上脸上，暖烘烘的。\n\n36、这时候正是早上八九点钟，明亮的阳光在树叶上涂了一圈又一圈金色银色的光环。\n\n37、金灿灿的阳光倾泻下来，注进万顷碧波，使单调而平静的海面而变得有些色彩了。\n\n38、太阳慢慢地透过云霞，露出了早已胀得通红的脸庞，像一个害羞的小娘张望着大地。\n\n39、阳光透过淡薄的云层，照耀着白茫茫的大地，反射出银色的光芒，耀得人眼睛发花。\n\n40、海面上跃出一轮红日，鲜艳夺目，海空顿时洒满了金辉，海面由墨蓝一变而为湛蓝。\n\n41、金灿灿的朝晖，渐渐染红了东方的天际，高高的黄山主峰被灿烂的云霞染成一片绯红。\n\n42、太阳像个老大老大的火球，光线灼人，公路被烈日烤得发烫，脚踏下去一步一串白烟。\n\n43、七月，透蓝的天空，悬着火球似的太阳，云彩好似被太阳烧化了，也消失得无影无踪。\n\n44、红艳艳的太阳光在山尖上时，雾气像幕布一样拉开了，城市渐渐地显现在金色的阳光","imgs":["http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_4a521b3ed49f5019261eb0ba31e4f5c9.jpg","http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_69d662508e1f59f310ee6c6902b47846.jpg","http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_73b27cd289abd925e6148952b4b54d1c.jpg"],"create_time":1483704825,"score":5,"u_id":11803,"name":"王纯杰","avatar":"http://qhcdn.vlahao.com/avatar/0246a5d3b5064a3db2f588617ff254ba.jpg"},{"comment":"28、夜的草原是这么宁静而安详，只有漫流的溪水声引起你对这大自然的退思。\n\n29、浪花是海上的奇景，可她更像一位舞蹈家，她能使人抛开烦恼，尽情地欣赏。\n\n30、阳光被层层叠叠的树叶过滤，漏到他身上变成了淡淡的圆圆的轻轻摇曳的光晕。\n\n31、忽然，迎面升起一轮红日，洒下的道道金光，就像条条金鞭，驱赶着飞云流雾。\n\n32、骄阳的两道光柱穿过房间，宛如两条透明的金带，内中闪耀着星星点点的尘埃。\n\n33、杉树枝头的芽簇已经颇为肥壮，嫩嫩的，映着天色闪闪发亮，你说春天还会远吗？\n\n34、就算你留恋开放在水中娇艳的水仙，别忘了山谷里寂寞的角落里，野百合也有春天\n\n35、深秋的太阳像被罩上橘红色灯罩，放射出柔和的光线，照得身上脸上，暖烘烘的。\n\n36、这时候正是早上八九点钟，明亮的阳光在树叶上涂了一圈又一圈金色银色的光环。\n\n37、金灿灿的阳光倾泻下来，注进万顷碧波，使单调而平静的海面而变得有些色彩了。\n\n38、太阳慢慢地透过云霞，露出了早已胀得通红的脸庞，像一个害羞的小娘张望着大地。\n\n39、阳光透过淡薄的云层，照耀着白茫茫的大地，反射出银色的光芒，耀得人眼睛发花。\n\n40、海面上跃出一轮红日，鲜艳夺目，海空顿时洒满了金辉，海面由墨蓝一变而为湛蓝。\n\n41、金灿灿的朝晖，渐渐染红了东方的天际，高高的黄山主峰被灿烂的云霞染成一片绯红。\n\n42、太阳像个老大老大的火球，光线灼人，公路被烈日烤得发烫，脚踏下去一步一串白烟。\n\n43、七月，透蓝的天空，悬着火球似的太阳，云彩好似被太阳烧化了，也消失得无影无踪。\n\n44、红艳艳的太阳光在山尖上时，雾气像幕布一样拉开了，城市渐渐地显现在金色的阳光","imgs":["http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_c31c1cf7da6e2b6b74d951a2610a5e84.jpg","http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_13514d1bc8dbda6622515bef562bf0e4.jpg","http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_1238171cd3c84bd8e880b166782a6d66.jpg"],"create_time":1483704782,"score":5,"u_id":11803,"name":"王纯杰","avatar":"http://qhcdn.vlahao.com/avatar/0246a5d3b5064a3db2f588617ff254ba.jpg"},{"comment":"饿就跟高跟冯绍峰哇吃好的 v 个会议预计发售的","imgs":["http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_04dbb4c81b76575bbbbf8c7b872752f1.jpg"],"create_time":1483697685,"score":5,"u_id":11803,"name":"王纯杰","avatar":"http://qhcdn.vlahao.com/avatar/0246a5d3b5064a3db2f588617ff254ba.jpg"},{"comment":"","imgs":[""],"create_time":1483687503,"score":5,"u_id":11803,"name":"王纯杰","avatar":"http://qhcdn.vlahao.com/avatar/0246a5d3b5064a3db2f588617ff254ba.jpg"},{"comment":"","imgs":[""],"create_time":1483687337,"score":5,"u_id":11803,"name":"王纯杰","avatar":"http://qhcdn.vlahao.com/avatar/0246a5d3b5064a3db2f588617ff254ba.jpg"},{"comment":"","imgs":[""],"create_time":1483687242,"score":5,"u_id":11803,"name":"王纯杰","avatar":"http://qhcdn.vlahao.com/avatar/0246a5d3b5064a3db2f588617ff254ba.jpg"},{"comment":"","imgs":[""],"create_time":1483687165,"score":5,"u_id":11803,"name":"王纯杰","avatar":"http://qhcdn.vlahao.com/avatar/0246a5d3b5064a3db2f588617ff254ba.jpg"},{"comment":"","imgs":[""],"create_time":1483687165,"score":5,"u_id":11803,"name":"王纯杰","avatar":"http://qhcdn.vlahao.com/avatar/0246a5d3b5064a3db2f588617ff254ba.jpg"},{"comment":"","imgs":[""],"create_time":1483687159,"score":5,"u_id":11803,"name":"王纯杰","avatar":"http://qhcdn.vlahao.com/avatar/0246a5d3b5064a3db2f588617ff254ba.jpg"},{"comment":"","imgs":[""],"create_time":1483687097,"score":5,"u_id":11803,"name":"王纯杰","avatar":"http://qhcdn.vlahao.com/avatar/0246a5d3b5064a3db2f588617ff254ba.jpg"}]
         * score : {"count":14,"percentage":["100.0%","0.0%","0.0%","0.0%","0.0%"],"scoreFloat":"10.0"}
         */

        @SerializedName("score")
        public ScoreBean score;
        @SerializedName("items")
        public List<CommentItem> items;

        public static class ScoreBean implements Serializable{
            /**
             * count : 14
             * percentage : ["100.0%","0.0%","0.0%","0.0%","0.0%"]
             * scoreFloat : 10.0
             */

            @SerializedName("count")
            public int count;
            @SerializedName("scoreFloat")
            public String scoreFloat;
            @SerializedName("percentage")
            public List<Double> percentage;
        }

    }
}
