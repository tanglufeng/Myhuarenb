package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.abcs.hqbtravel.entity.HuiYuanTeDian;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.sociax.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class AddServerGridVAdapter  extends BaseAdapter{
    private Context mcontext;
    private LayoutInflater inflater;
    private List<HuiYuanTeDian.VipTdListBean> data;

    public AddServerGridVAdapter(Context mcontext, List<HuiYuanTeDian.VipTdListBean> data) {
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
            convertView=inflater.inflate(R.layout.item_tedian_gridview,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }


        HuiYuanTeDian.VipTdListBean bean= data.get(position);


        if(bean!=null&& !TextUtils.isEmpty(bean.img)){
            ImageLoader.getInstance().displayImage(bean.img,holder.img, Options.getListOptions());
        }

        return convertView;
    }

    public class  ViewHolder{
        private  ImageView img;
        public ViewHolder(View view) {
            img=(ImageView) view.findViewById(R.id.img_logo);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(MyApplication.getWidth(), MyApplication.getWidth()*150/350);
//            layoutParams.setMargins(0,0,0,0);
            img.setLayoutParams(layoutParams);
        }
    }
}
