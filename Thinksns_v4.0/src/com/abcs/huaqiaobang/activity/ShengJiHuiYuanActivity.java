package com.abcs.huaqiaobang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.model.VipBean;
import com.abcs.hqbtravel.wedgt.MyListView;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.adapter.VipsAdapter;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ShengJiHuiYuanActivity extends BaseActivity implements View.OnClickListener{

    @InjectView(R.id.tljr_per_btn_lfanhui)
    RelativeLayout rlBack;
    @InjectView(R.id.listview)
    MyListView listview;
    @InjectView(R.id.tv_type_kai)
    TextView tv_type_kai;
    @InjectView(R.id.tv_hy_dengji)
    TextView tv_hy_dengji;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheng_ji_hui_yuan);
        ButterKnife.inject(this);
        EventBus.getDefault().register(this);
        setOnLick();
        inidatas();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void isFinishActivity(String msg){
        Log.i("zds", "isFinishActivity: "+msg);

        if(!TextUtils.isEmpty(msg)&&msg.equals("finish_activity")){
            ShengJiHuiYuanActivity.this.finish();
        }
    }


    Handler handler=new Handler();

private VipsAdapter adapters;
    private void inidatas(){

        ProgressDlgUtil.showProgressDlg("loading...",this);
        HttpRequest.sendPost(TLUrl.getInstance().URL_vips_list,"&key=" + MyApplication.getInstance().getMykey() , new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {

                if(msg==null){
                    return;
                }
                ProgressDlgUtil.stopProgressDlg();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zds", "msg=" + msg);
                        if(!TextUtils.isEmpty(msg)){

                           final VipBean vipBean=new Gson().fromJson(msg, VipBean.class);

                            if(vipBean.code==200){

                                if(vipBean.datas!=null){

                                    if(vipBean.datas.member!=null){
                                        if(!TextUtils.isEmpty(vipBean.datas.member.levelName)){
                                            tv_type_kai.setText("你当前是"+vipBean.datas.member.levelName);
                                        }
                                        if(!TextUtils.isEmpty(vipBean.datas.member.max_desc)){
                                            tv_hy_dengji.setText(vipBean.datas.member.max_desc+"");
                                        }
                                    }

                                    if(vipBean.datas.vipCard!=null&&vipBean.datas.vipCard.size()>0){
                                        adapters=new VipsAdapter(ShengJiHuiYuanActivity.this,vipBean.datas.vipCard);
                                        listview.setAdapter(adapters);
                                        adapters.notifyDataSetChanged();



                                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                VipBean.DatasBean.VipCardBean vipCardBean=(VipBean.DatasBean.VipCardBean)adapterView.getItemAtPosition(i);
                                                Intent iw=new Intent (ShengJiHuiYuanActivity.this,BuildHuiYuanActivity.class);
                                                iw.putExtra("vipBean",vipCardBean);
                                                iw.putExtra("meneydata",vipBean.datas);
                                                iw.putExtra("type",vipCardBean.levelId);
                                                iw.putExtra("isfrompop","2");
                                                iw.putExtra("money",vipCardBean.money);
                                                startActivity(iw);
                                            }
                                        });

                                    }


                                }
                            }

                        }
                    }
                });
            }
        });

    }
    private void setOnLick(){
        rlBack.setOnClickListener(this);
//        rl_putongka.setOnClickListener(this);
//        rl_jinka.setOnClickListener(this);
//        rl_bojinka.setOnClickListener(this);
//        rl_hei.setOnClickListener(this);
    }

    private String type;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tljr_per_btn_lfanhui:   // 返回
                finish();
                break;
//            case R.id.rl_putongka:   // 普通卡
//                Intent i=new Intent (this,BuildHuiYuanActivity.class);
//                type="1";
//                i.putExtra("type",type);
//                startActivity(i);
//                break;
//            case R.id.rl_jinka:   // 金卡
//                Intent ii=new Intent (this,BuildHuiYuanActivity.class);
//                type="2";
//                ii.putExtra("type",type);
//                startActivity(ii);
//                break;
//            case R.id.rl_bojinka:   // 铂金卡
//                Intent iw=new Intent (this,BuildHuiYuanActivity.class);
//                type="3";
//                iw.putExtra("type",type);
//                startActivity(iw);
//                break;
//            case R.id.rl_hei:   // 黑卡
//                Intent is=new Intent (this,BuildHuiYuanActivity.class);
//                type="4";
//                is.putExtra("type",type);
//                startActivity(is);
//                break;
        }
    }
}
