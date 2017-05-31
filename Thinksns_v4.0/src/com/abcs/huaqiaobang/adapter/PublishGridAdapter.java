package com.abcs.huaqiaobang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.haiwaigou.local.beans.Menu;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.sociax.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/***
 *
 */
public class PublishGridAdapter extends BaseAdapter {

    private ArrayList<Menu> list = new ArrayList<>();
    private Context mContext;

    public PublishGridAdapter(Context mContext, ArrayList<Menu> list) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.local_item_publish_grid, parent,false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder) convertView.getTag();
        }

        Menu mContent = (Menu) getItem(position);

        viewHolder.tv_name.setText(mContent.getMenuName());
        ImageLoader.getInstance().displayImage(mContent.getImgUrl(),viewHolder.img_icon, Options.getListOptions());

/*        if(mContent.getIs_show()==1){
            viewHolder.tv_name.setVisibility(View.GONE);
            viewHolder.img_icon.setVisibility(View.GONE);
        }else if(mContent.getIs_show()==0) {
            viewHolder.tv_name.setVisibility(View.VISIBLE);
            viewHolder.img_icon.setVisibility(View.VISIBLE);
        }*/


        return convertView;
    }

    public class ViewHolder{

        TextView tv_name;
        ImageView img_icon;

        public ViewHolder(View view) {

            tv_name=(TextView) view.findViewById(R.id.tv_name);
            img_icon=(ImageView) view.findViewById(R.id.img_icon);


        }
    }
}
