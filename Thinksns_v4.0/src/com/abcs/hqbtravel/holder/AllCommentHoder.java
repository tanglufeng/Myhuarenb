package com.abcs.hqbtravel.holder;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.PhotoPreview_BiDetialsActivity;
import com.abcs.hqbtravel.adapter.CommentGridViewAdapter;
import com.abcs.hqbtravel.entity.CommentItem;
import com.abcs.hqbtravel.wedgt.MyGridView;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.yixia.camera.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/8.
 */
public class AllCommentHoder extends BaseViewHolder<CommentItem> {

    private  com.abcs.huaqiaobang.view.CircleImageView img_user;
    private TextView tv_user_name;
    private TextView t_comment_info;
    private TextView tv_time,tv_gongjizhang;
    private RatingBar ratingBar;
    private ImageView img_1,img_2,img_3;
    private LinearLayout linear_imgs;
    private MyGridView mygridview;
    private RelativeLayout relative_pic1,relative_pic2,relative_pic3,rela_gongjiz;
    

    public AllCommentHoder(ViewGroup parent) {
        super(parent, R.layout.hqb_travle_all_comments_item_with);
        img_user=(com.abcs.huaqiaobang.view.CircleImageView) itemView.findViewById(R.id.img_user);
        tv_user_name=(TextView) itemView.findViewById(R.id.tv_user_name);
        ratingBar=(RatingBar) itemView.findViewById(R.id.ratingBar);
        t_comment_info=(TextView) itemView.findViewById(R.id.t_comment_info);
        linear_imgs=(LinearLayout) itemView.findViewById(R.id.linear_imgs);
        tv_time=(TextView) itemView.findViewById(R.id.tv_time);
        tv_gongjizhang=(TextView) itemView.findViewById(R.id.tv_gongjizhang);
        img_1=(ImageView) itemView.findViewById(R.id.img_1);
        relative_pic1=(RelativeLayout) itemView.findViewById(R.id.relative_pic1);
        img_2=(ImageView) itemView.findViewById(R.id.img_2);
        relative_pic2=(RelativeLayout) itemView.findViewById(R.id.relative_pic2);
        img_3=(ImageView) itemView.findViewById(R.id.img_3);
        relative_pic3=(RelativeLayout) itemView.findViewById(R.id.relative_pic3);
        rela_gongjiz=(RelativeLayout) itemView.findViewById(R.id.rela_gongjiz);
        mygridview=(MyGridView) itemView.findViewById(R.id.mygridview);

    }

    @Override
    public void setData(final CommentItem data) {

        if(!TextUtils.isEmpty(data.comment)){
            t_comment_info.setText(data.comment);
        }
        if(!TextUtils.isEmpty(data.name)){
            tv_user_name.setText(data.name);
        }
        if(data.createTime>0){
            tv_time.setText(Util.format1.format(data.createTime * 1000));
        }
        if(!TextUtils.isEmpty(data.avatar)){
            MyApplication.imageLoader.displayImage(data.avatar,img_user, MyApplication.getListOptions());
        }

        if(data.score>0){
            if(data.score<=5){
                ratingBar.setRating((float) data.score);
            }
        }else {
            ratingBar.setRating(0.0f);
        }

//        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                service_point = (int) rating;
//            }
//        });

        if(data.imgs!=null){

            if(data.imgs.size()>0){

//                initCommentsd(data.imgs);

                CommentGridViewAdapter adapter=new CommentGridViewAdapter(getContext(),data.imgs);
                mygridview.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                mygridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent();
                        intent.putExtra("position", i+"");
                        intent.putExtra("ulist", data.imgs);
                        Log.e("zds2", "commentItem.imgs==="+data.imgs.size()+"");
                        intent.setClass(getContext(), PhotoPreview_BiDetialsActivity.class);
                        getContext().startActivity(intent);
                    }
                });

//

//                linear_imgs.setVisibility(View.VISIBLE);
//                switch (data.imgs.size()){
//                    case 1:
//                        relative_pic1.setVisibility(View.VISIBLE);
//                        MyApplication.imageLoader.displayImage(data.imgs.get(0),img_1, MyApplication.getListOptions());
//                        relative_pic1.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//
//                            }
//                        });
//                        break;
//                    case 2:
//                        relative_pic2.setVisibility(View.VISIBLE);
//                        MyApplication.imageLoader.displayImage(data.imgs.get(1),img_2, MyApplication.getListOptions());
//                        relative_pic2.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//
//                            }
//                        });
//                        break;
//                    case 3:
//                        relative_pic3.setVisibility(View.VISIBLE);
//                        rela_gongjiz.setVisibility(View.VISIBLE);
//                        tv_gongjizhang.setText("共"+data.imgs.size()+"张");
//                        MyApplication.imageLoader.displayImage(data.imgs.get(2),img_3, MyApplication.getListOptions());
//                        relative_pic3.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//
//                            }
//                        });
//                        break;
//                    default:
//                        break;
//                }
            }else {
//                linear_imgs.setVisibility(View.GONE);
            }
        }
    }

    private int position=0;
    private List<Integer> POI=new ArrayList<>();
    private void initCommentsd( final ArrayList<String> data){
        mygridview.removeAllViews();

        for(int i=0;i<data.size();i++){
            POI.add(i);

            if(i<3){

                View itemView=View.inflate(getContext(), R.layout.item_travel_comment,null);

                ViewGroup parent = (ViewGroup) itemView.getParent();
                if (parent != null) {
                    parent.removeAllViews();
                }
                itemView.setLayoutParams(new ViewGroup.LayoutParams(MyApplication.getWidth()/3, ViewGroup.LayoutParams.MATCH_PARENT));

                ImageView img_logo=(ImageView) itemView.findViewById(R.id.logo);
                RelativeLayout rela_gongjiz=(RelativeLayout) itemView.findViewById(R.id.rela_gongjiz);
                TextView tv_gongjizhang=(TextView) itemView.findViewById(R.id.tv_gongjizhang);

                MyApplication.imageLoader.displayImage(data.get(i), img_logo, MyApplication.options);

                if(i==2){
                    rela_gongjiz.setVisibility(View.VISIBLE);
                    tv_gongjizhang.setText("共"+data.size()+"张");
                }

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("position", position+"");
                        intent.putExtra("ulist", data);
                        intent.setClass(getContext(), PhotoPreview_BiDetialsActivity.class);
                        getContext().startActivity(intent);
                    }
                });

                mygridview.addView(itemView);
            }
        }
    }
    
}
