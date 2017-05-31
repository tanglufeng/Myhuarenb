package com.abcs.huaqiaobang.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.abcs.haiwaigou.model.QuHao;
import com.abcs.hqbtravel.adapter.CityGridViewAdapter;
import com.abcs.sociax.android.R;

import java.util.ArrayList;
import java.util.List;


public class SelectQuHao2Adapter extends BaseAdapter{
    private Activity mcontext;
    private LayoutInflater inflater;
    private List<QuHao.BodyBean.CountrysBean> cityList=new ArrayList<>();

    public SelectQuHao2Adapter(Activity mcontext) {
        this.mcontext = mcontext;
        inflater = LayoutInflater.from(mcontext);
    }

    public void  addDatas(List<QuHao.BodyBean.CountrysBean> dat){
        if(dat!=null&&dat.size()>0){
            cityList.addAll(dat);
            notifyDataSetChanged();
        }
    }

    public void removeAllDAtas(List<QuHao.BodyBean.CountrysBean> dat){
        if(dat!=null&&dat.size()>0){
            for (int i=0;i<dat.size();i++){
                cityList.remove(i);
            }
            notifyDataSetChanged();
        }
    }
    @Override
    public int getCount() {
        return cityList.size();
    }

    @Override
    public QuHao.BodyBean.CountrysBean getItem(int position) {
        return cityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    CityGridViewAdapter mGridViewAdapter;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder=null;
        QuHao.BodyBean.CountrysBean bodyBean= getItem(position);
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_select_quhao,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }

        if(bodyBean!=null){
            if(!TextUtils.isEmpty(bodyBean.country)){
                holder.country_tv.setText(bodyBean.country+"");
            }
            if(!TextUtils.isEmpty(bodyBean.mobilePrefix)){
                holder.quhao_tv.setText(bodyBean.mobilePrefix+"");
            }


        }

        return convertView;
    }

    public class  ViewHolder{

        private TextView country_tv;
        private TextView quhao_tv;
        public ViewHolder(View view) {
            country_tv=(TextView) view.findViewById(R.id.country_tv);
            quhao_tv=(TextView) view.findViewById(R.id.quhao_tv);
        }

    }
}
