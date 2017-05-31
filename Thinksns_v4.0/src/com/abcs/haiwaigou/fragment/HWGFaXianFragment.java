package com.abcs.haiwaigou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abcs.haiwaigou.activity.CompanyConnectActivity;
import com.abcs.haiwaigou.activity.RechargeActivity;
import com.abcs.haiwaigou.activity.RedBagActivity;
import com.abcs.haiwaigou.adapter.FaXianAdapter;
import com.abcs.haiwaigou.model.FaXian;
import com.abcs.haiwaigou.yyg.activity.YYGActivity;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.ServerUtils;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.huaqiaobang.ytbt.common.utils.LogUtil;
import com.abcs.huaqiaobang.ytbt.common.utils.ToastUtil;
import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.android.ActivityHome;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HWGFaXianFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.rv_faxian)
    EasyRecyclerView rvFaXain;

    @Override
    public void onRefresh() {
        isRefresh = true;
        if(ServerUtils.isConnect(activity)){
            getDates();
        }else {
            ToastUtil.showMessage("请检查您的网络！");
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rvFaXain.setRefreshing(false);
            }
        }, 1500);
    }

    ActivityHome activity;
    View view;
    FaXianAdapter adapter;
    private Handler handler = new Handler();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (ActivityHome) getActivity();
        view = activity.getLayoutInflater().inflate(R.layout.fragemt_fa_xian, null);
        ButterKnife.inject(this, view);

        intRe();
    }

    private void intRe(){
        rvFaXain.setLayoutManager(new LinearLayoutManager(activity));
        rvFaXain.setRefreshListener(this);
        rvFaXain.setAdapter(adapter = new FaXianAdapter(activity));
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                FaXian.DatasBean itemsEntity = adapter.getAllData().get(position);

                Intent it=null;
                if(itemsEntity.type.equals("1")){
                    it = new Intent(activity, RedBagActivity.class);
                    it.putExtra("red_code","");
                }else  if(itemsEntity.type.equals("2")){
                    if (!Util.isYYGLogin) {
                        if (MyApplication.getInstance().self != null)
                            loginForYYG();
                    }

                    it = new Intent(activity, YYGActivity.class);
                }else  if(itemsEntity.type.equals("3")){
                    it = new Intent(activity, RechargeActivity.class);
                }else  if(itemsEntity.type.equals("4")){
                    if (MyApplication.getInstance().getMykey() == null) {
                        it = new Intent(activity, WXEntryActivity.class);
                        it.putExtra("isthome",true);
                    } else {
                        it = new Intent(activity, CompanyConnectActivity.class);
                    }
                }


                startActivity(it);
            }
        });

//        adapter.setNoMore(R.layout.view_nomore);
//        adapter.setMore(R.layout.view_more, this);
        adapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.resumeMore();
            }
        });

        if(ServerUtils.isConnect(activity)){
            getDates();
        }else {
            ToastUtil.showMessage("请检查您的网络！");
            return;
        }

    }

    private void loginForYYG() {
//        ModelUser snsUser=Thinksns.getMy();
//        Log.i("zjz","snsUser_id="+snsUser.getUid());
        HttpRequest.sendPost(TLUrl.getInstance().URL_yyg_login, "nickname=" + MyApplication.getInstance().self.getNickName() + "&userId="
                + MyApplication.getInstance().self.getId() + "&avator=" + MyApplication.getInstance().self.getAvatarUrl() + "&userName=" + URLEncoder.encode(MyApplication.getInstance().self.getUserName())
                + "&alias="+MyApplication.getInstance().self.getId() , new HttpRevMsg() {
            @Override
            public void revMsg(String msg) {
                if (msg == null) {
                    return ;
                }
                Log.i("zjz", "login_for_yyg=" + msg);
                try {
                    JSONObject json = new JSONObject(msg);
                    if (json.optInt("status") == 1) {
                        Util.isYYGLogin = true;
                        Log.i("zjz", "mainfragment1_YYG");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });
    }

    private boolean isRefresh=false;
    private boolean isFirest=true;

    private void getDates() {


        if(isFirest){
            ProgressDlgUtil.showProgressDlg("",activity);
        }
//        http://www.huaqiaobang.com/mobile/index.php?act=discovery&op=index
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base+"/mobile/index.php", "act=discovery&op=index", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if(isFirest){
                            ProgressDlgUtil.stopProgressDlg();
                            isFirest=false;
                        }

                            LogUtil.e("faxian", "msg==" + msg);
                            if (!TextUtils.isEmpty(msg)) {

                                if (isRefresh) {
                                    adapter.clear();
                                    isRefresh = false;
                                }

                                FaXian bean = new Gson().fromJson(msg, FaXian.class);
                                if (bean.state == 1) {
                                    if (bean.datas != null) {
                                        if (bean.datas.size() > 0) {
                                            adapter.addAll(bean.datas);
                                            adapter.notifyDataSetChanged();
                                        } else {

                                        }
                                    }
                                }
                            }}
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
}