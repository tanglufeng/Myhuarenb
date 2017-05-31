package com.abcs.huaqiaobang.tljr.news;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.tljr.news.fragment.NewsFragment;

public class SortNewsActivity extends BaseFragmentActivity {
    public static NewsFragment fragment;
//    private View view;
    private String defaultPicture ;

    private String sp, num, country,subject;
//    public int nowTypeSpecial = 0;
//    private String defaultPicture = "default";
//    private LocalDBManager dbManager;
//    public ZrcListView listView;
//    //    public NewsAdapter listviewAdapter;
//    public static ArrayList<News> dataList = new ArrayList<News>();
//    private int index = 0;
//    private NewsAdapter2 adapter;
//    private String isReadNewsId = "";
//    private boolean isLoadMore;

    @Override
    protected void onResume() {
        super.onResume();
//        if (adapter != null) {
//            adapter.notifyDataSetChanged();
//        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
       // fragment=null;
//        HuanQiuShiShi.currentListData.clear();
//        HuanQiuShiShi.currentListData.addAll(dataList);
//        ImageLoader.getInstance().clearMemoryCache();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_sort_news);
        sp = intent.getStringExtra("sp");
        country = intent.getStringExtra("country");
        num = intent.getStringExtra("num");
        subject = intent.getStringExtra("subject");
        defaultPicture= intent.getStringExtra("defaultPicture");

        TextView tiltle = (TextView) findViewById(R.id.tljr_txt_news_from_name);
        tiltle.setText(country);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

          fragment = new NewsFragment();
        Bundle data = new Bundle();
        data.putString("nowTypeName",country);
        data.putString("nowTypeSpecial", sp);
        data.putString("subject", subject);
        data.putString("defaultPicture", defaultPicture);



        fragment.setArguments(data);
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();



        ((View) findViewById(R.id.tljr_img_news_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


//
//

//
//        view = LayoutInflater.from(this).inflate(R.layout.activity_sort_news, null);
//
////        FrameLayout frame_contanier = (FrameLayout) view.findViewById(R.id.frame_contanier);
//        sp = intent.getStringExtra("sp");
//        country = intent.getStringExtra("country");
//        num = intent.getStringExtra("num");
//
////        NewsFragment newsFragment=new NewsFragment(nowTypeName,nowTypeSpecial,defaultPicture,1);
//
//
//        dbManager = new LocalDBManager(this);
//        dbManager.clearInvalidData();
////        toast_refresh = (RelativeLayout) view.findViewById(R.id.toast_refresh);
////        toast_refresh_text = (TextView) view
////                .findViewById(R.id.toast_refresh_text);
////        noNetView = (LinearLayout) view.findViewById(R.id.tljr_lny_noNet_hqss);
//        InitListView();
//        initView();
//
//
//        view.findViewById(R.id.tljr_img_news_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });


    }

    private Handler handler = new Handler();
//
//    private void initView() {
//
//        MyApplication application = MyApplication.getInstance();
//        if (application.checkNetWork()) {
//
//            String id = "0";
//            if (application.self != null) {
//                id = application.self.getId();
//            }
//            if (!isLoadMore) {
//                index = 0;
//            }
//            ProgressDlgUtil.showProgressDlg("请稍等", this);
//            String param = "sp=" + sp + "&num=" + num + "&index=" + index + "&need=10&pId=" + id + "&digest=100";
//            HttpRequest.sendGet(TLUrl.getInstance().URL_new + "subject", param, new HttpRevMsg() {
//                @Override
//                public void revMsg(final String msg) {
//                    ProgressDlgUtil.stopProgressDlg();
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            getData(msg);
//
//                            if (!isLoadMore) {
//
//                                adapter = new NewsAdapter2(SortNewsActivity.this, dataList, listView, 2, "default");
//
//                                listView.setAdapter(adapter);
//                                Log.i("refresh", "sucess");
//                                listView.setRefreshSuccess("刷新成功");
//                                listView.setLoadMoreSuccess();
//                            } else {
//                                try {
//                                    adapter.notifyDataSetChanged();
//                                    listView.setLoadMoreSuccess();
//                                } catch (Exception e) {
////                                    getActivity().finish();
//                                }
//
//                            }
//                        }
//                    });
//
//                }
//            });
//        } else {
//            listView.setRefreshFail("请检查网络");
//        }
//
//
//    }
//
//    public void showMessage(String msg) {
//        Message message = new Message();
//        message.obj = msg;
//        mHandler.sendMessage(message);
//    }
//
//    public void showMessage(int what, String msg) {
//        Message message = new Message();
//        message.what = what;
//        message.obj = msg;
//        mHandler.sendMessage(message);
//    }
//
//    public Handler mHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            if (null == view) {
//                return;
//            }
//            switch (msg.what) {
//                case 1:
//                    NoticeDialog.showNoticeDlg("正在加载中...", SortNewsActivity.this);
//                    break;
//                case 2:
//                    NoticeDialog.stopNoticeDlg();
//                    break;
//                case 5:
////                   main.red_circle
//
//
//                default:
//                    showToast(msg.obj.toString());
//                    break;
//            }
//        }
//    };
//
//    public void showToast(String msg) {
//        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//    }
//
//    private void getData(String msg) {
//        JSONObject countrynews = null;
//        try {
//            countrynews = new JSONObject(msg);
//            if (countrynews == null) {
//                listView.setLoadMoreSuccess();
//                listView.stopLoadMore();
//                return;
//            }
//            if (!isLoadMore) {
////            listView.stopLoadMore();
//            }
//            Log.i("tga", "reCmd====" + countrynews.toString());
//
////        boolean insert = dbManager.insertOnceRequestNews(countrynews, DBHandler.TABLE_HOTNEWS);
////        Log.i("tga", "插入-------------:" + insert);
//
//            if (!"".equals(countrynews.optString("result"))) {
//
//                JSONObject result = countrynews.getJSONObject("result");
//                if (!result.optBoolean("more", false)) {
//                    listView.stopLoadMore();
//                    listView.setLoadMoreSuccess();
//                }
//
//                index = result.optInt("index", 0);
//
//                JSONArray news_array = result.getJSONArray("news");
//                if (news_array.length() != 0) {
//
//                    for (int i = 0; i < news_array.length(); i++) {
//                        News news_temp = new News();
//
//                        String title = news_array.getJSONObject(i)
//                                .optString("title");
//                        news_temp.setTitle(title);
//                        String url = news_array.getJSONObject(i)
//                                .optString("purl");
//                        news_temp.setpUrl(url);
//                        String digest = news_array.getJSONObject(i)
//                                .optString("digest");
//                        news_temp.setDigest(digest);
//
//                        String time = news_array.getJSONObject(i)
//                                .optString("time");
//                        news_temp.setHaveSee(news_array.getJSONObject(i).getBoolean("read"));
//
//                        news_temp.setSytime(time);
//                        news_temp.setDate(news_temp.getSytime().substring(5, 10).replaceFirst("-", "月")
//                                + "日");
//                        try {
//                            news_temp.setImp_time(HuanQiuShiShi.getStandardDate(news_temp.getTime()));
//                            news_temp.setSimple_time(HuanQiuShiShi.getStandardDateBySimple(time));
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//
//                        String source = news_array.getJSONObject(i)
//                                .optString("source");
//
//                        news_temp.setSource(source);
//                        String surl = news_array.getJSONObject(i)
//                                .optString("surl");
//
//                        news_temp.setSurl(surl);
//                        news_temp.setId(news_array.getJSONObject(i)
//                                .optString("id"));
//                        news_temp.setContent(news_array.getJSONObject(i)
//                                .optString("content"));
//
//                        news_temp.setLetterSpecies(news_array
//                                .getJSONObject(i).optString("species"));
//
//                        news_temp.setHaveCai(news_array.getJSONObject(i)
//                                .optBoolean("hasOppose", false));
//                        news_temp.setHaveZan(news_array.getJSONObject(i)
//                                .optBoolean("hasPraise", false));
//
//                        dataList.add(news_temp);
//                    }
//
//                } else {
////                Log.i("loadmore","meiyouxinwen");
//
//
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//
//    private void InitListView() {
//        listView = (ZrcListView) view.findViewById(R.id.sortnews_listview);
//
//        // // 设置默认偏移量，主要用于实现透明标题栏功能。（可选）
//        // float density = getResources().getDisplayMetrics().density;
//        // listView.setFirstTopOffset((int) (50 * density));
//
////        listView.setFooterDividersEnabled(false);
////        listView.setHeaderDividersEnabled(false);
//        // listView.setSelector(R.drawable.tljr_listview_selector);
//        // 设置下拉刷新的样式（可选，但如果没有Header则无法下拉刷新）
//        final SimpleHeader header = new SimpleHeader(this);
//        header.setTextColor(0xffeb5041);
//        header.setCircleColor(0xffeb5041);
//        listView.setHeadable(header);
//
//        // 设置加载更多的样式（可选）
//        SimpleFooter footer = new SimpleFooter(this);
//        footer.setCircleColor(0xffeb5041);
//        listView.setFootable(footer);
//        listView.startLoadMore();
//        listView.setOnRefreshStartListener(new ZrcListView.OnStartListener() {
//
//            @Override
//            public void onStart() {
//                // TODO Auto-generated method stub
//                dataList.clear();
//                isLoadMore = false;
////                listView.refresh();
////                Log.i("tga", "setOnLoadMoreStartListener");
//                initView();
////                listView.stopLoadMore();
//                listView.startLoadMore();
//                listView.setLoadMoreSuccess();
//            }
//        });
//
//        listView.setOnLoadMoreStartListener(new ZrcListView.OnStartListener() {
//            @Override
//            public void onStart() {
//                isLoadMore = true;
//                Log.i("tga", "setOnLoadMoreStartListener");
//                initView();
//
//            }
//        });
//
//        listView.setOnItemClickListener(new ZrcListView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(final ZrcListView parent, final View view,
//                                    final int position, long id) {
//
//
//                News news = adapter.getItem(position);
//                if (!news.isHaveSee()) {
//                    isReadNewsId = isReadNewsId.equals("") ? news.getId()
//                            : isReadNewsId + "," + news.getId();
//                    Log.i("read", "isReadNewsId :" + isReadNewsId);
//                }
//                updataUserIsRead(); // 上传已阅 s
//                news.setHaveSee(true);
//
//
//                Intent intent = new Intent(SortNewsActivity.this, NewsActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("position", position);
//                bundle.putString("nowTypeName", "买卖资讯");
//                bundle.putString("sortNews","");
//
////                dataListbundle.putSerializable("news1", dataList);
//                intent.putExtras(bundle);
//                startActivity(intent);
//                handler.postDelayed(new Runnable() { // 点击显示已批阅，以及字体颜色变灰
//
//                    @Override
//                    public void run() {
//                        // TODO Auto-generated method stub
//                        int firstVisiblePosition = parent
//                                .getFirstVisiblePosition();
//                        int lastVisiblePosition = parent
//                                .getLastVisiblePosition();
//                        if (position >= firstVisiblePosition
//                                && position <= lastVisiblePosition) {
//                            View mView = parent.getChildAt(position
//                                    - firstVisiblePosition);
//                            if (mView.getTag() instanceof NewsAdapter.ViewHolder2) {
//                                NewsAdapter.ViewHolder2 vh = (NewsAdapter.ViewHolder2) view
//                                        .getTag();
//                                vh.iv_isRead
//                                        .setVisibility(View.VISIBLE);
//                                vh.imp_news_title
//                                        .setTextColor(Color.GRAY);
//                            }
//                        }
//                    }
//                }, 1000);
//            }
//        });
//    }
//
//    private void updataUserIsRead() {
//
//        if (MyApplication.getInstance().self == null) {
//            return;
//        }
//
//        if (isReadNewsId.equals("")) {
//            return;
//        }
//
//        Log.i("read", "updataUserIsRead-isReadNewsId :" + isReadNewsId);
//        try {
//            Log.i("read", "上传已阅-:" + TLUrl.getInstance().URL_newsIsRead + "?" + "type=1"
//                    + "&pId=" + MyApplication.getInstance().self.getId()
//                    + "&data=[" + isReadNewsId + "]");
//            HttpRequest.sendPost(TLUrl.getInstance().URL_newsIsRead, "type=1" + "&pId="
//                    + MyApplication.getInstance().self.getId() + "&data=["
//                    + isReadNewsId + "]", new HttpRevMsg() {
//
//                @Override
//                public void revMsg(String msg) {
//                    // TODO Auto-generated method stub
//                    Log.i("read", "上传已阅+msg:" + msg);
//                    isReadNewsId = "";
//
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//

}
