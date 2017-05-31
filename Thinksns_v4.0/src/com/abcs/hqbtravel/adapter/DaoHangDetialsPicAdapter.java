package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.abcs.huaqiaobang.MyApplication;
import com.abcs.hqbtravel.entity.DaohangDetials;
import com.abcs.sociax.android.R;

import java.util.List;

public class DaoHangDetialsPicAdapter extends BaseAdapter{
    private Context mcontext;
    private LayoutInflater inflater;
    private List<DaohangDetials.BodyEntity.GuideEntity.PicturesEntity> data;

    public DaoHangDetialsPicAdapter(Context mcontext, List<DaohangDetials.BodyEntity.GuideEntity.PicturesEntity> data) {
        this.mcontext = mcontext;
        inflater = LayoutInflater.from(mcontext);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_daohang_detials_pic,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
            MyApplication.imageLoader.displayImage(data.get(position).imgUrl.trim(), holder.img, MyApplication.getListOptions());
            holder.time.setText(data.get(position).createTime);
        }
        return convertView;
    }

    public class  ViewHolder{
        private  ImageView img;
        private  TextView time;
        public ViewHolder(View view) {

            img=(ImageView) view.findViewById(R.id.img_daohang_de_pic);
            time=(TextView) view.findViewById(R.id.tv_daohang_de_time);
        }
    }
}
