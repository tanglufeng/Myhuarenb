package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.PhotoPreview_BiDetialsActivity;
import com.abcs.hqbtravel.entity.CommentItem;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.yixia.camera.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TravelDetialCommentsAdapter extends BaseAdapter{
    private Context mcontext;
    private LayoutInflater inflater;
    private ArrayList<CommentItem> data;

    public TravelDetialCommentsAdapter(Context mcontext, ArrayList<CommentItem> data) {
        this.mcontext = mcontext;
        inflater = LayoutInflater.from(mcontext);
        this.data = data;
    }

    public void addDatas(List<CommentItem> data){

        if(data!=null&&data.size()>0){
            this.data.addAll(data);
            notifyDataSetChanged();
        }
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CommentItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.hqb_travle_all_comments_item1,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }

        if(!TextUtils.isEmpty(data.get(position).comment)){
            holder.t_comment_info.setText(data.get(position).comment);
        }
        if(!TextUtils.isEmpty(data.get(position).name)){
            holder.tv_user_name.setText(data.get(position).name);
        }
        if(data.get(position).createTime>0){
//            Util.format1.format(msg.getCreateTime())
            holder.tv_time.setText(Util.format1.format(data.get(position).createTime * 1000));
        }
        if(!TextUtils.isEmpty(data.get(position).avatar)){
            MyApplication.imageLoader.displayImage(data.get(position).avatar,holder.img_user, MyApplication.getListOptions());
        }

        if(data.get(position).score>0){
            if(data.get(position).score<=5){
                holder.ratingBar.setRating((float) data.get(position).score);
            }
        }else {
            holder.ratingBar.setRating(0.0f);
        }

//        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                service_point = (int) rating;
//            }
//        });

        if(data.get(position).imgs!=null){

            if(data.get(position).imgs.size()>0){

//                initCommentsd(holder,data.get(position).imgs);
                CommentGridViewAdapter adapter=new CommentGridViewAdapter(mcontext,data.get(position).imgs);
                holder.mygridview.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                holder.mygridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Intent intent = new Intent();
                        intent.putExtra("position", i+"");
                        intent.putExtra("ulist",data.get(position).imgs);
                        intent.setClass(mcontext, PhotoPreview_BiDetialsActivity.class);

                        Log.e("zds", "commentItem.imgs==="+data.get(position).imgs.size()+"");
                        mcontext.startActivity(intent);
                    }
                });

            }else {
//                holder.mygridview.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    private int position=0;
    private void initCommentsd(ViewHolder holder, final ArrayList<String> data){
        holder.mygridview.removeAllViews();

            for(int i=0;i<data.size();i++){

                if(i<3){
                    View itemView=View.inflate(mcontext, R.layout.item_travel_comment,null);

                    ViewGroup parent = (ViewGroup) itemView.getParent();
                    if (parent != null) {
                        parent.removeAllViews();
                    }
                    itemView.setLayoutParams(new ViewGroup.LayoutParams(MyApplication.getWidth()/3, ViewGroup.LayoutParams.MATCH_PARENT));

                    ImageView img_logo=(ImageView) itemView.findViewById(R.id.logo);
                    RelativeLayout rela_gongjiz=(RelativeLayout) itemView.findViewById(R.id.rela_gongjiz);
                    TextView tv_gongjizhang=(TextView) itemView.findViewById(R.id.tv_gongjizhang);

                    MyApplication.imageLoader.displayImage(data.get(i), img_logo, MyApplication.options);

                    position=i;
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
                            intent.setClass(mcontext, PhotoPreview_BiDetialsActivity.class);
                            mcontext.startActivity(intent);
                        }
                    });

                    holder.mygridview.addView(itemView);
                }
            }
    }
    public class  ViewHolder{
        private  com.abcs.huaqiaobang.view.CircleImageView img_user;
        private TextView tv_user_name;
//        private LinearLayout mygridview;
        private com.abcs.hqbtravel.wedgt.MyGridView mygridview;
        private TextView t_comment_info;
        private TextView tv_time,tv_gongjizhang;
        private RatingBar ratingBar;
        private ImageView img_1,img_2,img_3;
        private LinearLayout linear_imgs;
        private RelativeLayout relative_pic1,relative_pic2,relative_pic3,rela_gongjiz;
        public ViewHolder(View view) {
            img_user=(com.abcs.huaqiaobang.view.CircleImageView) view.findViewById(R.id.img_user);
            tv_user_name=(TextView) view.findViewById(R.id.tv_user_name);
            ratingBar=(RatingBar) view.findViewById(R.id.ratingBar);
            t_comment_info=(TextView) view.findViewById(R.id.t_comment_info);
            linear_imgs=(LinearLayout) view.findViewById(R.id.linear_imgs);
            mygridview=(com.abcs.hqbtravel.wedgt.MyGridView) view.findViewById(R.id.mygridview);
            tv_time=(TextView) view.findViewById(R.id.tv_time);
            tv_gongjizhang=(TextView) view.findViewById(R.id.tv_gongjizhang);
            img_1=(ImageView) view.findViewById(R.id.img_1);
            relative_pic1=(RelativeLayout) view.findViewById(R.id.relative_pic1);
            img_2=(ImageView) view.findViewById(R.id.img_2);
            relative_pic2=(RelativeLayout) view.findViewById(R.id.relative_pic2);
            img_3=(ImageView) view.findViewById(R.id.img_3);
            relative_pic3=(RelativeLayout) view.findViewById(R.id.relative_pic3);
            rela_gongjiz=(RelativeLayout) view.findViewById(R.id.rela_gongjiz);
        }
    }
}
