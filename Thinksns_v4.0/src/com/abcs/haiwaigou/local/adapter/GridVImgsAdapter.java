package com.abcs.haiwaigou.local.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.abcs.haiwaigou.model.GGAds;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.sociax.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class GridVImgsAdapter extends BaseAdapter{
    private Context mcontext;
    private LayoutInflater inflater;
    private List<GGAds> data;

    public GridVImgsAdapter(Context mcontext, List<GGAds> data) {
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
            convertView=inflater.inflate(R.layout.banner_item_no_scaletype,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }

        GGAds bean= data.get(position);
        if(bean!=null&& !TextUtils.isEmpty(bean.img)){
            ImageLoader.getInstance().displayImage(bean.img,holder.img, Options.getListOptions());
            holder.img.setVisibility(View.VISIBLE);
        }else {
            holder.img.setVisibility(View.GONE);
        }

        return convertView;
    }

    public class  ViewHolder{
        private  ImageView img;
        public ViewHolder(View view) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(MyApplication.getWidth()/2-20, (MyApplication.getWidth()/2-20)*288/505);
            img=(ImageView) view.findViewById(R.id.id_image);
            layoutParams.setMargins(5,5,5,5);
            img.setLayoutParams(layoutParams);
            img.setBackgroundResource(R.color.hwg_bg);
//            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }
}
