package com.abcs.hqbtravel.holder;

import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.model.find.QiangGou;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by Administrator on 2016/9/8.
 */
public class HWGHDongHoder extends BaseViewHolder<QiangGou.DatasEntry.GoodsListEntry> {

    private ImageView img_icon;

    private TextView t_price2,t_price,t_count,t_title;
    RelativeLayout re_buy;
    ProgressBar goods_prograss;

    public HWGHDongHoder(ViewGroup parent) {
        super(parent, R.layout.hwg_hdong_item_qiang);

        re_buy=(RelativeLayout) itemView.findViewById(R.id.re_buy);
        img_icon=(ImageView) itemView.findViewById(R.id.img_icon);
        t_title=(TextView) itemView.findViewById(R.id.t_title);
        t_count=(TextView) itemView.findViewById(R.id.t_count);
        t_price=(TextView) itemView.findViewById(R.id.t_price);
        t_price2=(TextView) itemView.findViewById(R.id.t_price2);

        goods_prograss=(ProgressBar) itemView.findViewById(R.id.goods_prograss);


    }

    @Override
    public void setData(QiangGou.DatasEntry.GoodsListEntry data) {

        if(data!=null){

            if(!TextUtils.isEmpty(data.goodsImage)){
                MyApplication.imageLoader.displayImage(data.goodsImage, img_icon, MyApplication.getListOptions());
            }

            t_title.setText(data.goodsName);
            t_count.setText("紧剩"+data.goodsNum+"件");

            t_price.setText("¥"+data.goodsMarketprice);
            t_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 删除线

            SpannableString spanString = new SpannableString("¥"+data.goodsPrice);
            AbsoluteSizeSpan span = new AbsoluteSizeSpan(24);
            spanString.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            t_price2.append(spanString);

            if(!TextUtils.isEmpty(data.max_num)){
                int pro = (int) (Float.valueOf(Integer.valueOf(data.max_num))/ Float.valueOf(Integer.valueOf(data.max_num)) * 100);
                goods_prograss.setProgress(pro);
            }
        }
    }
}
