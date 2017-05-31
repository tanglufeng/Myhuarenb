package com.abcs.haiwaigou.local.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.haiwaigou.local.evenbus.Pic;
import com.abcs.haiwaigou.model.BannersBean;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.hqbtravel.wedgt.ACache;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.GuanggaoActivity;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.sociax.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LocalGuangGaoFragment extends BaseFragment {

    @InjectView(R.id.grid_gg)
    GridView grid_gg;
    @InjectView(R.id.tv_null)
    TextView tv_null;

    private Activity activity;
    ACache aCache;
    private View view;
    private GridVAdapter adapter;
    Handler mhandle = new Handler();
    List<BannersBean> gg1=new ArrayList<>();

    public static LocalGuangGaoFragment newInstance(ArrayList<BannersBean> gg1) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("gg1", gg1);
        LocalGuangGaoFragment countryFragment = new LocalGuangGaoFragment();
        countryFragment.setArguments(bundle);

        return countryFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.item_local_gg, null);
        }
        activity = getActivity();
        aCache = ACache.get(activity);
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeView(view);

        ButterKnife.inject(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            gg1 = bundle.getParcelableArrayList("gg1");
        }

//        initDatas(cityId);
        initPic(gg1);

        return view;
    }

    private void initPic( List<BannersBean> gg1){
        Log.i("zds", "initPic: gg1"+gg1.size());
        if(gg1.size()>0){

            grid_gg.setVisibility(View.VISIBLE);
            tv_null.setVisibility(View.GONE);


            grid_gg.setAdapter(adapter=new GridVAdapter(activity,gg1));
            adapter.notifyDataSetChanged();

            grid_gg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    final BannersBean bean=(BannersBean) adapterView.getItemAtPosition(i);
                    Intent intent = new Intent(activity, GuanggaoActivity.class);
                    intent.putExtra("url", bean.newsUrl);
                    startActivity(intent);
                }
            });
     
        }else {
            grid_gg.setVisibility(View.GONE);
           tv_null.setVisibility(View.VISIBLE);
        }
    }

/*    private void initDatas(final String city){
        if(!TextUtils.isEmpty(city)){
            //        http://www.huaqiaobang.com/mobile/index.php?act=vip_td&op=find_vip_td&city_id=55&type=2
            HttpRequest.sendGet(TLUrl.getInstance().getInstance().URL_travel_tedian,"act=vip_td&op=find_vip_td&city_id="+city+"&type=2",new HttpRevMsg() {
                @Override
                public void revMsg(final String msg) {

                    mhandle.post(new Runnable() {
                        @Override
                        public void run() {
                            if (msg == null) {
                                return ;
                            }

                            Log.i("zds", "tedian=" + msg);
                            Log.i("zds", "tedian  url=" + TLUrl.getInstance().getInstance().URL_travel_tedian+"?"+"act=vip_td&op=find_vip_td&city_id="+city+"&type=2");

                            final HuiYuanTeDian tedian=new Gson().fromJson(msg, HuiYuanTeDian.class);

                            if(tedian!=null){

                                if(tedian.state==1){

                                    if(tedian.vipTdList!=null&&tedian.vipTdList.size()>0){
                                        linner_tedian.setVisibility(View.VISIBLE);
                                        tv_null.setVisibility(View.GONE);


                                        grid_gg.setAdapter(tedianAdapter=new GridVAdapter(activity,tedian.vipTdList));
                                        tedianAdapter.notifyDataSetChanged();

                                        grid_gg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                                final BannersBean bean=(BannersBean) adapterView.getItemAtPosition(i);



                                                if(Integer.valueOf(bean.jumpId)>0){   //专题
                                                    Intent intent= new Intent(activity, GaoErFuActivity.class);
                                                    intent.putExtra("words",bean.tdDesc+"");
                                                    intent.putExtra("title",bean.tdEnName+bean.tdCnName);   //"Golf Trip\n"+"您的高尔夫记忆"
                                                    intent.putExtra("text_position", 0);
                                                    intent.putExtra("picture", bean.tdDescImg+"");
                                                    intent.putExtra("special_id",bean.jumpId+"");
                                                    intent.putExtra("city_id",bean.cityId+"");
                                                    intent.putExtra("isMain",false);
                                                    intent.putExtra("isSale",false);
                                                    intent.putExtra("isWeek",false);
                                                    startActivity(intent);
                                                }else {  //商品详情
                                                    Intent it=new Intent(activity,GoodsDetailActivity.class);
                                                    it.putExtra("sid",bean.goodsId);
                                                    it.putExtra("pic", bean.img);
                                                    it.putExtra("isYun",false);
                                                    startActivity(it);
                                                }
                                            }
                                        });
                                    }
                                }else {
                                    linner_tedian.setVisibility(View.GONE);
                                    tv_null.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });
                }
            });
        }
    }*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChange(final Pic cityChange){
        Log.d("zds", "initguanggao:"+cityChange.city);
        initPic(MyApplication.guanggao);
//        initDatas(cityChange.city);
    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public class GridVAdapter  extends BaseAdapter {
        private Context mcontext;
        private LayoutInflater inflater;
        private List<BannersBean> data;

        public GridVAdapter(Context mcontext, List<BannersBean> data) {
            this.mcontext = mcontext;
            inflater = LayoutInflater.from(mcontext);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

           ViewHolder holder=null;
            if(convertView==null){
                convertView=inflater.inflate(R.layout.item_addserver_gridview,parent,false);
                holder=new ViewHolder(convertView);
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder) convertView.getTag();
            }


            BannersBean bean= data.get(position);


            if(bean!=null&& !TextUtils.isEmpty(bean.img)){
                ImageLoader.getInstance().displayImage(bean.img,holder.img, Options.getListOptions());
            }

            return convertView;
        }

        public class  ViewHolder{
            private ImageView img;
            public ViewHolder(View view) {
                img=(ImageView) view.findViewById(R.id.img_logo);
            }
        }
    }

}
