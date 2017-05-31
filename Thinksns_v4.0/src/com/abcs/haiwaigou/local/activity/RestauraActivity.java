package com.abcs.haiwaigou.local.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.abcs.haiwaigou.local.adapter.RestaurenAdapter;
import com.abcs.haiwaigou.local.adapter.RestaurenNoQuAdapter;
import com.abcs.haiwaigou.local.beans.AARease;
import com.abcs.haiwaigou.local.beans.HireFindNoQu;
import com.abcs.haiwaigou.local.beans.NewHireFind;
import com.abcs.haiwaigou.local.beans.RestauDetisl;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import java.util.ArrayList;
import java.util.List;

public class RestauraActivity extends BaseFragmentActivity implements View.OnClickListener,RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    RelativeLayout relativeBack;
    TextView tTitleName;

    View line;
    String title,menuId;
    ListView listview;
    EasyRecyclerView rvList;

    private int pageNo=1;
    private boolean isRefresh = false;

    RestaurenAdapter mAdapter;
    RestaurenNoQuAdapter mAdapterNoQu;
    View view;

    private Handler handler = new Handler();

    private  NewHireFind.MsgBean mNewHire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_activity_resturant);

         relativeBack=(RelativeLayout) findViewById(R.id.relative_back);
        tTitleName=(TextView) findViewById(R.id.t_title_name);
        line=(View) findViewById(R.id.line);
        listview=(ListView) findViewById(R.id.listview);
        rvList=(EasyRecyclerView) findViewById(R.id.listview_datas);

        initListener();

        title = getIntent().getStringExtra("title");
        menuId = getIntent().getStringExtra("menuId");
        tTitleName.setText(title+"");

        mNewHire=(NewHireFind.MsgBean)getIntent().getSerializableExtra("cate_hire");

        if(mNewHire!=null){
            if(mNewHire.areas!=null&&mNewHire.areas.size()>0){

                if(mNewHire.areas.size()==1){
                    listview.setVisibility(View.GONE);
                }else {
                    listview.setVisibility(View.VISIBLE);
                }

                adapter=new MyListAdapter(this,mNewHire.areas);
                listview.setAdapter(adapter);

                adapter.setSelectedPosition(0);
                adapter.notifyDataSetChanged();

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        if(!TextUtils.isEmpty(mNewHire.areas.get(i).categoryId)){
                            mAdapter.clear();
                            cate=mNewHire.areas.get(i).categoryId;
                            loading(mNewHire.areas.get(i).categoryId);
                        }

                        adapter.setSelectedPosition(i);
                        adapter.notifyDataSetChanged();

                    }
                });


                loading(mNewHire.areas.get(0).categoryId+"");

                rvList.setLayoutManager(new LinearLayoutManager(this));
                rvList.setRefreshListener(this);
                rvList.setAdapter(mAdapter = new RestaurenAdapter(this));
                mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        RestauDetisl.MsgBean datasBean=(RestauDetisl.MsgBean)mAdapter.getAllData().get(position);

                        Intent intent = new Intent(RestauraActivity.this, HireAndFindDetailActivity.class);
                        intent.putExtra("conId", datasBean.id+"");
                        intent.putExtra("typeName", "");
                        intent.putExtra("isform_fen", true);
                        startActivity(intent);
                    }
                });

//        adapter.setNoMore(R.layout.view_nomore);
//        adapter.setMore(R.layout.view_more, this);
                mAdapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAdapter.resumeMore();
                    }
                });
            }else {
                listview.setVisibility(View.GONE);

                rvList.setLayoutManager(new LinearLayoutManager(this));
                rvList.setRefreshListener(this);
                rvList.setAdapter(mAdapterNoQu = new RestaurenNoQuAdapter(this));
                mAdapterNoQu.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        HireFindNoQu.MsgBean datasBean=(HireFindNoQu.MsgBean)mAdapterNoQu.getAllData().get(position);

                        Intent intent = new Intent(RestauraActivity.this, HireAndFindDetailActivity.class);
                        intent.putExtra("conId", datasBean.id+"");
                        intent.putExtra("typeName", "");
                        intent.putExtra("isform_fen", true);
                        startActivity(intent);
                    }
                });

                mAdapterNoQu.setNoMore(R.layout.view_nomore);
                mAdapterNoQu.setMore(R.layout.view_more, this);
                mAdapterNoQu.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAdapterNoQu.resumeMore();
                    }
                });

                loadingWithoutQu(mNewHire.qyerCityId,menuId);

            }
        }else {

            showToast("暂无数据！");
        }

    }

    String cate=null;
    private void loadingWithoutQu(int qyer_city_id,String menuId){

        HttpRequest.sendGet(TLUrl.getInstance().URL_local_get_menuList, "cityId=" + qyer_city_id + "&page=" + pageNo + "&pageSize=10" + "&menuId=" + menuId, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (isRefresh) {
                            mAdapterNoQu.clear();
                            isRefresh = false;
                        }

                        Log.i("zds_noqu", "hire_msg=" + msg);

                        if(!TextUtils.isEmpty(msg)){

                            HireFindNoQu bean=new Gson().fromJson(msg, HireFindNoQu.class);

                                if(bean.status.equals("1")){
                                    if(bean.msg!=null){

                                        if(bean.msg.size()>0){
                                            mAdapterNoQu.addAll(bean.msg);
                                        }else {
                                            showToast("已加载全部数据");
                                            pageNo=-1;
                                            mAdapterNoQu.stopMore();
                                            return;
                                        }
                                    }
                                        pageNo =pageNo+1;
                                }else {
                                    showToast("请求失败！请重试！");}
                        }else {
                            showToast("请求失败！请重试！");
                        }
                    }
                });
            }
        });
    }

    private int page=1;
    private void loading(String category_id) {

//        https://japi.tuling.me/hrq/menu/getRestaurants?categoryId=88101
        HttpRequest.sendGet(TLUrl.getInstance().URL_local_rest_fenqu_detisal, "categoryId=" + category_id+"&page=" + page , new HttpRevMsg() {
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

                            RestauDetisl data= new Gson().fromJson(msg, RestauDetisl.class);

                            if(!TextUtils.isEmpty(data.status)){
                                if(data.status.equals("1")){
                                    if( data.msg!=null){

                                        if( data.msg.size()>0){
                                            mAdapter.addAll(data.msg);
                                        }else {
                                            showToast("暂无数据！");
                                        }
                                    }
                                }
                            }
                        }else {
                            showToast("请求失败！请重试！");
                        }
                    }
                });
            }
        });
    }
    private MyListAdapter adapter;
    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onRefresh() {

        page = 1;
        pageNo = 1;
        isRefresh = true;
        if(mNewHire.areas.size()>0){
            if(!TextUtils.isEmpty(mNewHire.areas.get(0).categoryId)){
                loading(mNewHire.areas.get(0).categoryId);
                adapter.setSelectedPosition(0);
            }
        }else {
            loadingWithoutQu(mNewHire.qyerCityId,menuId);
        }
    }


    @Override
    public void onLoadMore() {
        if (pageNo == -1) {
            mAdapterNoQu.stopMore();
            return;
        }
        loadingWithoutQu(mNewHire.qyerCityId,menuId);

        page=page+1;
        if(!TextUtils.isEmpty(cate)){
            loading(cate);
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public class MyListAdapter extends BaseAdapter {

        Context context;
        public MyListAdapter(Context context,List<AARease> list) {
            this.context = context;
            this.list = list;
        }

        List<AARease> list=new ArrayList<>();



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

            AARease bean=(AARease) getItem(i);

            vieeHolder.tv.setText(bean.name+"");

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
