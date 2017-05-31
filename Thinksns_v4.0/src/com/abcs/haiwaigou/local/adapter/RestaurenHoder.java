package com.abcs.haiwaigou.local.adapter;

import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abcs.haiwaigou.local.beans.RestauDetisl;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by Administrator on 2016/9/8.
 */
public class RestaurenHoder extends BaseViewHolder<RestauDetisl.MsgBean>{
    private TextView tv_title;
    private TextView tv_address;
    private TextView tv_phone;


    public RestaurenHoder(ViewGroup parent) {
        super(parent, R.layout.item_local_restdetials);

        tv_title=(TextView) itemView.findViewById(R.id.tv_title);
        tv_address=(TextView) itemView.findViewById(R.id.tv_address);
        tv_phone=(TextView) itemView.findViewById(R.id.tv_phone);

    }

    @Override
    public void setData(RestauDetisl.MsgBean data) {
//        super.setData(data);

        if(data!=null){

            if(!TextUtils.isEmpty(data.title)){
                tv_title.setText(data.title);
            }else {
                tv_title.setText("未知");
            }
            if(!TextUtils.isEmpty(data.ads)){
                tv_address.setText("地址:"+data.ads);
            }else {
                tv_address.setText("地址:未知");
            }
            if(!TextUtils.isEmpty(data.contact)){
                tv_phone.setText("电话:"+data.contact);
            }else {
                tv_phone.setText("电话:未知");
            }
        }

    }
}
