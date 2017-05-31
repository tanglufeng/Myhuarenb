package com.abcs.haiwaigou.HDong;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.hqbtravel.entity.RestauransBean;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 */
public class ReXiaoAdapter extends RecyclerArrayAdapter<RestauransBean>{
    public ReXiaoAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReXiaoHoder(parent);
    }

    public class ReXiaoHoder extends BaseViewHolder<RestauransBean> {

        private ImageView iv_logo;
        private TextView tv_pai_min; // 排名
        private TextView tv_title,tv_dec,tv_price;
        private TextView tv_scale_num;

        public ReXiaoHoder(ViewGroup parent) {
            super(parent, R.layout.item_hwg_hdong_rexiao);

            iv_logo=(ImageView) itemView.findViewById(R.id.iv_logo);
            tv_pai_min=(TextView) itemView.findViewById(R.id.tv_pai_min);
            tv_title=(TextView) itemView.findViewById(R.id.tv_title);
            tv_dec=(TextView) itemView.findViewById(R.id.tv_dec);
            tv_price=(TextView) itemView.findViewById(R.id.tv_price);
            tv_scale_num=(TextView) itemView.findViewById(R.id.tv_scale_num);
        }

        @Override
        public void setData(RestauransBean data) {

            MyApplication.imageLoader.displayImage(data.photo, iv_logo, MyApplication.getListOptions());

        }
    }
}
