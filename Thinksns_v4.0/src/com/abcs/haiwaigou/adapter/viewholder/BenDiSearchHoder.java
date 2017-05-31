package com.abcs.haiwaigou.adapter.viewholder;

import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.model.BenDiSearch;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;


public class BenDiSearchHoder extends BaseViewHolder<BenDiSearch.DatasBean>{

    private TextView tv_title,tv_store,tv_en_title;
    private TextView tv_price;
    private ImageView img_logo;
    public  ImageView ivGoodsMinus;
    public  TextView tvGoodsSelectNum;
    public  ImageView ivGoodsAdd;
    LinearLayout lin_tianjai;

    public BenDiSearchHoder(ViewGroup parent) {
        super(parent, R.layout.item_local_peisong2);
        img_logo=(ImageView) itemView.findViewById(R.id.img_logo);
        tv_title=(TextView) itemView.findViewById(R.id.tv_title);
        tv_en_title=(TextView) itemView.findViewById(R.id.tv_en_title);
        tv_store=(TextView) itemView.findViewById(R.id.tv_store);
        tv_price=(TextView) itemView.findViewById(R.id.tv_price);
        lin_tianjai=(LinearLayout) itemView.findViewById(R.id.lin_tianjai);
    }

    @Override
    public void setData(final BenDiSearch.DatasBean data) {

        if(!TextUtils.isEmpty(data.goodsImage)){
//                http://www.huaqiaobang.com/data/upload/shop/store/goods/11/11_05375327290881075.jpg
            MyApplication.imageLoader.displayImage(TLUrl.getInstance().URL_hwg_base+"/data/upload/shop/store/goods/11/"+data.goodsImage,img_logo,MyApplication.options);
        }

        tv_title.setText(data.goodsName+"");
        tv_en_title.setText(data.goodsEnName+"");
        tv_store.setText("货号:"+data.goodsSerial);
        tv_price.setText("€ "+data.goodsPrice);


    }
}
