package com.abcs.haiwaigou.local.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.fragment.adapter.CFViewPagerAdapter;
import com.abcs.haiwaigou.local.fragment.NewHireJobFragmentOthers;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.astuetz.PagerSlidingTabStrip;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HireAndFindActivityOthers extends BaseFragmentActivity implements View.OnClickListener {

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.t_title_name)
    TextView tTitleName;
    @InjectView(R.id.relative_publish)
    RelativeLayout relativePublish;
    @InjectView(R.id.comment_tabs)
    PagerSlidingTabStrip commentTabs;
    @InjectView(R.id.main_pager)
    ViewPager mainPager;

    Fragment currentFragment;
    int position;
    int curr_pos=0;
    int currentType;
    CFViewPagerAdapter viewPagerAdapter;
    //    String countryId;
    String cityId;
    String menuId;
    String title;
    @InjectView(R.id.linear_tab)
    LinearLayout linearTab;
    private ArrayList<String> nameList = new ArrayList<>();
    private ArrayList<String> idList = new ArrayList<>();
    private Handler handler = new Handler();
    public static HashMap<String ,Fragment>fragmentHashMap=new HashMap<String ,Fragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_activity_hire_and_find);
        ButterKnife.inject(this);
//        countryId=getIntent().getStringExtra("countryId");
        title = getIntent().getStringExtra("title");
        tTitleName.setText(title);
        cityId = getIntent().getStringExtra("cityId");
        menuId = getIntent().getStringExtra("menuId");
        initListener();
        initSubTitle();
    }

    private void initSubTitle() {
        HttpRequest.sendGet(TLUrl.getInstance().URL_local_get_subTitle, "menuId=" + menuId, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject mainObj = new JSONObject(msg);
                            if (mainObj.optString("status").equals("1")) {
                                JSONArray msgArray = mainObj.optJSONArray("msg");
                                initSubList(msgArray);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    private void initSubList(JSONArray msgArray) throws JSONException {
        if (msgArray.length() != 0) {
            for (int i = 0; i < msgArray.length(); i++) {
                JSONObject object = msgArray.getJSONObject(i);
                String name = object.optString("menuName");
                String id = object.optString("menuId");
                nameList.add(name);
                idList.add(id);
            }
            initViewPager();
        }
    }

    private void initListener() {
        relativeBack.setOnClickListener(this);
        relativePublish.setOnClickListener(this);
    }

    private void initViewPager() {
        viewPagerAdapter = new CFViewPagerAdapter(getSupportFragmentManager());
        initHireFind();
        //第三方Tab
        mainPager.setAdapter(viewPagerAdapter);
        mainPager.setOffscreenPageLimit(1);
        mainPager.setCurrentItem(position);
        commentTabs.setViewPager(mainPager);
        commentTabs.setIndicatorHeight(Util.dip2px(this, 4));
        commentTabs.setTextSize(Util.dip2px(this, 16));
        setSelectTextColor(position);
        setTextType();
        commentTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                setSelectTextColor(position);
                currentFragment = viewPagerAdapter.getItem(position);
                currentType = position + 1;
                curr_pos=position;
//                currentgoodsFragment.initRecycler();
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
        currentType = 1;
    }

    private void initHireFind() {
        fragmentHashMap.clear();
        if (nameList.size() <2) {
            linearTab.setVisibility(View.GONE);
        }
        for (int i = 0; i < nameList.size(); i++) {
            viewPagerAdapter.getDatas().add(NewHireJobFragmentOthers.newInstance(cityId, idList.get(i),nameList.get(i),menuId,title));
            viewPagerAdapter.getTitle().add(i, nameList.get(i));
        }
    }

    private void setTextType() {
        for (int i = 0; i < nameList.size(); i++) {
            View view = commentTabs.getChildAt(0);
            View viewText = ((LinearLayout) view).getChildAt(i);
            TextView tabTextView = (TextView) viewText;
            if (tabTextView != null) {
                Util.setFZLTHFont(tabTextView);
            }
        }
    }

    private void setSelectTextColor(int position) {
        for (int i = 0; i < nameList.size(); i++) {
            View view = commentTabs.getChildAt(0);
            View viewText = ((LinearLayout) view).getChildAt(i);
            TextView tabTextView = (TextView) viewText;
            if (tabTextView != null) {
                if (position == i) {
                    tabTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    tabTextView.setTextColor(getResources().getColor(R.color.hwg_text2));
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_back:
                finish();
                break;
            case R.id.relative_publish:
                go2Publish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fragmentHashMap.clear();
    }

    public void go2Publish(){
        Intent intent = new Intent(this, PublishMessageActivity.class);
        intent.putExtra("position",curr_pos);
        intent.putExtra("cityId", cityId);
        intent.putExtra("menuId", menuId);
        intent.putExtra("title", title);
        startActivity(intent);
    }
}
