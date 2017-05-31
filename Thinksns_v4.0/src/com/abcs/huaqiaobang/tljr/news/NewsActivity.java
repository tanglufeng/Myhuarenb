package com.abcs.huaqiaobang.tljr.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.fragment.MainFragment2;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.tljr.news.bean.News;
import com.abcs.huaqiaobang.tljr.news.fragment.DetailNewsFragment;
import com.abcs.huaqiaobang.tljr.news.fragment.NewsFragment;
import com.abcs.huaqiaobang.util.LogUtil;
import com.abcs.huaqiaobang.wxapi.official.share.ShareQQPlatform;
import com.abcs.huaqiaobang.wxapi.official.share.ShareWeiboPlatform;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;

public class NewsActivity extends BaseFragmentActivity implements View.OnClickListener,IWeiboHandler.Response {

    public static boolean isActive = false;


    /*
     * 新闻正文页面
     */
    public long PUBLISH_COMMENT_TIME;
    NewsActivity activity;
    public ViewPager viewpager;
    public static NewsDetailFragmentAdapter adapter;
    public DetailNewsFragment fragment_NewsDetails;

    private TextView tljr_txt_from_name; // 新闻来源



    public HashMap<String, Fragment> fragmentList = new HashMap<String, Fragment>();

    public News news;
    public int Current_position;
    private ArrayList<News> currentList = new ArrayList<News>();
    public String Tag = "NewsActivity";

    String nowTypeName;

    Realm myRealm;
    //RealmAsyncTask transaction ;



    public static final int MODE_PICTURE = 0; // 图文新闻
    public static final int MODE_SYNEWS = 1; //首页新闻
    public static final int MODE_SORT = 2; //首页新闻
    public static int current_Mode = 5;


    @Override
    protected void onStart() {
        super.onStart();
        isActive = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;
        HuanQiuShiShi.gotoDetailsNews =false;





    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRealm.close();
        fragmentList = null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ShareQQPlatform.getInstance().registerShare(this);
        ShareWeiboPlatform.getInstanse().regiesetShare(this, savedInstanceState, this);
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
       // BwManager.getInstance().initShare(this);
        setContentView(R.layout.tljr_activity_news);
        activity = (NewsActivity) this;


        initData();
        initView();


    }



    private void initData() {
        Bundle bundleObject = getIntent().getExtras();
        try {
            myRealm = Realm.getDefaultInstance();
        } catch (RealmMigrationNeededException e) {
            RealmConfiguration config = new RealmConfiguration.Builder(MyApplication.getInstance()).deleteRealmIfMigrationNeeded().build();
            Realm.setDefaultConfiguration(config);
            myRealm = Realm.getDefaultInstance();
        }
        Current_position = bundleObject.containsKey("position") ? bundleObject.getInt("position") : 0;
        nowTypeName = bundleObject.containsKey("nowTypeName") ? bundleObject.getString("nowTypeName") : "新闻";

        if (bundleObject.containsKey("NewsFragment")) {
            current_Mode = MODE_PICTURE;


                NewsFragment fragment = (NewsFragment) HuanQiuShiShi.fragmentList.get(nowTypeName);
                currentList = fragment.newsManager.getList();



        }
        else if (bundleObject.containsKey("sortNews")) {
            current_Mode = MODE_SORT;
            currentList = SortNewsActivity.fragment.newsManager.getList();
//            ArrayList<News> list = new ArrayList<News>(SortNewsActivity.dataList);

//            currentList = list;

        }
        else if (bundleObject.containsKey("SYNews")) {
            current_Mode = MODE_SYNEWS;

            ArrayList<News> list = new ArrayList<News>(MainFragment2.mNews);

            currentList = list;

        }  else if (bundleObject.containsKey("pushNews")) {
            current_Mode = MODE_PICTURE;
            news = (News) bundleObject.getSerializable("news");
            currentList.add(news);
            Current_position = 0;
        } else {
            finish();
        }
    }



    private void initView() {
		/*
		 * title bar
		 */
        tljr_txt_from_name = (TextView) findViewById(R.id.tljr_txt_news_from_name);

        ((RelativeLayout) findViewById(R.id.tljr_img_news_back)).setOnClickListener(this);
        viewpager = (ViewPager) findViewById(R.id.tljr_hqss_news_mViewPager);


        tljr_txt_from_name.setText(nowTypeName);

        adapter = new NewsDetailFragmentAdapter(activity.getSupportFragmentManager());
        adapter.notifyDataSetChanged();
        viewpager.setAdapter(adapter);

        viewpager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
             //   Constant.addClickCount();
                Current_position = position;

                if (position == 0) {
                    Toast.makeText(activity, "已经是第一页了", Toast.LENGTH_LONG).show();
                }

				/*
				 * 更新具体页面的已阅
				 */
                if (fragmentList.get(position + "") != null) {
                    DetailNewsFragment detailsFragment = (DetailNewsFragment) fragmentList.get(position + "");
                    detailsFragment.reFresh();
                }

                if (current_Mode == MODE_PICTURE ||current_Mode  ==MODE_SORT) {
					/*
					 * 上传已阅
					 */
                    final int f = position;
                    if (currentList.get(position) != null) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!currentList.get(f).isHaveSee() && !HuanQiuShiShi.id.contains(currentList.get(f).getId())) {
                                    JSONObject obj = new JSONObject();

                                    try {
                                        obj.put("time", currentList.get(f).getTime());
                                        obj.put("id", currentList.get(f).getId());
                                        obj.put("species", currentList.get(f).getSpecial());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    HuanQiuShiShi.id.add(currentList.get(f).getId());
                                    if (HuanQiuShiShi.readId == null) {
                                        HuanQiuShiShi.readId = new JSONArray();
                                    }
                                    HuanQiuShiShi.readId.put(obj);


                                    if(current_Mode  ==MODE_SORT){
                                        if (HuanQiuShiShi.readId.length() > 0) {
                                            HuanQiuShiShi.updataUserIsRead();
                                        }
                                    }else {
                                        if (HuanQiuShiShi.readId.length() > 3) {
                                            HuanQiuShiShi.updataUserIsRead();
                                        }
                                    }


                                }
                                myRealm.beginTransaction();
                                currentList.get(f).setHaveSee(true);
                                myRealm.copyToRealmOrUpdate(currentList.get(f));
                                myRealm.commitTransaction();
                                LogUtil.i("read", "viewpager see:" + currentList.get(f).isHaveSee());

                            }
                        }, 1000);

                    }

                    if (position == (currentList.size() - 10) || position == (currentList.size() - 1)) {
                        if (currentList.size() < 10) {
                            return;
                        }
                        if(current_Mode  ==MODE_SORT){
                            return;
                        }
                        Toast.makeText(activity, "正在加载更多新闻...", Toast.LENGTH_LONG).show();




                        if (HuanQiuShiShi.fragmentList.get(nowTypeName) instanceof NewsFragment) {
                            NewsFragment fg = (NewsFragment) HuanQiuShiShi.fragmentList.get(nowTypeName);
                            fg.getNewestNews(false, false);
                            fg.setAdapterListener(new NewsFragment.OnPagerAdapterListener() {
                                @Override
                                public void notifyDataAdapter() {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            NewsFragment fragment = (NewsFragment) HuanQiuShiShi.fragmentList.get(nowTypeName);
                                            currentList = fragment.newsManager.getList();
                                            if (adapter!=null && viewpager !=null){
                                                adapter.notifyDataSetChanged();
                                                viewpager.setCurrentItem(Current_position, true);
                                            }

                                            Toast.makeText(activity, "加载新闻完成...", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            });
                        }
                    }

                }




            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        viewpager.setCurrentItem(Current_position);

    }




    // StringBuffer isReadId = new StringBuffer();
//	public JSONArray readId = new JSONArray();


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

        if (currentList != null && currentList.size() > 0) {

            NewsFragment fragment = (NewsFragment) HuanQiuShiShi.fragmentList.get(nowTypeName);
            if (fragment != null && fragment.newsManager !=null) {
                fragment.newsManager.setList(currentList);
            }
        }

    }

    @Override
    public void onResponse(BaseResponse baseResp ) {
        switch (baseResp.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                showToast("分享成功");
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                showToast("取消分享");
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                showToast("分享失败，Error Message: " + baseResp.errMsg);
                break;
        }
    }


    public class NewsDetailFragmentAdapter extends FragmentPagerAdapter {
        FragmentManager fm;

        public NewsDetailFragmentAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;

        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            // TODO Auto-generated method stub
            // super.destroyItem(container, position, object);

            View view = (View) object;
            ((ViewPager) container).removeView(view);
            view = null;

        }

        @Override
        public Fragment getItem(int position) {

            if (fragmentList.get(position) == null) {
                DetailNewsFragment fg = new DetailNewsFragment();
                fg.setNews(currentList.get(position));
                fg.setRealm(myRealm);

                fragmentList.put(position + "", fg);
                return fg;
            } else {
                DetailNewsFragment fg = (DetailNewsFragment) fragmentList.get(position);
                return fg;
            }

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return currentList.size();
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     //   BwManager.getInstance().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
     //   BwManager.getInstance().onNewIntent(intent);
        super.onNewIntent(intent);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {

            case R.id.tljr_img_news_back:
                finish();
                break;

            default:
                break;
        }
    }

}