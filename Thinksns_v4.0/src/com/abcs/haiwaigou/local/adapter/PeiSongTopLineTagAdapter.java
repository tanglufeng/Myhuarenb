package com.abcs.haiwaigou.local.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.local.beans.TagArr;
import com.abcs.sociax.android.R;

import java.util.List;

/**
 * Created by Administrator on 2017/4/13.
 */
public class PeiSongTopLineTagAdapter extends RecyclerView.Adapter<PeiSongTopLineTagAdapter.MyTagHolder> {

    public final Context mContext;
    public final List<TagArr> topTagArr;

    public PeiSongTopLineTagAdapter(Context context, List<TagArr> topTagArr) {

        this.mContext = context;
        this.topTagArr = topTagArr;

    }

    @Override
    public MyTagHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_top_line_tag, parent, false);


        return new MyTagHolder(view);
    }

    @Override
    public void onBindViewHolder(MyTagHolder holder, int position) {
        holder.setData(topTagArr.get(position));
    }

    public  void addTag(TagArr tagArr){
        if (!topTagArr.contains(tagArr)){
            topTagArr.add(tagArr);
            MyUpdateUI.sendUpdateCollection(mContext,"tags");
            notifyDataSetChanged();
        }
    }

    public void deleteTag(TagArr tagArr){
        topTagArr.remove(tagArr);
        MyUpdateUI.sendUpdateCollection(mContext,"tags");
        notifyDataSetChanged();
    }

    public void clearAddDatas(){
        if(topTagArr!=null&&topTagArr.size()>0){
            topTagArr.clear();
            notifyDataSetChanged();
        }
    }
    public int getItemCount() {
        return topTagArr.size();
    }


    class MyTagHolder extends RecyclerView.ViewHolder {
        private ImageView iv_tab_del;
        private TextView tv_tab;

        public MyTagHolder(View itemView) {
            super(itemView);
            tv_tab = (TextView) itemView.findViewById(R.id.tv_tab);
            iv_tab_del= (ImageView) itemView.findViewById(R.id.iv_tab_del);
        }

        void setData(final TagArr tagArr) {
            tv_tab.setText(tagArr.tagName);
            iv_tab_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteTag(tagArr);
                }
            });
        }
    }


}
