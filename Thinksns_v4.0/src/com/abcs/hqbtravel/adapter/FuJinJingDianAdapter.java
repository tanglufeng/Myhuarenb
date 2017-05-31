package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.hqbtravel.entity.TouristAttractionsBean;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
public class FuJinJingDianAdapter extends BaseAdapter {
    private Context context;
    private List<TouristAttractionsBean> data;
    private LayoutInflater inflater;

    public FuJinJingDianAdapter(Context context, List<TouristAttractionsBean> data) {
        this.context = context;
        this.data = data;
        inflater=LayoutInflater.from(context);
    }


    public void clearDatas(){
        if(data!=null&&data.size()>0){
            data.clear();
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
    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder viewHolder=null;
//        if(convertView==null){
//            convertView=inflater.inflate(R.layout.item_fujin_jindian,parent,false);
//            viewHolder=new ViewHolder(convertView);
//            convertView.setTag(viewHolder);
//        }
//            viewHolder=(ViewHolder)convertView.getTag();
//            viewHolder.tv_name_jingdian1.setText(data.get(position).name);
//            MyApplication.imageLoader.displayImage(data.get(position).photo, viewHolder.img_jingdian1, MyApplication.getListOptions());
//        return convertView;
        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_travle_bichi,parent,false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        viewHolder=(ViewHolder)convertView.getTag();
        MyApplication.imageLoader.displayImage(data.get(position).photo, viewHolder.img_logo, MyApplication.getCircleImageOptions());
        if(!TextUtils.isEmpty(data.get(position).name)){
            viewHolder.tv_location.setText(data.get(position).name);
        }

        viewHolder.img_yuding.setVisibility(View.GONE);

        if(!TextUtils.isEmpty(data.get(position).product_id)){
            if(data.get(position).product_id.equals("-1")){  //  -1 隐藏
                viewHolder.img_yuding.setVisibility(View.GONE);
            }else {
                viewHolder.img_yuding.setVisibility(View.VISIBLE);
            }
        }

        viewHolder.tv_pin_number.setText(data.get(position).commentCount+"");

        viewHolder.tv_dis.setText(data.get(position).been+"");

        viewHolder.tv_view.setText(data.get(position).view+"");

        if(!TextUtils.isEmpty(data.get(position).cate)){
            viewHolder.tv_type_names.setText(data.get(position).cate);
        }

        viewHolder.tv_pinfen.setText(data.get(position).star+"");

        if (!TextUtils.isEmpty(data.get(position).price)){
            viewHolder.tv_price.setText("¥ "+data.get(position).price);
        }else{
            viewHolder.tv_price.setText("");
        }

        if(!TextUtils.isEmpty(data.get(position).distance)){
            if(!data.get(position).distance.equals("0m")){
                viewHolder.tv_type_distances.setVisibility(View.VISIBLE);
                viewHolder.tv_type_distances.setText(data.get(position).distance+"");
            }else {
                viewHolder.tv_type_distances.setVisibility(View.GONE);
            }

        }else {
            viewHolder.tv_type_distances.setVisibility(View.GONE);
        }

        String str1 = (data.get(position).star+"").substring(0, (data.get(position).star+"").indexOf(".")) ;

        if(!TextUtils.isEmpty(str1)){
            int s1 = Integer.parseInt(str1);
            double xiaoshu=data.get(position).star-s1;

            for(int i=0;i<s1;i++){
                if(i<5){
                    viewHolder.imgs.get(i).setImageResource(R.drawable.img_travel_startselrct);

                    if((i+1)<5){
                        if(xiaoshu<0.5){
                            viewHolder.imgs.get(i+1).setImageResource(R.drawable.img_travel_startnoselect);
                        }else {
                            viewHolder.imgs.get(i+1).setImageResource(R.drawable.img_travel_startselrct_ban);
                        }
                    }
                }
            }

        }

        return convertView;

    }

    private class ViewHolder{
//        ImageView img_jingdian1;
//        TextView tv_name_jingdian1;
//
//        public ViewHolder(View view) {
//            img_jingdian1=(ImageView)view.findViewById(R.id.img_jingdian1);
//            tv_name_jingdian1=(TextView)view.findViewById(R.id.tv_name_jingdian1);
//        }
//    }

        private ImageView img_logo;
        private ImageView img_yuding;
        private TextView tv_location;
        private TextView tv_dis,tv_view,tv_pin_number;
        private TextView tv_type_names;
        private TextView tv_type_distances;
        private TextView tv_pinfen;
        private LinearLayout linner_pinfen;
        private TextView tv_price;

        private ImageView img1;
        private ImageView img2;
        private ImageView img3;
        private ImageView img4;
        private ImageView img5;

        private List<ImageView> imgs=new ArrayList<>();


        public ViewHolder(View itemView) {
            img_yuding=(ImageView) itemView.findViewById(R.id.img_yuding);
            img_logo=(ImageView) itemView.findViewById(R.id.img_logo);
            tv_location=(TextView) itemView.findViewById(R.id.tv_location);
            tv_dis=(TextView) itemView.findViewById(R.id.tv_dis);
            tv_pin_number=(TextView) itemView.findViewById(R.id.tv_pin_number);
            tv_view=(TextView) itemView.findViewById(R.id.tv_view);
            tv_type_names=(TextView) itemView.findViewById(R.id.tv_type_names);
            tv_type_distances=(TextView) itemView.findViewById(R.id.tv_type_distances);
            tv_pinfen=(TextView) itemView.findViewById(R.id.tv_pinfen);
            linner_pinfen=(LinearLayout) itemView.findViewById(R.id.linner_pinfen);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            img1=(ImageView) itemView.findViewById(R.id.img1);
            img2=(ImageView) itemView.findViewById(R.id.img2);
            img3=(ImageView) itemView.findViewById(R.id.img3);
            img4=(ImageView) itemView.findViewById(R.id.img4);
            img5=(ImageView) itemView.findViewById(R.id.img5);

            imgs.add(img1);
            imgs.add(img2);
            imgs.add(img3);
            imgs.add(img4);
            imgs.add(img5);

        }
    }

}
