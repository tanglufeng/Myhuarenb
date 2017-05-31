package com.abcs.huaqiaobang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.abcs.hqbtravel.entity.TravelResponse;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;

import java.util.List;

/**
 * Created by zhou
 */
public class TravelYuYinAdapter extends RecyclerView.Adapter {

    private LayoutInflater mInflater;
    List<TravelResponse.BodyBean.AudiosBean> mData;
    private OnItemClickListener listener;


    public TravelYuYinAdapter(Context mContext, List<TravelResponse.BodyBean.AudiosBean> mData) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mData = mData;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        private ImageView img_logo;
//        private TextView tv_youji_cn;
//        private TextView tv_english;
//        private TextView tv_yuyin_numbers;
//        private TextView tv_yuyin_sizes;
        private RelativeLayout ret;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.item_travle_daohang22, null);
        ViewHolder viewHolder = new ViewHolder(view);


        viewHolder.img_logo=(ImageView) view.findViewById(R.id.img_logo);
//        viewHolder.tv_youji_cn=(TextView) view.findViewById(R.id.tv_youji_cn);
//        viewHolder.tv_english=(TextView) view.findViewById(R.id.tv_english);
//        viewHolder.tv_yuyin_numbers=(TextView) view.findViewById(R.id.tv_yuyin_numbers);
//        viewHolder.tv_yuyin_sizes=(TextView) view.findViewById(R.id.tv_yuyin_sizes);
        viewHolder.ret = (RelativeLayout) view.findViewById(R.id.ret);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder) {

            MyApplication.imageLoader.displayImage(mData.get(position).logo, ((ViewHolder) holder).img_logo, MyApplication.options);
//            ((ViewHolder) holder).tv_youji_cn.setText(mData.get(position).name + "");
//            ((ViewHolder) holder).tv_english.setText(mData.get(position).sightEnName + "");
//            ((ViewHolder) holder).tv_yuyin_numbers.setText("含" + mData.get(position).voiceCount + "处语音介绍");
//            ((ViewHolder) holder).tv_yuyin_sizes.setText(mData.get(position).voiceSize + "");
            
//            ImageLoader.getInstance().displayImage(mData.get(position).img, ((ViewHolder) holder).mImg, Options.getListOptions());
//            ((ViewHolder) holder).mTxtDesc.setText(mData.get(position).tdName);

            ((ViewHolder) holder).ret.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.OnItemClick(holder, position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnItemClickListener {
        public void OnItemClick(RecyclerView.ViewHolder holder, int position);
    }

    public void setOnItemClickListner(OnItemClickListener listener) {
        this.listener = listener;
    }
}
