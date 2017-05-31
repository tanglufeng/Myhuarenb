package com.abcs.hqbtravel.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.view.zjzbanner.LMBanners;
import com.abcs.haiwaigou.view.zjzbanner.adapter.LBaseAdapter;
import com.abcs.haiwaigou.view.zjzbanner.transformer.TransitionEffect;
import com.abcs.haiwaigou.view.zjzbanner.utils.ScreenUtils;
import com.abcs.hqbtravel.Contonst;
import com.abcs.hqbtravel.adapter.GridDetialsChiAdapter;
import com.abcs.hqbtravel.adapter.GridDetialsMaiAdapter;
import com.abcs.hqbtravel.adapter.GridDetialsWanAdapter;
import com.abcs.hqbtravel.adapter.TravelDetialCommentsAdapter;
import com.abcs.hqbtravel.adapter.TravelDetialGoodsAdapter;
import com.abcs.hqbtravel.entity.BannersBean;
import com.abcs.hqbtravel.entity.BiChiDetials;
import com.abcs.hqbtravel.entity.Comment;
import com.abcs.hqbtravel.entity.CommentItem;
import com.abcs.hqbtravel.entity.MyTickets;
import com.abcs.hqbtravel.entity.RestauransBean;
import com.abcs.hqbtravel.entity.ShopBean;
import com.abcs.hqbtravel.entity.TouristAttractionsBean;
import com.abcs.hqbtravel.wedgt.CommentUtils;
import com.abcs.hqbtravel.wedgt.Config;
import com.abcs.hqbtravel.wedgt.MyGridView;
import com.abcs.hqbtravel.wedgt.MyListView;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.abcs.sociax.t4.android.video.ToastUtils.showToast;

public class ShiBaJiaDetials2Activity extends Activity implements  View.OnClickListener{

    private LinearLayout liner_commet;
    private ImageView mMapView,img_back,img_bc1,img_bc2,img_bc3,img_bc4,img_bc5;

    private LinearLayout liner_tejia,liner_location,liner_phone,liner_opentime,liner_http,liner_tips,liner_detials,bibi_one,bibi_two,bibi_three,bbb_one,bbb_two,liner_instrution,liner_googs,bbb_three;

    private MyGridView grid_detials_chi;
    private MyGridView grid_detials_wan;
    private MyGridView grid_detials_mai;

    private RelativeLayout re_wenluka,imge_paly,relat_collect_in,relati_laiguo;
    MyListView listView;
    private MyListView listview_comments;
    private View bg_view;
    private LinearLayout liners_lookmore,left,liners_more_gonglie;
    private TextView tv_lookmore,tv_more_gonglie,tv_collect,tv_youji_note,tv_youji_notewe,tv_from_num;
    private ImageView tie,img_share;
    private List<ImageView> imgs=new ArrayList<>();

    private ScrollView scrollview;
    private TextView tv_address,tv_tips,tv_opentime,img_dddown,img_dddown2,img_dddown3;
    private TextView tv_name,tv_title,tv_piv_num,tv_pinfen,tv_site,tv_phone,tv_tags;
    private TextView tv_introduction,tv_pinlin_num,tv_chakan_all,tv_chakan_all_bg,tv_bichi,tv_morebichi,tv_bimai,tv_morebimai,tv_biwan,tv_morebiwan;
//    private ViewPager viewPager;
//    private ViewPagerAdapter adapter;
private LMBanners banners;
    private ArrayList<String> bannerString = new ArrayList<>();
    private List<Map<String, Object>> data;

//    private GridView gridLayout;
    private LinearLayout lin_kanall;
//    ProgressDialog dialog;
    private String cityId;
    private SwipeRefreshLayout swipeRefreshLayout;

    private List<BannersBean> newbanners=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_bi_chi_detials);

        cityId=getIntent().getStringExtra("cityId");
        initViews();
        setOnClick();
        if(!TextUtils.isEmpty(restaurantId)&&!TextUtils.isEmpty(cityId)){
            loadDatas(restaurantId,cityId);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
//        mhandle.postDelayed(mRunnable,200);
    }

    private Handler mhandle=new Handler();
    Runnable mRunnable=new Runnable() {
        @Override
        public void run() {
            scrollview.fullScroll(ScrollView.FOCUS_UP);
        }
    };

    @Override
    protected void onResume() {
//        mhandle.postDelayed(mRunnable,200);
        super.onResume();
        if(isFresh){
            loadDatas(restaurantId,cityId);
        }
    }

    private void initViews(){

        restaurantId=getIntent().getStringExtra("chiId");
        photo=getIntent().getStringExtra("chiphoto");
//        gridLayout=(GridView) findViewById(R.id.linnerLayout);
        tv_tags=(TextView) findViewById(R.id.tv_tags);
        bg_view=(View) findViewById(R.id.bg_view);
        tie=(ImageView)findViewById(R.id.tie);
        img_share=(ImageView)findViewById(R.id.img_share);
        listview_comments=(MyListView) findViewById(R.id.listview_comments);
        relat_collect_in=(RelativeLayout)findViewById(R.id.relat_collect_in);
        relati_laiguo=(RelativeLayout)findViewById(R.id.relati_laiguo);
        listview_comments=(MyListView) findViewById(R.id.listview_comments);
        liners_lookmore=(LinearLayout) findViewById(R.id.liners_lookmore);
        liners_more_gonglie=(LinearLayout) findViewById(R.id.liners_more_gonglie);
        left=(LinearLayout) findViewById(R.id.left);
        liner_commet=(LinearLayout)findViewById(R.id.liner_commet);
        tv_lookmore=(TextView) findViewById(R.id.tv_lookmore);
        tv_more_gonglie=(TextView) findViewById(R.id.tv_more_gonglie);
        tv_from_num=(TextView) findViewById(R.id.tv_from_num);
        tv_youji_note=(TextView) findViewById(R.id.tv_youji_note);
        tv_youji_notewe=(TextView) findViewById(R.id.tv_youji_notewe);
        tv_collect=(TextView) findViewById(R.id.tv_collect);
        grid_detials_chi=(MyGridView) findViewById(R.id.grid_detials_chi);
        grid_detials_wan=(MyGridView) findViewById(R.id.grid_detials_wan);
        grid_detials_mai=(MyGridView) findViewById(R.id.grid_detials_mai);
        lin_kanall=(LinearLayout) findViewById(R.id.lin_kanall);
        img_back=(ImageView)findViewById(R.id.img_back);
        imge_paly=(RelativeLayout)findViewById(R.id.imge_paly);
        mMapView = (ImageView) findViewById(R.id.bmapView);
        listView=(MyListView)findViewById(R.id.listview);
        scrollview=(ScrollView) findViewById(R.id.scrollview);


        mMapView.setOnClickListener(this);

        img_bc1=(ImageView)findViewById(R.id.img_bc1);
        img_bc2=(ImageView)findViewById(R.id.img_bc2);
        img_bc3=(ImageView)findViewById(R.id.img_bc3);
        img_bc4=(ImageView)findViewById(R.id.img_bc4);
        img_bc5=(ImageView)findViewById(R.id.img_bc5);
        imgs.add(img_bc1);
        imgs.add(img_bc2);
        imgs.add(img_bc3);
        imgs.add(img_bc4);
        imgs.add(img_bc5);


        re_wenluka=(RelativeLayout)findViewById(R.id.re_wenluka);
        liner_tejia=(LinearLayout)findViewById(R.id.liner_tejia);
        liner_location=(LinearLayout)findViewById(R.id.liner_location);
        bbb_two=(LinearLayout)findViewById(R.id.bbb_two);
        bbb_one=(LinearLayout)findViewById(R.id.bbb_one);

        liner_phone=(LinearLayout)findViewById(R.id.liner_phone);
        liner_opentime=(LinearLayout)findViewById(R.id.liner_opentime);
        liner_http=(LinearLayout)findViewById(R.id.liner_http);
        liner_tips=(LinearLayout)findViewById(R.id.liner_tips);
        liner_detials=(LinearLayout)findViewById(R.id.liner_detials);
        liner_instrution=(LinearLayout)findViewById(R.id.liner_instrution);
//        liner_googs=(LinearLayout)findViewById(R.id.liner_googs);
//        bibi_three=(LinearLayout)findViewById(R.id.bibi_three);
//        bibi_two=(LinearLayout)findViewById(R.id.bibi_two);
//        bibi_one=(LinearLayout)findViewById(R.id.bibi_one);
        bbb_three=(LinearLayout)findViewById(R.id.bbb_three);

//        tv_name=(TextView) findViewById(R.id.tv_name);

        tv_title=(TextView) findViewById(R.id.tv_title);
        tv_pinfen=(TextView) findViewById(R.id.tv_pinfen);
        tv_tips=(TextView) findViewById(R.id.tv_tips);
        tv_opentime=(TextView) findViewById(R.id.tv_opentime);
        tv_site=(TextView) findViewById(R.id.tv_site);
        tv_phone=(TextView) findViewById(R.id.tv_phone);
        tv_address=(TextView) findViewById(R.id.tv_address);
        img_dddown=(TextView) findViewById(R.id.img_dddown);
        img_dddown2=(TextView) findViewById(R.id.img_dddown2);
        img_dddown3=(TextView) findViewById(R.id.img_dddown3);
        tv_introduction=(TextView) findViewById(R.id.tv_introduction);
        tv_chakan_all=(TextView) findViewById(R.id.tv_chakan_all);


        tv_biwan=(TextView) findViewById(R.id.tv_biwan);
        tv_morebiwan=(TextView) findViewById(R.id.tv_morebiwan);

        tv_bichi=(TextView) findViewById(R.id.tv_bichi);
        tv_morebichi=(TextView) findViewById(R.id.tv_morebichi);

        tv_bimai=(TextView) findViewById(R.id.tv_bimai);
        tv_morebimai=(TextView) findViewById(R.id.tv_morebimai);


        tv_chakan_all_bg=(TextView) findViewById(R.id.tv_chakan_all_bg);
        tv_piv_num=(TextView) findViewById(R.id.tv_piv_num);
        tv_pinlin_num=(TextView) findViewById(R.id.tv_pinlin_num);

//        tv_biwan.setText("必吃");
//        tv_bichi.setText("附近必吃");
//        tv_bimai.setText("");

        banners=(LMBanners) findViewById(R.id.banners);
//
//        viewPager=(ViewPager) findViewById(viewPager);
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//
//            @Override
//            public void onPageScrolled(int page, float positionOffset,
//                                       int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int page) {
//                tv_piv_num.setText((page+1)+"/"+newbanners.size());
//            }
//        });

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
        //设置刷新时动画的颜色，可以设置4个
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDatas(restaurantId,cityId);
                mhandle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });


    }



    List<TouristAttractionsBean> biwan=new ArrayList<>();
    public void getBiWanData(List<TouristAttractionsBean> oldbiwan){

        biwan.clear();
        if(oldbiwan!=null&&oldbiwan.size()>0) {

               bbb_one.setVisibility(View.VISIBLE);
            for(int i=0;i<oldbiwan.size();i++){
                if(i<=2){
                    biwan.add(oldbiwan.get(i));
                }
            }

            grid_detials_wan.setAdapter(mDetialsWanAdapter=new GridDetialsWanAdapter(this,biwan));
            mDetialsWanAdapter.notifyDataSetChanged();

            grid_detials_wan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    TouristAttractionsBean servicesEntity=(TouristAttractionsBean)mDetialsWanAdapter.getItem(position);
                    Intent it=new Intent(ShiBaJiaDetials2Activity.this,BiWanDetialsActivity.class);
                    it.putExtra("cityId", cityId);
                    it.putExtra("bwanId", servicesEntity.id);
                    it.putExtra("photo", servicesEntity.photo);

                    startActivity(it);
                }
            });


//                for(int i=0;i<biwan.size();i++){
//                    if(i<3){
//                        final TouristAttractionsBean servicesEntity=biwan.get(i);
//
//                        View itemView=getLayoutInflater().inflate(R.layout.item_travle_dujinsbi,null);
//
//                        ViewGroup parent = (ViewGroup) itemView.getParent();
//                        if (parent != null) {
//                            parent.removeAllViews();
//                        }
//                        itemView.setLayoutParams(new ViewGroup.LayoutParams(MyApplication.getWidth()/3, ViewGroup.LayoutParams.WRAP_CONTENT));
//                        ImageView img_logo=(ImageView) itemView.findViewById(R.id.img_logo);
//                        TextView tv_distancess=(TextView) itemView.findViewById(R.id.tv_distancess);
//                        TextView tv_myname=(TextView) itemView.findViewById(R.id.tv_myname);
//
//                        MyApplication.imageLoader.displayImage(biwan.get(i).photo, img_logo, MyApplication.getListOptions());
//                        if( !TextUtils.isEmpty(biwan.get(i).name)){
//
//                            tv_myname.setText(biwan.get(i).name);
//                        }
//                        if( !TextUtils.isEmpty(biwan.get(i).distance)){
//
//                            tv_distancess.setText(biwan.get(i).distance);
//                        }
//
//                        itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent it=new Intent(BiChiDetialsActivity.this,BiWanDetialsActivity.class);
//
//                                it.putExtra("cityId", cityId);
//                                it.putExtra("bwanId", servicesEntity.id);
//                                it.putExtra("photo", servicesEntity.photo);
//
//
//                                startActivity(it);
//                            }
//                        });
//                        bibi_one.addView(itemView);
//                    }
//                }
//            else {
//                bbb_one.setVisibility(View.GONE);
//            }
        }
    }

    private GridDetialsChiAdapter mDetialsChiAdapter;
    private GridDetialsWanAdapter mDetialsWanAdapter;
    private GridDetialsMaiAdapter mDetialsMaiAdapter;

    List<RestauransBean> bichi=new ArrayList<>();
    public void getBiChiData(List<RestauransBean> oldbichi){

        bichi.clear();
        if(oldbichi!=null&&oldbichi.size()>0) {

            bbb_three.setVisibility(View.VISIBLE);
            for(int i=0;i<oldbichi.size();i++){
                if(i<=2){
                    bichi.add(oldbichi.get(i));
                }
            }

            grid_detials_chi.setAdapter(mDetialsChiAdapter=new GridDetialsChiAdapter(this,bichi));
            mDetialsChiAdapter.notifyDataSetChanged();

            grid_detials_chi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    RestauransBean servicesEntity=(RestauransBean)mDetialsChiAdapter.getItem(position);
                    Intent it=new Intent(ShiBaJiaDetials2Activity.this,BiChiDetialsActivity.class);

                    it.putExtra("cityId", cityId);
                    it.putExtra("chiId", servicesEntity.id);
                    it.putExtra("chiphoto", servicesEntity.photo);

                    startActivity(it);
                }
            });
//
//
//                for(int i=0;i<bichi.size();i++){
//                    if(i<3){
//                        final RestauransBean servicesEntity=bichi.get(i);
//
//                        View itemView=getLayoutInflater().inflate(R.layout.item_travle_dujinsbi,null);
//
//                        ViewGroup parent = (ViewGroup) itemView.getParent();
//                        if (parent != null) {
//                            parent.removeAllViews();
//                        }
//                        itemView.setLayoutParams(new ViewGroup.LayoutParams(MyApplication.getWidth()/3, ViewGroup.LayoutParams.WRAP_CONTENT));
//                        ImageView img_logo=(ImageView) itemView.findViewById(R.id.img_logo);
//                        TextView tv_distancess=(TextView) itemView.findViewById(R.id.tv_distancess);
//                        TextView tv_myname=(TextView) itemView.findViewById(R.id.tv_myname);
//
//                        MyApplication.imageLoader.displayImage(biwan.get(i).photo, img_logo, MyApplication.getListOptions());
//                        if( !TextUtils.isEmpty(biwan.get(i).name)){
//
//                            tv_myname.setText(biwan.get(i).name);
//                        }
//                        if( !TextUtils.isEmpty(biwan.get(i).distance)){
//
//                            tv_distancess.setText(biwan.get(i).distance);
//                        }
//
//                        itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent it=new Intent(BiChiDetialsActivity.this,BiChiDetialsActivity.class);
//
//
//                                it.putExtra("cityId", cityId);
//                                it.putExtra("chiId", servicesEntity.id);
//                                it.putExtra("chiphoto", servicesEntity.photo);
//
//
//                                startActivity(it);
//                            }
//                        });
//                        bibi_three.addView(itemView);
//                    }
//                }
////            else {
////                bbb_one.setVisibility(View.GONE);
////            }
        }
    }
    List<ShopBean> bimai=new ArrayList<>();
    public void getBiMaiData(List<ShopBean> oldbimai){

        bimai.clear();
        if(oldbimai!=null&&oldbimai.size()>0) {

                bbb_two.setVisibility(View.VISIBLE);
            for(int i=0;i<oldbimai.size();i++){
                if(i<=2){
                    bimai.add(oldbimai.get(i));
                }
            }

            grid_detials_mai.setAdapter(mDetialsMaiAdapter=new GridDetialsMaiAdapter(this,bimai));
            mDetialsMaiAdapter.notifyDataSetChanged();

            grid_detials_mai.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    ShopBean servicesEntity=(ShopBean)mDetialsMaiAdapter.getItem(position);
                    Intent it=new Intent(ShiBaJiaDetials2Activity.this,BiMaiDetialsActivity.class);
                    it.putExtra("cityId", cityId);
                    it.putExtra("shopId", servicesEntity.id);
                    it.putExtra("photo", servicesEntity.photo);

                    startActivity(it);
                }
            });

//
//                for(int i=0;i<bimai.size();i++){
//                    if(i<3){
//                        final ShopBean servicesEntity=bimai.get(i);
//                        View itemView=getLayoutInflater().inflate(R.layout.item_travle_dujinsbi,null);
//                        ViewGroup parent = (ViewGroup) itemView.getParent();
//                        if (parent != null) {
//                            parent.removeAllViews();
//                        }
//
//                        itemView.setLayoutParams(new ViewGroup.LayoutParams(MyApplication.getWidth()/3, ViewGroup.LayoutParams.WRAP_CONTENT));
//                        ImageView img_logo=(ImageView) itemView.findViewById(R.id.img_logo);
//                        TextView tv_distancess=(TextView) itemView.findViewById(R.id.tv_distancess);
//                        TextView tv_myname=(TextView) itemView.findViewById(R.id.tv_myname);
//
//                        MyApplication.imageLoader.displayImage(bimai.get(i).photo, img_logo, MyApplication.getListOptions());
//                        if( !TextUtils.isEmpty(bimai.get(i).name)){
//
//                            tv_myname.setText(bimai.get(i).name);
//                        }
//                        if( !TextUtils.isEmpty(bimai.get(i).distance)){
//
//                            tv_distancess.setText(bimai.get(i).distance);
//                        }
//
//                        itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent it=new Intent(BiChiDetialsActivity.this,BiMaiDetialsActivity.class);
//                                it.putExtra("cityId", cityId);
//                                it.putExtra("shopId", servicesEntity.id);
//                                it.putExtra("photo", servicesEntity.photo);
//
//                                startActivity(it);
//                            }
//                        });
//                        bibi_two.addView(itemView);}
//                }
        }
    }
    public List<Map<String, Object>> getData(List<BannersBean> banners) {
        List<Map<String, Object>> mdata = new ArrayList<Map<String, Object>>();

        for(int i=0;i<banners.size();i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("url", banners.get(i).url);
            map.put("view", new ImageView(this));
            mdata.add(map);
        }
        return  mdata;
    }
    private void setOnClick(){
        img_share.setOnClickListener(this);
        tv_youji_note.setOnClickListener(this);
        tv_youji_notewe.setOnClickListener(this);
        relat_collect_in.setOnClickListener(this);
        liners_lookmore.setOnClickListener(this);
        liners_more_gonglie.setOnClickListener(this);
        left.setOnClickListener(this);
        relati_laiguo.setOnClickListener(this);
        imge_paly.setOnClickListener(this);
        img_back.setOnClickListener(this);
        re_wenluka.setOnClickListener(this);
        tv_chakan_all.setOnClickListener(this);
        img_dddown.setOnClickListener(this);
        img_dddown2.setOnClickListener(this);
        img_dddown3.setOnClickListener(this);
        tv_morebichi.setOnClickListener(this);
        tv_morebimai.setOnClickListener(this);
        tv_morebiwan.setOnClickListener(this);
        lin_kanall.setOnClickListener(this);
    }

    private String restaurantId;
    private String photo;
//    private int restaurantId=18;
    private String lng;
    private String lat;
    private String title;
    private String cn_name;
    private String en_name;
    private String location_name;

    private String photos_url;
    private String voice_url;


    List<CommentItem> nes_commnets;
    private Comment comment;
    private void getComment(String detailId){

        com.abcs.huaqiaobang.util.HttpRequest.sendGet(Contonst.HOST+"/getComments", "detailId=" + detailId + "&pageNo=1" +"&pageSize=10"+ "&type=" + type, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                mhandle.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zds","getcomment=="+msg);
                        try {

                            comment=  new Gson().fromJson(msg, Comment.class);
                            if(comment!=null){
                                if(comment.result==1){

                                    if(comment.body.items!=null&&comment.body.items.size()>0){   //否则无数据
                                        liner_commet.setVisibility(View.VISIBLE);

                                        ArrayList<CommentItem> comm=new ArrayList<CommentItem>();
                                        nes_commnets=new ArrayList<CommentItem>();
                                        for(int i=0;i<comment.body.items.size();i++){
                                            if(i<3){
                                                comm.add(comment.body.items.get(i));
                                            }
                                            nes_commnets.add(comment.body.items.get(i));
                                        }

                                        listview_comments.setAdapter(commentAdapter=new TravelDetialCommentsAdapter(ShiBaJiaDetials2Activity.this,comm));
                                        commentAdapter.notifyDataSetChanged();

                                        tv_pinlin_num.setText(comment.body.score.count+"条评论");
                                        tv_more_gonglie.setText("查看"+comment.body.score.count+"条真实攻略");
                                        tv_from_num.setText("来自"+comment.body.score.count+"位华人攻略");
                                        liners_more_gonglie.setVisibility(View.VISIBLE);
//                                        if(comment.body.score.count>3){
//                                        }else {
//                                            liners_more_gonglie.setVisibility(View.GONE);
//                                        }
                                    }
                                }else {
                                    showToast("解析出错！");
                                }
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            ProgressDlgUtil.stopProgressDlg();
                        }
                    }
                });

            }
        });

    }

    private   List<MyTickets> nes_wda;


    class UrlImgAdapter implements LBaseAdapter<String> {
        private Context mContext;

        public UrlImgAdapter(Context context) {
            mContext = context;
        }

        @Override
        public View getView(final LMBanners lBanners, final Context context, final int position, String data) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.banner_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.id_image);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageLoader.getInstance().displayImage(data, imageView, Options.getHDOptions());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ShiBaJiaDetials2Activity.this,SanBiDetialsPicActivity.class);
                    intent.putExtra("detialsId",detailId+"");
                    intent.putExtra("type",type+"");
                    ShiBaJiaDetials2Activity.this.startActivity(intent);
                }
            });
            return view;
        }

    }


    private void initBanners() {

//        for (int i = 0; i < goods_images.length; i++) {
//            bannerString.add(goods_images[i].replaceAll("_360", "_1280"));
//        }
        if (banners != null) {
            //设置Banners高度
            banners.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenUtils.dip2px(this, 300)));
            //本地用法
            banners.setAdapter(new UrlImgAdapter(this), bannerString);
            //网络图片
//        mLBanners.setAdapter(new UrlImgAdapter(MainActivity.this), networkImages);
            //参数设置
            banners.setAutoPlay(true);//自动播放
            banners.setVertical(false);//是否可以垂直
            banners.setScrollDurtion(500);//两页切换时间
            banners.setCanLoop(true);//循环播放
            banners.setSelectIndicatorRes(R.drawable.img_hwg_indicator_select);//选中的原点
            banners.setUnSelectUnIndicatorRes(R.drawable.img_hwg_indicator_unselect);//未选中的原点
//        mLBanners.setHoriZontalTransitionEffect(TransitionEffect.Default);//选中喜欢的样式
//        banners.setHoriZontalCustomTransformer(new ParallaxTransformer(R.id.id_image));//自定义样式
            banners.setHoriZontalTransitionEffect(TransitionEffect.Alpha);//Alpha
            banners.setDurtion(5000);//切换时间
            if (bannerString.size() == 1) {

                banners.hideIndicatorLayout();//隐藏原点
            } else {

                banners.showIndicatorLayout();//显示原点
            }
            banners.setIndicatorPosition(LMBanners.IndicaTorPosition.BOTTOM_MID);//设置原点显示位置
        }


    }

    private void loadDatas(String id,String cityId){
        ProgressDlgUtil.showProgressDlg("Loading...", this);
//        http://120.24.19.29:7075/sightdetail/getAdultDetail?id=5&cityId=55&uid=10666
        HttpRequest.sendGet(Contonst.HOST+"/sightdetail/getAdultDetail", "id=" + id + "&cityId="+ cityId+ "&uid="+ MyApplication.getInstance().getUid() , new HttpRevMsg(){
            @Override
            public void revMsg(final String msg) {
                mhandle.post(new Runnable() {
                    @Override
                    public void run() {

                        if (msg == null) {
                            ProgressDlgUtil.stopProgressDlg();
                            return;
                        }

                        final BiChiDetials response=new Gson().fromJson(msg,BiChiDetials.class);
//                        final AdultsDetials response=new Gson().fromJson(msg,AdultsDetials.class);
                        ProgressDlgUtil.stopProgressDlg();

                        if(swipeRefreshLayout!=null&&swipeRefreshLayout.isRefreshing()){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        if(response.result==1){


                            bg_view.setVisibility(View.GONE);
                            Log.e("zds",new Gson().toJson(response)+"");
                            detailId=response.body.detail.detail_id;
                            iid=response.body.id;
                            type=response.body.type+"";


                            if(response.body.noteCount>0){  // 提到的游记数量
                                noteCount=response.body.noteCount;
                                tv_youji_note.setText(response.body.noteCount+"");
                            }else{
                                tv_youji_note.setText("0");
                            }

                            if(response.body.ifCollection==0){//cc
                                tie.setImageResource(R.drawable.img_detials_collect);
                                tv_collect.setText("加入收藏");
                            }else if(response.body.ifCollection==1) {
                                tie.setImageResource(R.drawable.img_detials_collect_sele);
                                tv_collect.setText("已收藏");
                            }

                            getComment(detailId);

                            if(response.body.shops!=null&&response.body.shops.size()>0){
                                getBiMaiData(response.body.shops);
                                MyApplication.shopsBeen=response.body.shops;
                            }
                            if(response.body.touristAttractions!=null&&response.body.touristAttractions.size()>0){
                                getBiWanData(response.body.touristAttractions);
                                MyApplication.touristAttractionsBeen=response.body.touristAttractions;
                            }
                            if(response.body.restaurants!=null&&response.body.restaurants.size()>0){
                                getBiChiData(response.body.restaurants);
                                MyApplication.restaurantsBeen=response.body.restaurants;
                            }


                            if(!TextUtils.isEmpty(response.body.name)){
                                tv_title.setText(response.body.name);
                                title=response.body.name;
//                                if(response.body.name.length()>20){
//                                    tv_title.setText(response.body.name.substring(0,20)+"...");
////                                tv_name.setText(response.body.name.substring(0,10)+"...");
//                                }else {
//                                    tv_title.setText(response.body.name);
////                                tv_name.setText(response.body.name);
//                                }
                            }

//                            newbanners.clear();
//
//                            if(response.body.banners!=null&&response.body.banners.size()>0){
//                                newbanners.addAll(response.body.banners);
//                                photos_url=newbanners.get(0).url;
//                            }else {
//                                if(!TextUtils.isEmpty(photo)){
//                                    newbanners.add(new BannersBean(photo));
//                                    photos_url=photo;
//                                }
//                            }
                            newbanners.clear();
                            bannerString.clear();
                            if(response.body.banners!=null&&response.body.banners.size()>0){
                                newbanners.addAll(response.body.banners);
                                photos_url=newbanners.get(0).url;

                                for(int i=0;i<response.body.banners.size();i++){
                                    bannerString.add(response.body.banners.get(i).url);
                                }

                            }else {
                                if(!TextUtils.isEmpty(photo)){
                                    newbanners.add(new BannersBean(photo));
                                    photos_url=photo;
                                    bannerString.add(photo);
                                }
                            }

                            initBanners();


//                            data = getData(newbanners);
//                            adapter = new ViewPagerAdapter(data);
//                            viewPager.setAdapter(adapter);
//                            tv_piv_num.setText("1/"+newbanners.size());

                            if(response.body.detail!=null){

                                liner_detials.setVisibility(View.VISIBLE);

                                if(!TextUtils.isEmpty(response.body.detail.cnName)){
                                    cn_name=response.body.detail.cnName;
                                }else {
                                    cn_name=response.body.name;
                                }

                                if(!TextUtils.isEmpty(response.body.detail.voiceUrl)){
                                    voice_url=response.body.detail.voiceUrl;
                                }
                                if(!TextUtils.isEmpty(response.body.detail.enName)){
                                    en_name=response.body.detail.enName;
                                }
                                if(!TextUtils.isEmpty(response.body.detail.localName)){
                                    location_name=response.body.detail.localName;
                                }

                                if(!TextUtils.isEmpty(response.body.detail.introduction)){
                                    introduc=response.body.detail.introduction;
                                    if(introduc.length()>75){
                                        lin_kanall.setVisibility(View.VISIBLE);
                                        tv_introduction.setText(response.body.detail.introduction.substring(0,75)+"...");
                                    }else{
                                        lin_kanall.setVisibility(View.GONE);
                                        tv_introduction.setText(response.body.detail.introduction);
                                    }
                                }else {
                                    introduc="";
                                    liner_instrution.setVisibility(View.GONE);
                                }

                                if(!TextUtils.isEmpty(response.body.detail.address)){
                                    t_address=response.body.detail.address;
                                    liner_location.setVisibility(View.VISIBLE);
//                                    if(response.body.detail.address.length()>20){
//                                        img_dddown2.setVisibility(View.VISIBLE);
//                                        tv_address.setText(response.body.detail.address.substring(0,20)+"...");
//                                    }else{
//                                        img_dddown2.setVisibility(View.GONE);
//                                    }
                                    tv_address.setText(response.body.detail.address);


                                }else {
                                    t_address="";
                                    liner_location.setVisibility(View.GONE);
                                }

                                if(!TextUtils.isEmpty(response.body.detail.openTime)){
                                    liner_opentime.setVisibility(View.VISIBLE);

                                    t_openTime=response.body.detail.openTime;
//                                    if(response.body.detail.openTime.length()>20){
//                                        img_dddown.setVisibility(View.VISIBLE);
//                                        tv_opentime.setText(response.body.detail.openTime.substring(0,20)+"...");
//                                    }else{
//                                        img_dddown.setVisibility(View.GONE);
//                                    }
                                    tv_opentime.setText(response.body.detail.openTime);

                                }else {
                                    t_openTime="";
                                    liner_opentime.setVisibility(View.GONE);
                                }
                                if(!TextUtils.isEmpty(response.body.detail.phone)){
                                    liner_phone.setVisibility(View.VISIBLE);
                                    String div= CommentUtils.divPhoneifHas(response.body.detail.phone);
                                    if(!TextUtils.isEmpty(div)){
                                        tv_phone.setText(div);
                                    }else {
                                        tv_phone.setText(response.body.detail.phone);
                                    }
                                }else {
                                    liner_phone.setVisibility(View.GONE);
                                }
                                if(!TextUtils.isEmpty(response.body.detail.site)){
                                    liner_http.setVisibility(View.VISIBLE);
                                    tv_site.setText(response.body.detail.site);

                                }else {
                                    liner_http.setVisibility(View.GONE);
                                }
                                if(!TextUtils.isEmpty(response.body.detail.tips)){
                                    liner_tips.setVisibility(View.VISIBLE);
                                    tv_tips.setText(response.body.detail.tips);

                                }else {
                                    liner_tips.setVisibility(View.GONE);
                                }

                                tv_pinfen.setText(response.body.star+"");

                                if(!TextUtils.isEmpty(response.body.detail.lng)&&!TextUtils.isEmpty(response.body.detail.lat)){
                                    lng=response.body.detail.lng;
                                    lat=response.body.detail.lat;

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                locationByGet(Double.parseDouble(response.body.detail.lng),Double.parseDouble(response.body.detail.lat));
                                            }catch (IOException e){
                                                Log.i("locationByGet：",e.getMessage()+"");
                                            }
                                        }
                                    }).start();
                                }


                                if(response.body.tickets!=null&&response.body.tickets.size()>0){   //否则无数据
                                    liner_tejia.setVisibility(View.VISIBLE);
                                    Log.e("zds_预定大小|||||||||",response.body.tickets.size()+"");
                                    List<MyTickets> da=new ArrayList<MyTickets>();
                                    nes_wda=new ArrayList<MyTickets>();
                                    if(response.body.tickets.get(0)!=null){
                                        da.add(response.body.tickets.get(0));
                                    }

                                    listView.setAdapter(goodsAdapter=new TravelDetialGoodsAdapter(ShiBaJiaDetials2Activity.this,da));
                                    goodsAdapter.notifyDataSetChanged();

                                    for (int i=0;i<response.body.tickets.size();i++){

                                        if(i==0){
                                        }else {
                                            nes_wda.add(response.body.tickets.get(i));
                                        }
                                    }

                                    if(nes_wda!=null&&nes_wda.size()>0){
                                        liners_lookmore.setVisibility(View.VISIBLE);
                                        tv_lookmore.setText("查看全部"+response.body.tickets.size()+"个");
                                    }else {
                                        liners_lookmore.setVisibility(View.GONE);
                                    }

                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                            if(MyApplication.getInstance().getMykey()==null){
                                                startActivity(new Intent(ShiBaJiaDetials2Activity.this,WXEntryActivity.class));
                                            }else {

                                            MyTickets myTickets=goodsAdapter.getItem(position);
                                            Intent it=new Intent(ShiBaJiaDetials2Activity.this,GoodsDetailActivity.class);
                                            it.putExtra("sid",myTickets.product_id);
                                            it.putExtra("pic", myTickets.img);
                                            it.putExtra("isYun",false);
                                            startActivity(it);
                                        }}
                                    });
                                }
                                if(!response.body.detail.smyAudioId.equals("-1")){   //否则无语音
                                    smy_audio_id=response.body.detail.smyAudioId;
                                    imge_paly.setVisibility(View.VISIBLE);
                                }

                            }else {
                                liner_detials.setVisibility(View.GONE);
                            }
                            String str1 = (String.valueOf(response.body.star)).substring(0, (response.body.star+"").indexOf(".")) ;
                            if(!TextUtils.isEmpty(str1)){
                                int s1 = Integer.parseInt(str1);
                                double xiaoshu=response.body.star-s1;

                                for(int i=0;i<s1;i++){
                                    if(i<5){
                                        imgs.get(i).setImageResource(R.drawable.img_travel_startselrct);

                                        if((i+1)<5){
                                            if(xiaoshu<0.5){
                                                imgs.get(i+1).setImageResource(R.drawable.img_travel_startnoselect);
                                            }else {
                                                imgs.get(i+1).setImageResource(R.drawable.img_travel_startselrct_ban);
                                            }
                                        }
                                    }
                                }

                            }

                            if(response.body.tags!=null&&response.body.tags.size()>0){

                                StringBuilder builder= new StringBuilder();
                                for(int i=0;i<response.body.tags.size();i++){
                                    builder.append(response.body.tags.get(i)+"  ");
                                }
                                tv_tags.setText(builder);

//                        gridLayout.setAdapter(new DetialsTagAdapter(BiChiDetialsActivity.this,response.body.tags));
                            }
                        }else {
                            Toast.makeText(ShiBaJiaDetials2Activity.this,"无数据！",Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        });
    }

    private boolean iscoolect=true;
    private boolean isFresh=false;
    private int isCoolec=0;


    private boolean isAll=true;
    private  String introduc=null;
    private  String t_openTime=null;
    private  String t_address=null;
    private  String t_tips=null;
    private Bitmap bitmap;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bitmap!=null){
            bitmap.recycle();
        }
    }

    private String smy_audio_id;
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_youji_note:  // 提到的游记
            case R.id.tv_youji_notewe:  // 提到的游记
                if(noteCount>0){
                    Intent typr=   new Intent(this,YouJiFromSBDetailActivity.class);
                    typr.putExtra("detialsId",iid+"");
                    typr.putExtra("cityId",cityId);
                    typr.putExtra("type",type+"");
                    startActivity(typr);
                }
                break;
            case R.id.img_share: // 分享
//                showPopupView();
                break;
            case R.id.relati_laiguo: // 我来过
                isFresh=true;
                if(MyApplication.getInstance().getMykey()==null){
                    startActivity(new Intent(ShiBaJiaDetials2Activity.this,WXEntryActivity.class));
                }else {
                    Intent tyr=   new Intent(this,AddCommentActivity.class);
                    tyr.putExtra("detialsId",detailId+"");
                    tyr.putExtra("type",type+"");
                    tyr.putExtra("logo",photos_url+"");
                    tyr.putExtra("title",title+"");
                    startActivity(tyr);
                }

                break;
            case R.id.left: // 传图
                isFresh=true;
                if(MyApplication.getInstance().getMykey()==null){
                    startActivity(new Intent(ShiBaJiaDetials2Activity.this,WXEntryActivity.class));
                }else {
                    Intent ty=   new Intent(this,ChuanTuActivity2.class);
                    ty.putExtra("detialsId",detailId+"");
                    ty.putExtra("type",type+"");
                    ty.putExtra("logo",photos_url+"");
                    ty.putExtra("title",title+"");
                    startActivity(ty);
                }
                break;
            case R.id.relat_collect_in: // 收藏
                if(iscoolect){  //（1：收藏，0：取消收藏）
                    isCoolec=1;
                    iscoolect=false;
                }else {
                    isCoolec=0;
                    iscoolect=true;
                }

                if(MyApplication.getInstance().getMykey()==null){
                    startActivity(new Intent(this,WXEntryActivity.class));
                }else {
                    ifCollect(isCoolec);
                }

                break;
            case R.id. liners_lookmore:  //查看更多
                goodsAdapter.addDatas(nes_wda);
                nes_wda.clear();
                liners_lookmore.setVisibility(View.GONE);
                break;
            case R.id. liners_more_gonglie:  //查看攻略
                Intent tet=  new Intent(this,AllCommentActivity.class);
                tet.putExtra("detialsId",detailId+"");
                tet.putExtra("type",type+"");
                tet.putExtra("progerss",comment);
                startActivity(tet);

//                goodsAdapter.addDatas(nes_wda);
//                nes_wda.clear();
//                liners_lookmore.setVisibility(View.GONE);
                break;


            case R.id.imge_paly:  //  跳到播放
                Intent itt=new Intent(ShiBaJiaDetials2Activity.this,DaoHangDetialsActivity2.class);
                itt.putExtra("dahangid", smy_audio_id);
                startActivity(itt);
                break;
            case R.id.tv_morebichi: /******更多必玩********/
                Intent it=new Intent(ShiBaJiaDetials2Activity.this,MoreFuJinSBiActivity.class);
                 it.putExtra("cityId", cityId);
                 it.putExtra("type", Contonst.KEY_WAN);
                 startActivity(it);
                break;
            case R.id.tv_morebimai: /******更多必买********/
                Intent t=new Intent(ShiBaJiaDetials2Activity.this,MoreFuJinSBiActivity.class);
                 t.putExtra("cityId", cityId);
                 t.putExtra("type", Contonst.KEY_MAI);
                startActivity(t);

                break;
            case R.id.tv_morebiwan: /******更多必吃********/
                Intent tt=new Intent(ShiBaJiaDetials2Activity.this,MoreFuJinSBiActivity.class);
                tt.putExtra("cityId", cityId);
                tt.putExtra("type", Contonst.KEY_CHI);
                startActivity(tt);

                break;
            case R.id.re_wenluka:  //问路卡
                Intent intent=  new Intent(this,WenLuKaActivity.class);
                intent.putExtra("cn_name",cn_name);
                intent.putExtra("en_name",en_name);
                intent.putExtra("location_name",location_name);
                intent.putExtra("photos_url",photos_url);
                intent.putExtra("voice_url",voice_url);
                startActivity(intent);
                break;
            case R.id.bmapView:  //查看地图

                Intent i=new Intent(this,BaiDuMapActivity.class);
                i.putExtra("lng",lng);
                i.putExtra("lat",lat);
                i.putExtra("title",title);
                startActivity(i);
                break;
            case R.id.lin_kanall:
            case R.id.tv_chakan_all:
                if(isAll){
                    tv_introduction.setText(introduc+"");
                    lin_kanall.setVisibility(View.GONE);
//                    tv_chakan_all_bg.setBackgroundResource(R.drawable.ima_zhuli_down);
//                    tv_chakan_all.setText("收起");
//                    isAll=false;
                }
//                else {
//
//                    if(!TextUtils.isEmpty(introduc)&&introduc.length()>75){
//                        tv_introduction.setText(introduc.substring(0,75)+"...");
//                    }
//                    tv_chakan_all_bg.setBackgroundResource(R.drawable.ima_zhuli_down);
//                    tv_chakan_all.setText("查看全部");
//                    isAll=true;
//                }
                break;
            case R.id.img_dddown:
                tv_opentime.setText(t_openTime+"");
                break;
            case R.id.img_dddown2:
                tv_address.setText(t_address+"");
                break;
            case R.id.img_dddown3:
                tv_tips.setText(t_tips+"");
                break;
        }
    }

    String detailId,iid;
    String type;
    int noteCount=0;

    private void ifCollect(final int isCoolec){

        ProgressDlgUtil.showProgressDlg("Loading...", this);

//        http://120.24.19.29:7075/ifCollection?detailId=3575&type=2&/ifCollection=0&uid=13074
        com.abcs.huaqiaobang.util.HttpRequest.sendGet(Contonst.HOST+"/ifCollection", "detailId=" + detailId + "&ifCollection="+isCoolec + "" +"&uid="+MyApplication.getInstance().getUid()+""+ "&type=" + type, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {

                mhandle.post(new Runnable() {
                    @Override
                    public void run() {
                        ProgressDlgUtil.stopProgressDlg();
                        try {
                            if(msg==null){
                                return;
                            }else {
                                JSONObject jsonObject=new JSONObject(msg);
                                if(jsonObject!=null){
                                    if(jsonObject.optInt("result")==1){
                                        if(isCoolec==0){
                                            tie.setImageResource(R.drawable.img_detials_collect);
                                            tv_collect.setText("加入收藏");
                                        }else if(isCoolec==1) {
                                            tie.setImageResource(R.drawable.img_detials_collect_sele);
                                            tv_collect.setText("已收藏");
                                        }
                                    }
                                }
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private TravelDetialCommentsAdapter commentAdapter;
    public class ViewPagerAdapter extends PagerAdapter {

        List<Map<String,Object>> viewLists;

        public ViewPagerAdapter(List<Map<String,Object>> lists)
        {
            viewLists = lists;
        }

        @Override
        public int getCount() {    //获得size
            // TODO Auto-generated method stub
            return viewLists.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View view, int position, Object object)      //销毁Item
        {
            ImageView x=(ImageView)viewLists.get(position).get("view");
            x.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ((ViewPager) view).removeView(x);
        }

        @Override
        public Object instantiateItem(View view, int position)  //实例化Item
        {
            ImageView x=(ImageView)viewLists.get(position).get("view");
            x.setScaleType(ImageView.ScaleType.FIT_CENTER);
            MyApplication.imageLoader.displayImage(viewLists.get(position).get("url").toString(), x, MyApplication.getListOptions());
            ((ViewPager) view).addView(x, 0);

            return viewLists.get(position).get("view");
        }

    }

    /**
     * 通过GET方式发送的请求
     * @param lng
     * @param lat
     * 坐标格式：lng<经度>，lat<纬度>，例如116.43213,38.76623。
     */
    public void locationByGet(double lng, double lat) throws IOException{

        InputStream is=null;
        try {
            // 设置请求的地址 通过URLEncoder.encode(String sd, String enc)
            // 使用指定的编码机制将字符串转换为 application/x-www-form-urlencoded 格式
            // 根据地址创建URL对象(网络访问的url)
            String spec="http://api.map.baidu.com/staticimage/v2?ak="+ Config.BAIDU_AK+"&mcode="+Config.BAIDU_MCODE+"&center="+lng+","+lat+"&width=570&height=200&zoom=9&markers="+lng+","+lat+"";
            URL url = new URL(spec);
            // url.openConnection()打开网络链接
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("GET");// 设置请求的方式
            urlConnection.setReadTimeout(5000);// 设置超时的时间
            urlConnection.setConnectTimeout(5000);// 设置链接超时的时间
            urlConnection.setDoInput(true);
            urlConnection.connect();
//            // 设置请求的头
//            urlConnection
//                    .setRequestProperty("User-Agent",
//                            "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
            // 获取响应的状态码 404 200 505 302
            if (urlConnection.getResponseCode() == 200) {
                // 获取响应的输入流对象
                is = urlConnection.getInputStream();

                final Bitmap bitmap = BitmapFactory.decodeStream(is);
                ShiBaJiaDetials2Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        mMapView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        mMapView.setImageBitmap(bitmap);
                    }
                });

                // 释放资源
                is.close();
//                // 创建字节输出流对象
//                ByteArrayOutputStream os = new ByteArrayOutputStream();
//                // 定义读取的长度
//                int len = 0;
//                // 定义缓冲区
//                byte buffer[] = new byte[1024];
//                // 按照缓冲区的大小，循环读取
//                while ((len = is.read(buffer)) != -1) {
//                    // 根据读取的长度写入到os对象中
//                    os.write(buffer, 0, len);
//                }
//                os.close();
                // 返回字符串
//                String result = new String(os.toByteArray());
//                System.out.println("***************" + result
//                        + "******************");
            } else {
                System.out.println("------------------链接失败-----------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
private TravelDetialGoodsAdapter goodsAdapter;
}
