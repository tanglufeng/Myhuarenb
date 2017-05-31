package com.abcs.haiwaigou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RedBagComfirActivity extends BaseActivity implements View.OnClickListener {

    public static final String VOUNCHER = "0";
    public static final String RECHARGE = "1";

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.t_money)
    TextView tMoney;
    @InjectView(R.id.linear_detail)
    LinearLayout linearDetail;

    String money;
    String keyword;
    @InjectView(R.id.linear_recharge)
    LinearLayout linearRecharge;
    @InjectView(R.id.t_price)
    TextView tPrice;
    @InjectView(R.id.t_name)
    TextView tName;
    @InjectView(R.id.t_desc)
    TextView tDesc;
    @InjectView(R.id.t_end_time)
    TextView tEndTime;
    @InjectView(R.id.relative_voucher)
    RelativeLayout relativeVoucher;
    @InjectView(R.id.t_tishi)
    TextView tTishi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_red_bag_comfir);
        ButterKnife.inject(this);

        if (getIntent().getStringExtra("type").equals(RECHARGE)) {
            linearRecharge.setVisibility(View.VISIBLE);
            relativeVoucher.setVisibility(View.GONE);
            money = getIntent().getStringExtra("money");
            tMoney.setText(money);
            tTishi.setText("已存入余额");
        }else if(getIntent().getStringExtra("type").equals(VOUNCHER)){
            linearRecharge.setVisibility(View.GONE);
            relativeVoucher.setVisibility(View.VISIBLE);
            tPrice.setText("yen" + getIntent().getStringExtra("price"));
            tDesc.setText(getIntent().getStringExtra("limit"));
            tEndTime.setText(getIntent().getStringExtra("time"));
            tName.setText(getIntent().getStringExtra("name"));
            tTishi.setText("已存入我的卡包");
            if(Integer.valueOf(getIntent().getStringExtra("price"))<50){
                //蓝色
                tMoney.setBackgroundResource(R.drawable.img_hwg_my_coupon_blue);
            }else if(Integer.valueOf(getIntent().getStringExtra("price"))>=50&&Integer.valueOf(getIntent().getStringExtra("price"))<100){
                tMoney.setBackgroundResource(R.drawable.img_hwg_my_coupon_yellow);
            }else if(Integer.valueOf(getIntent().getStringExtra("price")) >= 100) {
                tMoney.setBackgroundResource(R.drawable.img_hwg_my_coupon_pink);
            }
        }
        keyword = getIntent().getStringExtra("keyword");
        setOnListener();
    }

    private void setOnListener() {
        relativeBack.setOnClickListener(this);
        linearDetail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_back:
                finish();
                break;
            case R.id.linear_detail:
                Intent intent = new Intent(this, RedBagDetailActivity.class);
                intent.putExtra("keyword", keyword);
                startActivity(intent);
                break;
        }

    }

//    http://www.huaqiaobang.com/mobile/index.php?act=word_receive&op=find_red_envelope&key=4009b47c3e51a629fc20a12ff65c27b7&value=我爱
}
