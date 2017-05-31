package com.abcs.haiwaigou.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.adapter.DeliverAdapter;
import com.abcs.haiwaigou.fragment.customtool.FullyLinearLayoutManager;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.sociax.android.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/6/8 0008.
 */
public class Package2Fragment extends BaseFragment {

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
    private View view;
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;

    private String message;
    private ArrayList<String> msg;
    private String express_name;
    private String shipping_code;
    DeliverAdapter deliverAdapter;

    public static Package2Fragment newInstance(String express_name, String shipping_code, String  msg) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("msg", msg);
        bundle.putSerializable("express_name", express_name);
        bundle.putSerializable("shipping_code", shipping_code);
        Package2Fragment fragment = new Package2Fragment();
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
        if(tDeliverCode!=null)
            tDeliverCode.setText(shipping_code);
        if(tDeliverCompany!=null)
            tDeliverCompany.setText(express_name);
        fullyLinearLayoutManager = new FullyLinearLayoutManager(activity);
        deliverAdapter = new DeliverAdapter();
        recyclerView.setFocusable(false);
        recyclerView.setLayoutManager(fullyLinearLayoutManager);
        String[] info=message.split(",");
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
