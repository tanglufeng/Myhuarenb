package com.abcs.huaqiaobang.tljr.news.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.tljr.news.SortNewsActivity;
import com.abcs.huaqiaobang.tljr.news.adapter.CharacterParser;
import com.abcs.huaqiaobang.tljr.news.adapter.PinyinComparator;
import com.abcs.huaqiaobang.tljr.news.adapter.SortAdapter;
import com.abcs.huaqiaobang.tljr.news.bean.CountrySortModel;
import com.abcs.huaqiaobang.tljr.news.view.SideBar;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2015/12/26.
 */
public class SortNewsFragment extends Fragment {


    private Context context;
    private View view;
    private SwipeRefreshLayout refreshLayout;
    public ListView sortListView;
    //    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private CharacterParser characterParser;
    private List<CountrySortModel> SourceDateList;

    private Handler handler;
    private MyApplication application;
    private int index = -1;
    private boolean isFrist = true;
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;


    /*
     * 传入的新闻频道所需参数 -
     */
    public String nowTypeName = "";
    public String nowTypeSpecial = "";
    public String defaultPicture = "";


    public static SortNewsFragment getInstance() {

        SortNewsFragment f = new SortNewsFragment();

        Bundle b = new Bundle();

        f.setArguments(b);

        return f;
    }

    @Override
    public void onResume() {
        super.onResume();
      //  sortListView.setRefreshTime(Util.format10.format(new Date()));
        refreshLayout.setRefreshing(true);
        initData();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle args = getArguments();
        nowTypeName = args != null ? args.getString("nowTypeName") : "";
        nowTypeSpecial = args != null ? args.getString("nowTypeSpecial") : "";
        defaultPicture = args != null ? args.getString("defaultPicture") : "";
        super.onCreate(savedInstanceState);

        application = MyApplication.getInstance();

        context = getActivity();
        handler = new Handler();

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.fragment_news_sort, null);
        }
        SourceDateList = new ArrayList<>();
        initViews();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (index != -1 && HuanQiuShiShi.currentListData.size() != 0) {
//            HuanQiuShiShi.currentListData.clear();
//        }
    }

    private void initViews() {
        characterParser = CharacterParser.getInstance();

        sideBar = (SideBar) view.findViewById(R.id.sidrbar);
        dialog = (TextView) view.findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                if (adapter != null) {
                    int position = adapter.getPositionForSection(s.charAt(0));
                    if (position !=-1) {//listview头，
                        sortListView.setSelection(position);
                    }
                }

            }
        });
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.id_swipe_ly);
       sortListView = (ListView) view.findViewById(R.id.country_lvcountry);
      //  sortListView = (XListLiView) view.findViewById(R.id.country_lvcountry);
//        SimpleHeader header = new SimpleHeader(context);
//        header.setTextColor(0xffeb5041);
//        header.setCircleColor(0xffeb5041);
//        sortListView.setHeadable(header);

      //  sortListView.setPullLoadEnable(false);


        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;
                    index = position;
                    Intent intent = new Intent(context, SortNewsActivity.class);
                    intent.putExtra("sp", ((CountrySortModel) adapter.getItem(position)).getSpecies());
                    intent.putExtra("num", ((CountrySortModel) adapter.getItem(position)).getSubject());
                    intent.putExtra("country", ((CountrySortModel) adapter.getItem(position)).getCountry());
                    intent.putExtra("subject", ((CountrySortModel) adapter.getItem(position)).getSubject());
                    intent.putExtra("defaultPicture",defaultPicture);


                    context.startActivity(intent);
                }


            }
        });




        refreshLayout.setColorSchemeResources(android.R.color.holo_red_light);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                initData();

            }
        });





        initData();
    }

    public void initData() {
        String   url = TLUrl.getInstance().URL_new + "api/main/new";
        String pid = "0";// 默认无
//        if (SourceDateList != null) {
//            SourceDateList.clear();
//        }
        if (application.getInstance().self != null) {
            pid = application.self.getId();
        }
        if (application.checkNetWork()) {
            String params = "species=" + nowTypeSpecial + "&platform=2" + "&uid=" + pid;
            HttpRequest.sendPost(url, params, new HttpRevMsg() {
                @Override
                public void revMsg(final String msg) {

                    handler.post(new Runnable() {
                        @Override
                        public void run() {


                            JSONObject sortnews = null;
                            try {
                                sortnews = new JSONObject(msg);
                                if (sortnews != null) {
                                    getData(sortnews);
                                }
                                SourceDateList = filledData(SourceDateList);
                                Collections.sort(SourceDateList, new PinyinComparator());
                                adapter = new SortAdapter(context, SourceDateList);
                                sortListView.setAdapter(adapter);
                                refreshLayout.setRefreshing(false);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });

                }
            });
        } else {
            SourceDateList = filledData(SourceDateList);
            Collections.sort(SourceDateList, new PinyinComparator());
            adapter = new SortAdapter(context, SourceDateList);
            sortListView.setAdapter(adapter);
            refreshLayout.setRefreshing(false);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return view;
    }

    private void getData(JSONObject response) {

        if (!"1".equals(response.optString("status"))) {
            sideBar.setVisibility(View.INVISIBLE);
            return;
        }
        SourceDateList.clear();




//
        JSONArray news = null;
        try {
            JSONObject  obj = response.getJSONObject("joData");
            news = obj.getJSONArray("news");
            if (news.length() != 0) {

                for (int i = 0; i < news.length(); i++) {
                    JSONObject object = news.getJSONObject(i);

                    CountrySortModel countrySortModel = new CountrySortModel();

                    countrySortModel.setCountry(object.optString("title"));
                    countrySortModel.setDesc(object.optString("summary"));
                    countrySortModel.setDate(object.optString("time"));
                    countrySortModel.setSpecies(object.optString("species"));
                    countrySortModel.setSubject(object.optString("subject"));


                    //国家分类图标
                    String iconUrl = object.optString("subjectIconUrl");
                    if ("".equals(iconUrl)) {
                        countrySortModel.setCountry_imgurl(object.optString("purl"));
                    } else {
                        countrySortModel.setCountry_imgurl(iconUrl);
                    }
                    countrySortModel.setCount(object.optInt("count", 0));
                    SourceDateList.add(countrySortModel);
                    Log.i("sort", countrySortModel.getCount() + "");

                }

            } else {
                //错误处理

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private List<CountrySortModel> filledData(List<CountrySortModel> date) {
        List<CountrySortModel> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();

        for (int i = 0; i < date.size(); i++) {
            CountrySortModel sortModel = new CountrySortModel();
            sortModel.setCountry(date.get(i).getCountry());
            sortModel.setDesc(date.get(i).getDesc());
            sortModel.setSubject(date.get(i).getSubject());
            sortModel.setSpecies(date.get(i).getSpecies());
            sortModel.setDate(date.get(i).getDate());
            sortModel.setCount(date.get(i).getCount());
            sortModel.setCountry_imgurl(date.get(i).getCountry_imgurl());
            String pinyin = characterParser.getSelling(date.get(i).getCountry());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {

                //对重庆多音字做特殊处理
                if (pinyin.startsWith("zhongqing")) {
                    sortString = "C";
                    sortModel.setSortLetters("C");
                } else {
                    sortModel.setSortLetters(sortString.toUpperCase());
                }

                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            }

            mSortList.add(sortModel);
        }
        Collections.sort(indexString);
//        sideBar.setIndexText(indexString);
        return mSortList;

    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//
//        if (getUserVisibleHint() && MyApplication.getInstance().self != null && isFrist) {
//            isFrist = false;
//        }
//
//    }


}
