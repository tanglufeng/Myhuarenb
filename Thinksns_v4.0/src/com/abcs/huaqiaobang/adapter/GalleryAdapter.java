package com.abcs.huaqiaobang.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.model.Goods;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.Options;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by zhou on 2016/4/14.
 */
public class GalleryAdapter extends RecyclerView.Adapter {

    private LayoutInflater mInflater;
    List<Goods> mData;
    private OnItemClickListener listener;


    public GalleryAdapter(Context mContext, List<Goods> mData) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mData = mData;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        ImageView mImg;
        TextView mTxtDesc;
        TextView mTxtPrice;
        TextView mTxtOriginalPrice;
        LinearLayout good;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.goods_item, null);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.mImg = (ImageView) view.findViewById(R.id.good_img);
        viewHolder.mTxtDesc = (TextView) view.findViewById(R.id.good_desc);
        viewHolder.mTxtOriginalPrice = (TextView) view.findViewById(R.id.original_price);
        viewHolder.mTxtPrice = (TextView) view.findViewById(R.id.real_price);
        viewHolder.good = (LinearLayout) view.findViewById(R.id.good);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder) {
            ImageLoader.getInstance().displayImage(mData.get(position).getGoods_url()
                    , ((ViewHolder) holder).mImg, Options.getListOptions());
            ((ViewHolder) holder).mTxtPrice.setText("¥" + mData.get(position).getPromote_money());
            ((ViewHolder) holder).mTxtOriginalPrice.setText("¥" + mData.get(position).getMoney());
            ((ViewHolder) holder).mTxtDesc.setText(mData.get(position).getTitle());
            ((ViewHolder) holder).mTxtOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            ((ViewHolder) holder).good.setOnClickListener(new View.OnClickListener() {
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
