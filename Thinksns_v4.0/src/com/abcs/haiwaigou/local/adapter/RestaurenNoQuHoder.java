package com.abcs.haiwaigou.local.adapter;

import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abcs.haiwaigou.local.beans.HireFindNoQu;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by Administrator on 2016/9/8.
 */
public class RestaurenNoQuHoder extends BaseViewHolder<HireFindNoQu.MsgBean>{
    private TextView tv_title;
    private TextView tv_address;
    private TextView tv_phone;


    public RestaurenNoQuHoder(ViewGroup parent) {
        super(parent, R.layout.item_local_restdetials);

        tv_title=(TextView) itemView.findViewById(R.id.tv_title);
        tv_address=(TextView) itemView.findViewById(R.id.tv_address);
        tv_phone=(TextView) itemView.findViewById(R.id.tv_phone);

    }

    @Override
    public void setData(HireFindNoQu.MsgBean data) {
//        super.setData(data);

        if(data!=null){

            tv_title.setText(data.title+"");
            tv_phone.setText("电话:"+data.contact+"");

            if(!TextUtils.isEmpty(data.ads)){
                tv_address.setText("地址:"+data.ads+"");
            }else {

                if(data.pubTime<2*1000000000){
                    tv_address.setText("发布时间:"+Util.formatzjz3.format(data.pubTime * 1000));
                }else {
                    tv_address.setText("发布时间:"+Util.formatzjz3.format(data.pubTime));
                }
            }
        }

    }
}
