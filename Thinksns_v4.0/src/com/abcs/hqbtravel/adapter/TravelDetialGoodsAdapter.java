package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.hqbtravel.entity.MyTickets;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import java.util.List;

public class TravelDetialGoodsAdapter extends BaseAdapter{
    private Context mcontext;
    private LayoutInflater inflater;
    private List<MyTickets> data;

    public TravelDetialGoodsAdapter(Context mcontext, List<MyTickets> data) {
        this.mcontext = mcontext;
        inflater = LayoutInflater.from(mcontext);
        this.data = data;
    }

    public void addDatas(List<MyTickets> data){

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
    public MyTickets getItem(int position) {
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
            convertView=inflater.inflate(R.layout.item_travle_yuding,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        if(!TextUtils.isEmpty(data.get(position).intro)){
            holder.tv_title.setText(data.get(position).intro);
        }
        if(!TextUtils.isEmpty(data.get(position).cheap_price)){
            holder.tv_price.setText(data.get(position).cheap_price);
        }
        if(!TextUtils.isEmpty(data.get(position).product_price)){
            holder.tv_cheapprice.setText(data.get(position).product_price);
            //添加删除线
            holder.tv_cheapprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        if(!TextUtils.isEmpty(data.get(position).img)){

            MyApplication.imageLoader.displayImage(TLUrl.getInstance().URL_hwg_base+"/data/upload/shop/store/goods/1/"+data.get(position).img,holder.img_logo, MyApplication.getListOptions());
        }

        return convertView;
    }

    public class  ViewHolder{
        private  ImageView img_logo;
        private TextView tv_title;
        private TextView tv_price;
        private TextView tv_cheapprice;
        public ViewHolder(View view) {
            img_logo=(ImageView) view.findViewById(R.id.img_logo);
            tv_title=(TextView) view.findViewById(R.id.tv_title);
            tv_price=(TextView) view.findViewById(R.id.tv_price);
            tv_cheapprice=(TextView) view.findViewById(R.id.tv_cheapprice);
        }
    }
}
