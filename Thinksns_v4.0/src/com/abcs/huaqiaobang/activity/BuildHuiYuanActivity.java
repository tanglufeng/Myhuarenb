package com.abcs.huaqiaobang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.model.VipBean;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.sociax.android.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BuildHuiYuanActivity extends BaseActivity implements View.OnClickListener{

    @InjectView(R.id.tljr_per_btn_lfanhui)
    RelativeLayout rlBack;
    @InjectView(R.id.relative_quanyi)
    RelativeLayout relative_quanyi;
    @InjectView(R.id.relative_know)
    RelativeLayout relative_know;
    @InjectView(R.id.relative_pay)
    RelativeLayout relative_pay;
    @InjectView(R.id.imge_kai_type)
    ImageView imge_kai_type;
    @InjectView(R.id.img_notice)
    ImageView img_notice;
    @InjectView(R.id.img_quanyi)
    ImageView img_quanyi;
    @InjectView(R.id.tv_title)
    TextView tv_title;
    @InjectView(R.id.tv_quan_yi)
    TextView tv_quan_yi;
    @InjectView(R.id.tv_xuzhi)
    TextView tv_xuzhi;
    @InjectView(R.id.tv_price)
    TextView tv_price;

    private boolean isLookQuanYi=false;
    private boolean isLookNotice=false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void isFinishActivity(String msg){
        Log.i("zds", "isFinishActivity: "+msg);

        if(!TextUtils.isEmpty(msg)&&msg.equals("finish_activity")){
            BuildHuiYuanActivity.this.finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_hui_yuan);
        ButterKnife.inject(this);
        EventBus.getDefault().register(this);
        setOnLick();
        getExtre();
    }

    private VipBean.DatasBean meneydata;
    private  double curren_total_money;

    private void getExtre(){
        vipCardBean=(VipBean.DatasBean.VipCardBean) getIntent().getSerializableExtra("vipBean");
        meneydata=(VipBean.DatasBean) getIntent().getSerializableExtra("meneydata");
        type=getIntent().getStringExtra("type");
        if(meneydata!=null){
            if(meneydata.member.total_vip_money>0){
                curren_total_money=meneydata.member.total_vip_money;
            }

            if(!TextUtils.isEmpty(type)&&type.equals("1")){
                imge_kai_type.setImageResource(R.drawable.huiyuan_bg);
                tv_title.setText(vipCardBean.levelName+"");
                money=(vipCardBean.money-curren_total_money)+"";
                tv_price.setText("￥"+money+"/年 | 购买");    //￥ 10000/年 | 充值
            }else if(!TextUtils.isEmpty(type)&&type.equals("2")){
                tv_title.setText(vipCardBean.levelName+"");
                imge_kai_type.setImageResource(R.drawable.img_yinh_jin_bg);
                money=(vipCardBean.money-curren_total_money)+"";
                tv_price.setText("￥"+money+"/年 | 购买");    //￥ 10000/年 | 充值
            }else if(!TextUtils.isEmpty(type)&&type.equals("3")){
                tv_title.setText(vipCardBean.levelName+"");
                imge_kai_type.setImageResource(R.drawable.img_yinh_baijin_bg);
                money=(vipCardBean.money-curren_total_money)+"";
                tv_price.setText("￥"+money+"/年 | 购买");    //￥ 10000/年 | 充值
            }else {
                tv_title.setText(vipCardBean.levelName+"");
                imge_kai_type.setImageResource(R.drawable.img_yinh_hei_bg);
                money=(vipCardBean.money-curren_total_money)+"";
                tv_price.setText("￥"+money+"/年 | 购买");    //￥ 10000/年 | 充值
            }
        }
    }

    private void setOnLick(){
        rlBack.setOnClickListener(this);
        relative_quanyi.setOnClickListener(this);
        relative_know.setOnClickListener(this);
        relative_pay.setOnClickListener(this);
    }
    private VipBean.DatasBean.VipCardBean vipCardBean;
    private String type;
    private String money;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tljr_per_btn_lfanhui:   // 返回
                finish();
                break;
            case R.id.relative_quanyi:   // 会员权益
                if(!isLookQuanYi){
                    if(!TextUtils.isEmpty(vipCardBean.introduce)){
                        tv_quan_yi.setVisibility(View.VISIBLE);
                        tv_quan_yi.setText(vipCardBean.introduce);
                    }else {
                        tv_quan_yi.setVisibility(View.GONE);
                    }
                    isLookQuanYi=true;
                    img_quanyi.setImageResource(R.drawable.img_up);
                }else {
                    img_quanyi.setImageResource(R.drawable.img_yinh_back);
                    tv_quan_yi.setVisibility(View.GONE);
                    isLookQuanYi=false;
                }
                break;
            case R.id.relative_know:   // 用户须知
                if(!isLookNotice){
                    if(!TextUtils.isEmpty(vipCardBean.notice)){
                        tv_xuzhi.setVisibility(View.VISIBLE);
                        tv_xuzhi.setText(vipCardBean.notice);
                    }else {
                        tv_xuzhi.setVisibility(View.GONE);
                    }
                    isLookNotice=true;
                    img_notice.setImageResource(R.drawable.img_up);
                }else {
                    img_notice.setImageResource(R.drawable.img_yinh_back);
                    tv_xuzhi.setVisibility(View.GONE);
                    isLookNotice=false;
                }
                break;
            case R.id.relative_pay:   // 会员升级  充值
                Intent iw=new Intent (this,HuiYuanPayActivity2.class);
                iw.putExtra("title",vipCardBean.levelName+"");
                iw.putExtra("levelid",vipCardBean.levelId+"");
                iw.putExtra("money",money+"");
                iw.putExtra("isfrompop",false);
                startActivity(iw);
                break;
        }
    }
}
