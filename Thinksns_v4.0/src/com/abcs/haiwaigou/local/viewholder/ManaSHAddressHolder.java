package com.abcs.haiwaigou.local.viewholder;

import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.haiwaigou.local.beans.New;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

public class ManaSHAddressHolder extends BaseViewHolder<New> {
    private ImageView imgNew;
    private TextView tvTitle;

    public ManaSHAddressHolder(ViewGroup parent) {
        super(parent, R.layout.local_item_new);

        imgNew = (ImageView) itemView.findViewById(R.id.img_new);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_new);
    }

    @Override
    public void setData(New data) {
//        super.setData(data);
        if (!TextUtils.isEmpty(data.getTitle())) {
            tvTitle.setText(data.getTitle());
        }
        MyApplication.imageLoader.displayImage(data.getPurl(), imgNew, MyApplication.options);

    }
}
