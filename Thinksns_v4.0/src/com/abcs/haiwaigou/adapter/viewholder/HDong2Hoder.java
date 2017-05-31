//package com.abcs.haiwaigou.adapter.viewholder;
//
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import com.abcs.haiwaigou.model.HDong2;
//import com.abcs.huaqiaobang.MyApplication;
//import com.abcs.huaqiaobang.util.Util;
//import com.abcs.sociax.android.R;
//import com.jude.easyrecyclerview.adapter.BaseViewHolder;
//
///**
// */
//public class HDong2Hoder extends BaseViewHolder<HDong2.DatasEntry> {
//
//    private ImageView img_logo;
//    private LinearLayout liner;
//
//
//    public HDong2Hoder(ViewGroup parent) {
//        super(parent, R.layout.item_hwg_pic);
//
//        liner=(LinearLayout) itemView.findViewById(R.id.liner);
//        img_logo=(ImageView) itemView.findViewById(R.id.img_logo);
//
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Util.WIDTH, Util.WIDTH*33/75);
//        liner.setLayoutParams(layoutParams);
//    }
//
//    @Override
//    public void setData(HDong2.DatasEntry data) {
//
//        MyApplication.imageLoader.displayImage(data.image, img_logo, MyApplication.getCircleImageOptions());
//
//    }
//}
