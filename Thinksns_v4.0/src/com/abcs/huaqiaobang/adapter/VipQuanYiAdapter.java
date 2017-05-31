package com.abcs.huaqiaobang.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.huaqiaobang.model.VIP_CENTER;
import com.abcs.sociax.android.R;

import java.util.HashMap;
import java.util.List;

import static com.abcs.sociax.android.R.id.tv_quan_yi;

/**
 * Created by Administrator on 2016/1/19.
 */
public class VipQuanYiAdapter extends BaseAdapter {

//    private List<VIP_CENTER.InstrAtegy> list = null;
    private List<HashMap<String, VIP_CENTER.IntroduceArrBean>> list;
    private int currentItem = -1; //用于记录点击的 Item 的 position，是控制 item 展开的核心

    private Context mContext;

    public VipQuanYiAdapter(Context mContext, List<HashMap<String, VIP_CENTER.IntroduceArrBean>> list) {
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

        ViewHolder holder=null;
        if(convertView==null){
            convertView=LayoutInflater.from(mContext).inflate(R.layout.item_vip_quanyi, null);
            holder=new ViewHolder(convertView);

            convertView.setTag(holder);
        }else {

            holder=(ViewHolder)convertView.getTag();
        }

//        VIP_CENTER.InstrAtegy datasBean = (VIP_CENTER.InstrAtegy) getItem(position);
        HashMap<String, VIP_CENTER.IntroduceArrBean> item = (HashMap<String, VIP_CENTER.IntroduceArrBean>)getItem(position);


        // 注意：我们在此给响应点击事件的区域 添加Tag，为了记录点击的 position，我们正好用 position 设置 Tag
        holder.relative_quanyi.setTag(position);

        if(item!=null){
            VIP_CENTER.IntroduceArrBean datasBean=item.get("key");
            if(!TextUtils.isEmpty(datasBean.name)){
                holder.tv_quanyi_name.setText(datasBean.name);
            }
            if(!TextUtils.isEmpty(datasBean.introduce)){
                holder.tv_quan_yi_content.setText(datasBean.introduce);
            }
        }

        //根据 currentItem 记录的点击位置来设置"对应Item"的可见性（在list依次加载列表数据时，每加载一个时都看一下是不是需改变可见性的那一条）
        if (currentItem == position) {
            holder.relative_hihe.setVisibility(View.VISIBLE);
        } else {
            holder.relative_hihe.setVisibility(View.GONE);
        }

        holder.relative_quanyi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //用 currentItem 记录点击位置
                int tag = (Integer) view.getTag();
                if (tag == currentItem) { //再次点击
                    currentItem = -1; //给 currentItem 一个无效值
                } else {
                    currentItem = tag;
                }
                //通知adapter数据改变需要重新加载
                notifyDataSetChanged(); //必须有的一步
            }
        });

        return convertView;
    }


    public class ViewHolder{

        TextView tv_quanyi_name;
        TextView tv_quan_yi_content;
        RelativeLayout relative_quanyi,relative_hihe;
        public ViewHolder(View view) {

            relative_quanyi=(RelativeLayout) view.findViewById(R.id.relative_quanyi);
            relative_hihe=(RelativeLayout) view.findViewById(R.id.relative_hihe);
            tv_quanyi_name=(TextView) view.findViewById(R.id.tv_quanyi_name);
            tv_quan_yi_content=(TextView) view.findViewById(tv_quan_yi);

        }
    }

}
