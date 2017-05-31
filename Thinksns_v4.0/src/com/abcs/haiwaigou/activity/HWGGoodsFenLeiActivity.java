package com.abcs.haiwaigou.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.fragment.MainFragment3;
import com.abcs.haiwaigou.fragment.adapter.CFViewPagerAdapter;
import com.abcs.haiwaigou.utils.ACache;
import com.abcs.haiwaigou.utils.SecondUtils;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.ytbt.common.utils.LogUtil;
import com.abcs.sociax.android.R;
import com.astuetz.PagerSlidingTabStrip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HWGGoodsFenLeiActivity extends BaseFragmentActivity implements View.OnClickListener {


    Fragment currentFragment;

    @InjectView(R.id.fenlei_tabs)
    PagerSlidingTabStrip mainTabs;
    @InjectView(R.id.relative_back)
    RelativeLayout relative_back;
    @InjectView(R.id.fenlei_pager)
    ViewPager mainPager;
    @InjectView(R.id.t_refresh)
    TextView tRefresh;
    @InjectView(R.id.t_title)
    TextView t_title;
    private View view;
    private Activity activity;
    CFViewPagerAdapter viewPagerAdapter;
    private Handler handler = new Handler();
    private ArrayList<String> nameList = new ArrayList<>();
    private ArrayList<String> idList = new ArrayList<>();
    private ArrayList<String> imgs = new ArrayList<>();
    private ArrayList<String> desc = new ArrayList<>();
    private String[] strs;
    private ACache aCache;
    public static TextView car_num;
    private boolean isSpecialDialogFirst = true;
    private boolean isReLoad = false;
    private boolean loadSuccess = false;
    private String specidId;



    private String keyword;
    private String picture;
    private String words = "";
    boolean isMain;
    boolean isSale;
    boolean isWeek;
    int text_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.hwg_goods_fenlei_fragment, null);
        }
        setContentView(view);

        ButterKnife.inject(this,view);

        activity = this;
        aCache = ACache.get(activity);

        words = getIntent().getStringExtra("words");
        picture = getIntent().getStringExtra("picture");
        text_position = getIntent().getIntExtra("text_position", 0);
//        isMain = getIntent().getBooleanExtra("isMain", false);
//        isSale = getIntent().getBooleanExtra("isSale", false);
        isWeek = getIntent().getBooleanExtra("isWeek", false);


        specidId=getIntent().getStringExtra("special_id");

        Log.e("zds_specidId======",specidId+"");

        if(getIntent().getStringExtra("title").equals("null")||getIntent().getStringExtra("title").equals("")){
            t_title.setText("海外购");
        }else {
            t_title.setText(getIntent().getStringExtra("title"));
        }

        initListener();
        initMyView(specidId);
//        initViewPager();
        //测试弹出对话框
        initSpecialNoticeDialog();


    }

    /**
     * 弹出一个特别的对话框
     * 显示的内容是可能是由于运货延期
     */
    private void initSpecialNoticeDialog() {
        //
        if (!isSpecialDialogFirst) {
            return;
        }
        isSpecialDialogFirst = false;
        HttpRequest.sendGet(TLUrl.getInstance().URL_HWG_DLG, "act=advert&op=findAdvert", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            LogUtil.e("mwj140", "msg==" + msg);
                            if (msg != null && !"".equals(msg)) {
                                JSONObject json = new JSONObject(msg);
                                if (json.optInt("code") == 200) {
                                    JSONArray advertInfo = json.optJSONObject("datas").optJSONArray("advertInfo");
                                    int i = advertInfo.length();
                                    if (i == 0) {
                                        return;
                                    }
                                    for (int j = 0; j < i; j++) {
                                        JSONObject info = advertInfo.optJSONObject(j);
                                        if ("1".equals(info.optString("status"))) {
                                            //显示
                                            int num = Integer.parseInt(info.optString("type"));
                                              /*  private  int    TYPE_TEXT=1;//文本
    private   int   TYPE_LINETEXT=2;//超链接
    private  int  TYPE_PICTURE=3;//图片
    private  int   TYPE_OTHER=4;//其他*/
                                            switch (num) {
                                                case 1:
                                                    LogUtil.e("mwj140", "showNoticeDlg");
                                                    SecondUtils.showNoticeDlg(info.optString("content"), activity);
                                                    break;

                                            }
                                        }
                                    }

                                }

                            }
                        } catch (Exception ex) {

                        }

                    }
                });
            }
        });


    }

    private void initListener() {
        tRefresh.setOnClickListener(this);
        relative_back.setOnClickListener(this);
    }

    private void initMyView(String specidId) {
        JSONArray mainArray = aCache.getAsJSONArray(TLUrl.getInstance().HWGDETIADLS);
        if (mainArray != null) {
            try {
                Log.i("zds", "有本地mainsize");
                initMainView(mainArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {

            ProgressDlgUtil.showProgressDlg("",activity);
            Log.i("zds", "刷新mainsize");

//            http://www.huaqiaobang.com/mobile/index.php?act=goods_special&op=find_head&id=74  act=goods_special&op=find_head
            HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base+"/mobile/index.php","act=goods_special&op=find_head&id="+specidId, new HttpRevMsg() {
                @Override
                public void revMsg(final String msg) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("zds", "main_list=" + msg);
                            if (msg.length() == 0) {
                                if (tRefresh != null)
                                    tRefresh.setVisibility(View.VISIBLE);
                                return;
                            }

                            if (tRefresh != null)
                                tRefresh.setVisibility(View.GONE);

                            try {
                                loadSuccess = true;
                                nameList.clear();
                                idList.clear();
                                imgs.clear();
                                desc.clear();
                                JSONObject object= new JSONObject(msg);
                                if(object!=null){

                                    JSONArray array=object.optJSONArray("special_list");
                                    if (aCache.getAsJSONArray(TLUrl.getInstance().HWGDETIADLS) == null) {
                                        aCache.put(TLUrl.getInstance().HWGDETIADLS, array, 24 * 60 * 60);
                                    }
                                    isReLoad = true;
                                    initMainView(array);
                                    ProgressDlgUtil.stopProgressDlg();
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                Log.i("zds", e.toString());
                                Log.i("zds", msg);
                                e.printStackTrace();

                            } finally {
                                ProgressDlgUtil.stopProgressDlg();
                                if (!loadSuccess && tRefresh != null) {
                                    tRefresh.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });

                }
            });
        }

    }

    private void initMainView(JSONArray array) throws JSONException {

if(array!=null&&array.length()>0){
    for (int i = 0; i < array.length(); i++) {
        JSONObject mObject = array.getJSONObject(i);
//        "id": "1",
//                "special_id": "74",
//                "special_name": "\u6d4b\u8bd51",
//                "item_id": "74",
//                "img": "http:\/\/www.huaqiaobang.com\/data\/upload\/mobile\/special\/s8\/s8_a0cce74d2c134d932bc0fc71c8a3db65.png",
//                "descs": "\u6d4b\u8bd5\u63cf\u8ff01",
//                "titles": null


        String name = mObject.optString("special_name");
        String id = mObject.optString("item_id");
        String img = mObject.optString("img");
        String descs = mObject.optString("descs");
        nameList.add(name);
        idList.add(id);
        imgs.add(img);
        desc.add(descs);
//        nameList.add(0, name);
//        idList.add(0, id);
    }
    initViewPager();
}

    }


    private void initViewPager() {
        strs = new String[nameList.size()];
        //第三方Tab
        viewPagerAdapter = new CFViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < nameList.size(); i++) {
            strs[i] = TLUrl.getInstance().HWGDETIADLS + i;
            Log.i("zds", "str[" + i + "]=" + strs[i]);
            if (i == 0) {
                viewPagerAdapter.getDatas().add(MainFragment3.newInstance(idList.get(0), strs[0],imgs.get(0),desc.get(0)));
                viewPagerAdapter.getTitle().add(0, nameList.get(0));
            } else {
                viewPagerAdapter.getDatas().add(MainFragment3.newInstance(idList.get(i), strs[i],imgs.get(i),desc.get(i)));
                viewPagerAdapter.getTitle().add(i, nameList.get(i));
            }
            Log.i("zds", "idList.get(" + i + ")=" + idList.get(i));
            Log.i("zds", "nameList.get(" + i + ")=" + nameList.get(i));
        }
        //滑动的viewpager
        mainPager.setAdapter(viewPagerAdapter);
        mainPager.setOffscreenPageLimit(1);
        mainPager.setCurrentItem(0);
//        pager.setPageTransformer(true, new DepthPageTransformer());
        mainTabs.setViewPager(mainPager);
        mainTabs.setIndicatorHeight(Util.dip2px(activity, 4));
        mainTabs.setTextSize(Util.dip2px(activity, 16));
        setSelectTextColor(0);
        setTextType();
        mainTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                setSelectTextColor(position);
                currentFragment = viewPagerAdapter.getItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int position) {

                System.out.println("Change Posiont:" + position);

                // TODO Auto-generated method stub

            }
        });
        currentFragment = viewPagerAdapter.getItem(0);
    }

    private void setTextType() {
        for (int i = 0; i < nameList.size(); i++) {
            View view = mainTabs.getChildAt(0);
//            if (view instanceof LinearLayout) {
            View viewText = ((LinearLayout) view).getChildAt(i);
            TextView tabTextView = (TextView) viewText;
            if (tabTextView != null) {
//                SpannableString msp = new SpannableString(tabTextView.getText());
//                msp.setSpan(new RelativeSizeSpan(0.2f),0,msp.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                tabTextView.setText(msp);
//                Typeface fontFace = Typeface.createFromAsset(activity.getAssets(),
//                        "font/fangzhenglantinghei.TTF");
//                tabTextView.setTypeface(fontFace);
                Util.setFZLTHFont(tabTextView);
            }
//            }
        }
    }

    private void setSelectTextColor(int position) {
        for (int i = 0; i < nameList.size(); i++) {
            View view = mainTabs.getChildAt(0);
//            if (view instanceof LinearLayout) {
            View viewText = ((LinearLayout) view).getChildAt(i);
            TextView tabTextView = (TextView) viewText;
            if (tabTextView != null) {
//                SpannableString msp = new SpannableString(tabTextView.getText());
//                msp.setSpan(new RelativeSizeSpan(0.2f),0,msp.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                tabTextView.setText(msp);
                if (position == i) {
                    tabTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    tabTextView.setTextColor(getResources().getColor(R.color.hwg_text2));
                }
            }
//            }
        }
    }



    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.t_refresh:
                loadSuccess = false;
                aCache.remove(TLUrl.getInstance().HWGDETIADLS);
                ProgressDlgUtil.showProgressDlg("Loading...", activity);
                initMyView(specidId);
                initSpecialNoticeDialog();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ProgressDlgUtil.stopProgressDlg();
                    }
                }, 2000);
                break;
            case R.id.relative_back:
              finish();
                break;
        }
    }
}