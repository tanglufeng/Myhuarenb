package com.abcs.haiwaigou.yyg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.PayWayActivity;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.model.Options;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.NumberFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class YYGBuyActivity extends BaseActivity implements View.OnClickListener{

    @InjectView(R.id.relative_close)
    RelativeLayout relativeClose;
    @InjectView(R.id.img_icon)
    ImageView imgIcon;
    @InjectView(R.id.t_name)
    TextView tName;
    @InjectView(R.id.t_all_need)
    TextView tAllNeed;
    @InjectView(R.id.t_remain)
    TextView tRemain;
    @InjectView(R.id.btn_cart_reduce)
    Button btnCartReduce;
    @InjectView(R.id.btn_cart_num_edit)
    EditText btnCartNumEdit;
    @InjectView(R.id.btn_cart_add)
    Button btnCartAdd;
    @InjectView(R.id.img_buy_all)
    ImageView imgBuyAll;
    @InjectView(R.id.t_probability)
    TextView tProbability;
    @InjectView(R.id.t_buy)
    TextView tBuy;


    String goods_title;
    String goods_img;
    String act_id;
    int all_need;
    int remain_need;
    int num=1;
    double price_one;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_yyg_activity_yygbuy);
        ButterKnife.inject(this);
        goods_title=getIntent().getStringExtra("title");
        goods_img=getIntent().getStringExtra("img_icon");
        act_id=getIntent().getStringExtra("act_id");
        all_need=getIntent().getIntExtra("all_need", 0);
        remain_need=getIntent().getIntExtra("remain_need", 0);
        price_one=getIntent().getDoubleExtra("price_one",0);
        double ex=Double.valueOf(num)/Double.valueOf(all_need);
        NumberFormat nt= NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(2);
        tProbability.setText(nt.format(ex));
        initView();
        setOnListener();
        initEdit();
    }

    private void setOnListener() {
        btnCartReduce.setOnClickListener(this);
        btnCartAdd.setOnClickListener(this);
        relativeClose.setOnClickListener(this);
        tBuy.setOnClickListener(this);
        imgBuyAll.setOnClickListener(this);
    }

    private void initEdit() {
        btnCartNumEdit.setText("1");
        btnCartNumEdit.setSelection(btnCartNumEdit.getText().length());
        btnCartNumEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int temp = Integer.parseInt(btnCartNumEdit.getText().toString());
                    num = temp;
                    btnCartNumEdit.setSelection(btnCartNumEdit.getText().length());
                    if (temp > remain_need) {
                        btnCartNumEdit.setText(remain_need + "");
                        btnCartAdd.setEnabled(false);
                        btnCartReduce.setEnabled(true);
                    } else if (btnCartNumEdit.getText().toString().equals("")) {
                        btnCartNumEdit.setText("1");
                        btnCartReduce.setEnabled(false);
                        btnCartAdd.setEnabled(true);
                    } else if (temp < 1) {
                        btnCartNumEdit.setText("1");
                        btnCartReduce.setEnabled(false);
                        btnCartAdd.setEnabled(true);
                    } else if (temp == remain_need) {
                        btnCartAdd.setEnabled(false);
                        btnCartReduce.setEnabled(true);
                    } else if (temp == 1) {
                        btnCartReduce.setEnabled(false);
                        btnCartAdd.setEnabled(true);
                    } else {
                        btnCartReduce.setEnabled(true);
                        btnCartAdd.setEnabled(true);
                    }
                    double ex=Double.valueOf(num)/Double.valueOf(all_need);
                    NumberFormat nt= NumberFormat.getPercentInstance();
                    //设置百分数精确度2即保留两位小数
                    nt.setMinimumFractionDigits(2);
                    tProbability.setText(nt.format(ex));
                } catch (Exception e) {
                    showToast("数量不能为空");
                    num=0;
                    tProbability.setText("0%");
                    btnCartReduce.setEnabled(false);
                    btnCartAdd.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initView() {
        ImageLoader.getInstance().displayImage(goods_img, imgIcon, Options.getHDOptions());
        tAllNeed.setText("总需" + all_need + "人次");
        tRemain.setText(remain_need + "");
        tName.setText(goods_title);

    }

    @Override
    public void onClick(View v) {
        double ex;
        NumberFormat nt;
        switch (v.getId()){
            case R.id.relative_close:
                finish();
                break;
            case R.id.btn_cart_add:
                num++;
                if (num == remain_need) {
                    btnCartAdd.setEnabled(false);
                }
                ex =Double.valueOf(num)/Double.valueOf(all_need);
                //获取格式化对象
                nt = NumberFormat.getPercentInstance();
                //设置百分数精确度2即保留两位小数
                nt.setMinimumFractionDigits(2);
                tProbability.setText(nt.format(ex));
                btnCartReduce.setEnabled(true);
                btnCartNumEdit.setText(num + "");
                break;
            case R.id.btn_cart_reduce:
                num--;
                if (num == 1) {
                    btnCartReduce.setEnabled(false);
                }
                btnCartAdd.setEnabled(true);
                ex =Double.valueOf(num)/Double.valueOf(all_need);
                //获取格式化对象
                nt = NumberFormat.getPercentInstance();
                //设置百分数精确度2即保留两位小数
                nt.setMinimumFractionDigits(2);
                tProbability.setText(nt.format(ex));
                btnCartNumEdit.setText(num + "");
                break;
            case R.id.img_buy_all:
                num=remain_need;
                ex =Double.valueOf(num)/Double.valueOf(all_need);
                //获取格式化对象
                nt = NumberFormat.getPercentInstance();
                //设置百分数精确度2即保留两位小数
                nt.setMinimumFractionDigits(2);
                tProbability.setText(nt.format(ex));
                btnCartNumEdit.setText(num + "");
                btnCartReduce.setEnabled(true);
                btnCartAdd.setEnabled(false);
                break;
            case R.id.t_buy:
                if(num==0){
                    showToast("数量不能为空！");
                    return;
                }
                Intent intent=new Intent(this, PayWayActivity.class);
                intent.putExtra("yungou",true);
                intent.putExtra("act_id",act_id);
                intent.putExtra("num",num);
                intent.putExtra("yungou_money",Double.valueOf(num * price_one));
                startActivity(intent);
                finish();
                break;
        }

    }
}
