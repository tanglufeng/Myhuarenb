package com.abcs.haiwaigou.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.local.adapter.HWGFenLeisAdapter;
import com.abcs.haiwaigou.local.beans.HWGFenLei;
import com.abcs.haiwaigou.local.beans.PinLeiRight;
import com.abcs.haiwaigou.utils.ACache;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.ServerUtils;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.ytbt.common.utils.ToastUtil;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class HWGFenLeiFragment extends BaseFragment implements RecyclerArrayAdapter.OnLoadMoreListener,SwipeRefreshLayout.OnRefreshListener{
    @Override
    protected void lazyLoad() {

    }
    Activity activity;
    String objectName,plate_id;
    ListView listview;
    EasyRecyclerView rvList;
    private int pageNo=1;
    HWGFenLeisAdapter mAdapter;
    private View view;
    boolean isRefresh = false;
    int picWith;
    int picHeight;
    int picHeight2;

Handler handler=new Handler();

    private void getLeftDatas(){
        //        http://www.huaqiaobang.com/mobile/index.php?act=goods_class&op=get_goods_class

        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base+"/mobile/index.php", "act=goods_class&op=get_goods_class" , new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(!TextUtils.isEmpty(msg)){
                            initLMyDatas(msg);
                        }else {
                            showToast(activity,"请求失败！请重试！");
                        }
                    }
                });
            }
        });
    }


    private String category_id;
    private void loading() {

//       http://www.huaqiaobang.com/mobile/index.php?act=goods_class&op=get_goods_class&id=1104
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base+"/mobile/index.php", "act=goods_class&op=get_goods_class&id="+category_id , new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zds", "msg=" + msg);
                        if (isRefresh) {
                            mAdapter.clear();
                            isRefresh = false;
                        }

                        if(!TextUtils.isEmpty(msg)){

                            PinLeiRight data= new Gson().fromJson(msg, PinLeiRight.class);

                            if(data!=null){
                                if(data.state==1){
                                    if( data.datas!=null){

                                        if(data.datas.size()>0){
                                            mAdapter.addAll(data.datas);
                                            mAdapter.notifyDataSetChanged();
                                        }else {
//                                            pageNo=-1;
                                            mAdapter.stopMore();
                                            return;
                                        }
//                                        pageNo = pageNo+1;
                                        mAdapter.notifyDataSetChanged();
                                    }

                                }else if(data.state==-1){
                                    mAdapter.stopMore();
                                }
                            }
                        }else {
                            showToast(activity,"请求失败！请重试！");
                        }
                    }
                });
            }
        });
    }
    private MyListAdapter adapter;


    @Override
    public void onRefresh() {

//        pageNo = 1;
        isRefresh = true;
        if(ServerUtils.isConnect(activity)){
            getLeftDatas();
        }else {
            ToastUtil.showMessage("请检查您的网络！");
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rvList.setRefreshing(false);
            }
        }, 1000);
    }


    @Override
    public void onLoadMore() {
//        loading();
        mAdapter.stopMore();
    }


    public class MyListAdapter extends BaseAdapter {

        Context context;
        public MyListAdapter(Context context,List<HWGFenLei.DatasBean> list) {
            this.context = context;
            this.list = list;
        }

        List<HWGFenLei.DatasBean> list=new ArrayList<>();



        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            MyListAdapter.VieeHolder vieeHolder=null;
            if(view==null){
                view= LayoutInflater.from(context).inflate(R.layout.item_hwgfenlei,viewGroup,false);
                vieeHolder=new MyListAdapter.VieeHolder(view);
                view.setTag(vieeHolder);
            }else {
                vieeHolder=(MyListAdapter.VieeHolder) view.getTag();
            }

            HWGFenLei.DatasBean bean=(HWGFenLei.DatasBean) getItem(i);

            vieeHolder.tv.setText(bean.className+"");

            if (selectedPosition == i) {
                vieeHolder.rela_bg.setSelected(true);
                vieeHolder.rela_bg.setPressed(true);
                vieeHolder.rela_bg.setBackgroundColor(getResources().getColor(R.color.left_item_bg));
                vieeHolder.line_bg.setBackground(getResources().getDrawable(R.drawable.img_left_g));
            } else {
                vieeHolder.rela_bg.setSelected(false);
                vieeHolder.rela_bg.setPressed(false);
                vieeHolder.rela_bg.setBackgroundColor(getResources().getColor(R.color.left_item_bg_no));
                vieeHolder.line_bg.setBackground(getResources().getDrawable(R.drawable.transparent));
            }

            return view;


        }
        public class VieeHolder{
            TextView tv;
            TextView line_bg;
            RelativeLayout rela_bg;

            public VieeHolder(View view) {

                tv=(TextView) view.findViewById(R.id.tv);
                line_bg=(TextView) view.findViewById(R.id.line_bg);
                rela_bg=(RelativeLayout) view.findViewById(R.id.rela_bg);
            }
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        private int selectedPosition = -1;// 选中的位置

    }


    public static HWGFenLeiFragment newInstance(String plateId, String objectName) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("plate_id", plateId);
        bundle.putSerializable("objectName", objectName);
        HWGFenLeiFragment fragment = new HWGFenLeiFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    ACache aCache;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        activity = getActivity();
        aCache = ACache.get(activity);
        if (view == null) {
            view = activity.getLayoutInflater().inflate(
                    R.layout.local_activity_wanle, null);
//            ButterKnife.inject(this, view);

            Bundle bundle = getArguments();
            if (bundle != null) {
                plate_id = bundle.getString("plate_id");
                objectName = bundle.getString("objectName");
            }
            picWith = Util.WIDTH;
            picHeight = picWith * 33 / 72;
            picHeight2 = picWith * 39 / 72;

            listview=(ListView)  view.findViewById(R.id.listview);
            rvList=(EasyRecyclerView)  view.findViewById(R.id.listview_datas);
            initListener();
            if(ServerUtils.isConnect(activity)){
                getLeftDatas();
            }else {
                ToastUtil.showMessage("请检查您的网络！");
            }
        }
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null) {
            p.removeView(view);
        }
        return view;
    }

    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;


    private void initLMyDatas(String msg){

        HWGFenLei data= new Gson().fromJson(msg, HWGFenLei.class);

        if(data!=null){
            if(data.state==1){
                if(data.datas!=null){

                    if( data.datas.size()>0){
                        adapter=new MyListAdapter(activity,data.datas);
                        listview.setAdapter(adapter);

                        HWGFenLei.DatasBean rr=(HWGFenLei.DatasBean)adapter.getItem(0);
                        if(rr!=null){
                            if(!TextUtils.isEmpty(rr.id)){
                                category_id=rr.id;
                                 loading();
                            }
                        }

                        adapter.setSelectedPosition(0);
                        adapter.notifyDataSetChanged();

                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                long currentTime = Calendar.getInstance().getTimeInMillis();
                                if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                                    lastClickTime = currentTime;
                                    //////////////
                                    mAdapter.clear();
                                    HWGFenLei.DatasBean datasBean=(HWGFenLei.DatasBean)adapter.getItem(i);

                                    if(!TextUtils.isEmpty(datasBean.id)){

                                        Log.i("zds","datasBean.gcId=="+datasBean.id);
                                        category_id=datasBean.id;
                                        pageNo = 1;
                                        isRefresh = true;
                                        loading();
                                        adapter.setSelectedPosition(i);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                                Log.i("zds", "onItemClick: 太快了");
                            }
                        });

                    }else {
                        showToast(activity,"暂无数据！");
                    }
                }
            }
        }
    }

    private void initListener() {

        rvList.setLayoutManager(new LinearLayoutManager(activity));
        rvList.setRefreshListener(this);
        rvList.setAdapter(mAdapter = new HWGFenLeisAdapter(activity));
//        mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//            }
//        });

        mAdapter.setNoMore(R.layout.view_nomore);
        mAdapter.setMore(R.layout.view_more, this);
        mAdapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.resumeMore();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
