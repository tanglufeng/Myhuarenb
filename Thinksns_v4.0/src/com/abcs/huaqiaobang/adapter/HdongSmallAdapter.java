package com.abcs.huaqiaobang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.model.find.SmallEntry;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.sociax.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by zhou on 2016/4/14.
 */
public class HdongSmallAdapter extends RecyclerView.Adapter {

    private LayoutInflater mInflater;
    List<SmallEntry> mData;
    private OnItemClickListener listener;


    public HdongSmallAdapter(Context mContext, List<SmallEntry> mData) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mData = mData;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        ImageView img;
        TextView tv;
        RelativeLayout root;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.item_hwg_hdong, null);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.img=(ImageView) view.findViewById(R.id.img);
        viewHolder.tv=(TextView) view.findViewById(R.id.tv);
        viewHolder.root=(RelativeLayout) view.findViewById(R.id.root);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder) {

            ImageLoader.getInstance().displayImage(mData.get(position).img,((ViewHolder) holder).img, Options.getListOptions());

            if(!TextUtils.isEmpty(mData.get(position).title)){
                ((ViewHolder) holder).tv.setText(mData.get(position).title);
                ((ViewHolder) holder).tv.setVisibility(View.GONE);
            }else {
                ((ViewHolder) holder).tv.setVisibility(View.GONE);
            }


            ((ViewHolder) holder).root.setOnClickListener(new View.OnClickListener() {
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
