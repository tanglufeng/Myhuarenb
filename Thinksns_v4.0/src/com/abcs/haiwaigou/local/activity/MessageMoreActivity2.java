package com.abcs.haiwaigou.local.activity;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.local.model.MoreNewsInfo;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.ytbt.common.utils.ToastUtil;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MessageMoreActivity2 extends BaseActivity implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener  {

    @Override
    public void onRefresh() {
        page = 1;
        isRefresh = true;
        lazyLoad();
    }

    @Override
    public void onLoadMore() {
        page=page+1;
        lazyLoad();
    }

    @InjectView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @InjectView(R.id.listview)
    ListView listview;

    private boolean isRefresh=false;
    String cityId;
    String typeName;
    MyHeirAdapter adapter;
    
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.hwg_yyg_fragment_goods3, null);
        }
        setContentView(view);
        ButterKnife.inject(this);

        cityId=getIntent().getStringExtra("cityId");
        isRefresh = true;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setRefreshListener(this);
        recyclerView.setAdapter(adapter = new MyHeirAdapter(this));

        adapter.setNoMore(R.layout.view_nomore);
        adapter.setMore(R.layout.view_more, this);
        adapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.resumeMore();
            }
        });

        lazyLoad();
    }
    @Override
    public void onPause() {
        super.onPause();
    }

    int page=1;
    Handler handler=new Handler();
    MyListAdapter myListAdapter;
    
    protected void lazyLoad() {
        ProgressDlgUtil.showProgressDlg("",this);
//        http://newapi.tuling.me/japi/hrq/conListDetail/newestInfo.json?page=1&pageSize=15&cityId=41&version=v1.0
        HttpRequest.sendGet(TLUrl.getInstance().URL_local_get_more_msg, "cityId=" + cityId + "&page=" + page + "&pageSize=20" + "&version=v1.0", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zds", "msg=" + msg);

                        if(!TextUtils.isEmpty(msg)){

                            if(isRefresh){
                                adapter.clear();
                            }

                            MoreNewsInfo data= new Gson().fromJson(msg, MoreNewsInfo.class);

                            if(data!=null){
                                if(data.status==1){
                                    if(data.msg!=null){
                                        if(data.msg.size()>0){
                                            adapter.addAll(data.msg);
                                            if (isRefresh) {
                                                MoreNewsInfo.MsgEntry itemsEntity = adapter.getAllData().get(0);
                                                if(itemsEntity!=null){
                                                    if(itemsEntity.date!=null&&itemsEntity.date.size()>0){
                                                        myListAdapter=new MyListAdapter(MessageMoreActivity2.this,itemsEntity.date);
                                                        listview.setAdapter(myListAdapter);
                                                        adapter.setSelectedPosition(0);
                                                    }
                                                }
                                                isRefresh = false;
                                            }

                                            adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(int position) {
                                                    Log.i("zds", "onItemClick: position"+position);

                                                    adapter.setSelectedPosition(position);

                                                    MoreNewsInfo.MsgEntry itemsEntity = adapter.getAllData().get(position);

                                                    if(itemsEntity!=null){
                                                        if(itemsEntity.date!=null){
                                                            if(itemsEntity.date.size()>0){
                                                                Log.i("zds", "onItemClick: itemsEntity.date.size()"+itemsEntity.date.size());
                                                                if(myListAdapter!=null){
                                                                    myListAdapter.addAll(itemsEntity.date);
                                                                    adapter.setSelectedPosition(position);
                                                                    recyclerView.scrollToPosition(position);
                                                                }
                                                            }else {
                                                                if(myListAdapter!=null){
                                                                    myListAdapter.clearAll();
                                                                }

                                                                ToastUtil.showMessage("暂无数据！");
                                                            }
                                                        }
                                                    }
                                                }
                                            });

                                            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                                    MoreNewsInfo.MsgEntry.DateEntry datasBean=(MoreNewsInfo.MsgEntry.DateEntry)adapterView.getItemAtPosition(i);
                                                    Intent intent = new Intent(MessageMoreActivity2.this, HireAndFindDetailActivity.class);
                                                    intent.putExtra("conId", datasBean.id+"");
                                                    intent.putExtra("typeName", typeName);
                                                    intent.putExtra("isform_fen", true);
                                                    startActivity(intent);
                                                }
                                            });


                                        }else {
                                            adapter.stopMore();
                                            ToastUtil.showMessage("已加载全部数据！");
                                        }
                                    }
                                }
                            }

                        }else {
                            ToastUtil.showMessage("请求失败！请重试！");
                        }

                        ProgressDlgUtil.stopProgressDlg();
                    }
                });
            }
        });

    }

    public class MyHeirAdapter extends RecyclerArrayAdapter<MoreNewsInfo.MsgEntry>{
        public void setSelectedPosition(int position) {
            selectedPosition = position;
            notifyDataSetChanged();
        }

        private int selectedPosition = -1;// 选中的位置

        public MyHeirAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyHeirHoder(parent);
        }

        public class MyHeirHoder extends BaseViewHolder<MoreNewsInfo.MsgEntry> {

            TextView tv;
            TextView line_bg;
            RelativeLayout rela_bg;

            public MyHeirHoder(ViewGroup parent) {
                super(parent, R.layout.item_hwgfenlei);
                tv=(TextView) itemView.findViewById(R.id.tv);
                line_bg=(TextView) itemView.findViewById(R.id.line_bg);
                rela_bg=(RelativeLayout) itemView.findViewById(R.id.rela_bg);
            }

            @Override
            public void setData(MoreNewsInfo.MsgEntry bean) {
                if(bean!=null){
                    if(!TextUtils.isEmpty(bean.groupTime)){
                        tv.setText(bean.groupTime);
                    }
                }

                if (selectedPosition == getAdapterPosition()) {
                    rela_bg.setSelected(true);
                    rela_bg.setPressed(true);
                    rela_bg.setBackgroundColor(getResources().getColor(R.color.left_item_bg));
                    line_bg.setBackground(getResources().getDrawable(R.drawable.img_left_g));
                } else {
                    rela_bg.setSelected(false);
                    rela_bg.setPressed(false);
                    rela_bg.setBackgroundColor(getResources().getColor(R.color.left_item_bg_no));
                    line_bg.setBackground(getResources().getDrawable(R.drawable.transparent));
                }
            }
        }
    }

    public class MyListAdapter extends BaseAdapter {

        Context context;
        List<MoreNewsInfo.MsgEntry.DateEntry> list=new ArrayList<>();
        public MyListAdapter(Context context,List<MoreNewsInfo.MsgEntry.DateEntry> list) {
            this.context = context;
            this.list = list;
        }

        private void addAll(List<MoreNewsInfo.MsgEntry.DateEntry> dateEntryList){
            if(dateEntryList!=null&&dateEntryList.size()>0){
                list.clear();
                list.addAll(dateEntryList);
                notifyDataSetChanged();
            }
        }
        private void clearAll(){
            if(list.size()>0){
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

            VieeHolder holder=null;
            if(view==null){
                view= LayoutInflater.from(context).inflate(R.layout.local_item_msg_more,null);
                holder=new VieeHolder(view);
                view.setTag(holder);
            }else {
                holder=(VieeHolder) view.getTag();
            }

            MoreNewsInfo.MsgEntry.DateEntry msg=(MoreNewsInfo.MsgEntry.DateEntry) getItem(i);

            if(msg!=null){

                Log.i("zds",msg.title+"");
                holder.t_title.setText(msg.title);
                holder.t_address.setText(msg.country + " " + msg.cityName);

                if (msg.createTime < 2 * 1000000000) {
                    holder.t_time.setText(Util.format1.format(msg.createTime*1000));
                } else {
                    holder.t_time.setText(Util.format1.format(msg.createTime));
                }
                holder.t_tips.setVisibility(TextUtils.isEmpty(msg.typeName) ? View.GONE : View.VISIBLE);
                holder.t_tips.setText(msg.typeName);

            }

            return view;
        }
        public class VieeHolder{
            public RelativeLayout relative_root;
            public ImageView img_icon;
            public ImageView img_location;
            public TextView t_title;
            public TextView t_time;
            public TextView t_address;
            public TextView t_tips;
            public TextView t_text;

            public VieeHolder(View itemView) {
                relative_root = (RelativeLayout) itemView.findViewById(R.id.relative_root);
                img_icon = (ImageView) itemView.findViewById(R.id.img_icon);
                img_location = (ImageView) itemView.findViewById(R.id.img_location);
                t_title = (TextView) itemView.findViewById(R.id.t_title);
                t_time = (TextView) itemView.findViewById(R.id.t_time);
                t_address = (TextView) itemView.findViewById(R.id.t_address);
                t_tips = (TextView) itemView.findViewById(R.id.t_tips);
                t_text = (TextView) itemView.findViewById(R.id.t_text);
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }
}
