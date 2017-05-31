//package com.abcs.haiwaigou.HDong;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v4.app.Fragment;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.LinearLayoutManager;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.abcs.haiwaigou.activity.GoodsDetailActivity;
//import com.abcs.haiwaigou.activity.HWGGoodsFenLeiActivity;
//import com.abcs.haiwaigou.activity.HotActivity;
//import com.abcs.haiwaigou.activity.HotSpecialActivity;
//import com.abcs.haiwaigou.activity.LinkActivity;
//import com.abcs.haiwaigou.adapter.HDong2Adapter;
//import com.abcs.haiwaigou.model.HDong2;
//import com.abcs.huaqiaobang.MyApplication;
//import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
//import com.abcs.huaqiaobang.dialog.ShowMessageDialog;
//import com.abcs.huaqiaobang.util.HttpRequest;
//import com.abcs.huaqiaobang.util.HttpRevMsg;
//import com.abcs.huaqiaobang.util.Util;
//import com.abcs.huaqiaobang.ytbt.common.utils.LogUtil;
//import com.abcs.sociax.android.R;
//import com.abcs.sociax.t4.android.ActivityHome;
//import com.abcs.sociax.t4.android.video.ToastUtils;
//import com.google.gson.Gson;
//import com.jude.easyrecyclerview.EasyRecyclerView;
//import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
//import com.thinksns.sociax.thinksnsbase.activity.widget.EmptyLayout;
//import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//
//import static com.abcs.sociax.t4.android.video.ToastUtils.showToast;
//
//public class HWGHuoDongFragment2 extends Fragment implements SwipeRefreshLayout.OnRefreshListener  {
//
//    public static final String FLAG_BANNER = "1";//广告banner
//    public static final String FLAG_UPDATEWEEK = "2";//每周上新
//    public static final String FLAG_RECOMMEND = "3";//热门推荐
//    public static final String FLAG_BRAND = "4";//品牌推荐
//    public static final String FLAG_HOTTODAY = "5";//今日最热
//    public static final String FLAG_SALETODAY = "6";//今日特卖
//    public static final String FLAG_ACTIVITY = "8";//活动
//    public static final String TYPE_KEYWORD = "1";//商品关键字
//    public static final String TYPE_SPECIAL = "2";//商品专题
//    public static final String TYPE_LINK = "3";//链接
//    public static final String TYPE_OTHERS = "4";//其他
//    public static final String TYPE_GOODS = "5";//商品id
//
//
//    @InjectView(R.id.rv_hdong)
//    EasyRecyclerView rvHuoDong;
//    @InjectView(R.id.error_layout)
//    EmptyLayout empty_layout;
//    ActivityHome activity;
//    View view;
//    HDong2Adapter adapter;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        activity = (ActivityHome) getActivity();
//        view = activity.getLayoutInflater().inflate(R.layout.fragemt_h_dong, null);
//        ButterKnife.inject(this, view);
//
//        intRe();
//    }
//
//    View top_view;
//
//    private void intRe(){
//
//        empty_layout.setNoDataContent(getResources().getString(R.string.empty_content));
//        empty_layout.setOnLayoutClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                empty_layout.setErrorType(EmptyLayout.NETWORK_LOADING);
//                getDates();
//            }
//        });
//
//        rvHuoDong.setLayoutManager(new LinearLayoutManager(activity));
//        rvHuoDong.setRefreshListener(this);
//        adapter = new HDong2Adapter(activity);
//        top_view = View.inflate(activity, R.layout.item_hdong_text_top, null);
//
//        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
//            @Override
//            public View onCreateView(ViewGroup parent) {
//                return top_view;
//            }
//
//            @Override
//            public void onBindView(View headerView) {
//
//            }
//        });
//
//        rvHuoDong.setAdapter(adapter);
//        adapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                adapter.resumeMore();
//            }
//        });
//
//        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                Intent intent = null;
//                HDong2.DatasEntry datasEntry=adapter.getAllData().get(position);
//
//                switch (datasEntry.type){
//                    case TYPE_SPECIAL:
//
//                        Log.e("zdsww",datasEntry.other+"");
//
//                        if(datasEntry.other.equals("1")){
//                            intent = new Intent(activity, HWGGoodsFenLeiActivity.class);
//                            intent.putExtra("special_id", datasEntry.manyId);
//                        }else {
//                            intent = new Intent(activity, HotSpecialActivity.class);
//                            intent.putExtra("special_id", datasEntry.depict);
//                        }
//
//                        intent.putExtra("words", datasEntry.descRibe);
//                        intent.putExtra("text_position", position);
//                        intent.putExtra("isWeek", true);
//                        intent.putExtra("picture", datasEntry.image);
//                        intent.putExtra("title", datasEntry.title);
//                        activity.startActivity(intent);
//                        break;
//                    case TYPE_KEYWORD:
//                        intent = new Intent(activity, HotActivity.class);
//                        intent.putExtra("words", datasEntry.descRibe);
//                        intent.putExtra("keyword", datasEntry.depict);
//                        intent.putExtra("isWeek", true);
//                        intent.putExtra("text_position", position);
//                        intent.putExtra("title", datasEntry.title);
//                        intent.putExtra("picture", datasEntry.image);
//                        activity.startActivity(intent);
//                        break;
//                    case TYPE_GOODS:
//                        intent = new Intent(activity, GoodsDetailActivity.class);
//                        intent.putExtra("sid", datasEntry.depict);
//                        intent.putExtra("pic", datasEntry.image);
//                        activity.startActivity(intent);
//                        break;
//                    case TYPE_LINK:
//                        intent = new Intent(activity, LinkActivity.class);
//                        intent.putExtra("words", datasEntry.descRibe);
//                        intent.putExtra("keyword", datasEntry.depict);
//                        intent.putExtra("title", datasEntry.title);
//                        intent.putExtra("picture", datasEntry.image);
//                        activity.startActivity(intent);
//                        break;
//                    case TYPE_OTHERS:
//                        if(MyApplication.getInstance().self!=null){
//                            initVouncher();
//                        }
//                        break;
//                    default:
//                        ToastUtils.showToast(activity,"敬请期待");
//                        break;
//                }
//            }
//        });
//        getDates();
//    }
//
//    private void initVouncher() {
//        ProgressDlgUtil.showProgressDlg("Loading...",activity);
//        HttpRequest.sendPost(TLUrl.getInstance().URL_first_get_vouncher+MyApplication.getInstance().getMykey(),null , new HttpRevMsg() {
//            @Override
//            public void revMsg(final String msg) {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            JSONObject mainObj=new JSONObject(msg);
//                            if(mainObj.optString("code").equals("200")){
//                                JSONArray array=mainObj.optJSONArray("datas");
//                                if(array!=null){
//                                    JSONObject object=array.getJSONObject(0);
//                                    String title=object.optString("voucher_title");
//                                    String desc=object.optString("voucher_desc");
//                                    String limit=object.optString("voucher_limit");
//                                    String prcie=object.optString("voucher_price");
//                                    String endTime= Util.format1.format(object.optLong("voucher_end_date")*1000);
//                                    String string="["+title+"]，"+"满"+limit+"减"+prcie+"，有效期至"+endTime;
//                                    new ShowMessageDialog(view,activity,Util.WIDTH * 4 / 5,string,"优惠券领取成功");
//                                }else {
//                                    JSONObject error=mainObj.optJSONObject("datas");
//                                    showToast(activity,error.optString("error"));
//                                }
//                            }else {
//                                showToast(activity,"领取失败！");
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }finally {
//                            ProgressDlgUtil.stopProgressDlg();
//                        }
//                    }
//                });
//            }
//        });
//    }
//
//    private boolean isRefresh = false;
//    boolean isFirst = true;
//    Handler handler=new Handler();
//    private void getDates() {
//
//        if(isFirst){
//            ProgressDlgUtil.showProgressDlg("",activity);
//        }
//
////        http://www.huaqiaobang.com/mobile/index.php?act=activity&op=index
//        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base+"/mobile/index.php", "act=activity&op=index", new HttpRevMsg() {
//            @Override
//            public void revMsg(final String msg) {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                            handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                rvHuoDong.setRefreshing(false);
//                                adapter.stopMore();
//                            }
//                        },300);
//
//                        if(isFirst){
//                            ProgressDlgUtil.stopProgressDlg();
//                            isFirst=false;
//                        }
//                            LogUtil.e("huodong", "msg==" + msg);
//                            if (!TextUtils.isEmpty(msg)) {
//                                rvHuoDong.setVisibility(View.VISIBLE);
//                                empty_layout.setErrorType(EmptyLayout.HIDE_LAYOUT);
//                                if (isRefresh) {
//                                    adapter.clear();
//                                    isRefresh = false;
//                                }
//
//                                final HDong2 bean = new Gson().fromJson(msg, HDong2.class);
//
//                                if(bean!=null&&bean.state==1){
//                                    if(bean.datas!=null){
//                                        if(bean.datas.size()>0){
//
//                                            adapter.addAll(bean.datas);
//                                            adapter.notifyDataSetChanged();
//
//                                        }else {
//                                        }
//                                    }
//                                }
//                            }else {
//                                rvHuoDong.setVisibility(View.GONE);
//                                empty_layout.setErrorType(EmptyLayout.NETWORK_ERROR);
//                            }
//                    }
//                });
//            }
//        });
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//        ViewGroup p = (ViewGroup) view.getParent();
//
//        if (p != null)
//            p.removeAllViewsInLayout();
//        ButterKnife.inject(this, view);
//        return view;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.reset(this);
//    }
//
//    @Override
//    public void onRefresh() {
//        isRefresh = true;
//        getDates();
//    }
//}