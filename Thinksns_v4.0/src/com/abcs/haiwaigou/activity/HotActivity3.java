package com.abcs.haiwaigou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.fragment.HotItemFragment;
import com.abcs.haiwaigou.fragment.adapter.CFViewPagerAdapter;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.ServerUtils;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.astuetz.PagerSlidingTabStrip;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.abcs.sociax.t4.android.video.ToastUtils.showToast;


public class HotActivity3 extends FragmentActivity implements View.OnClickListener{
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_back:
                finish();
                break;
            case R.id.t_refresh:
                loadSuccess = false;
                ProgressDlgUtil.showProgressDlg("Loading...", this);
                initMyView();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ProgressDlgUtil.stopProgressDlg();
                    }
                }, 2000);
                break;
        }
    }

    Fragment currentFragment;
    @InjectView(R.id.main_tabs)
    PagerSlidingTabStrip mainTabs;
    @InjectView(R.id.relative_back)
    RelativeLayout relative_back;
    @InjectView(R.id.main_pager)
    public ViewPager mainPager;
    @InjectView(R.id.t_refresh)
    TextView tRefresh;
    @InjectView(R.id.t_title)
    TextView t_title;
    private View view;
    CFViewPagerAdapter viewPagerAdapter;
    private Handler handler = new Handler();
    private ArrayList<String> nameList = new ArrayList<>();
    private ArrayList<String> idList = new ArrayList<>();
    private String[] strs;
    public static TextView car_num;
    private boolean loadSuccess = false;

    private int position=0;
    public int  curr_position=0;
    private String class_2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_hot3);

        ButterKnife.inject(this);

        class_2=getIntent().getStringExtra("class_2");  //二级分类的id
        position=getIntent().getIntExtra("position",0);

        if (!ServerUtils.isConnect(this)) {
            handler.post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    showToast("请检查您的网络");
                }
            });
            return;
        }else {
            initListener();
            initMyView();
        }
    }

    private void initListener() {
        tRefresh.setOnClickListener(this);
        relative_back.setOnClickListener(this);
    }

    private void initMyView() {

        ProgressDlgUtil.showProgressDlg("",this);

        if(getIntent().getStringExtra("title").equals("null")||getIntent().getStringExtra("title").equals("")){
            t_title.setText("全部");
        }else {
            t_title.setText(getIntent().getStringExtra("title"));
        }

        //            http://www.huaqiaobang.com/mobile/index.php?act=goods_class&op=get_goods_class&class_2=470
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base+"/mobile/index.php", "act=goods_class&op=get_goods_class&class_2="+class_2, new HttpRevMsg() {
            //            HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_wanle, "act=travel&op=travel_country", new HttpRevMsg() {
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

                            JSONObject jsonObject= new JSONObject(msg);
                            if(jsonObject.optInt("state")==1){
                                JSONArray array=jsonObject.optJSONArray("datas");

                                initMainView(array);
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

    private void initMainView(JSONArray array) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            JSONObject mObject = array.getJSONObject(i);
            String name = mObject.optString("gc_name");
            String id = mObject.optString("gc_id");
            nameList.add(name);
            idList.add(id);
        }
        initViewPager();
    }


    private void initViewPager() {
        strs = new String[nameList.size()];
        //第三方Tab
        viewPagerAdapter = new CFViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < nameList.size(); i++) {
            strs[i] = TLUrl.getInstance().HWGMAIN + i;
            Log.i("zds", "str[" + i + "]=" + strs[i]);
            viewPagerAdapter.getDatas().add(HotItemFragment.newInstance(idList.get(i), class_2,position+"",strs[i]));
            viewPagerAdapter.getTitle().add(i, nameList.get(i));
        }


        //滑动的viewpager
        mainPager.setAdapter(viewPagerAdapter);
        mainPager.setOffscreenPageLimit(1);
        mainTabs.setViewPager(mainPager);
        mainTabs.setIndicatorHeight(Util.dip2px(this, 4));
        mainTabs.setTextSize(Util.dip2px(this, 16));
        setSelectTextColor(0);
        setTextType();
        mainTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                 curr_position=position;
                Log.i("zds","postion==="+position);
                setSelectTextColor(position);
                currentFragment = viewPagerAdapter.getItem(position);
                t_title.setText(nameList.get(position));

              /*  HotItemFragment fragment  = (HotItemFragment) viewPagerAdapter.getItem(position);
                 fragment.getDatas();*/

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

        try {
            if(position>0){
                Log.i("zds_po",position+"");
                mainPager.setCurrentItem(position);
               /* HotItemFragment fragment  = (HotItemFragment) viewPagerAdapter.getItem(position);
                fragment.getDatas();*/
            }else {
                mainPager.setCurrentItem(0);
              /*  HotItemFragment fragment = (HotItemFragment) viewPagerAdapter.getItem(0);
                fragment.getDatas();*/
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        ProgressDlgUtil.stopProgressDlg();
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
    protected void onDestroy() {
        ButterKnife.reset(this);
        super.onDestroy();
    }

}
