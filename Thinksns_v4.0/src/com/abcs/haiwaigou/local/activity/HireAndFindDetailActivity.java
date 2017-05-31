package com.abcs.haiwaigou.local.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.PhotoPreviewActivity;
import com.abcs.haiwaigou.local.adapter.GridVImgsAdapter;
import com.abcs.haiwaigou.local.beans.HireFind;
import com.abcs.haiwaigou.local.beans.Msg;
import com.abcs.haiwaigou.local.fragment.HireJobFragment;
import com.abcs.haiwaigou.model.GGAds;
import com.abcs.haiwaigou.utils.ACache;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.haiwaigou.view.zjzbanner.LMBanners;
import com.abcs.haiwaigou.view.zjzbanner.adapter.LBaseAdapter;
import com.abcs.haiwaigou.view.zjzbanner.transformer.TransitionEffect;
import com.abcs.haiwaigou.view.zjzbanner.utils.ScreenUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.GuanggaoActivity;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.huaqiaobang.wxapi.official.share.ShareQQPlatform;
import com.abcs.huaqiaobang.wxapi.official.share.ShareWeiXinPlatform;
import com.abcs.huaqiaobang.wxapi.official.share.ShareWeiboPlatform;
import com.abcs.huaqiaobang.wxapi.official.share.util.ShareContent;
import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.android.weiba.ActivityWeibaCommon;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class HireAndFindDetailActivity extends BaseActivity implements View.OnClickListener, IWeiboHandler.Response  {

    public final String Tag = "HireAndFindDetailActivity";

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.t_title_name)
    TextView tTitleName;
    @InjectView(R.id.relative_share)
    RelativeLayout relative_share;
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

    String conId;
    @InjectView(R.id.banners)
    LMBanners banners;
    @InjectView(R.id.linear_pic)
    RelativeLayout linearPic;
    @InjectView(R.id.linear_content)
    LinearLayout linearContent;
    @InjectView(R.id.t_last)
    TextView tLast;
    @InjectView(R.id.t_next)
    TextView tNext;
    @InjectView(R.id.l_bottom)
    LinearLayout lBottom;
//    @InjectView(R.id.iv_gg_new1)
//    ImageView ivGgNew1;
//    @InjectView(R.id.iv_gg_new2)
//    ImageView ivGgNew2;
//    @InjectView(R.id.iv_gg_new3)
//    ImageView ivGgNew3;
//    @InjectView(R.id.iv_gg_new4)
//    ImageView ivGgNew4;

    @InjectView(R.id.gridView)
    com.abcs.haiwaigou.view.MyGridView gridView;
    @InjectView(R.id.swipeRefresh)
    android.support.v4.widget.SwipeRefreshLayout swipeRefresh;


    private Handler handler = new Handler();
    String[] goods_images;
    private ArrayList<String> bannerString = new ArrayList<>();
    public  ArrayList<HireFind> hireFinds = new ArrayList<HireFind>();
    public  ArrayList<Msg> msgs = new ArrayList<Msg>();
    public static boolean isMore;
    int curPosition;
    boolean isMsg;
    private ACache aCache;
    private String curTypeName;
    public ShareWeiXinPlatform shareWeiXinPlatform;

    private boolean isform_fen;

//    ArrayList<ImageView> adsList=new ArrayList<>();
    ArrayList<GGAds> adsListGG=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_activity_hire_and_find_detail);
        ButterKnife.inject(this);
        aCache = ACache.get(this);
        conId = getIntent().getStringExtra("conId");
        isform_fen = getIntent().getBooleanExtra("isform_fen",false);
        Log.i("zjz", "conId=" + conId);
        Log.i("zjz", "isform_fen=" + isform_fen);
        if(isform_fen){
            lBottom.setVisibility(View.GONE);
        }else {
            lBottom.setVisibility(View.VISIBLE);
        }

        shareWeiXinPlatform = new ShareWeiXinPlatform(this);
        ShareQQPlatform.getInstance().registerShare(this);
        ShareWeiboPlatform.getInstanse().regiesetShare(this, savedInstanceState, this);

//        adsList.clear();
//        adsList.add(ivGgNew1);
//        adsList.add(ivGgNew2);
//        adsList.add(ivGgNew3);
//        adsList.add(ivGgNew4);

        initView(conId);

        tTitleName.setText("详情");
        relativeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        isMsg = getIntent().getBooleanExtra("isMsg", false);
        if (!isMsg) {
            if(getIntent().hasExtra("datas")){
                hireFinds = getIntent().getParcelableArrayListExtra("datas");
                Log.i("zjz", "hireFinds.size=" + hireFinds.size());
                curTypeName = getIntent().getStringExtra("typeName");
                Log.i("zjz", "curTypeName=" + curTypeName);
                for (int i = 0; i < hireFinds.size(); i++) {
                    if (conId.equals(hireFinds.get(i).getId())) {
                        curPosition = i;
                    }
                }
            }
        } else {
            if(getIntent().hasExtra("datas")){
                msgs = getIntent().getParcelableArrayListExtra("datas");
                Log.i("zjz", "msgs.size=" + msgs.size());
                for (int i = 0; i < msgs.size(); i++) {
                    if (conId.equals(msgs.get(i).getId())) {
                        curPosition = i;
                    }
                }
            }
        }

        tLast.setOnClickListener(this);
        relative_share.setOnClickListener(this);
        tNext.setOnClickListener(this);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(conId.length()>0){
                            initView(conId);
                        }

                        swipeRefresh.setRefreshing(false);
                    }
                },1000);
            }
        });
    }


    private void initView(final String conId) {
  /*      JSONObject mainObj = aCache.getAsJSONObject(TLUrl.getInstance().LOCAL_MSG_DETAIL + conId);
        if (mainObj != null) {
            initJsonObject(mainObj);
        } else {*/
            ProgressDlgUtil.showProgressDlg("Loading...", this);
            HttpRequest.sendGet(TLUrl.getInstance().URL_local_get_detail, "conId=" + conId, new HttpRevMsg() {
                @Override
                public void revMsg(final String msg) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject mainObj = new JSONObject(msg);
                               /* aCache.remove(TLUrl.getInstance().LOCAL_MSG_DETAIL + conId);
                                if (aCache.getAsJSONObject(TLUrl.getInstance().LOCAL_MSG_DETAIL + conId) == null) {
                                    aCache.put(TLUrl.getInstance().LOCAL_MSG_DETAIL + conId, mainObj, 7 * 24 * 60 * 60);
                                }*/
                                Log.i("zjz", "local_Detail_msg=" + msg);
                                initJsonObject(mainObj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } finally {
                                ProgressDlgUtil.stopProgressDlg();
                            }
                        }
                    });
                }
            });
//        }

    }

    private void initJsonObject(JSONObject mainObj){
        if (mainObj.optString("status").equals("1")) {
            JSONObject msgObj = mainObj.optJSONObject("msg");

            /*广告*/
            try {
                if(msgObj.has("guanggao")){
                    JSONArray guanggao=msgObj.optJSONArray("guanggao");
                    if(guanggao!=null){
                        if(guanggao.length()>0){
                            adsListGG.clear();
                            for (int i = 0; i < guanggao.length(); i++) {
                                 JSONObject jgg_object=guanggao.getJSONObject(i);
                                adsListGG.add(new GGAds(jgg_object.optString("img"),jgg_object.optString("url")));
                            }

                            gridView.setAdapter(new GridVImgsAdapter(this,adsListGG));
                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    GGAds gg_bean=(GGAds) parent.getItemAtPosition(position);
                                    if(gg_bean!=null){
                                        if(!TextUtils.isEmpty(gg_bean.url)){
                                            if(gg_bean.is_jump==1){  // 跳微吧
                                                Intent intent;
                                                if (TextUtils.isEmpty(MyApplication.getInstance().getMykey())) {
                                                    intent = new Intent(HireAndFindDetailActivity.this, WXEntryActivity.class);
                                                    intent.putExtra("isthome",true);
                                                } else {
                                                    intent = new Intent(HireAndFindDetailActivity.this, ActivityWeibaCommon.class);
                                                    intent.putExtra("name", "全部微吧");
                                                    intent.putExtra("type", ActivityWeibaCommon.FRAGMENT_WEIBA_ALL);
                                                }
                                                startActivity(intent);
                                            }else {
                                                Intent intent = new Intent(HireAndFindDetailActivity.this, GuanggaoActivity.class);
                                                intent.putExtra("url_local", gg_bean.url);
                                                startActivity(intent);
                                            }
                                        }
                                    }
                                }
                            });

                    /*        for (int i = 0; i < guanggao.length(); i++) {
                                if (i < 4) {
                                    final JSONObject jgg_object=guanggao.getJSONObject(i);
                                    adsList.get(i).setVisibility(View.VISIBLE);
                                    if(!TextUtils.isEmpty(jgg_object.optString("img"))){
                                        MyApplication.imageLoader.displayImage(jgg_object.optString("img"), adsList.get(i), MyApplication.getListOptions());
                                    }

                                    adsList.get(i).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(HireAndFindDetailActivity.this, GuanggaoActivity.class);
                                            intent.putExtra("url_local", jgg_object.optString("url"));
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }*/
                        }else {
                          /*  for (int i = 0; i < adsList.size(); i++) {
                                adsList.get(i).setVisibility(View.GONE);
                            }*/
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            /*发布时间*/

            if (tPublishTime != null) {
                tPublishTime.setVisibility(View.VISIBLE);
                if (msgObj.optLong("pubTime") < 2 * 1000000000) {
                    tPublishTime.setText(Util.format11.format(msgObj.optLong("pubTime") * 1000));
                } else {
                    tPublishTime.setText(Util.format11.format(msgObj.optLong("pubTime")));
                }
            }
            /*电话*/
            if (tPhone != null)
                tPhone.setText(msgObj.optString("contact"));
            if (tTitle != null)
                tTitle.setText(msgObj.optString("title"));

            /*地址*/
            if(!TextUtils.isEmpty(msgObj.optString("ads"))){
                if (tAddress != null)
                    tAddress.setText(msgObj.optString("ads") );
            }else {
                if (tAddress != null)
                    tAddress.setText("暂无");
            }

           /* if(!msgObj.optString("ads").equals("暂无")){
//                    tAddress.setText(msgObj.optString("countryName") + msgObj.optString("cityName") +msgObj.optString("ads") );
            }else {
                tAddress.setText(msgObj.optString("countryName") + msgObj.optString("cityName"));
            }*/

            if (linearTop != null)
                linearTop.setVisibility(View.VISIBLE);
            if (linearBottom != null)
                linearBottom.setVisibility(View.VISIBLE);

            /*详细内容*/
            if(!TextUtils.isEmpty(msgObj.optString("content"))){
                if (tContent != null)
                    tContent.setText(msgObj.optString("content"));
                if (linearContent != null)
                    linearContent.setVisibility(View.VISIBLE);
            }else {
                if (linearContent != null)
                    linearContent.setVisibility(View.GONE);
            }

            /*联系人*/
            if(!TextUtils.isEmpty(msgObj.optString("contactMan"))){
                if (relativeConnectMan != null)
                    relativeConnectMan.setVisibility(View.VISIBLE);
                if (tConnectMan != null)
                    tConnectMan.setText(msgObj.optString("contactMan"));
            }else {
                if (relativeConnectMan != null)
                    relativeConnectMan.setVisibility(View.GONE);
            }

            /*bangner轮播图*/
            goods_images = msgObj.optString("imgUrls").split(",");
            if (goods_images != null) {
                if (TextUtils.isEmpty(msgObj.optString("imgUrls")) || msgObj.optString("imgUrls").equals("null")) {
                    linearPic.setVisibility(View.GONE);
                } else {
                    linearPic.setVisibility(View.VISIBLE);
                    initBanners();
                }
            }
        }
    }


    private void initBanners() {
        bannerString.clear();
        for (int i = 0; i < goods_images.length; i++) {
            bannerString.add(Util.myTrim(goods_images[i]));
            Log.i("zjz", "goods_img=" + goods_images[i]);
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

    private PopupWindow popupWindow;

    @SuppressLint("InflateParams")
    @SuppressWarnings("deprecation")
    private void showPopupView() {
        if (popupWindow == null) {
            // 一个自定义的布局，作为显示的内容
            RelativeLayout contentView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.tljr_dialog_share_news2, null);

            ((Button) contentView.findViewById(R.id.btn_cancle)).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (popupWindow != null)
                        popupWindow.dismiss();
                }
            });

            LinearLayout ly1 = (LinearLayout) contentView.findViewById(R.id.ly1);

            for (int i = 0; i < ly1.getChildCount(); i++) {
                final int m = i;
                ly1.getChildAt(i).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        shareNewsUrl(m);
                        popupWindow.dismiss();
                    }
                });
            }
            popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
//			popupWindow.getContentView().measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setAnimationStyle(R.style.AnimationPreview);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    setAlpha(1f);
                }
            });
        }

        setAlpha(0.8f);
        int[] location = new int[2];
//        View v = findViewById(R.id.tljr_news_bottom_f);
        relative_share.getLocationOnScreen(location);
        popupWindow.showAtLocation(relative_share, Gravity.NO_GRAVITY, location[0], location[1]);
    }

    private void setAlpha(float f) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = f;
        lp.dimAmount = f;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    // type 0:QQ 1 微信 2新浪微博 3 朋友圈
    private void shareNewsUrl(int type) {
        switch (type) {
            case 0:
                ShareQQPlatform.getInstance().share(this, "", tTitle.getText().toString().trim(), "", "华人邦", ShareContent.appName);
//                ShareQQPlatform.getInstance().share(this, news.getSurl(), tTitle.getText().toString().trim(), news.getpUrl(), "华人邦", ShareContent.appName);
                break;
            case 1:
                shareWeiXinPlatform.setUrl("");
//                shareWeiXinPlatform.setUrl(news.getSurl());
                shareWeiXinPlatform.setTitle(tTitle.getText().toString().trim().length() > 22 ?tTitle.getText().toString().trim().substring(0, 22) + "..." : tTitle.getText().toString().trim());

                String ct = Util.getTextFromHtml(tContent.getText().toString().trim());

                shareWeiXinPlatform.setContent(ct.length() > 26 ? ct.substring(0,
                        26) + "..." : ct);
                shareWeiXinPlatform.wechatShare(0);

                break;
            case 2:
                ShareWeiboPlatform.getInstanse().share(this,"", tTitle.getText().toString().trim(), tTitle.getText().toString().trim());
//                ShareWeiboPlatform.getInstanse().share(this,news.getSurl(), tTitle.getText().toString().trim(), tTitle.getText().toString().trim());
                break;
            case 3:
                shareWeiXinPlatform.setUrl("");
//                shareWeiXinPlatform.setUrl(news.getSurl());
                shareWeiXinPlatform.setTitle(tTitle.getText().toString().trim());
                shareWeiXinPlatform.wechatShare(1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_share:  // 分享
                showPopupView();
                break;
            case R.id.t_last:
                if (curPosition == 0) {
                    showToast("已经是第一条信息");
                    return;
                }
                hideView();
                curPosition -= 1;
                if (!isMsg) {
                    initView(hireFinds.get(curPosition).getId());
                } else {
                    initView(msgs.get(curPosition).getId());
                }
                break;
            case R.id.t_next:
                if (!isMsg) {
                    if (curPosition == hireFinds.size() - 1) {
                        showToast("已经是最后一条信息");
                        return;
                    }
                    if (isMore && curPosition == hireFinds.size() - 2) {
                        showToast("正在加载更多信息...");
                        Log.i("zjz", "fragmentHashMap.size=" + HireAndFindActivity.fragmentHashMap.size());
                        if (HireAndFindActivity.fragmentHashMap.get(curTypeName) instanceof HireJobFragment) {
                            final HireJobFragment hireJobFragment = (HireJobFragment) HireAndFindActivity.fragmentHashMap.get(curTypeName);
                            hireJobFragment.initMoreDates();
                            hireJobFragment.initInterfacer(new HireJobFragment.InitMoreInterfacer() {
                                @Override
                                public void notifyDataSetChanged() {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            hireFinds = hireJobFragment.hireAndFindModel.getHireFindList();
                                            Log.i("zjz", "more_size=" + hireFinds.size());
                                            showToast("加载完成");
                                        }
                                    });

                                }
                            });
                        }
                    }
                    hideView();
                    curPosition += 1;
                    initView(hireFinds.get(curPosition).getId());
                } else {
                    if (curPosition == msgs.size() - 1) {
                        showToast("已经是最后一条信息");
                        return;
                    }
                    if (isMore && curPosition == msgs.size() - 2) {
                        showToast("正在加载更多信息...");
                        final MessageMoreActivity messageMoreActivity = MessageMoreActivity.msgMoreList.get(MyString.MSGMORE);
                        messageMoreActivity.initMoreDates();
                        messageMoreActivity.initInterfacer(new MessageMoreActivity.InitMoreInterfacer() {
                            @Override
                            public void notifyDataSetChanged() {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        msgs = messageMoreActivity.hireAndFindModel.getMsgList();
                                        Log.i("zjz", "more_size=" + messageMoreActivity.hireAndFindModel.getMsgList().size());
                                        showToast("加载完成");
                                    }
                                });

                            }
                        });
                    }

                    hideView();
                    curPosition += 1;
                    initView(msgs.get(curPosition).getId());
                }

                break;
        }
    }

    private void hideView() {
        linearPic.setVisibility(View.INVISIBLE);
        linearTop.setVisibility(View.INVISIBLE);
        linearBottom.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        switch (baseResponse.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                showToast("分享成功");
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                showToast("取消分享");
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                showToast("分享失败，Error Message: " + baseResponse.errMsg);
                break;
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
                    intent.setClass(HireAndFindDetailActivity.this, PhotoPreviewActivity.class);
                    startActivity(intent);
                }
            });
            return view;
        }

    }

}
