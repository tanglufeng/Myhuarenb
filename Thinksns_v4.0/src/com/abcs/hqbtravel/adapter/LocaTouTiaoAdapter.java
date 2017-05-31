package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.haiwaigou.local.beans.LNews;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import static com.abcs.sociax.android.R.id.img_icon;
import static com.abcs.sociax.android.R.id.t_content;
import static com.abcs.sociax.android.R.id.t_time;
import static com.abcs.sociax.android.R.id.t_title;

/**
 * Created by Administrator on 2016/11/23.
 */

public class LocaTouTiaoAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private Context mContext;
    private List<LNews> mCities;
    public LocaTouTiaoAdapter(Context context, List<LNews> mCities) {
        this.mContext = context;
        this.mCities = mCities;
        this.inflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return mCities.size();
    }

    @Override
    public LNews getItem(int position) {
        return mCities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CityGridViewHolder cityGridViewHolder = null;
        if (convertView==null){
            convertView = inflater.inflate(R.layout.local_item_news,parent,false);
            cityGridViewHolder = new CityGridViewHolder();
            cityGridViewHolder.img_icon = (ImageView) convertView.findViewById(img_icon);
            cityGridViewHolder.t_title = (TextView) convertView.findViewById(t_title);
            cityGridViewHolder.t_time = (TextView) convertView.findViewById(t_time);
            cityGridViewHolder.t_content = (TextView) convertView.findViewById(t_content);

            convertView.setTag(cityGridViewHolder);
        }else
        {
            cityGridViewHolder  = (CityGridViewHolder) convertView.getTag();
        }

        LNews citysBean=mCities.get(position);

        ImageLoader.getInstance().displayImage(citysBean.getImage(), cityGridViewHolder.img_icon, Options.getListOptions());
        cityGridViewHolder.t_title.setText(citysBean.getTitle());
        cityGridViewHolder.t_time.setText(Util.formatDisplayTime(citysBean.getAuditTime()));

//        view.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent(activity, NewsDetailActivity.class);
//                intent.putExtra("content", lNews.getContent());
//                intent.putParcelableArrayListExtra("datas",localModel.getNewsList());
//                activity.startActivity(intent);
//            }
//        });

        

            return convertView;
    }

    public  class CityGridViewHolder{
        ImageView img_icon ;
        TextView t_title ;
        TextView t_time ;
        TextView t_content ;
    }
}
