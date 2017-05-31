package com.abcs.haiwaigou.HDong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.activity.HWGGoodsFenLeiActivity;
import com.abcs.haiwaigou.activity.HotActivity;
import com.abcs.haiwaigou.activity.HotSpecialActivity;
import com.abcs.haiwaigou.activity.LinkActivity;
import com.abcs.haiwaigou.model.HDong2;
import com.abcs.haiwaigou.model.HDong2Item;
import com.abcs.hqbtravel.wedgt.MyListView;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.dialog.ShowMessageDialog;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.ytbt.common.utils.LogUtil;
import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.android.ActivityHome;
import com.abcs.sociax.t4.android.video.ToastUtils;
import com.google.gson.Gson;
import com.thinksns.sociax.thinksnsbase.activity.widget.EmptyLayout;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.abcs.sociax.t4.android.video.ToastUtils.showToast;

public class HWGHuoDongFragment3 extends Fragment  {

    @InjectView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @InjectView(R.id.rv_hdong)
    ListView rvHuoDong;
    @InjectView(R.id.error_layout)
    EmptyLayout empty_layout;

    ActivityHome activity;
    View view;

    HdongAdapterL adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (ActivityHome) getActivity();
        view = activity.getLayoutInflater().inflate(R.layout.fragemt_h_dong2, null);
        ButterKnife.inject(this, view);

        intRe();
    }

    private void intRe(){

        rvHuoDong.setVisibility(View.INVISIBLE);
        empty_layout.setNoDataContent(getResources().getString(R.string.empty_content));
        empty_layout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                empty_layout.setErrorType(EmptyLayout.NETWORK_LOADING);
                getDates();
            }
        });

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDates();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh.setRefreshing(false);
                    }
                },1000);
            }
        });
        getDates();
    }

    private void initVouncher() {
        ProgressDlgUtil.showProgressDlg("Loading...",activity);
        HttpRequest.sendPost(TLUrl.getInstance().URL_first_get_vouncher+MyApplication.getInstance().getMykey(),null , new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject mainObj=new JSONObject(msg);
                            if(mainObj.optString("code").equals("200")){
                                JSONArray array=mainObj.optJSONArray("datas");
                                if(array!=null){
                                    JSONObject object=array.getJSONObject(0);
                                    String title=object.optString("voucher_title");
                                    String desc=object.optString("voucher_desc");
                                    String limit=object.optString("voucher_limit");
                                    String prcie=object.optString("voucher_price");
                                    String endTime= Util.format1.format(object.optLong("voucher_end_date")*1000);
                                    String string="["+title+"]，"+"满"+limit+"减"+prcie+"，有效期至"+endTime;
                                    new ShowMessageDialog(view,activity,Util.WIDTH * 4 / 5,string,"优惠券领取成功");
                                }else {
                                    JSONObject error=mainObj.optJSONObject("datas");
                                    showToast(activity,error.optString("error"));
                                }
                            }else {
                                showToast(activity,"领取失败！");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            ProgressDlgUtil.stopProgressDlg();
                        }
                    }
                });
            }
        });
    }

    boolean isFirst = true;
    Handler handler=new Handler();
    private void getDates() {

        if(isFirst){
            ProgressDlgUtil.showProgressDlg("",activity);
        }

//        http://newapi.tuling.me/huaqiaobang/mobile/index.php?act=activity&op=index
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base+"/mobile/index.php", "act=activity&op=index", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if(isFirst){
                            ProgressDlgUtil.stopProgressDlg();
                            isFirst=false;
                        }
                            LogUtil.e("huodong", "msg==" + msg);
                            if (!TextUtils.isEmpty(msg)) {
                                rvHuoDong.setVisibility(View.VISIBLE);
                                empty_layout.setErrorType(EmptyLayout.HIDE_LAYOUT);

                                final HDong2 bean = new Gson().fromJson(msg, HDong2.class);

                                if(bean!=null&&bean.state==1){
                                    if(bean.datas!=null){
                                        if(bean.datas.size()>0){

                                            adapter = new HdongAdapterL(activity,bean.datas);
                                            rvHuoDong.setAdapter(adapter);

                                        }else {
                                            rvHuoDong.setVisibility(View.GONE);
                                            empty_layout.setErrorType(EmptyLayout.NODATA);
                                        }
                                    }
                                }
                            }else {
                                rvHuoDong.setVisibility(View.GONE);
                                empty_layout.setErrorType(EmptyLayout.NETWORK_ERROR);
                            }
                    }
                });
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup p = (ViewGroup) view.getParent();

        if (p != null)
            p.removeAllViewsInLayout();
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public class HdongAdapterL extends BaseAdapter {

        public static final String TYPE_KEYWORD = "1";//商品关键字
        public static final String TYPE_SPECIAL = "2";//商品专题
        public static final String TYPE_LINK = "3";//链接
        public static final String TYPE_OTHERS = "4";//其他
        public static final String TYPE_GOODS = "5";//商品id

        private Activity mcontext;
        private LayoutInflater inflater;
        private List<HDong2.DatasEntry> cityList;

        public HdongAdapterL(Activity mcontext, List<HDong2.DatasEntry> cityList) {
            this.mcontext = mcontext;
            this.cityList = cityList;
            inflater = LayoutInflater.from(mcontext);
        }

        @Override
        public int getCount() {
            return cityList.size();
        }

        @Override
        public HDong2.DatasEntry getItem(int position) {
            return cityList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
           ViewHolder holder= null;
            if (convertView == null){
                convertView = View.inflate(mcontext,R.layout.item_hdong_iten_l,null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            if (!TextUtils.isEmpty(cityList.get(position).title)){
                holder.tv.setText(cityList.get(position).title);
            }

            if (cityList.get(position).list != null && cityList.get(position).list.size()>0){
                holder.cityListView.setAdapter(new HDongItemSAdapter(mcontext,cityList.get(position).list));
            }

            holder.cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HDong2Item datasEntry=(HDong2Item)parent.getItemAtPosition(position);
                    if(datasEntry!=null){
                        Intent intent = null;
                        switch (datasEntry.type){
                            case TYPE_SPECIAL:

                                Log.e("zdsww",datasEntry.other+"");

                                if(datasEntry.other.equals("1")){
                                    intent = new Intent(activity, HWGGoodsFenLeiActivity.class);
                                    intent.putExtra("special_id", datasEntry.manyId);
                                }else {
                                    intent = new Intent(activity, HotSpecialActivity.class);
                                    intent.putExtra("special_id", datasEntry.depict);
                                }

                                intent.putExtra("words", datasEntry.descRibe);
                                intent.putExtra("text_position", position);
                                intent.putExtra("isWeek", true);
                                intent.putExtra("picture", datasEntry.image);
                                intent.putExtra("title", datasEntry.title);
                                activity.startActivity(intent);
                                break;
                            case TYPE_KEYWORD:
                                intent = new Intent(activity, HotActivity.class);
                                intent.putExtra("words", datasEntry.descRibe);
                                intent.putExtra("keyword", datasEntry.depict);
                                intent.putExtra("isWeek", true);
                                intent.putExtra("text_position", position);
                                intent.putExtra("title", datasEntry.title);
                                intent.putExtra("picture", datasEntry.image);
                                activity.startActivity(intent);
                                break;
                            case TYPE_GOODS:
                                intent = new Intent(activity, GoodsDetailActivity.class);
                                intent.putExtra("sid", datasEntry.depict);
                                intent.putExtra("pic", datasEntry.image);
                                activity.startActivity(intent);
                                break;
                            case TYPE_LINK:
                                intent = new Intent(activity, LinkActivity.class);
                                intent.putExtra("words", datasEntry.descRibe);
                                intent.putExtra("keyword", datasEntry.depict);
                                intent.putExtra("title", datasEntry.title);
                                intent.putExtra("picture", datasEntry.image);
                                activity.startActivity(intent);
                                break;
                            case TYPE_OTHERS:
                                if(MyApplication.getInstance().self!=null){
                                    initVouncher();
                                }
                                break;
                            default:
                                ToastUtils.showToast(activity,"敬请期待");
                                break;
                        }
                    }
                }
            });

            return convertView;
        }

        public class  ViewHolder{
            private TextView tv;  //title
            private MyListView cityListView;  // item
            public ViewHolder(View view) {
                tv=(TextView) view.findViewById(R.id.tv);
                cityListView= (MyListView) view.findViewById(R.id.item_lv_area);
            }

        }
    }


    /**
     * 里层listView的adapter  item
     */
    public class HDongItemSAdapter extends BaseAdapter {

        private List<HDong2Item> countrys;
        private Activity mcontext;

        public HDongItemSAdapter(Activity mcontext, List<HDong2Item> countrys) {
            this.mcontext = mcontext;
            this.countrys = countrys;
        }

        @Override
        public int getCount() {
            return countrys.size();
        }

        @Override
        public HDong2Item getItem(int position) {
            return countrys.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null){
                convertView = View.inflate(mcontext, R.layout.item_hwg_pic,null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (!TextUtils.isEmpty(countrys.get(position).image)){
                MyApplication.imageLoader.displayImage(countrys.get(position).image,holder.img_logo, MyApplication.getCircleImageOptions());
            }

            return convertView;
        }

        public class ViewHolder{
            private ImageView img_logo;
            private LinearLayout liner;
            LinearLayout.LayoutParams layoutParams ;


            public ViewHolder(View itemView){
                liner=(LinearLayout) itemView.findViewById(R.id.liner);
                img_logo=(ImageView) itemView.findViewById(R.id.img_logo);
                layoutParams=(LinearLayout.LayoutParams)liner.getLayoutParams();
                layoutParams.width = Util.WIDTH;
                layoutParams.height = Util.WIDTH*33/75;
                liner.setLayoutParams(layoutParams);
            }
        }
    }
}