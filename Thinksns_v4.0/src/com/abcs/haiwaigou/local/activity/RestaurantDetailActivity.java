package com.abcs.haiwaigou.local.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.PhotoPreviewActivity;
import com.abcs.haiwaigou.view.zjzbanner.LMBanners;
import com.abcs.haiwaigou.view.zjzbanner.adapter.LBaseAdapter;
import com.abcs.haiwaigou.view.zjzbanner.transformer.TransitionEffect;
import com.abcs.haiwaigou.view.zjzbanner.utils.ScreenUtils;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RestaurantDetailActivity extends BaseActivity {

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.t_title_name)
    TextView tTitleName;
    @InjectView(R.id.relative_share)
    RelativeLayout relativeShare;
    @InjectView(R.id.t_title)
    TextView tTitle;
    @InjectView(R.id.t_publish_time)
    TextView tPublishTime;
    @InjectView(R.id.t_subway)
    TextView tSubway;
    @InjectView(R.id.t_address)
    TextView tAddress;
    @InjectView(R.id.t_company)
    TextView tCompany;
    @InjectView(R.id.t_job)
    TextView tJob;
    @InjectView(R.id.t_salary)
    TextView tSalary;
    @InjectView(R.id.t_work_time)
    TextView tWorkTime;
    @InjectView(R.id.t_relax_time)
    TextView tRelaxTime;
    @InjectView(R.id.t_content)
    TextView tContent;
    @InjectView(R.id.t_connectMan)
    TextView tConnectMan;
    @InjectView(R.id.t_phone)
    TextView tPhone;
    @InjectView(R.id.t_email)
    TextView tEmail;
    @InjectView(R.id.relative_subway)
    RelativeLayout relativeSubway;
    @InjectView(R.id.relative_address)
    RelativeLayout relativeAddress;
    @InjectView(R.id.relative_company)
    RelativeLayout relativeCompany;
    @InjectView(R.id.relative_job)
    RelativeLayout relativeJob;
    @InjectView(R.id.relative_salary)
    RelativeLayout relativeSalary;
    @InjectView(R.id.relative_work_time)
    RelativeLayout relativeWorkTime;
    @InjectView(R.id.relative_relax_time)
    RelativeLayout relativeRelaxTime;
    @InjectView(R.id.relative_connectMan)
    RelativeLayout relativeConnectMan;
    @InjectView(R.id.relative_phone)
    RelativeLayout relativePhone;
    @InjectView(R.id.relative_email)
    RelativeLayout relativeEmail;
    @InjectView(R.id.linear_top)
    LinearLayout linearTop;
    @InjectView(R.id.linear_bottom)
    LinearLayout linearBottom;


    @InjectView(R.id.banners)
    LMBanners banners;
    @InjectView(R.id.linear_pic)
    RelativeLayout linearPic;
    private Handler handler = new Handler();
    String conId;
    String[] goods_images;
    private ArrayList<String> bannerString = new ArrayList<>();
    private ArrayList<String> conIdList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_activity_restaurant_detail);
        ButterKnife.inject(this);
        conId = getIntent().getStringExtra("conId");
        Log.i("zjz", "conId=" + conId);
        initView();
        tTitleName.setText("详情");
        relativeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Log.i("zjz", "conIdList.size=" + conIdList.size());
    }


    private void initView() {
        HttpRequest.sendGet(TLUrl.getInstance().URL_local_get_detail, "conId=" + conId, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject mainObj = new JSONObject(msg);
                            Log.i("zjz", "local_Detail_msg=" + msg);
                            if (mainObj.optString("status").equals("1")) {
                                JSONObject msgObj = mainObj.optJSONObject("msg");
                                goods_images = msgObj.optString("imgUrls").split(",");
                                if (tContent != null)
                                    tContent.setText(msgObj.optString("content"));
                                if (tPublishTime != null) {
                                    if (msgObj.optLong("pubTime") < 2 * 1000000000) {
                                        tPublishTime.setText(Util.format11.format(msgObj.optLong("pubTime") * 1000));
                                    } else {
                                        tPublishTime.setText(Util.format11.format(msgObj.optLong("pubTime")));
                                    }
                                }
                                if (tConnectMan != null)
                                    tConnectMan.setText(msgObj.optString("contactMan"));
                                if (tAddress != null)
                                    tAddress.setText(msgObj.optString("countryName") + msgObj.optString("cityName") + msgObj.optString("ads"));
                                if (tPhone != null)
                                    tPhone.setText(msgObj.optString("contact"));
                                if (tTitle != null)
                                    tTitle.setText(msgObj.optString("title"));
                                if (linearTop != null)
                                    linearTop.setVisibility(View.VISIBLE);
                                if (linearBottom != null)
                                    linearBottom.setVisibility(View.VISIBLE);

                                if (goods_images != null) {
                                    if(TextUtils.isEmpty(msgObj.optString("imgUrls"))||msgObj.optString("imgUrls").equals("null")){
                                        linearPic.setVisibility(View.GONE);
                                    }else {
                                        linearPic.setVisibility(View.VISIBLE);
                                        initBanners();
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    private void initBanners() {
        bannerString.clear();
        for (int i = 0; i < goods_images.length; i++) {
            bannerString.add(Util.myTrim(goods_images[i]));
        }
        if (banners != null) {
            //设置Banners高度
            banners.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ScreenUtils.dip2px(this, 190)));
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
                    Intent intent = new Intent();
                    intent.putExtra("image", goods_images[position]);
                    intent.putExtra("ulist", bannerString);
                    intent.setClass(RestaurantDetailActivity.this, PhotoPreviewActivity.class);
                    startActivity(intent);
                }
            });
            return view;
        }

    }

}
