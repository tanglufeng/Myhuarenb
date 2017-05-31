package com.abcs.haiwaigou.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.adapter.DeliverAdapter;
import com.abcs.haiwaigou.fragment.customtool.FullyLinearLayoutManager;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.haiwaigou.yyg.view.ReadMoreTextView;
import com.abcs.sociax.android.R;

import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/6/8 0008.
 */
public class Package1Fragment extends BaseFragment {

    Activity activity;
    @InjectView(R.id.t_deliver_company)
    TextView tDeliverCompany;
    @InjectView(R.id.linear_company)
    LinearLayout linearCompany;
    @InjectView(R.id.t_deliver_code)
    TextView tDeliverCode;
    @InjectView(R.id.linear_code)
    LinearLayout linearCode;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.t_goods_info)
    ReadMoreTextView tGoodsInfo;
    @InjectView(R.id.card_info)
    CardView cardInfo;
    private View view;
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;


    private ArrayList<String> msg;
    private String message;
    private String express_name;
    private String shipping_code;
    private String goods_info;
    DeliverAdapter deliverAdapter;

    public static Package1Fragment newInstance(String express_name, String shipping_code, String msg, String goods_info) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("msg", msg);
        bundle.putSerializable("express_name", express_name);
        bundle.putSerializable("shipping_code", shipping_code);
        bundle.putSerializable("goods_info", goods_info);
        Package1Fragment fragment = new Package1Fragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        activity = getActivity();

        if (view == null) {
            view = activity.getLayoutInflater().inflate(
                    R.layout.hwg_fragment_my_package, null);
            ButterKnife.inject(this, view);
            Bundle bundle = getArguments();
            if (bundle != null) {
                message = bundle.getString("msg");
                express_name = bundle.getString("express_name");
                shipping_code = bundle.getString("shipping_code");
                goods_info = bundle.getString("goods_info");
                Log.i("zjz","goods_info="+goods_info);
            }
            isPrepared = true;
            lazyLoad();
//            initView();
        }
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeView(view);


        return view;
//        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
//        ViewGroup parent = (ViewGroup)mFragmentView.getParent();
//        if(parent != null) {
//            parent.removeView(mFragmentView);
//        }
//        return mFragmentView;
    }

    private void initView() {
        recyclerView.setLayoutManager(new FullyLinearLayoutManager(activity));
    }

    FullyLinearLayoutManager fullyLinearLayoutManager;

    private void initRecycler() {
        if (tDeliverCode != null)
            tDeliverCode.setText(shipping_code);
        if (tDeliverCompany != null)
            tDeliverCompany.setText(express_name);
        if (tGoodsInfo != null&&goods_info!=null) {
            String[] strings = goods_info.split("---");
            Log.i("zjz", "goods_info_size=" + strings.length);
            StringBuffer buffer = new StringBuffer();
            for (int j = 0; j < strings.length; j++) {
                if (!strings[j].equals("")) {
//                    if (strings[j].length() > 25) {
//                        buffer.append((j + 1) + "." + strings[j].substring(0, 25) + "...");
//                    } else {
                        buffer.append("["+(j + 1) +"]"+ "." + strings[j]);
//                    }
                }
                if(j!=strings.length-1){
                    buffer.append("\n");
                }
            }
            Log.i("zjz", "goods_info_ch=" + buffer.toString());
            tGoodsInfo.setText(buffer.toString());
        }
        fullyLinearLayoutManager = new FullyLinearLayoutManager(activity);
        deliverAdapter = new DeliverAdapter();
        recyclerView.setFocusable(false);
        recyclerView.setLayoutManager(fullyLinearLayoutManager);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            private float startX, startY, offsetX, offsetY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;
                        if (offsetX < -5) {
                            System.out.println("left");
                        } else if (offsetX > 5) {
                            System.out.println("right");
                        }
                        if (offsetY < -1) {
                            //up
                            cardInfo.setVisibility(View.GONE);
                        } else if (offsetY > 1) {
                            //down
                            cardInfo.setVisibility(View.VISIBLE);
                        }
                        break;
                }
                return false;
            }
        });
//        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                int lastVisibleItem = fullyLinearLayoutManager.findLastVisibleItemPosition();
//                int totalItemCount = fullyLinearLayoutManager.getItemCount();
//                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
//                // dy>0 表示向下滑动
//                if (fullyLinearLayoutManager.findFirstVisibleItemPosition() == 0) {
//                    cardInfo.setVisibility(View.VISIBLE);
//                } else {
//                    cardInfo.setVisibility(View.GONE);
//                }
//            }
//        });
        Log.i("zjz","message="+message);
        String[] info = message.split("##");
        for (int i = 0; i < info.length; i++) {
            Goods goods = new Goods();
            Log.i("zjz", "msg" + i + info[i]);
            String[] split = info[i].split("&nbsp;&nbsp;");
            if (split.length == 2) {
                goods.setTitle(split[0]);
                goods.setSubhead(split[1]);
            }
            deliverAdapter.getDatas().add(goods);
            deliverAdapter.notifyDataSetChanged();
        }
//        try {
//            Log.i("zjz", "msg1=" + msg);
//            JSONObject object = new JSONObject(msg);
//            JSONArray jsonArray = object.getJSONArray("deliver_info");
//            for (int i = 0; i < jsonArray.length(); i++) {
//                Goods goods = new Goods();
//                Log.i("zjz", "title=" + jsonArray.get(i).toString());
//                String[] split = jsonArray.get(i).toString().split("&nbsp;&nbsp;");
//                if (split.length == 2) {
//                    goods.setTitle(split[0]);
//                    goods.setSubhead(split[1]);
//                }
//                deliverAdapter.getDatas().add(goods);
//            }
//            Log.i("zjz", "size=" + deliverAdapter.getDatas().size());
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            Log.i("zjz", e.toString());
//            Log.i("zjz", msg);
//            e.printStackTrace();
//        }

        recyclerView.setAdapter(deliverAdapter);
        mHasLoadedOnce = true;

    }


    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }

        initRecycler();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
