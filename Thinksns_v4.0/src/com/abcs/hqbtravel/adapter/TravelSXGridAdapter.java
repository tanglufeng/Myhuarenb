package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.abcs.hqbtravel.entity.ShaiXuanBean;
import com.abcs.sociax.android.R;

import java.util.List;

public class TravelSXGridAdapter extends BaseAdapter{
    private Context mcontext;
    private LayoutInflater inflater;
    private List<ShaiXuanBean.BodyBean> data;

    public TravelSXGridAdapter(Context mcontext, List<ShaiXuanBean.BodyBean> data) {
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
            convertView=inflater.inflate(R.layout.item_travel_sx,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }


        ShaiXuanBean.BodyBean bean= data.get(position);


        if(bean!=null&& !TextUtils.isEmpty(bean.name)){
            holder.tv_shanqu.setText(bean.name);
        }

        return convertView;
    }

    public class  ViewHolder{
        private TextView tv_shanqu;
        public ViewHolder(View view) {
            tv_shanqu=(TextView) view.findViewById(R.id.tv_shanqu);
        }
    }
}
