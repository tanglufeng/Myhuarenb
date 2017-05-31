package com.abcs.haiwaigou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.fragment.adapter.CFViewPagerAdapter;
import com.abcs.haiwaigou.utils.ACache;
import com.abcs.haiwaigou.utils.SecondUtils;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.ytbt.common.utils.LogUtil;
import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.android.ActivityHome;
import com.astuetz.PagerSlidingTabStrip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zjz on 2016/9/13.
 */
public class HWGGoodsFragment extends Fragment implements View.OnClickListener {

    Fragment currentFragment;
    @InjectView(R.id.main_tabs)
    PagerSlidingTabStrip mainTabs;
    @InjectView(R.id.main_pager)
    ViewPager mainPager;
    @InjectView(R.id.t_refresh)
    TextView tRefresh;
    private View view;
    private ActivityHome activity;
    CFViewPagerAdapter viewPagerAdapter;
    private Handler handler = new Handler();
    private ArrayList<String> nameList = new ArrayList<>();
    private ArrayList<String> idList = new ArrayList<>();
    private String[] strs;
    private ACache aCache;
    public static TextView car_num;
//    private MyBroadCastReceiver myBroadCastReceiver;
    private boolean isSpecialDialogFirst = true;
  /*  private  int    TYPE_TEXT=1;//文本
    private   int   TYPE_LINETEXT=2;//超链接
    private  int  TYPE_PICTURE=3;//图片
    private  int   TYPE_OTHER=4;//其他*/

    private boolean isReLoad = false;
    private boolean loadSuccess = false;

//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.i("zjz","goodsFragment_resume");
//        myBroadCastReceiver = new MyBroadCastReceiver(activity, updateUI);
//        myBroadCastReceiver.register();
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (ActivityHome) getActivity();
        aCache = ACache.get(activity);
        view = activity.getLayoutInflater().inflate(
                R.layout.hwg_goods_fragment, null);
        ButterKnife.inject(this, view);


        initListener();
        initMyView();
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

//    MyBroadCastReceiver.UpdateUI updateUI = new MyBroadCastReceiver.UpdateUI() {
//        @Override
//        public void updateShopCar(Intent intent) {
//
//        }
//
//        @Override
//        public void updateCarNum(Intent intent) {
//        }
//
//        @Override
//        public void updateCollection(Intent intent) {
//            if (intent.getStringExtra("type").equals(MyUpdateUI.MYHWG)) {
//                if (!isReLoad) {
//                    initListener();
//                    initMyView();
//                    Log.i("zjz", "reLoad");
//                }
//            }
//        }
//
//        @Override
//        public void update(Intent intent) {
//
//        }
//    };

    private void initListener() {
        tRefresh.setOnClickListener(this);
    }

    private void initMyView() {
        JSONArray mainArray = aCache.getAsJSONArray(TLUrl.getInstance().HWGMAIN);
        if (mainArray != null) {
            try {
                Log.i("zjz", "有本地mainsize");
                initMainView(mainArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("zjz", "刷新mainsize");
            HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_main, null, new HttpRevMsg() {
                @Override
                public void revMsg(final String msg) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("zjz", "main_list=" + msg);
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
                                JSONArray array = new JSONArray(msg);
                                if (aCache.getAsJSONArray(TLUrl.getInstance().HWGMAIN) == null) {
                                    aCache.put(TLUrl.getInstance().HWGMAIN, array, 24 * 60 * 60);
                                }
                                isReLoad = true;
                                initMainView(array);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                Log.i("zjz", e.toString());
                                Log.i("zjz", msg);
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
        for (int i = 0; i < array.length(); i++) {
            JSONObject mObject = array.getJSONObject(i);
            String name = mObject.optString("plate_name");
            String id = mObject.optString("plate_id");
            nameList.add(0, name);
            idList.add(0, id);
        }
        initViewPager();
    }

    private void initViewPager() {
        strs = new String[nameList.size()];
        //第三方Tab
        viewPagerAdapter = new CFViewPagerAdapter(getChildFragmentManager());
        for (int i = 0; i < nameList.size(); i++) {
            strs[i] = TLUrl.getInstance().HWGMAIN + i;
            Log.i("zjz", "str[" + i + "]=" + strs[i]);
            if (i == 0) {
                viewPagerAdapter.getDatas().add(MainFragment1.newInstance(idList.get(0), strs[0]));
                viewPagerAdapter.getTitle().add(0, nameList.get(0));
            } else {
                viewPagerAdapter.getDatas().add(MainFragment2.newInstance(idList.get(i), strs[i]));
                viewPagerAdapter.getTitle().add(i, nameList.get(i));
            }
            Log.i("zjz", "idList.get(" + i + ")=" + idList.get(i));
            Log.i("zjz", "nameList.get(" + i + ")=" + nameList.get(i));
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup p = (ViewGroup) view.getParent();

        if (p != null)
            p.removeAllViewsInLayout();
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        if (myBroadCastReceiver != null)
//            myBroadCastReceiver.unRegister();
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.t_refresh:
                loadSuccess = false;
                aCache.remove(TLUrl.getInstance().HWGMAIN);
                ProgressDlgUtil.showProgressDlg("Loading...", activity);
                initMyView();
                initSpecialNoticeDialog();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ProgressDlgUtil.stopProgressDlg();
                    }
                }, 2000);
                break;
        }
    }
}