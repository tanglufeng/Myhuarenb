package com.thinksns.sociax.thinksnsbase.utils;


public class TLUrl {
    public static boolean isChange = false;
    private static TLUrl instance;
    private TLUrl (){}

    public static TLUrl getInstance() {
        if (instance == null || isChange) {
            instance = new TLUrl();
            isChange= false;
        }
        return instance;
    }

    /**中国**/    //http://newapi.tuling.me
    /**东南亚**/    //http://dny.huaqiaobang.com
    /**欧洲**/    //http://47.91.75.249

    public static final String  URL_CHIAN="http://newapi.tuling.me";
    public static final String  URL_DONGNANYA="http://dny.huaqiaobang.com";
    public static final String  URL_OUZHOU="http://47.91.75.249";

    public static String URL_BASE = URL_CHIAN;
    public static String URL_huayouhui = "newapi.tuling.me";

    public  String getUrl(){
        return  URL_BASE;
    }
    public  String getHuaUrl(){
        return  URL_huayouhui;
    }

    public  void setHuaUrl(String URL_huayouhui) {
        TLUrl.URL_huayouhui = URL_huayouhui;
    }

    public  String URL_LEVEL= "https://api.cavacn.com/userlevel/0.2";// 账号等级
    public  String URL_bindbank = "http://oss.aliyuncs.com/tuling/h5/bindbank.html";
    public  String URL_avatar = "http://qhcdn.vlahao.com/newavatar/";
    public   String URL_get_country_quhao= getUrl()+"/travel/find/getAreaCode";//获取国家区号
    public  String URL_goods_base = getUrl()+"/japi";
    public  String URL_AD =  URL_goods_base+"/StockDataService/home?platform=1";// 广告图
    public  String URL_getAvatar =  URL_goods_base+"/epay/GetInputAvatar";
    public  String URL_hwg_hot_text= URL_goods_base+"/im/rest/PrefacePHP/findall"; //海外购专题文字
    public  String URL_hwg_goods_message= URL_goods_base+"/im/rest/PrefacePHP/findcalljava"; //商品购买、收货消息
    public  String URL_hwg_goods_msg_del_one= URL_goods_base+"/im/rest/PrefacePHP/deleteoneinfo"; //删除消息
    public  String URL_hwg_goods_msg_del_all= URL_goods_base+"/im/rest/PrefacePHP/deleteallinfo"; //清空消息
    public  String URL_ISHULI= URL_goods_base+"/im/rest/User/hwgestate";//是否隐藏个状化护

    public  String URL_local_get_morenews = getUrl()+"/news/QhWebNewsServer/api/country/get";
    public  String URL_local_get_newslist = getUrl()+"/news/QhWebNewsServer/api/country/index";  //本地调2个接口 这个是新闻的新接口  ?countryId=215

    public  String URL_vip_base = getUrl()+"/japi";
    public  String URL_translate_latlng_to_address= URL_vip_base+"/finance/GeoServlet";//经纬度转换地址 ?lat=22.530363&lng=114.023588&uid=10666
    public  String URL_vip_yinlian=  URL_vip_base+"/finance/FuYouVipPay";//vip 银联支付

    public  String URL_local_new_base = getUrl()+"/news";
    public  String URL_new=  URL_local_new_base+"/QhWebNewsServer/";// 新闻接口
    public  String URL_local_get_more_wx_news= URL_local_new_base+"/QhWebNewsServer/api/country/get";//获取更多微信新闻 ?cityId=13&type=0
    public  String URL_local_get_wx_news= URL_local_new_base+"/QhWebNewsServer/api/country/index";//获取首页微信新闻 ?cityId=13

    //登录  URL_user_base  ok ok
    public  String URL_user_base = getUrl()+"/user";
    public  String URL_matchcode =  URL_user_base +"/user/matchcode?iou=c";// 验证码确认
    public  String URL_login =  URL_user_base +"/login?iou=1";// 登录
    public  String URL_regist =  URL_user_base +"/regist?iou=1";// 注册
    public  String URL_forgotpwd =  URL_user_base +"/user/forgotpwd?iou=c";// 忘记密码
    public  String URL_sendsms =  URL_user_base +"/user/sendsms?iou=c";// 发送短信验证码
    public  String URL_isexist =  URL_user_base +"/user/isexist?iou=c";// 帐号是否存在
    public  String URL_registbyphone =  URL_user_base +"/user/registbyphone?iou=c";// 手机注册
    public  String URL_login_by_phone =  URL_user_base +"/login/phone?iou=c";// 手机快捷登录
    public  String URL_user =  URL_user_base +"/user/";
    public  String URL_wechat =  URL_user_base +"/login/wechat";
    public  String URL_getbindinfo =  URL_user_base +"/user/getbindinfo?iou=c";
    public  String URL_oauth =  URL_user_base +"/oauth";
    public  String URL_changepwd =  URL_user_base +"/user/changepwdbyphone?iou=c";

    //URL_bind_base ok ok
    public  String URL_bind_base = getUrl()+"/japi";
    public  String URL_bindentity =  URL_bind_base +"/finance/BindIdentity";
    public  String URL_bindBank =  URL_bind_base +"/finance/BindBank";
    public  String URL_productServlet =  URL_bind_base +"/finance/ProductServlet";
    public  String URL_userServlet =  URL_bind_base +"/finance/UserServlet";
    public  String URL_indexPage =  URL_bind_base +"/finance/IndexPage";
    public  String URL_alipay =  URL_bind_base +"/finance/AliPay";
    public  String URL_customer =  URL_bind_base +"/finance/UserServlet";// 获取用户下线

    //添加 ok ok
    public  String URL_hwg_new_base = "http://112.74.192.23:7078";
    public  String URL_newsIsRead =  URL_hwg_new_base +"/QhWebNewsServer/read";// 新闻已阅
    public  String URL_comment =  URL_hwg_new_base +"/QhWebNewsServer/news/comment";
    public  String URL_collect =  URL_hwg_new_base +"/QhWebNewsServer/News/collect";// 新闻收藏
    public  String URL_collectdel =  URL_hwg_new_base +"/QhWebNewsServer/q_collection";// 收藏删除
    public  String URL_praise =  URL_hwg_new_base +"/QhWebNewsServer/News/praise";// 新闻点赞
    public  String URL_oppose =  URL_hwg_new_base +"/QhWebNewsServer/oppose";// 新闻点赞
    public  String URL_jjxw =  URL_hwg_new_base +"/QhOtherServer/jj/";// 基金新闻
    public  String URL_jjyjbg =  URL_hwg_new_base +"/QhOtherServer/yjbg/";// 基金研究报告
    public  String URL_newold =  URL_hwg_new_base +"/QhWebNewsServer/News/old";// 旧新闻
    public  String URL_wxxx =  URL_hwg_new_base +"/DYWeiXinHao/article/get";
    public  String URL_wxwz =  URL_hwg_new_base +"/DYWeiXinHao/article/getall";
    public  String URL_wxh =  URL_hwg_new_base +"/DYWeiXinHao/weixinhao";
    public  String URL_dmg =  URL_hwg_new_base +"/ZhiLiYinHang/StockGroupServlet";
    public  String URL_newsInitChannel =  URL_hwg_new_base +"/QhWebNewsServer/news/type";
    public  String URL_share =  URL_hwg_new_base +"/ZhiLiYinHang/PushServlet";// 分享
    public  String URL_dm =  URL_hwg_new_base +"/ZhiLiYinHang/DaiTouServlet";
    public  String URL_customer_service =  URL_hwg_new_base +"/HQChat/rest/online/getList";// 客服
    public  String URL_customer_service_send =  URL_hwg_new_base +"/HQChat/rest/online/send";// 客服
    public  String URL_main_layout =  URL_hwg_new_base +"/StockDataService/hc";// 首页布局
    public  String URL_main_type_layout =  URL_hwg_new_base +"/StockDataService/vc";// 主布局type


    //海外购接口  ok ok
    public  String URL_hwg_cart_base = "http://58.96.180.203:8080/HaiWaiGou/rest";
    public  String URL_hwg_banner=  URL_hwg_cart_base +"/country/getbannerimg";//海外购首页banner图
    public  String URL_hwg_country=  URL_hwg_cart_base +"/country/getCountryMain";//九宫格国家馆
    public  String URL_hwg_countrygoods=  URL_hwg_cart_base +"/country/getcountryList";//国家馆分类
    public  String URL_hwg_country_tuijian=  URL_hwg_cart_base +"/country/getCountryRec";//国家馆推荐
    public  String URL_hwg_pinpai=  URL_hwg_cart_base +"/shopsort/getbrandidimg";//品牌更新分类
    public  String URL_hwg_allpinpai=  URL_hwg_cart_base +"/shopsort/getbrandidmsg?pages=1&pagelist=10";//所有品牌更新分类
    public  String URL_hwg_pinpai_goods=  URL_hwg_cart_base +"/country/getbrandidList";//品牌商品列表
    public  String URL_hwg_pinpai_sort=  URL_hwg_cart_base +"/shopsort/BrandidSort";//品牌商品列表价格升序
    public  String URL_hwg_pinpai_sortbyprice_down=  URL_hwg_cart_base +"/shopsort/BrandidSort?brandid=1&key=brandidDMoney&pages=1&pagelist=10";//品牌商品列表价格降序
    public  String URL_hwg_pinpai_sortbysales_=  URL_hwg_cart_base +"/shopsort/BrandidSort?brandid=1&key=brandidDSale&pages=1&pagelist=10";//品牌商品列表销量降序
    public  String URL_hwg_goods_detail=  URL_hwg_cart_base +"/country/getShopMsg";//商品详情
    public  String URL_hwg_goods_photos=  URL_hwg_cart_base +"/shopsort/getShopImg";//商品详情图片展示
    public  String URL_hwg_add_cart=  URL_hwg_cart_base +"/shopcart/add";//添加入购物车
    public  String URL_hwg_cart=  URL_hwg_cart_base +"/shopcart/getMsg";//购物车列表查看
    public  String URL_hwg_cart_btnadd=  URL_hwg_cart_base +"/shopcart/getaddShopOne";//购物车增加按钮
    public  String URL_hwg_cart_btnreduce=  URL_hwg_cart_base +"/shopcart/getdelShopOne";//购物车减少按钮
    public  String URL_hwg_cart_delone=  URL_hwg_cart_base +"/shopcart/getDelShopOne";//购物车单件删除
    public  String URL_hwg_cart_deltwos=  URL_hwg_cart_base +"/shopcart/delshop";//购物车多件删除
    public  String URL_hwg_cart_delall=  URL_hwg_cart_base +"/shopcart/delallshop";//购物车全部删除
    public  String URL_hwg_pay=  URL_hwg_cart_base +"/shopcart/shopBuy";//支付宝
    public  String URL_hwg_pay_goods=  URL_hwg_cart_base +"/shopcart/getshopoid";//支付宝下单

    //海外购 ok  ok
    public  String URL_hwg_base = getUrl()+"/huaqiaobang";
    public  String URL_hwg_pic_head=  URL_hwg_base+"/data/upload/mobile/special/s8/"; //海外购首页分类
    public  String URL_hwg_return_pic_head=   URL_hwg_base+"/data/upload/shop/refund/";//退货凭证图片
    public  String URL_hwg_store_head=   URL_hwg_base+"/data/upload/shop/store/";//店铺头像
    public  String URL_hwg_comment_goods=   URL_hwg_base+"/data/upload/shop/store/goods/";//评论商品图片 6(geval_storeid)
    public  String URL_hwg_comment_share=   URL_hwg_base+"/data/upload/shop/member/";//评论晒单图片 13(geval_frommemberid)
    public  String URL_hwg_remai=   URL_hwg_base+"/data/upload/";//热卖图片头
    public  String URL_hwg_qianggou=   URL_hwg_base+"/data/upload/shop/groupbuy/";
    public  String URL_hwg_brand_pic_head=  URL_hwg_base+"/data/upload/shop/brand/"; //首页品牌图片头部

    //海外购  ok ok
    public  String URL_hwg_goods_base = getUrl()+"/huaqiaobang/mobile/index.php";
    public  String URL_hwg_pay_callback= URL_hwg_goods_base +"?act=member_buy&op=calljava"; //支付成功回复  key   pay_sn 支付单号
    public  String URL_hwg_goods_buy_all_and_again= URL_hwg_goods_base +"?act=member_cart&op=buy_again"; //一键购买/再买一次  POST key goodsinfo
    public  String URL_hwg_main= URL_hwg_goods_base +"?act=many_index&op=find_many_list"; //海外购首页模板
    public  String URL_bendi_addche= URL_hwg_goods_base +"?act=member_cart&op=ios_cart_add"; //本地商品添加购物车
    public  String URL_bendi_jiesuan= URL_hwg_goods_base +"?act=member_cart&op=new_cart_add"; //本地商品结算
    public  String URL_hwg_type= URL_hwg_goods_base +"?act=many_index&op=az_find_many"; //海外购首页分类 get &plate_id
    public  String URL_hwg_week=  URL_hwg_goods_base +"?act=goods&op=newgoods_list&state=1&key=1&order=&curpage=";//海外购每周上新
    public  String URL_hwg_week_byprice_up=  URL_hwg_goods_base +"?act=goods&op=newgoods_list&state=1&key=3&order=1&curpage=";//每周上新商品价格升序
    public  String URL_hwg_week_byprice_down=  URL_hwg_goods_base +"?act=goods&op=newgoods_list&state=1&key=3&order=2&curpage=";//每周上新商品价格降序
    public  String URL_hwg_week_bysale_up=  URL_hwg_goods_base +"?act=goods&op=newgoods_list&state=1&key=1&order=1&curpage=";//每周上新商品销量升序
    public  String URL_hwg_week_bysale_down=  URL_hwg_goods_base +"?act=goods&op=newgoods_list&state=1&key=1&order=2&curpage=";//每周上新商品销量降序
    public  String URL_hwg_week_bylike_up=  URL_hwg_goods_base +"?act=goods&op=newgoods_list&state=1&key=2&order=1&curpage=";//每周上新商品人气升序 GET
    public  String URL_hwg_week_bylike_down=  URL_hwg_goods_base +"?act=goods&op=newgoods_list&state=1&key=2&order=2&curpage=";//每周上新商品人气降序 GET
    public  String URL_hwg_week_bynew_down=  URL_hwg_goods_base +"?act=goods&op=newgoods_list&state=1&key=4&order=2&curpage=";//每周上新商品新品降序 GET
    public  String URL_hwg_update=  URL_hwg_goods_base +"?act=goods&op=newgoods_list&state=0&key=1&order=&curpage=";//海外购预售上新
    public  String URL_hwg_update_byprice_up=  URL_hwg_goods_base +"?act=goods&op=newgoods_list&state=0&key=3&order=1&curpage=";//预售上新商品价格升序
    public  String URL_hwg_update_byprice_down=  URL_hwg_goods_base +"?act=goods&op=newgoods_list&state=0&key=3&order=2&curpage=";//预售上新商品价格降序
    public  String URL_hwg_update_bysale_up=  URL_hwg_goods_base +"?act=goods&op=newgoods_list&state=0&key=1&order=1&curpage=";//预售上新商品销量升序
    public  String URL_hwg_update_bysale_down=  URL_hwg_goods_base +"?act=goods&op=newgoods_list&state=0&key=1&order=2&curpage=";//预售上新商品销量降序
    public  String URL_hwg_update_bylike_up=  URL_hwg_goods_base +"?act=goods&op=newgoods_list&state=0&key=2&order=1&curpage=";//预售上新商品人气升序 GET
    public  String URL_hwg_update_bylike_down=  URL_hwg_goods_base +"?act=goods&op=newgoods_list&state=0&key=2&order=2&curpage=";//预售上新商品人气降序 GET
    public  String URL_hwg_update_bynew_down=  URL_hwg_goods_base +"?act=goods&op=newgoods_list&state=0&key=4&order=2&curpage=";//预售上新商品新品降序 GET


    //海外购PHP接口  ok
    public  String URL_hwg_head=   URL_hwg_base+"/mobile/index.php";//海外购接口前部
    public  String URL_hwg_home=   URL_hwg_base+"/mobile/index.php?act=index";//海外购首页
    public  String URL_hwg_user=   URL_hwg_base+"/mobile/index.php?act=member_index";//用户信息
    public  String URL_hwg_good_remai=   URL_hwg_base+"/mobile/index.php?act=goods&op=good_remai";//海外购首页热卖
    public  String URL_hwg_good_qianggou=   URL_hwg_base+"/mobile/index.php?act=goods&op=goods_groupbuy&page=5";//海外购首页抢购
    //  public  String URL_hwg_gdetail=   URL_hwg_base+"/mobile/index.php?act=goods&op=goods_detail";//商品详情
    public  String URL_hwg_gdetail=   URL_hwg_base+"/mobile/index.php?act=goods&op=new_goods_detail";//商品详情===新版
    public  String URL_hwg_login=   URL_hwg_base+"/mobile/index.php?act=login";//登录
    public  String URL_hwg_cartlist=   URL_hwg_base+"/mobile/index.php?act=member_cart&op=cart_list";//购物车
    public  String URL_hwg_cart_del=   URL_hwg_base+"/mobile/index.php?act=member_cart&op=cart_del";//购物车删除
    public  String URL_hwg_cart_del_all=   URL_hwg_base+"/mobile/index.php?act=member_cart&op=cart_delAll";//购物车清空
    public  String URL_hwg_cart_del_select=   URL_hwg_base+"/mobile/index.php?act=member_cart&op=cart_del_many";//购物车 删除所选  批量
    public  String URL_hwg_cartnum_edit=   URL_hwg_base+"/mobile/index.php?act=member_cart&op=cart_edit_quantity";//购物车删除
    public  String URL_hwg_goodssort=   URL_hwg_base+"/mobile/index.php?act=goods_class";//商品分类
    public  String URL_hwg_all_goods=   URL_hwg_base+"/mobile/index.php?act=goods&op=goods_list&key=4&page=10&curpage=1";//商品详情列表
    public  String URL_hwg_home_all_goods=   URL_hwg_base+"/mobile/index.php?act=goods&op=goods_list";//首页商品详情列表
    public  String URL_hwg_sort_byprice_up=   URL_hwg_base+"/mobile/index.php?act=goods&op=goods_list&key=3&order=1&page=10";//商品价格升序
    public  String URL_hwg_sort_byprice_down=   URL_hwg_base+"/mobile/index.php?act=goods&op=goods_list&key=3&order=2&page=10";//商品价格降序
    public  String URL_hwg_sort_bysale_up=   URL_hwg_base+"/mobile/index.php?act=goods&op=goods_list&key=1&order=1&page=10";//商品销量升序
    public  String URL_hwg_sort_bysale_down=   URL_hwg_base+"/mobile/index.php?act=goods&op=goods_list&key=1&order=2&page=10";//商品销量降序
    public  String URL_hwg_sort_bylike_up=   URL_hwg_base+"/mobile/index.php?act=goods&op=goods_list&key=2&order=1&page=10";//商品人气升序 GET  curpage &gc_id=594
    public  String URL_hwg_sort_bylike_down=   URL_hwg_base+"/mobile/index.php?act=goods&op=goods_list&key=2&order=2&page=10";//商品人气降序 GET curpage &gc_id=594
    public  String URL_hwg_sort_bynew_down=   URL_hwg_base+"/mobile/index.php?act=goods&op=goods_list&key=4&order=2&page=10";//商品新品降序 GET curpage &gc_id=594
    public  String URL_hwg_search_bynew_down=   URL_hwg_base+"/mobile/index.php?act=goods&op=goods_list&key=4&order=2&page=10";//查找商品新品降序 GET curpage keyword
    public  String URL_hwg_store_bynew_down=   URL_hwg_base+"/mobile/index.php?act=store&op=goods_list&key=3&order=1&page=10";//店铺商品新品降序 GET &curpage=1&store_id=6
    public  String URL_hwg_store_byprice_up=   URL_hwg_base+"/mobile/index.php?act=goods&op=goods_list&key=3&order=1&page=10";//店铺价格升序
    public  String URL_hwg_store_byprice_down=   URL_hwg_base+"/mobile/index.php?act=goods&op=goods_list&key=3&order=2&page=10";//店铺价格降序
    public  String URL_hwg_store_bysale_up=   URL_hwg_base+"/mobile/index.php?act=goods&op=goods_list&key=1&order=1&page=10";//店铺销量升序
    public  String URL_hwg_store_bysale_down=   URL_hwg_base+"/mobile/index.php?act=goods&op=goods_list&key=1&order=2&page=10";//店铺销量降序
    public  String URL_hwg_store_bylike_up=   URL_hwg_base+"/mobile/index.php?act=goods&op=goods_list&key=2&order=1&page=10";//店铺人气升序 GET  curpage &gc_id=594
    public  String URL_hwg_store_bylike_down=   URL_hwg_base+"/mobile/index.php?act=goods&op=goods_list&key=2&order=2&page=10";//店铺人气降序 GET curpage &gc_id=594
    public  String URL_hwg_wanle=  URL_hwg_base+"/mobile/index.php";//海外购的 玩乐

    public  String URL_hwg_add_to_cart=   URL_hwg_base+"/mobile/index.php?act=member_cart&op=cart_add";//加入购物车
    //  http://www.huaqiaobang.com/mobile/index.php?act=buy&op=verify_vip?goods_id=101146&key=939f6c2c1ad7199187be733cc714955a
    public  String URL_hwg_comfirm_isSale=   URL_hwg_base+"/mobile/index.php?act=buy&op=verify_vip";//验证是否可以购买商品
    public  String URL_hwg_store=   URL_hwg_base+"/mobile/index.php?act=shop_class";//所有商铺分类
    public  String URL_hwg_store_list=   URL_hwg_base+"/mobile/index.php?act=shop&op=shop_list&key=4&page=10&curpage=1";//所有商铺列表
    public  String URL_hwg_store_detail=   URL_hwg_base+"/mobile/index.php?act=store&op=store_detail";//商铺详情
    public  String URL_hwg_store_goods=   URL_hwg_base+"/mobile/index.php?act=store&op=goods_list";//商铺商品详情 &store_id=6
    public  String URL_bendi_goods=   URL_hwg_base+"/mobile/index.php?act=goods&op=native_search";//搜索本地商品
    public  String URL_hwg_order=   URL_hwg_base+"/mobile/index.php?act=member_order&op=order_list&page=10";//订单信息
    public  String URL_hwg_order_del=   URL_hwg_base+"/mobile/index.php?act=member_order&op=change_state&state_type=order_delete";//删除订单 &order_id= key  post
    public  String URL_hwg_order2=   URL_hwg_base+"/mobile/index.php?act=member_order&op=index";//订单信息  &key=8ed2e80793e7c178bb6ee4420112507c&order_sn=&query_start_date=&query_end_date=&state_type=&recycle=&curpage=2
    public  String URL_hwg_order_detail=  URL_hwg_base+"/mobile/index.php?act=member_order&op=show_order"; //订单详情  order_id= +"key=546e3e61e70eecbd9f9f1fc07a360f95"
    public  String URL_hwg_order_cancle=   URL_hwg_base+"/mobile/index.php?act=member_order&op=order_cancel";//取消订单order_id,key
    public  String URL_hwg_favorite_add=   URL_hwg_base+"/mobile/index.php?act=member_favorites&op=favorites_add";//添加收藏POST goods_id,key
//    http://www.huaqiaobang.com/mobile/index.php?act=goods&op=find_goods_favorites&goods_id=101146&key=939f6c2c1ad7199187be733cc714955a
    public  String URL_hwg_favorite_find_iscollect=   URL_hwg_base+"/mobile/index.php";//是否收藏GET goods_id,key
    public  String URL_hwg_favorite=   URL_hwg_base+"/mobile/index.php?act=member_favorites&op=favorites_list";//查看收藏列表POST key
    public  String URL_hwg_favorite_list=   URL_hwg_base+"/mobile/index.php?act=member_favorites&op=fglist&show=list";//查看收藏列表POST key curpage
    public  String URL_hwg_favorite_del=   URL_hwg_base+"/mobile/index.php?act=member_favorites&op=favorites_del";//删除收藏POST fav_id:goods_id  key:key
    public  String URL_hwg_address_list=   URL_hwg_base+"/mobile/index.php?act=member_address&op=address_list";//收货地址列表POST key:key
    public  String URL_hwg_address_add=   URL_hwg_base+"/mobile/index.php?act=member_address&op=address_add";//添加收货地址POST key:key,true_name:true_name,mob_phone:mob_phone,tel_phone:tel_phone,city_id:city_id,area_id:area_id,address:address,area_info:area_info
    public  String URL_hwg_address_del=   URL_hwg_base+"/mobile/index.php?act=member_address&op=address_del";//删除收货地址POST address_id:address_id,key:key
    public  String URL_hwg_address_info=   URL_hwg_base+"/mobile/index.php?act=member_address&op=address_info";//收货地址信息POST key:key,address_id:result.datas.address_id
    public  String URL_hwg_address_edit=   URL_hwg_base+"/mobile/index.php?act=member_address&op=address_edit";//修改收货地址信息POST key: key,true_name: true_name,mob_phone: mob_phone,tel_phone: tel_phone,city_id: city_id,area_id: area_id,address: address,area_info: area_info,address_id: address_id
    public  String URL_hwg_address_addedit=   URL_hwg_base+"/mobile/index.php?act=member_address&op=address&type=add";//增加修改收货地址信息POST key: key,true_name: true_name,mob_phone: mob_phone,tel_phone: tel_phone,city_id: city_id,area_id: area_id,address: address,area_info: area_info,address_id: address_id  is_default
    public  String URL_hwg_address_change=   URL_hwg_base+"/mobile/index.php?act=member_buy&op=change_address";//保存收货地址信息POST key:key,area_id:area_id,city_id:city_id,freight_hash:freight_hash
    public  String URL_hwg_province=   URL_hwg_base+"/mobile/index.php?act=member_address&op=area_list";//获取省份POST &key=5a629e08d52f614016291c3b413bb4e1
    public  String URL_hwg_city=   URL_hwg_base+"/mobile/index.php?act=member_address&op=area_list";//获取城市POST &key= &area_id=3
    public  String URL_hwg_area=   URL_hwg_base+"/mobile/index.php?act=member_address&op=area_list";//获取区县POST &key= &area_id=3
    public  String URL_hwg_buystep1=   URL_hwg_base+"/mobile/index.php?act=member_buy&op=buy_step1";//购买步骤1POST &key=1&ifcart=1&cart_id=34
    public  String URL_hwg_buy_step1=   URL_hwg_base+"/mobile/index.php?act=buy&op=buy_step1";//购买步骤1POST  key   cart_id[0]  ifcart=1   store_id
    public  String URL_hwg_buystep2=   URL_hwg_base+"/mobile/index.php?act=member_buy&op=buy_step2";//提交订单POST
    public  String URL_hwg_buy_step2=   URL_hwg_base+"/mobile/index.php?act=buy&op=buy_step2";//提交订单POST
    public  String URL_hwg_invoice_list=   URL_hwg_base+"/mobile/index.php?act=member_invoice&op=invoice_list";//个人发票列表POST &key=b35bc658195323c62496491d97904dfc
    public  String URL_hwg_invoice_add=   URL_hwg_base+"/mobile/index.php?act=member_invoice&op=invoice_add";//添加发票POST

    //    http://www.huaqiaobang.com/mobile/index.php?act=vip&op=find_vip_yyg&key=a7954eaa08758d90c9b6de16beb63e17
    public  String URL_vip_isggao=   URL_hwg_base+"/mobile/index.php";//是否有Vip广告
    public  String URL_vip_ishuodong=   URL_hwg_base+"/mobile/index.php?act=vip&op=find_vip_yyg";//是否有海外购活动
    public  String URL_vip_huodong_detials=   URL_hwg_base+"/mobile/index.php?act=vip&op=vip_yyg_detail&key=";//海外购活动详情
    //    http://www.huaqiaobang.com/mobile/index.php?act=member_payment&op=pay
    public  String URL_vip_zhifubao=   URL_hwg_base+"/mobile/index.php?act=member_payment&op=vip_pay";//vip 支付宝支付
    //个人发票  key:key,inv_title_select=person,inv_content:inv_content
    //公司发票  key:key,inv_title_select=company,inv_title:inv_title,inv_content:inv_content
    public  String URL_hwg_invoice_del=   URL_hwg_base+"/mobile/index.php?act=member_invoice&op=invoice_del";//删除发票POST
    public  String URL_hwg_confirm_pwd=   URL_hwg_base+"/mobile/index.php?act=member_buy&op=check_password";//支付密码POST key:key,password:loginpassword
    public  String URL_hwg_zhifu=   URL_hwg_base+"/mobile/index.php";//支付 &key=2ed0e443a942c2bf90209eb6d05a2a7b&pay_sn=570510083706271013&payment_code=alipay
    //直接结算 key:4bf3306769676d5c8ee3da305c617868,cart_id:100077|1,address_id:15,vat_hash:QplLdnZIbnWzcD8_Zg3aGYUb9iw67yoYkKO,offpay_hash:vyupBX3otQ8k27EvK4nVhM74F-H4E8mKVrD-oFi,offpay_hash_batch:DKD8CaJ3CnB6pAWzcbZHC9b9Br3T3EfIUiOeYvqUpP2QVFI,pay_name:online,invoice_id:undefined,voucher:rcb_pay:0,pd_pay:0
    //从购物车结算 key:4bf3306769676d5c8ee3da305c617868,ifcart:1,cart_id:39|1,address_id:15,vat_hash:vzsUd_ZDayoUxhK9hSoTl4.4csWBEowkFVN,offpay_hash:A2AmxTBo9OWUUE5p1LFoYlJQHuY8W5WGfrTEs5s,offpay_hash_batch:sKHaEeGqp9F-T-3zQ2Fq0a1rxUT7ih01cUJ8Idp1pDRzHii,pay_name:online,invoice_id:undefined,voucher:,rcb_pay:0,pd_pay:0
    public  String URL_hwg_deliver_seach=   URL_hwg_base+"/mobile/index.php?act=member_order&op=search_deliver";//物流查询 key order_id
    public  String URL_hwg_order_receive=   URL_hwg_base+"/mobile/index.php?act=member_order&op=order_receive";//确认收货 order_id:order_id,key:key
    public  String URL_hwg_goods_search=   URL_hwg_base+"/mobile/index.php?act=goods&op=goods_list&key=4&page=10";//搜索商品 &curpage=1&keyword=
    public  String URL_hwg_store_search=   URL_hwg_base+"/mobile/index.php?act=store_list&keyword=";//搜索店铺 &keyword=
    public  String URL_hwg_special=   URL_hwg_base+"/mobile/index.php?act=index&op=special";//首页专题 &special_id=3=
    //    http://www.huaqiaobang.com/mobile/index.php?act=dudao&op=find_dudao_index&key=91f71c62fa363e465aa6a6d824aad4d8
    public  String URL_hwg_find_dudao=   URL_hwg_base+"/mobile/index.php?act=dudao&op=find_dudao_index";//首页  独到
    public  String URL_vips_list=   URL_hwg_base+"/mobile/index.php?act=vip&op=find_vip_card";//vip 会员列表
    public  String URL_vips_center=   URL_hwg_base+"/mobile/index.php?act=vip&op=vip_index";//vip 会员中心
    public  String URL_travel_detials_upload_pic=   URL_hwg_base+"/mobile/index.php?act=vip&op=vip_index";//vip 会员中心
    //    http://www.huaqiaobang.com/mobile/index.php?act=member_feedback&op=add_member_feedback&key=939f6c2c1ad7199187be733cc714955a&key=939f6c2c1ad7199187be733cc714955a
    public  String URL_hqb_feedback=   URL_hwg_base+"/mobile/index.php?act=member_feedback&op=add_member_feedback";//意见反馈
    public  String URL_hwg_chongzhi=   URL_hwg_base+"/mobile/index.php?act=predeposit&op=rechargecard_add";//充值卡充值 & rc_sn= &key=
    public  String URL_hwg_paypwd=   URL_hwg_base+"/mobile/index.php?act=security&op=modify_paypwd";//设置修改支付密码 password    confirm_password    key
    public  String URL_hwg_member=   URL_hwg_base+"/mobile/index.php?act=security&op=index";//用户信息  key
    public  String URL_hwg_phone_bind=   URL_hwg_base+"/mobile/index.php?act=security&op=modify_mobile";//手机绑定  key   mobile   vcode
    public  String URL_hwg_email_bangding=   URL_hwg_base+"/mobile/index.php?act=security&op=send_bind_email";//邮箱绑定 & email=  &key=
    public  String URL_hwg_phone_bind_step1=   URL_hwg_base+"/mobile/index.php";//手机绑定发送验证码 act=security&op=send_auth_code type=mobile/email  key  get请求
    public  String URL_hwg_phone_bind_first=   URL_hwg_base+"/mobile/index.php";//第一次手机绑定  act=security&op=send_modify_mobile &mobile=18977934562 key  get请求
    public  String URL_hwg_verify_code=   URL_hwg_base+"/mobile/index.php?act=security&op=auth";//统一验证码验证 key  type  auth_code  post请求
    public  String URL_hwg_comment=   URL_hwg_base+"/mobile/index.php?act=member_evaluate&op=add";//发表商品评论 &order_id=211 goods[100156][score] 商品评分   goods[100156][comment] 评论  anony 是否匿名（0/1）  store_desccredit 宝贝与描述相符   store_servicecredit 卖家的服务态度  store_deliverycredit 卖家的发货速度  key  post请求
    public  String URL_hwg_iscomment=   URL_hwg_base+"/mobile/index.php?act=member_order&op=index&key=083ac599d1d198fa422a1a0236f73ad2&order_sn=8000000000024201&query_start_date=&query_end_date=&state_type=&recycle=";//订单列表
    public  String URL_hwg_comment_list=   URL_hwg_base+"/mobile/index.php?act=member_evaluate&op=list";//我的商品评论列表 &curpage=4&key=083ac599d1d198fa422a1a0236f73ad2  get请求
    public  String URL_hwg_comment_share_step1=   URL_hwg_base+"/mobile/index.php?act=member_evaluate&op=add_image";//晒单步骤1 &geval_id=113 key= post请求
    public  String URL_hwg_comment_share_step2=   URL_hwg_base+"/mobile/index.php?act=sns_album&op=swfupload";//晒单步骤2 key   category_id=   file= post请求
    public  String URL_hwg_comment_share_step3=   URL_hwg_base+"/mobile/index.php?act=member_evaluate&op=add_image_save";//晒单步骤3 geval_id=  evaluate_image[n]=   key=  post请求
    public  String URL_hwg_comment_share_delete=   URL_hwg_base+"/mobile/index.php?act=sns_album&op=album_pic_del";//晒单删除图片 &id= key get请求，，key是post请求    id=file_id
    public  String URL_hwg_goods_comment=   URL_hwg_base+"/mobile/index.php?act=member_evaluate&op=comments_list";//商品评论 &goods_id=100156&type=all&curpage=2&key=a0f3a149057190cdac1e93ae1d4807ec
    public  String URL_hwg_goods_comment2=   URL_hwg_base+"/mobile/index.php?act=discountgoods&op=comments_list";//商品评论 &goods_id=100156&type=all&curpage=2&key=a0f3a149057190cdac1e93ae1d4807ec
    public  String URL_hwg_goods_refund=   URL_hwg_base+"/mobile/index.php?act=member_refund&op=add_refund";//退货退款 &order_id=270 &goods_id=289 key   refund_pic1   refund_pic2  refund_pic3  goods_num产品数量  reason_id退款原因99 98 97 96 95 0   refund_type类型:1为退款,2为退货
    public  String URL_hwg_refund=   URL_hwg_base+"/mobile/index.php?act=member_refund&op=add_refund_all";//订单退款 &order_id=269 key  refund_pic1    refund_pic2    refund_pic3   buyer_message原因
    public  String URL_hwg_refund_list=   URL_hwg_base+"/mobile/index.php?act=member_refund&op=index";//退款申请列表  type（'order_sn','refund_sn','goods_name'） 编号类型  key 值  add_time_from-add_time_to 开始-结束时间  get 请求
    public  String URL_hwg_refund_list_detail=   URL_hwg_base+"/mobile/index.php?act=member_refund&op=view";//退款申请详情  refund_id 编号 key   退款记录查看  get请求
    public  String URL_hwg_return_list=   URL_hwg_base+"/mobile/index.php?act=member_return&op=index";//退货申请列表  type（'order_sn','refund_sn','goods_name'） 编号类型  key 值  add_time_from-add_time_to 开始-结束时间  get 请求
    public  String URL_hwg_return_list_detail=   URL_hwg_base+"/mobile/index.php?act=member_return&op=view";//退货申请详情  refund_id 编号    退货记录查看  get请求
    public  String URL_hwg_return_deliver=   URL_hwg_base+"/mobile/index.php?act=member_return&op=ship";//退货物流填写 express_id     invoice_no
    public  String URL_hwg_vouncher_list=   URL_hwg_base+"/mobile/index.php?act=pointvoucher&op=index&sc_id=&price=&isable=&points_min=&points_max=&orderby=";//代金券列表get &key=eb5b53cd234a2c32799f1a695c653d7d&curpage=1
    public  String URL_hwg_vouncher_detail=   URL_hwg_base+"/mobile/index.php?act=pointvoucher&op=voucherexchange";//代金券详情 get vid &key=
    public  String URL_hwg_my_vouncher=   URL_hwg_base+"/mobile/index.php?act=member_points&op=showvoucher";//我的代金券详情 &key=ad18fb770a18635288e6798adbd5dce9&voucher_code=420514983297516015
    public  String URL_hwg_vouncher_save=   URL_hwg_base+"/mobile/index.php?act=pointvoucher&op=voucherexchange_save";//代金券兑换post vid &key=
    public  String URL_hwg_point_detail=   URL_hwg_base+"/mobile/index.php?act=member_points&op=index";//积分明细 &stage=&stime=&etime=&description=&key=&curpage=
    //stage=regist注册 login登录 comments商品评论 order订单消费 system积分管理 pointorder礼品兑换 app积分兑换
    public  String URL_hwg_my_voucher=   URL_hwg_base+"/mobile/index.php?act=member_voucher&op=voucher_list";//我的代金券列表 post  key= &voucher_state=1未使用 2已使用 3已过期 &curpage

    public  String URL_hwg_login_for_point=  URL_hwg_base+"/mobile/index.php?act=login1&op=index&inajax=1"; //登录获取积分  user_name=&password=&key=   post
    public  String URL_hwg_fuyou_pay=  URL_hwg_base+"/mobile/index.php?act=member_payment&op=pay"; //富友支付  &key=89733e91ff43ba5e7918433edf6a97ce&pay_sn=180516121977532015&payment_code=fuyou
    public  String URL_hwg_buy_suitgoods=  URL_hwg_base+"/mobile/index.php"; //商品详情购买优惠套装  GET ?act=member_cart&op=suitadd?bl_id=?&key=?
    public  String URL_hwg_apply_cash=  URL_hwg_base+"/mobile/index.php?act=predeposit&op=pd_cash_add"; //提现  pdc_amount 提现金额  pdc_bank_name 收款银行  pdc_bank_no 收款账号  pdc_bank_user 开户人姓名  password 支付密码 pdc_bank_phone post请求
    public  String URL_hwg_recharge_list=  URL_hwg_base+"/mobile/index.php?act=predeposit&op=rcb_log_list"; //充值卡详情  get请求  key curpage=1
    public  String URL_hwg_predeposit_list=  URL_hwg_base+"/mobile/index.php?act=predeposit&op=pd_cash_list"; //预存款提现列表  get请求  &key=5bfc10c25c36136111c3295cfe953222&sn_search=&paystate_search=&curpage=1
    public  String URL_hwg_predeposit_detail=  URL_hwg_base+"/mobile/index.php?act=predeposit&op=pd_cash_info"; //预存款提现详情  get请求 id=3&key=
    public  String URL_hwg_redbag_lingqu=  URL_hwg_base+"/mobile/index.php?act=word_receive&op=index"; //领取红包  get请求 id=3&key=
    public  String URL_hwg_redbag_detail=  URL_hwg_base+"/mobile/index.php?act=word_receive&op=find_red_envelope"; //领取红包  get请求 id=3&key=
    public  String URL_hwg_send_redbag_num=  URL_hwg_base+"/mobile/index.php?act=circle_index&op=count_red_envelope"; //已经发出红包数
    public  String URL_hwg_my_redbag=  URL_hwg_base+"/mobile/index.php?act=word_receive&op=my_red_envelope"; //我的红包记录
    public  String URL_hwg_my_order_num=  URL_hwg_base+"/mobile/index.php?act=member_order&op=countorder"; //订单分类数量
    public  String URL_hwg_brand_list=  URL_hwg_base+"/mobile/index.php?act=brand&op=index"; //首页品牌列表
    public  String URL_hwg_my_vourcher_count=  URL_hwg_base+"/mobile/index.php?act=member_voucher&op=my_voucher"; //我的优惠券数量

    public  String URL_hwg_hot_search=  URL_hwg_base+"/mobile/index.php";//热门搜索
    public  String URL_hwg_company_connect=  URL_hwg_base+"/mobile/index.php?act=order_add&op=add_enterprise";//企业绑定 key enterprise_name invitation_code
    public  String URL_hwg_find_company_connect=  URL_hwg_base+"/mobile/index.php?act=order_add&op=find_enterprise";//查找企业绑定 key
    public  String URL_hwg_edit_company_connect=  URL_hwg_base+"/mobile/index.php?act=order_add&op=edit_enterprise";//修改企业绑定 keyenterprise_name invitation_code
    public  String URL_hwg_yyg_send_goods=  URL_hwg_base+"/mobile/index.php?act=order_add&op=add_order";//一元购发货 goods_id address_id key
    public  String URL_local_upload_pic=  URL_hwg_base+"/mobile/index.php?act=many_index&op=add_picture";//本地上传图片
    public  String URL_first_get_vouncher=  URL_hwg_base+"/mobile/index.php?act=member_voucher&op=get_voucher&key=";
    public  String URL_travel_tedian=  URL_hwg_base+"/mobile/index.php"; // 旅游 会员特典   city_id=55&type=2

    public  String URL_local_peisong=  URL_hwg_base+"/mobile/index.php";//本地 商品配送
    public  String URL_hwg_wangles_left=  URL_hwg_base+"/mobile/index.php";//海外购 玩乐
    public  String URL_hwg_fenlei_left=  URL_hwg_base+"/mobile/index.php";//海外购 玩乐  http://www.huaqiaobang.com/mobile/index.php?act=goods_class&op=get_goods_class
    //yyg  ok
    public  String URL_yyg_goods_list = URL_bind_base+"/xys/activity/getActivityList"; //一元购商品列表  get  ? status=0&page=1&pageSize=3
    public  String URL_yyg_login =  URL_bind_base+"/xys/user/addUser"; //一元购用户登录  get  ? nickname=测试1&userId=1&avator=2211
    public  String URL_yyg_pay =  URL_bind_base+"/xys/drecord/pay"; //一元购支付 post  &activityId=" + activity_id + "&buyNum=" + num + "&payType=0
    public  String URL_yyg_pay_new =  URL_bind_base+"/xys/drecord/pay_new"; //  googleplay 版 一元购支付 post  &activityId=" + activity_id + "&buyNum=" + num + "&payType=0
    public  String URL_yyg_goods_detail =  URL_bind_base+"/xys/activity/getActivityParticulars"; //一元购商品详情  get  ? activityId=1014"
    public  String URL_yyg_goods_record=  URL_bind_base+"/xys/drecord/getAllRecordsByActivityId"; //一元购购买记录  get  ? page=2&pageSize=10&activityId=1014
    public  String URL_yyg_goods_my_buy_record=  URL_bind_base+"/xys/drecord/getRecordsListByUserId"; //一元购我的购买记录  get  ?userId=10017&page=1&pageSize=5
    public  String URL_yyg_goods_my_lottery_record=  URL_bind_base+"/xys/drecord/getGainRecordsListByUserId"; //一元购我的中奖记录  get ?userId=10001&page=1&pageSize=5
    public  String URL_yyg_goods_prerecord =  URL_bind_base+"/xys/activity/getBeforeActivityList"; //一元购往期揭晓 get ?activityId=1016&page=1&pageSize=5
    public  String URL_yyg_buy_code =  URL_bind_base+"/xys/activity/getBuyCodeByActivityId"; //一元购幸运号码 get ?activityId=1016&page=1&pageSize=10&userId=11389
    public  String URL_yyg_my_balance =  URL_bind_base+"/xys/user/getUserMoney"; //一元购用户余额 get ?userId=13182
    public  String URL_yyg_lottery_num =  URL_bind_base+"/xys/drecord/getRecordsCount"; //一元购揭晓期数 get
    public  String URL_yyg_exchange =  URL_bind_base+"/xys/drecord/exchangeMoney"; //一元购兑换商品 post userId= &activityId=
    public  String URL_yyg_add_address =  URL_bind_base+"/xys/address/addConsigneeAddress"; //一元购添加地址
    public  String URL_yyg_find_my_outcome =  URL_bind_base+"/xys/drecord/getTotalPaymentVolume"; //一元购总支出 post userId=
    public  String URL_yyg_find_my_outcome_list =  URL_bind_base+"/xys/incomeAndExpenses/getRecordList"; //一元购总支出列表 get  page=1&pageSize=10&userId=10001



    //local  本地
    public  String URL_local_rest_fenqu_detisal =  URL_bind_base +"/hrq/menu/getRestaurants";//本地 通讯录 分区详情
    public  String URL_local_main_fragment =  URL_bind_base +"/hrq/conListDetail/getHomePage.json";//本地首页get  ?countryEnName=fr&cityId=1
    public  String URL_local_publish =  URL_bind_base +"/hrq/conListDetail/issueMessage.post";//本地发布 post  countryEnName  cityId title content contact contactMan listTypeId
    public  String URL_local_get_menuList =  URL_bind_base +"/hrq/conListDetail/getConListByConType.json";//本地菜单列表 get  ?countryEnName=it&cityId=1&page=1&pageSize=5&menuParentId=100&type=1
    public  String URL_local_get_menuList_new =  URL_bind_base +"/hrq/City/getCity";//新版本地菜单列表 get  ?version=version1.0
    public  String URL_local_get_subTitle =  URL_bind_base +"/hrq/menu/getSubMenu.json";//获取列表title get  ?menuId=100
    public  String URL_local_get_detail =  URL_bind_base +"/hrq/conListDetail/getConByid.json";//详情信息 get  ?conId=966
    public  String URL_local_get_area =  URL_bind_base +"/hrq/City/getArea.json";//获取大洲 get
    public  String URL_local_get_country =  URL_bind_base +"/hrq/City/getCountryListByAreaId.json";//获取国家?areaId=759
    public  String URL_local_get_city =  URL_bind_base +"/hrq/City/getCityListByCountryId.json";//获取城市?countryId=11
    public  String URL_local_get_mypublish =  URL_bind_base +"/hrq/conListDetail/getMyIssueList.json";//获取我的发布?userId=11
    public  String URL_local_get_more_news =  URL_bind_base +"/hrq/news//getNewsList";//获取更多新闻 page=2&pageSize=20&cityId=11593
    public  String URL_local_get_more_msg =  URL_bind_base +"/hrq/conListDetail/newestInfo.json";//获取更多消息 page=2&pageSize=20&cityId=11593
    public  String URL_local_search_city =  URL_bind_base +"/hrq/City/getCityListByName.json";//城市查找 ?cityName
    public  String URL_local_delete_mypublish =  URL_bind_base +"/hrq/conListDetail/delMyPublish";//删除我的发布?id=60499
    public  String URL_local_get_area2_location =  URL_bind_base +"/hrq/City/getCity";//获取城市_新版
    public  String URL_local_get_area3_location =  URL_bind_base +"/hrq/City/getCity";//获取城市_新版?version=v1.0


    //一元云购接口
    public  String URL_GOODS_SORT= URL_hwg_new_base +"/XiaoYiShi/rest/get/shopSort"; //商品分类接口 cateid= ，&pages=1，&pagelist=30
    public  String URL_GOODS_ALL= URL_hwg_new_base +"/XiaoYiShi/rest/get/shopSortList"; //所有商品接口 key=shopList&pages=1&pagelist=30
    public  String URL_GOODS_BUY= URL_hwg_new_base +"/XiaoYiShi/rest/shopBuy/memBuy"; //商品支付接口"uid=" + MyApplication.getInstance().getUuid()
    public  String URL_GOODS_JX= URL_hwg_new_base +"/XiaoYiShi/rest/get/shopSortList";  //最新揭晓接口 key=jsShopList&pages=1&pagelist=30
    public  String URL_GOODS_LOGIN= URL_hwg_new_base +"/XiaoYiShi/rest/member/login"; //信息存储接口 "uid=" + uid + "&nickname=" + nickname+"&avatar="+avatar
    public  String URL_GOODS_SHOPCAR= URL_hwg_new_base +"/XiaoYiShi/rest/shopCart/add"; //加入购物车接口"uid=" +  "&id="+ "&num=1"+"&qishu="+
    public  String URL_GOODS_SHOPCARDES= URL_hwg_new_base +"/XiaoYiShi/rest/shopCart/getCartMsg";//购物车所有商品接口 "uid="
    public  String URL_GOODS_SHOPCARDEL= URL_hwg_new_base +"/XiaoYiShi/rest/shopCart/delOne"; //购物车列表删除接口 "uid=" +"&id="+"&qishu="+
    public  String URL_GOODS_UPDATENUM= URL_hwg_new_base +"/XiaoYiShi/rest/shopCart/UpdateNum";//购物车数量更新接口"uid=" +"&id="+"&num="+"&qishu="+
    public  String URL_GOODS_MYRECORD= URL_hwg_new_base +"/XiaoYiShi/rest/shopBuy/memBuyJil";    //我的云购记录接口 "uid=" + "&pages=1&pagelist=30"
    public  String URL_GOODS_MYRECORDDETAIL= URL_hwg_new_base +"/XiaoYiShi/rest/shopBuy/jilMsg";//我的云购记录详情接口"uid="+"&id="+"&qishu="
    public  String URL_GOODS_MYCOST= URL_hwg_new_base +"/XiaoYiShi/rest/member/moneyMsg";//我的消费记录 "uid="+ +"&pages=1&pagelist=20"

    public  String URL_LOGINFORHWG=  URL_hwg_base+"/mobile/index.php?act=login";                        //3
    public  String URL_HWGREGISTER=  URL_hwg_base+"/mobile/index.php?act=login&op=register";            //3


    //本地存储文件名
    public  String HWGBANNER="hwgBannerObject";//海外购banner
    public  String HWGHOT="hwgHotObject";//海外购热销
    public  String HWGWEEK="hwgWeekObject";//海外购每周热卖
    public  String HWGUPDATE="hwgUpdateObject";//海外购最近更新
    public  String HWGMAIN="hwgMainObject";//海外购首页fragment
    public  String HWGWABLE="hwgWanLeObject";//海外购首页fragment
    public  String HWGDETIADLS="hwgdetials";//海外购首页二级页面
    public  String LOCALMENU="localMenu";
    public  String LOCALMESSAGE="localMessage";
    public  String LOCALNEWS="localNews";
    public  String LOCALCOMMUNITY="localCommunity";
    public  String LOCALFRAGMENT="localFragment";//本地首页
    public  String LOCALFRAGMENTWX="localFragmentWx";//本地首页微信新闻
    public  String LOCALAREA="localArea";//本地地区
    public  String LOCALCOUNTRY="localCountry";//本地国家
    public  String HWGLUXURY="hwgLuxury";//海外购轻奢
    public  String HWGDUDAO="hwgdudao";//海外购独到
    public  String HWGDDAO="hwgddao";//海外购独到
    public  String LOCAL_MSG_DETAIL="localMsgDetail_";//本地消息详情
    public  String LOCAL_NEWS_DETAIL="localNewsDetail_";
    public  String LOGINTOKEN="loginToken";//登录token
    //这是刚刚进入海外购页面显示对话框的接口
    public  String URL_HWG_DLG=  URL_hwg_base+"/mobile/index.php";
}
