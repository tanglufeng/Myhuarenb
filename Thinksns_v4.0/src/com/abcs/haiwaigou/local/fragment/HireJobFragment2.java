package com.abcs.haiwaigou.local.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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

import com.abcs.haiwaigou.local.activity.HireAndFindActivity2;
import com.abcs.haiwaigou.local.activity.HireAndFindDetailActivity;
import com.abcs.haiwaigou.local.model.NewHire;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zds
 */
public class HireJobFragment2 extends BaseFragment implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
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

    HireAndFindActivity2 activity;
    @InjectView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @InjectView(R.id.listview)
    ListView listview;

    private boolean isRefresh=false;
    private View view;
    String menuId, oLd_menuId;
    String cityId;
    String typeName;
    MyHeirAdapter adapter;


    public static HireJobFragment2 newInstance(String cityId, String menuId, String typeName, String oLd_menuId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("cityId", cityId);
        bundle.putSerializable("menuId", menuId);
        bundle.putSerializable("oLd_menuId", oLd_menuId);
        bundle.putSerializable("typeName", typeName);
        HireJobFragment2 hireJobFragment = new HireJobFragment2();
        hireJobFragment.setArguments(bundle);
        return hireJobFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HireAndFindActivity2) getActivity();
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.hwg_yyg_fragment_goods3, null);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeView(view);
        ButterKnife.inject(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            cityId = bundle.getString("cityId");
            menuId = bundle.getString("menuId");
            oLd_menuId = bundle.getString("oLd_menuId");
            typeName = bundle.getString("typeName");
        }

        isRefresh = true;
        HireAndFindActivity2.fragmentHashMap.put(typeName, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setRefreshListener(this);
        myListAdapter=new MyListAdapter(activity);
        recyclerView.setAdapter(adapter = new MyHeirAdapter(activity));

        adapter.setNoMore(R.layout.view_nomore);
        adapter.setMore(R.layout.view_more, this);
        adapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.resumeMore();
            }
        });

        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    int page=1;
    Handler handler=new Handler();
    MyListAdapter myListAdapter;
    @Override
    protected void lazyLoad() {
//        https://japi.tuling.me/hrq/conListDetail/getConListByConType.json?menuId=8010&cityId=41&version=v2.0&page=1&pageSize=20
        HttpRequest.sendGet(TLUrl.getInstance().URL_local_get_menuList, "cityId=" + cityId + "&page=" + page + "&pageSize=20" + "&menuId=" + menuId, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zds", "msg=" + msg);

                        if(!TextUtils.isEmpty(msg)){

//                            ProgressDlgUtil.stopProgressDlg();

                            if(isRefresh){
                                adapter.clear();
                            }

                            NewHire data= new Gson().fromJson(msg, NewHire.class);

                            if(data!=null){
                                if(data.status==1){
                                    if(data.msg!=null){
                                        if(data.msg.size()>0){
                                            adapter.addAll(data.msg);
                                            if (isRefresh) {
                                                NewHire.MsgEntry itemsEntity = adapter.getAllData().get(0);
                                                if(itemsEntity!=null){
                                                    if(itemsEntity.date!=null&&itemsEntity.date.size()>0){
                                                        myListAdapter.addAll(itemsEntity.date);
                                                        adapter.setSelectedPosition(0);
                                                    }
                                                }
                                                isRefresh = false;
                                            }

                                            adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(int position) {
                                                NewHire.MsgEntry itemsEntity = adapter.getAllData().get(position);

                                                    if(itemsEntity!=null){
                                                        if(itemsEntity.date!=null&&itemsEntity.date.size()>0){
                                                            myListAdapter.addAll(itemsEntity.date);
                                                            adapter.setSelectedPosition(position);
                                                        }
                                                    }
                                                }
                                            });

                                            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                                    NewHire.MsgEntry.DateEntry datasBean=(NewHire.MsgEntry.DateEntry)myListAdapter.getItem(i);
                                                    Intent intent = null;
                                                    intent = new Intent(activity, HireAndFindDetailActivity.class);
                                                    intent.putExtra("conId", datasBean.id);
                                                    intent.putExtra("typeName", typeName);
                                                    intent.putExtra("isform_fen", true);
                                                    activity.startActivity(intent);
                                                }
                                            });
                                        }else {
                                            adapter.stopMore();
                                            showToast(activity,"已加载全部数据！");
                                        }
                                    }
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

    public class MyHeirAdapter extends RecyclerArrayAdapter<NewHire.MsgEntry>{
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

        public class MyHeirHoder extends BaseViewHolder<NewHire.MsgEntry> {

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
            public void setData(NewHire.MsgEntry bean) {
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
        List<NewHire.MsgEntry.DateEntry> list=new ArrayList<>();
        public MyListAdapter(Context context) {
            this.context = context;
        }

        private void addAll(List<NewHire.MsgEntry.DateEntry> dateEntryList){
            if(dateEntryList!=null&&dateEntryList.size()>0){
                list.clear();
                list.addAll(dateEntryList);
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
                view= LayoutInflater.from(context).inflate(R.layout.local_item_hire_find_job,viewGroup,false);
                vieeHolder=new VieeHolder(view);
                view.setTag(vieeHolder);
            }else {
                vieeHolder=(VieeHolder) view.getTag();
            }

            NewHire.MsgEntry.DateEntry data=(NewHire.MsgEntry.DateEntry) getItem(i);

            if(data!=null){
                if(!TextUtils.isEmpty(data.title)){
                    vieeHolder.t_title.setText(data.title);
                    if(Util.containsString(data.title.substring(0, 1))){
                        vieeHolder.t_text.setText(data.title.substring(1, 2));
                    }else {
                        vieeHolder.t_text.setText(data.title.substring(0, 1));
                    }
                }else {
                    vieeHolder.t_title.setText("未知");
                }
                if(data.pubTime==0){
                    vieeHolder.t_time.setVisibility(View.GONE);
                }else {
                    vieeHolder.t_time.setVisibility(View.VISIBLE);
                    if(data.pubTime<2*1000000000){
                        vieeHolder.t_time.setText(Util.formatzjz3.format(data.pubTime * 1000));
                    }else {
                        vieeHolder.t_time.setText(Util.formatzjz3.format(data.pubTime));
                    }
                }

                if(TextUtils.isEmpty(data.imgUrl)){
                    if(!TextUtils.isEmpty(oLd_menuId)){
                        if(oLd_menuId.equals(MyString.LOCAL_MENU1)){
                            vieeHolder.img_icon.setImageResource(R.drawable.img_local_gongzuo);
                        }else if(oLd_menuId.equals(MyString.LOCAL_MENU1)){
                            vieeHolder.img_icon.setImageResource(R.drawable.img_local_gongzuo);
                        }else if(oLd_menuId.equals(MyString.LOCAL_MENU3)){
                            vieeHolder.img_icon.setImageResource(R.drawable.img_local_fangwu);
                        }else if(oLd_menuId.equals(MyString.LOCAL_MENU4)){
                            vieeHolder.img_icon.setImageResource(R.drawable.img_local_genduo);
                        }else if(oLd_menuId.equals(MyString.LOCAL_MENU2)){
                            vieeHolder.img_icon.setImageResource(R.drawable.img_local_shengyi);
                        }else {
                            vieeHolder.img_icon.setImageResource(R.drawable.img_local_huangye);
                        }
                    }
                    vieeHolder.t_text.setVisibility(View.VISIBLE);
                }else {
                    vieeHolder.t_text.setVisibility(View.GONE);
                    ImageLoader.getInstance().displayImage(data.imgUrl, vieeHolder.img_icon, Options.getListOptions());
                }
            }

            return view;
        }
        public class VieeHolder{
            public TextView t_title;
            public TextView t_time;
            public TextView t_text;
            public ImageView img_icon;

            public VieeHolder(View view) {
                t_title= (TextView) view.findViewById(R.id.t_title);
                t_time= (TextView) view.findViewById(R.id.t_time);
                t_text= (TextView) view.findViewById(R.id.t_text);
                img_icon= (ImageView) view.findViewById(R.id.img_icon);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
