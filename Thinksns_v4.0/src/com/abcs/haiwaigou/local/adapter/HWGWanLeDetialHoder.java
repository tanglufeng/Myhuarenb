package com.abcs.haiwaigou.local.adapter;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.haiwaigou.local.beans.HWGWanLeDeTials;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

/**
 * Created by Administrator on 2016/9/8.
 */
public class HWGWanLeDetialHoder extends BaseViewHolder<HWGWanLeDeTials.GoodsListBean>{
    private TextView tv_title,tv_store;
    private TextView tv_price;
    private ImageView img_logo;


    public HWGWanLeDetialHoder(ViewGroup parent) {
        super(parent, R.layout.item_local_peisong);

        img_logo=(ImageView) itemView.findViewById(R.id.img_logo);
        tv_title=(TextView) itemView.findViewById(R.id.tv_title);
        tv_store=(TextView) itemView.findViewById(R.id.tv_store);
        tv_price=(TextView) itemView.findViewById(R.id.tv_price);

    }

    @Override
    public void setData(HWGWanLeDeTials.GoodsListBean data) {
//        super.setData(data);

        if(data!=null){

            if(!TextUtils.isEmpty(data.goodsImage)){
//                http://www.huaqiaobang.com/data/upload/shop/store/goods/1/11_05375327290881075.jpg
                MyApplication.imageLoader.displayImage(TLUrl.getInstance().URL_hwg_base+"/data/upload/shop/store/goods/1/"+data.goodsImage,img_logo,MyApplication.options);
            }
            tv_title.setText(data.goodsName+"");

            if(data.goodsMarketprice.equals(data.goodsPrice)){
                tv_store.setVisibility(View.GONE);
            }else {
                tv_store.setVisibility(View.VISIBLE);
                tv_store.setText("￥ "+data.goodsMarketprice);
                //添加删除线
                tv_store.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }

            tv_price.setText("￥ "+data.goodsPrice);
        }

    }
}
