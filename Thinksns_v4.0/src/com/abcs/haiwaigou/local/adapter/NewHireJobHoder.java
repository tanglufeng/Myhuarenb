package com.abcs.haiwaigou.local.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.local.beans.NewHireFind;
import com.abcs.huaqiaobang.tljr.news.Options;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Administrator on 2016/9/8.
 */
public class NewHireJobHoder extends BaseViewHolder<NewHireFind.MsgBean>{
    public RelativeLayout relative_root;
    public TextView t_title;
    public TextView t_numbers;
    public TextView tv_address;
    public ImageView img_icon;


    public NewHireJobHoder(ViewGroup parent) {
        super(parent, R.layout.local_item_new_hire_find_job);
        relative_root= (RelativeLayout) itemView.findViewById(R.id.relative_root);
        t_title= (TextView) itemView.findViewById(R.id.t_title);
        t_numbers= (TextView) itemView.findViewById(R.id.t_numbers);
        tv_address= (TextView) itemView.findViewById(R.id.tv_address);
        img_icon= (ImageView) itemView.findViewById(R.id.img_icon);

    }

    @Override
    public void setData(NewHireFind.MsgBean data) {
//        super.setData(data);

        if(data!=null){

            t_title.setText(data.cateName+"("+data.intro+")");
            t_numbers.setText(data.count+"");
            tv_address.setText(data.shortName+"");
            ImageLoader.getInstance().displayImage(data.img, img_icon, Options.getListOptions());
        }

    }
}
