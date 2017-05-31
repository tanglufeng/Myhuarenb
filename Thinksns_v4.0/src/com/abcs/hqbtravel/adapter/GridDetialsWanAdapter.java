package com.abcs.hqbtravel.adapter;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.hqbtravel.entity.TouristAttractionsBean;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;

import java.util.List;

public class GridDetialsWanAdapter extends BaseAdapter {
    private Context context;
    private List<TouristAttractionsBean> data;
    private LayoutInflater inflater;

    public GridDetialsWanAdapter(Context context, List<TouristAttractionsBean> data) {
        this.context = context;
        this.data = data;
        inflater= LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public TouristAttractionsBean getItem(int position) {
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
            convertView=inflater.inflate(R.layout.item_travle_dujinsbi,parent,false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        MyApplication.imageLoader.displayImage(data.get(position).photo, viewHolder.img_logo, MyApplication.getListOptions());
        if( !TextUtils.isEmpty(data.get(position).name)){

            viewHolder.tv_myname.setText(data.get(position).name);
        }
        if( !TextUtils.isEmpty(data.get(position).distance)){

            viewHolder.tv_distancess.setText(data.get(position).distance);
        }

        return convertView;
    }

    private class ViewHolder{
        ImageView img_logo;
        TextView tv_distancess;
        TextView tv_myname;


        public ViewHolder(View itemView) {

             img_logo=(ImageView) itemView.findViewById(R.id.img_logo);
             tv_distancess=(TextView) itemView.findViewById(R.id.tv_distancess);
             tv_myname=(TextView) itemView.findViewById(R.id.tv_myname);

        }
    }
}
