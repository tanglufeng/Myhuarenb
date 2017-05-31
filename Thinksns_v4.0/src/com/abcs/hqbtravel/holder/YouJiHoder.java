package com.abcs.hqbtravel.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.hqbtravel.entity.YouJi;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/9/8.
 */
public class YouJiHoder extends BaseViewHolder<YouJi.BodyEntity.ItemsEntity> {

    private TextView tv_youji_numbers;
    private TextView tv_youji_time;
    private TextView tv_youji_name;
    private TextView tv_youji_title;

    private ImageView img_youji_dec;

    private LinearLayout liner_youji_tag;



    public YouJiHoder(ViewGroup parent) {
        super(parent, R.layout.item_travle_youji);

        liner_youji_tag=(LinearLayout) itemView.findViewById(R.id.liner_youji_tag);
        img_youji_dec=(ImageView) itemView.findViewById(R.id.img_youji_dec);
        tv_youji_numbers=(TextView) itemView.findViewById(R.id.tv_youji_numbers);
        tv_youji_time=(TextView) itemView.findViewById(R.id.tv_youji_time);
        tv_youji_name=(TextView) itemView.findViewById(R.id.tv_youji_name);
        tv_youji_title=(TextView) itemView.findViewById(R.id.tv_youji_title);


    }

    @Override
    public void setData(YouJi.BodyEntity.ItemsEntity data) {

        MyApplication.imageLoader.displayImage(data.photo, img_youji_dec, MyApplication.getCircleImageOptions());

        tv_youji_numbers.setText(data.read+"");
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        tv_youji_time.setText(format.format(new Date(Long.valueOf(data.time)))+"");
        tv_youji_name.setText(data.author+"");
        tv_youji_title.setText(data.title+"");

        if(data.taglist!=null&&data.taglist.size()>0){

            liner_youji_tag.setVisibility(View.VISIBLE);
            liner_youji_tag.removeAllViews();

            for(int i=0;i<data.taglist.size();i++){
//                final YouJi.BodyEntity.ItemsEntity.TaglistEntity entity=data.taglist.get(i);
                View itemView=View.inflate(getContext(), R.layout.item_youji_tag,null);
                ViewGroup parent = (ViewGroup) itemView.getParent();
                if (parent != null) {
                    parent.removeAllViews();
                }

                TextView tv_youji_tagname=(TextView) itemView.findViewById(R.id.tv_youji_tagname);

              /*  if(i==0){
                    tv_youji_tagname.setBackground(getContext().getResources().getDrawable(R.drawable.bg_lan));
                }else  if(i==1){
                    tv_youji_tagname.setBackground(getContext().getResources().getDrawable(R.drawable.bg_hong));
                }else if(i==2) {
                    tv_youji_tagname.setBackground(getContext().getResources().getDrawable(R.drawable.bg_cheng));
                }else{
                    tv_youji_tagname.setBackground(getContext().getResources().getDrawable(R.drawable.bg_lan));
                }*/

                tv_youji_tagname.setBackground(getContext().getResources().getDrawable(R.drawable.img_tag_yji));
                tv_youji_tagname.setText(data.taglist.get(i).name);

                liner_youji_tag.addView(itemView);
            }
        }else {
            liner_youji_tag.setVisibility(View.GONE);
        }

    }
}
