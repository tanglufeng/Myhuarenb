package com.abcs.haiwaigou.local.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import com.abcs.haiwaigou.local.model.MoreNewsInfo;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.ytbt.common.utils.ToastUtil;
import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.unit.UnitSociax;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import java.util.ArrayList;
import java.util.List;

public class OthersActivity extends BaseFragmentActivity implements View.OnClickListener,PullToRefreshBase.OnRefreshListener2<ListView> {

    RelativeLayout relativeBack;
    View line;
    TextView tTitleName;
    String title,menuId,cityId;
    PullToRefreshListView pullToRefreshListView;
    ListView listview;
    EasyRecyclerView rvList;
    private boolean isRefresh = false;
    RestaurenAdapter mAdapter;
    View view;

    private MyListAdapter adapter;

    private static final String TAG = "OthersActivity";
    
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        Log.i(TAG, "onPullDownToRefresh: ");
        page=1;
        isRefresh=true;
        loading();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        Log.i(TAG, "onPullUpToRefresh: ");
        page=page+1;
        loading();
    }

    public class RestaurenAdapter extends RecyclerArrayAdapter<MoreNewsInfo.MsgEntry.DateEntry>{
        public RestaurenAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
            return new RestaurenHoder(parent);
        }

        public class RestaurenHoder extends BaseViewHolder<MoreNewsInfo.MsgEntry.DateEntry>{
            private TextView tv_title;
            private TextView tv_address;
            private TextView tv_phone;


            public RestaurenHoder(ViewGroup parent) {
                super(parent, R.layout.item_local_restdetials);

                tv_title=(TextView) itemView.findViewById(R.id.tv_title);
                tv_address=(TextView) itemView.findViewById(R.id.tv_address);
                tv_phone=(TextView) itemView.findViewById(R.id.tv_phone);

            }

            @Override
            public void setData(MoreNewsInfo.MsgEntry.DateEntry data) {

                if(data!=null){
                    if(!TextUtils.isEmpty(data.title)){
                        tv_title.setText(data.title);
                    }else {
                        tv_title.setText("未知");
                    }
                    if(!TextUtils.isEmpty(data.ads)){
                        tv_address.setText("地址:"+data.ads);
                    }else {
                        tv_address.setText("地址:未知");
                    }
                    if(!TextUtils.isEmpty(data.contact)){
                        tv_phone.setText("电话:"+data.contact);
                    }else {
                        tv_phone.setText("电话:未知");
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_activity_resturant_tongxun);

         relativeBack=(RelativeLayout) findViewById(R.id.relative_back);
        tTitleName=(TextView) findViewById(R.id.t_title_name);
        line=(View) findViewById(R.id.line);
        pullToRefreshListView=(com.handmark.pulltorefresh.library.PullToRefreshListView) findViewById(R.id.listview);
        rvList=(EasyRecyclerView) findViewById(R.id.listview_datas);

        pullToRefreshListView.setOnRefreshListener(this);

        //设置列表样式
        listview = pullToRefreshListView.getRefreshableView();
        listview.setDividerHeight(UnitSociax.dip2px(this, 0.5f));
        listview.setDivider(new ColorDrawable(getResources().getColor(R.color.bg_listview_divider)));

        initListener();
        title = getIntent().getStringExtra("title");
        menuId = getIntent().getStringExtra("menuId");
        cityId = getIntent().getStringExtra("cityId");
        tTitleName.setText(title+"");

        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(mAdapter = new RestaurenAdapter(this));
        mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        MoreNewsInfo.MsgEntry.DateEntry datasBean=(MoreNewsInfo.MsgEntry.DateEntry)mAdapter.getAllData().get(position);
                        Intent intent = new Intent(OthersActivity.this, HireAndFindDetailActivity.class);
                        intent.putExtra("conId", datasBean.id+"");
                        intent.putExtra("typeName", datasBean.typeName);
                        intent.putExtra("isform_fen", true);
                        startActivity(intent);
                    }
                });

        mAdapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.resumeMore();
            }
        });

        adapter=new MyListAdapter(this);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.i(TAG, "onItemClick: position====="+i);

                MoreNewsInfo.MsgEntry itemsEntity = (MoreNewsInfo.MsgEntry)adapterView.getItemAtPosition(i);

                if(itemsEntity!=null){
                    if(itemsEntity.date!=null){
                        mAdapter.clear();
                        if(itemsEntity.date.size()>0){
                            Log.i("zds", "onItemClick: itemsEntity.date.size()"+itemsEntity.date.size());
                            mAdapter.addAll(itemsEntity.date);
                        }else {
                            ToastUtil.showMessage("暂无数据！");
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }

                adapter.setSelectedPosition(i-1);
                adapter.notifyDataSetChanged();

            }
        });

        loading();
    }

    int page=1;
    private boolean isFirest=true;
    private void loading() {

        ProgressDlgUtil.showProgressDlg("",this);
//        http://newapi.tuling.me/japi/hrq/conListDetail/getConListByConType.json?menuId=1010&cityId=41&version=v2.0&page=1&pageSize=15
        HttpRequest.sendGet(TLUrl.getInstance().URL_local_get_menuList, "menuId=" + menuId+"&cityId=" + cityId+"&version=v2.0"+"&page=" + page+"&pageSize=15", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zds", "msg=" + msg);
                        if (isRefresh) {
                            adapter.clearAllDatas();
                            mAdapter.clear();
                        }
                        if(!TextUtils.isEmpty(msg)){

                            MoreNewsInfo data= new Gson().fromJson(msg, MoreNewsInfo.class);

                            if(data!=null){
                                if(data.status==1){
                                    if(data.msg!=null){
                                        if(data.msg.size()>0){
                                            line.setVisibility(View.VISIBLE);
                                            adapter.addListData(data.msg);
                                            if(isFirest){
                                                MoreNewsInfo.MsgEntry itemsEntity = (MoreNewsInfo.MsgEntry)adapter.getItem(0);
                                                if(itemsEntity!=null){
                                                    if(itemsEntity.date!=null){
                                                        if(itemsEntity.date.size()>0){
                                                            mAdapter.clear();
                                                            mAdapter.addAll(itemsEntity.date);
                                                        }else {
                                                            ToastUtil.showMessage("暂无数据！");
                                                        }
                                                        mAdapter.notifyDataSetChanged();
                                                    }
                                                }

                                                adapter.setSelectedPosition(0);
                                                adapter.notifyDataSetChanged();
                                                isFirest=false;
                                            }

                                            if(isRefresh){
                                                MoreNewsInfo.MsgEntry itemsEntity = (MoreNewsInfo.MsgEntry)adapter.getItem(0);
                                                if(itemsEntity!=null){
                                                    if(itemsEntity.date!=null){
                                                        if(itemsEntity.date.size()>0){
                                                            mAdapter.clear();
                                                            mAdapter.addAll(itemsEntity.date);
                                                        }else {
                                                            ToastUtil.showMessage("暂无数据！");
                                                        }
                                                        mAdapter.notifyDataSetChanged();
                                                    }
                                                }

                                                adapter.setSelectedPosition(0);
                                                adapter.notifyDataSetChanged();
                                                isRefresh = false;
                                            }
                                        }else {
                                            ToastUtil.showMessage("已加载全部数据！");
                                        }
                                    }
                                }
                            }

                        }else {
                            showToast("请求失败！请重试！");
                        }

                        ProgressDlgUtil.stopProgressDlg();
                        pullToRefreshListView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pullToRefreshListView.onRefreshComplete();
                            }
                        },1000);
                    }
                });
            }
        });
    }

    private void initListener() {
        relativeBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_back:
                finish();
                break;
        }
    }

    public class MyListAdapter extends BaseAdapter {

        Context context;
        public MyListAdapter(Context context) {
            this.context = context;
        }

        List<MoreNewsInfo.MsgEntry> list=new ArrayList<>();

        private void addListData(List<MoreNewsInfo.MsgEntry> datas){
            if(datas!=null&&datas.size()>0){
                list.addAll(datas);
                notifyDataSetChanged();
            }
        }


        private void clearAllDatas(){
            if(list!=null&&list.size()>0){
                list.clear();
                notifyDataSetChanged();
            }
        }
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

            VieeHolder vieeHolder=null;
            if(view==null){
                view= LayoutInflater.from(context).inflate(R.layout.item_text,viewGroup,false);
                vieeHolder=new VieeHolder(view);
                view.setTag(vieeHolder);
            }else {
                vieeHolder=(VieeHolder) view.getTag();
            }

            MoreNewsInfo.MsgEntry bean=(MoreNewsInfo.MsgEntry) getItem(i);

            vieeHolder.tv.setText(bean.groupTime);

            if (selectedPosition == i) {
                vieeHolder.rela_bg.setSelected(true);
                vieeHolder.rela_bg.setPressed(true);
                vieeHolder.rela_bg.setBackgroundColor(getResources().getColor(R.color.hwg_bg));
            } else {
                vieeHolder.rela_bg.setSelected(false);
                vieeHolder.rela_bg.setPressed(false);
                vieeHolder.rela_bg.setBackgroundColor(Color.TRANSPARENT);

            }

            return view;


        }
       public class VieeHolder{
           TextView tv;
           RelativeLayout rela_bg;

           public VieeHolder(View view) {

               tv=(TextView) view.findViewById(R.id.tv);
               rela_bg=(RelativeLayout) view.findViewById(R.id.rela_bg);
           }
       }
        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }
        private int selectedPosition = -1;// 选中的位置
    }
}
