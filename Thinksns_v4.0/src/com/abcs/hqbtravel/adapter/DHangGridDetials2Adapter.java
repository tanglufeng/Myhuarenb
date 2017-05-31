package com.abcs.hqbtravel.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.hqbtravel.entity.DaohangDetials2;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;

import java.util.List;

public class DHangGridDetials2Adapter extends BaseAdapter {
    private Context context;
    private List<DaohangDetials2.BodyEntity.GuideEntity.PicturesEntity> data;
    private LayoutInflater inflater;

    public DHangGridDetials2Adapter(Context context, List<DaohangDetials2.BodyEntity.GuideEntity.PicturesEntity> data) {
        this.context = context;
        this.data = data;
        inflater= LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public DaohangDetials2.BodyEntity.GuideEntity.PicturesEntity getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_daohang_detials2,parent,false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
            viewHolder=(ViewHolder)convertView.getTag();
            viewHolder.tv_name_jingdian1.setText(data.get(position).voiceName);
            MyApplication.imageLoader.displayImage(data.get(position).imgUrl, viewHolder.img_jingdian1, MyApplication.getCircleImageOptions());

        return convertView;
    }

    private class ViewHolder{
        ImageView img_jingdian1;
        TextView tv_name_jingdian1;

        public ViewHolder(View view) {

            img_jingdian1=(ImageView)view.findViewById(R.id.img_jingdian1);
            tv_name_jingdian1=(TextView)view.findViewById(R.id.tv_name_jingdian1);
        }
    }
}
