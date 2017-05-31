package com.abcs.haiwaigou.HDong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.sociax.android.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ReXiaoShiShiFragment extends BaseFragment {


    boolean first = false;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    
    @InjectView(R.id.listview)
    ListView listview;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private String mParam1;
    private String mParam2;

    public Handler handler = new Handler();
    private RequestQueue mRequestQueue;
    Activity activity;
    View view;


    public static ReXiaoShiShiFragment newInstance(String param1, String param2) {
        ReXiaoShiShiFragment fragment = new ReXiaoShiShiFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_re_xiao_shi_shi, container, false);
        ButterKnife.inject(this, view);

        activity=getActivity();
        mRequestQueue = Volley.newRequestQueue(activity);

        initAllDates();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },300);
                initAllDates();
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Goods goods = adapter.getItem(position);
                Intent intent = new Intent(activity, GoodsDetailActivity.class);
                intent.putExtra("sid", goods.getGoods_id());
                intent.putExtra("pic", goods.getPicarr());
                startActivity(intent);
            }
        });
        return view;
    }

    MygridGoodsAdapter adapter;
    private void initAllDates() {
        final ArrayList<Goods> dataList = new ArrayList<>();
        if (!first) {
            ProgressDlgUtil.showProgressDlg("", activity);
        }
        String param = null;
//        http://www.huaqiaobang.com/mobile/index.php?act=activity&op=get_activitty_goods&type=1&param=123
        param = TLUrl.getInstance().URL_hwg_base + "/mobile/index.php?act=activity&op=get_activitty_goods&type=" + mParam1+"&param="+mParam2;
        Log.i("zds", "hot_special_url=" + param);
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, param, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zds", "hot_special_msg=" + response);
                        JSONArray datas = response.getJSONArray("datas");
                        dataList.clear();
                        for (int i = 0; i < datas.length(); i++) {

                            JSONObject object = datas.getJSONObject(i);
                            JSONObject jsonObject1 = object.getJSONObject("goods");

                            JSONArray item = jsonObject1.getJSONArray("item");
                            for (int j = 0; j < item.length(); j++) {
                                Goods goods = new Goods();
                                JSONObject goodsObj = item.getJSONObject(j);
                                goods.setGoods_id(goodsObj.optString("goods_id"));
                                goods.setTitle(goodsObj.optString("goods_name"));
                                if (goodsObj.has("zhuangtai")) {
                                    goods.setXiangou(1);
                                }
                                goods.setMoney(goodsObj.optDouble("goods_promotion_price"));
                                goods.setPicarr(goodsObj.optString("goods_image"));
                                goods.setGoods_salenum(goodsObj.optString("goods_salenum"));
                                goods.setDescription(goodsObj.optString("goods_jingle"));
                                dataList.add(goods);
                            }
                        }
                        Log.i("zds", "hot_special_size=" + dataList.size());

                        adapter=new MygridGoodsAdapter(activity,dataList);
                        listview.setAdapter(adapter);

                    } else {
                        Log.i("zds", "goodsActivity解析失败");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zds", e.toString());
                    e.printStackTrace();

                } finally {
                    ProgressDlgUtil.stopProgressDlg();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressDlgUtil.stopProgressDlg();
            }
        });
        mRequestQueue.add(jr);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    protected void lazyLoad() {

    }

    public class MygridGoodsAdapter extends BaseAdapter {
        private ArrayList<Goods> goodsList;
        Activity activity;
        LayoutInflater inflater = null;
        public Handler handler = new Handler();


        public MygridGoodsAdapter(Activity activity, ArrayList<Goods> goodsList) {

            this.activity = activity;
            this.goodsList = goodsList;
            inflater = LayoutInflater.from(activity);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder mHolder = null;
            final Goods goods = getItem(position);
            if (convertView == null) {
                LayoutInflater mInflater = LayoutInflater.from(activity);
                convertView = mInflater.inflate(R.layout.item_hwg_hdong_rexiao, null);
                mHolder = new ViewHolder(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }

            if(!TextUtils.isEmpty(goods.getPicarr())){
                MyApplication.imageLoader.displayImage(goods.getPicarr(), mHolder.iv_logo, MyApplication.getListOptions());
            }
            if(!TextUtils.isEmpty(goods.getTitle())){
                mHolder.tv_title.setText(goods.getTitle());
            }
            if(!TextUtils.isEmpty(goods.getDescription())){
                mHolder.tv_dec.setText(goods.getDescription());
            }
            if(!TextUtils.isEmpty(goods.getGoods_salenum())){
                mHolder.tv_scale_num.setText("已售"+goods.getGoods_salenum()+"件");
            }
            if(goods.getMoney()>0){
                mHolder.tv_price.setText("¥ "+goods.getMoney());
            }

            mHolder.tv_pai_min.setText((position+1)+"");

            if(position<=2){
                mHolder.re_paiming.setBackgroundResource(R.drawable.iv_hwg_rexiao_item_pai);
            }else {
                mHolder.re_paiming.setBackgroundResource(R.drawable.iv_hwg_rexiao_pai2);
            }
            return convertView;

        }


        private class ViewHolder {
            private ImageView iv_logo;
            private TextView tv_pai_min; // 排名
            RelativeLayout re_paiming;
            private TextView tv_title,tv_dec,tv_price;
            private TextView tv_scale_num;

            public ViewHolder(View itemView) {
                iv_logo=(ImageView) itemView.findViewById(R.id.iv_logo);
                tv_pai_min=(TextView) itemView.findViewById(R.id.tv_pai_min);
                re_paiming=(RelativeLayout) itemView.findViewById(R.id.re_paiming);
                tv_title=(TextView) itemView.findViewById(R.id.tv_title);
                tv_dec=(TextView) itemView.findViewById(R.id.tv_dec);
                tv_price=(TextView) itemView.findViewById(R.id.tv_price);
                tv_scale_num=(TextView) itemView.findViewById(R.id.tv_scale_num);
            }
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return goodsList == null ? 0 : goodsList.size();
        }


        @Override
        public Goods getItem(int position) {
            if (goodsList != null && goodsList.size() != 0) {
                if (position >= goodsList.size()) {
                    return goodsList.get(0);
                }
                return goodsList.get(position);
            }
            return null;
        }


        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
    }
}
