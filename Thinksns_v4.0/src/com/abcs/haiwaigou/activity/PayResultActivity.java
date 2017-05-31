package com.abcs.haiwaigou.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;

public class PayResultActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout relative_cg;
    private RelativeLayout relative_sb;
    private RelativeLayout relative_reason;
    private TextView t_reason;
    boolean result;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_pay_result);
        result= (boolean) getIntent().getSerializableExtra("result");
        code= (String) getIntent().getSerializableExtra("code");
        initView();
        initResult();
        setListener();
    }

    private void initResult() {
        if(result){
            relative_cg.setVisibility(View.VISIBLE);
            relative_sb.setVisibility(View.GONE);
            relative_reason.setVisibility(View.GONE);

        }else {
            relative_cg.setVisibility(View.GONE);
            relative_sb.setVisibility(View.VISIBLE);
            relative_reason.setVisibility(View.VISIBLE);
            setText(code);

        }
    }

    private void setText(String code) {
        String msg = "";
        switch (code) {
            case "6001":
                msg = "您已取消支付";
                break;
            case "6002":
                msg = "网络出问题啦";
                break;
            default:
                msg = "您的账户余额不足";
                break;
        }
        t_reason.setText(msg);
    }

    private void setListener() {
        findViewById(R.id.tljr_img_news_back).setOnClickListener(this);
        findViewById(R.id.t_back_to_shop).setOnClickListener(this);
        findViewById(R.id.t_continue_pay).setOnClickListener(this);
    }

    private void initView() {
        relative_cg= (RelativeLayout) findViewById(R.id.relative_cg);
        relative_sb= (RelativeLayout) findViewById(R.id.relative_sb);
        relative_reason= (RelativeLayout) findViewById(R.id.relative_reason);
        t_reason= (TextView) findViewById(R.id.t_reason);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tljr_img_news_back:
                finish();
                break;
            case R.id.t_back_to_shop:
                showToast("正在装修中...");
                break;
            case R.id.t_continue_pay:
                showToast("准备完善...");
                break;
        }
    }
}
