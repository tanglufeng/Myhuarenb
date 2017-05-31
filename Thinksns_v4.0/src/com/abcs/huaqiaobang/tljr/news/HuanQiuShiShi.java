package com.abcs.huaqiaobang.tljr.news;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.abcs.haiwaigou.activity.HuanqiuActivity;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.main.MainActivity;
import com.abcs.huaqiaobang.tljr.data.Constent;
import com.abcs.huaqiaobang.tljr.news.adapter.NewsFragmentPagerAdapter;
import com.abcs.huaqiaobang.tljr.news.channel.bean.ChannelItem;
import com.abcs.huaqiaobang.tljr.news.fragment.NewsFragment;
import com.abcs.huaqiaobang.tljr.news.fragment.SortNewsFragment;
import com.abcs.huaqiaobang.tljr.news.tabs.MyHorizontalScrollView;
import com.abcs.huaqiaobang.tljr.news.tabs.PagerSlidingTabStrip;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.abcs.huaqiaobang.util.ServerUtils;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.android.fragment.FragmentSociax;

import org.json.JSONArray;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


@SuppressLint("HandlerLeak")
public class HuanQiuShiShi extends FragmentSociax implements OnClickListener {

    public static final String Tag = "HuanQiuShiShi";
    public static boolean isDowanLoading = false;
    public static boolean channel_change = false;

    private RelativeLayout view;
//    private ActivityHome activity;
    private HuanqiuActivity activity;
    public ViewPager pager;
    private PagerSlidingTabStrip tabs;

    private ArrayList<ChannelItem> userChannelList ;
    public static HashMap<String, Fragment> fragmentList = new HashMap<String, Fragment>();

    NewsFragmentPagerAdapter mAdapetr;
    private Handler handler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        activity = (ActivityHome) getActivity();
        activity = (HuanqiuActivity) getActivity();
        view = (RelativeLayout) activity.getLayoutInflater().inflate(
                R.layout.tljr_fragment_huanqiushishi, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            window = getWindow();
//            view.findViewById(R.id.statusbar).setVisibility(View.VISIBLE);
        }

        //	initView();
        initView();
    }



    public void initView() {



//		String params = "platform=1&uid=10001"+"&data="+FileUtil.getFromAssets("test.properties");
//		 
//		NetUtil.sendPost(UrlUtil.URL_new+"api/utc/update", params, new NetResult() {
//			@Override
//			public void result(String msg) {
//			 LogUtil.i("uppp", msg);
//			}
//		});





//        if (Constant.isNewsGuideToast == 0) {
//            // 引导提示Dialog
//            cueDialogShow();
//        }
        if(userChannelList!=null){
            return;
        }
                String k="";

//        int mb =(int) (getFolderSize(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES))/1024/1024);
//        if(mb>15){
//            Util.deleteDir(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
//        }

        userChannelList = new ArrayList<ChannelItem>() ;
//        FrameLayout tljr_flame_add_channel = (FrameLayout) view
//                .findViewById(R.id.tljr_flame_add_channel);
//        tljr_flame_add_channel.setOnClickListener(this);
//        ImageView btn_add_channel = (ImageView) view
//                .findViewById(R.id.tljr_btn_add_channel);
//        btn_add_channel.setOnClickListener(this);
        //d
		/*
		 * 搜索
		 */
//        ImageView tljr_hqss_search = (ImageView) view
//                .findViewById(R.id.tljr_hqss_search);
//        tljr_hqss_search.setOnClickListener(this);

		/*
		 * 离线下载
		 */
//        ImageView tljr_hqss_underline = (ImageView) view
//                .findViewById(R.id.tljr_hqss_underline);
//        tljr_hqss_underline.setOnClickListener(this);
		
		/*
		 *  按日期查找新闻
		 */

        (view.findViewById(R.id.find_date)).setOnClickListener(this);
        pager = (ViewPager) view.findViewById(R.id.hqss_pager);
        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.hqss_tabs);

        setChangeView();

    }

    @Override
    public void initIntentData() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    public void setChangeView() {
        initChannelData();
        initFragment();
        initTabs();
    }

    private void initChannelData() {

        userChannelList = new ArrayList<ChannelItem>( MainActivity.userChannelList);

    }

    private void initFragment() {
        fragmentList.clear();
        int count = userChannelList.size();
        for (int i = 0; i < count; i++) {

            int channelType = userChannelList.get(i).getChannelType();
            if (channelType == 0  ) {
                NewsFragment pictureFragment = new NewsFragment();
                Bundle data = new Bundle();
                data.putString("nowTypeName", userChannelList.get(i).getName());
                data.putString("nowTypeSpecial", userChannelList.get(i)
                        .getSpecies());
                data.putString("defaultPicture", userChannelList.get(i)
                        .getContentPictureUrl());
                pictureFragment.setArguments(data);
                fragmentList.put(userChannelList.get(i).getName(),
                        pictureFragment);
            }
            else if (channelType == 4) {
                SortNewsFragment sortNewsFragment = SortNewsFragment.getInstance();
                Bundle data = new Bundle();
                data.putString("nowTypeName", userChannelList.get(i).getName());
                data.putString("nowTypeSpecial", userChannelList.get(i)
                        .getSpecies());
                data.putString("defaultPicture", userChannelList.get(i)
                        .getContentPictureUrl());
                sortNewsFragment.setArguments(data);
                fragmentList
                        .put(userChannelList.get(i).getName(), sortNewsFragment);
            }

        }
        mAdapetr = new NewsFragmentPagerAdapter(
                activity.getSupportFragmentManager(), userChannelList,
                activity, fragmentList);
        pager.setAdapter(mAdapetr);
    }

    /*
     * 标题滑动回弹 所需参数
     */
    int scrollX; // 滑动滚动时的 X
    int scrollTo; // 最后判断好X属于在哪个区间，scrollTo =所在区间
    public static ArrayList<Integer> leftList = new ArrayList<Integer>();

    private void initTabs() {
        // tabs.setIndicatorHeight(Util.dip2px(activity, 3));
        tabs.setTextSize(16);
        tabs.setViewPager(pager);
        tabs.setTabPaddingLeftRight(32); // defalut =24
        tabs.setIndicatorHeight(Util.dip2px(activity, 3));
        tabs.smoothScrollTo(0, 0);
		/*
		 * 新闻标题栏回弹效果
		 */
        scrollX = 0;
        scrollTo = 0;
        tabs.setHandler(handler);
        tabs.setOnScrollStateChangedListener(new MyHorizontalScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(
                    MyHorizontalScrollView.ScrollType scrollType) {
                // TODO Auto-generated method stub
                if (scrollType == MyHorizontalScrollView.ScrollType.IDLE) {
                    scrollX = tabs.getScrollX();
                    if (scrollX < 100) {
                        tabs.smoothScrollTo(0, 0);
                        tempX = scrollTo;
                        return;
                    }
                    for (int i = 0; i < leftList.size() - 1; i++) {
                        if (leftList.get(i) < scrollX
                                && scrollX < leftList.get(i + 1)) {
                            if (i == 0) {
                                scrollTo = scrollX - tempX <= 0 ? 0 : leftList
                                        .get(1);
                                break;
                            }
                            scrollTo = scrollX - tempX < 0 ? leftList.get(i) - 10
                                    : leftList.get(i + 1) + 10;
                            break;
                        }
                    }
                    tabs.smoothScrollTo(scrollTo, 0);
                    tempX = scrollTo;
                }

            }
        });

        leftList.clear();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int k = 0; k < tabs.tabsContainer.getChildCount(); k++) {
                    int leftX = tabs.tabsContainer.getChildAt(k).getLeft();
                    leftList.add(leftX);
                }
            }
        }, 2000);

    }

//    public void cueDialogShow() {
//        new CueDialog(activity, 1).show();
//    }

    int offset = 0;
    int tempX = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup p = (ViewGroup) view.getParent();

        if (p != null)
            p.removeAllViewsInLayout();
        return view;
    }

    @Override
    public int getLayoutId() {
        return R.layout.tljr_fragment_huanqiushishi;
    }

    public static <T> T[] concatAll(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    public void refreshFragment() {

        LogUtil.i("testchannel", "refreshFragment");
        if (mAdapetr != null && tabs != null) {
            mAdapetr.setFragments(fragmentList);
            userChannelList=null;
            fragmentList.clear();
            mAdapetr = null;
            tabs = null;
            pager = null;
            initView();
            tabs.notifyDataSetChanged();

            mAdapetr.notifyDataSetChanged();
            tabs.smoothScrollTo(0, 0);
        }
    }



    public static int turntoPage = 0;
    public static final int RESULT_CODE_TRUE = 1111;
    public static final int RESULT_CODE_FALSE = 222;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case RESULT_CODE_TRUE:
                refreshFragment();

                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        // pager.setCurrentItem(turntoPage);
                        pager.setCurrentItem(turntoPage, false);
                        turntoPage = 0;
                        HuanQiuShiShi.channel_change = false;
                    }
                }, 600);

                break;
            case RESULT_CODE_FALSE:
                break;

            default:
                break;
        }

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
//            case R.id.tljr_flame_add_channel:
//            case R.id.tljr_btn_add_channel:
//
//                if (MyApplication.getInstance().self != null) {
//
//                    Intent it = new Intent(activity, ChannelActivity.class);
//                    startActivityForResult(it, 1);
//                    activity.overridePendingTransition(R.anim.slide_in_from_right,
//                            R.anim.slide_out_to_right);
//
//                } else {
//                    activity.login();
//                    return;
//                }
//
//                break;

//            case R.id.tljr_hqss_underline:
//                Intent it = new Intent(activity, DownLoadActivity.class);
//                startActivity(it);
//                activity.overridePendingTransition(R.anim.slide_in_from_right,
//                        R.anim.slide_out_to_right);
//                break;
//
//            case R.id.tljr_hqss_search:
//                Intent it2 = new Intent(activity, NewsSeachActivity.class);
//                startActivity(it2);
//                activity.overridePendingTransition(R.anim.slide_in_from_right,
//                        R.anim.slide_out_to_right);
//                break;

            case R.id.find_date:

                if (ServerUtils.isConnect(activity)) {
                    if (pager !=null &&userChannelList !=null&&fragmentList !=null &&fragmentList.get(userChannelList.get(pager.getCurrentItem()).getName()) != null
                            && fragmentList.get(userChannelList.get(pager.getCurrentItem()).getName()) instanceof NewsFragment)
                    {
                        NewsFragment f = (NewsFragment) fragmentList.get(userChannelList.get(pager.getCurrentItem())
                                .getName());
                        f.choseDayGetNews();
                    }else{
                        Toast.makeText(activity, "当前频道暂不支持日期查询", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(activity, "请检查网络连接", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }

    public static final String FINISH_ACTION = "download_finish";





    /*
     * 上传已阅数据
     */
    // public String isReadNewsId = "";
    public static JSONArray readId;
    public static ArrayList<String> id = new  ArrayList<String>();
    public static void updataUserIsRead() {
        if (MyApplication.getInstance().self != null && readId != null && readId.length() > 0) {
            //	String url = UrlUtil.URL_new + "api/uc/read";
            String url = TLUrl.getInstance().URL_new + "api/uc/read";
            // String url =
            // "http://192.168.0.12:8080/QhWebNewsServer/api/uc/read";
            String params = "uid=" + MyApplication.getInstance().self.getId() +"&data=" + readId.toString()+"&platform=2";
            LogUtil.i("read", url + "?" + params);


            String pId = Constent.preference.getString("news_p_id", "0");
            HttpRequest.sendPost(url, params, new HttpRevMsg() {

                @Override
                public void revMsg(String msg) {
                    LogUtil.i("read", "上传已阅+msg:" + msg);
                    readId = null;
                }
            });







        }
    }




    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        IntentFilter download_Filter = new IntentFilter();
        download_Filter.addAction(FINISH_ACTION);
        activity.registerReceiver(download_finish, download_Filter);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        activity.unregisterReceiver(download_finish);
    }

    public static boolean gotoDetailsNews;
    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        LogUtil.i("read", Tag+": onPause()");
        if(!gotoDetailsNews){
            LogUtil.i(Tag, Tag+": updataUserIsRead()");
            updataUserIsRead();
        }

    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        LogUtil.i("read", Tag+": onStop()");
        if(!gotoDetailsNews){
            LogUtil.i(Tag, Tag+": updataUserIsRead()");
            updataUserIsRead();
        }

    }

    private BroadcastReceiver download_finish = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(FINISH_ACTION)) {
                HuanQiuShiShi.isDowanLoading = false;
            }
        }
    };
    public static long getFolderSize(File file){

        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++)
            {
                if (fileList[i].isDirectory())
                {
                    size = size + getFolderSize(fileList[i]);

                }else{
                    size = size + fileList[i].length();

                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //return size/1048576;
        return size;
    }
    public static String getStandardDateBySimple(String timeStr)
            throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",
                Locale.getDefault());
        Date date = sdf.parse(timeStr);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        String dayCurrent = cal.get(Calendar.MONDAY) + "-"
                + cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(date);
        String day = cal.get(Calendar.MONDAY) + "-"
                + cal.get(Calendar.DAY_OF_MONTH);

        if (day.equals(dayCurrent)) {
            return "今天     " + timeStr.substring(11, 16);

        } else {
            return timeStr.substring(5, 16);
        }

    }

    public static String getStandardDate(String timeStr) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",
                Locale.getDefault());
        Date date = sdf.parse(timeStr);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        String dayCurrent = cal.get(Calendar.MONDAY) + "-"
                + cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(date);
        String day = cal.get(Calendar.MONDAY) + "-"
                + cal.get(Calendar.DAY_OF_MONTH);

        if (day.equals(dayCurrent)) {
            return timeStr.substring(11, 16);

        } else {
            return timeStr.substring(5, 16);
        }

    }
    public static   String getStandardDate(long str)   {


        StringBuffer sb = new StringBuffer();

        long t = str;
        long time = System.currentTimeMillis() - t;
        long mill = (long) Math.ceil(time / 1000);// 秒前

        long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前

        long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时

        long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前

        if (day - 1 > 0) {
            sb.append(day + "天");
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天");
            } else {
                sb.append(hour + "小时");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时");
            } else {
                sb.append(minute + "分钟");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分钟");
            } else {
                sb.append(mill + "秒");
            }
        } else {
            sb.append("刚刚");
        }
        if (!sb.toString().equals("刚刚")) {
            sb.append("前");
        }
        return sb.toString();
    }

}