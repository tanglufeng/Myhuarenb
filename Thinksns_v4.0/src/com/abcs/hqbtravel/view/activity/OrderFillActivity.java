package com.abcs.hqbtravel.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;


public class OrderFillActivity extends Activity implements  View.OnClickListener {

    private ImageView img_back;
    private LinearLayout ret_price_jian;
    private LinearLayout ret_price_jia;
    private TextView tv_price_number;
    private TextView tv_type1;
    private TextView tv_type2;
    private TextView tv_type3;
    private TextView tv_type4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_order_fill);

        img_back=(ImageView)findViewById(R.id.img_back);

        ret_price_jian=(LinearLayout)findViewById(R.id.ret_price_jian);
        ret_price_jia=(LinearLayout)findViewById(R.id.ret_price_jia);
        tv_price_number=(TextView)findViewById(R.id.tv_price_number);

        tv_type1=(TextView)findViewById(R.id.tv_type1);
        tv_type2=(TextView)findViewById(R.id.tv_type2);
        tv_type3=(TextView)findViewById(R.id.tv_type3);
        tv_type4=(TextView)findViewById(R.id.tv_type4);

        img_back.setOnClickListener(this);
        ret_price_jian.setOnClickListener(this);
        ret_price_jia.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.ret_price_jian:
                tv_price_number.setText((Integer.parseInt(tv_price_number.getText().toString().trim())-1)+"");
                break;
            case R.id.ret_price_jia:
                tv_price_number.setText((Integer.parseInt(tv_price_number.getText().toString().trim())+1)+"");
                break;
        }
    }
}
