package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.android.bean.CeSu;

import java.util.List;

public class CeSuAdapter extends BaseAdapter{
    private Context mcontext;
    private LayoutInflater inflater;
    private List<CeSu> data;

    public CeSuAdapter(Context mcontext) {
        this.mcontext = mcontext;
        inflater = LayoutInflater.from(mcontext);
    }

    public void addDatas( List<CeSu> data){
        if(data!=null&&data.size()>0){
            this.data=data;
            notifyDataSetChanged();
        }
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
    public View getView(int i, View convertView, ViewGroup parent) {

        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_cesu,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }


        CeSu bean= data.get(i);


        if(!TextUtils.isEmpty(bean.getServerName())&&!TextUtils.isEmpty(bean.getCurrWanSu())){
            holder.tv_name.setText(bean.getServerName()+"   "+bean.getCurrWanSu());
        }

        if (selectedPosition == i) {
            holder.lon.setSelected(true);
            holder.lon.setPressed(true);
            holder.lon.setBackground(mcontext.getResources().getDrawable(R.drawable.bg_cesu));
            holder.tv_name.setTextColor(mcontext.getResources().getColor(R.color.white));
        } else {
            holder.lon.setSelected(false);
            holder.lon.setPressed(false);
            holder.lon.setBackground(mcontext.getResources().getDrawable(R.drawable.bg_cesu2));
            holder.tv_name.setTextColor(mcontext.getResources().getColor(R.color.gray));
        }
        return convertView;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    private int selectedPosition = -1;// 选中的位置

    public class  ViewHolder{
        private TextView tv_name;
        private LinearLayout lon;
        public ViewHolder(View view) {
            tv_name=(TextView) view.findViewById(R.id.tv_name);
            lon=(LinearLayout) view.findViewById(R.id.lon);
        }
    }
}
