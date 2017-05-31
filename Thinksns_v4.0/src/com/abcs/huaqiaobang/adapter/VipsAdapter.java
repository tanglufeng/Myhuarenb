package com.abcs.huaqiaobang.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.haiwaigou.model.VipBean;
import com.abcs.sociax.android.R;

import java.util.List;

/**
 * Created by Administrator on 2016/1/19.
 */
public class VipsAdapter extends BaseAdapter {

    private List<VipBean.DatasBean.VipCardBean> list = null;
    private Context mContext;

    public VipsAdapter(Context mContext, List<VipBean.DatasBean.VipCardBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;

        if(convertView==null){

            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_vips_type, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder) convertView.getTag();
        }


        VipBean.DatasBean.VipCardBean vipCardBean = (VipBean.DatasBean.VipCardBean) getItem(position);

        if(!TextUtils.isEmpty(vipCardBean.description)){
            viewHolder.tv_instr.setText(vipCardBean.description);
        }
        if(!TextUtils.isEmpty(vipCardBean.levelName)){
            viewHolder.tv_type_kai_puto.setText(vipCardBean.levelName);
        }
        if(!TextUtils.isEmpty(vipCardBean.levelId)){
            if(vipCardBean.levelId.equals("1")){
                viewHolder.img_information.setImageResource(R.drawable.img_vip_putong);
            }else if(vipCardBean.levelId.equals("2")) {
                viewHolder.img_information.setImageResource(R.drawable.img_vip_jinka);
            }else if(vipCardBean.levelId.equals("3")) {
                viewHolder.img_information.setImageResource(R.drawable.img_vip_baijin);
            }
            else if(vipCardBean.levelId.equals("4")) {
                viewHolder.img_information.setImageResource(R.drawable.img_vip_heika);
            }
        }


        return convertView;
    }


    public class ViewHolder{
        ImageView img_information;
        TextView tv_type_kai_puto;
        TextView tv_instr;

        public ViewHolder(View view) {
             img_information = (ImageView) view.findViewById(R.id.img_information);
             tv_type_kai_puto = (TextView) view.findViewById(R.id.tv_type_kai_puto);
             tv_instr = (TextView) view.findViewById(R.id.tv_instr);
        }
    }
}
