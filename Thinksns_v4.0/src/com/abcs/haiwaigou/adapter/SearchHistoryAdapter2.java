package com.abcs.haiwaigou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.abcs.haiwaigou.adapter.viewholder.SearchHistoryViewHolder;
import com.abcs.sociax.android.R;

import java.util.ArrayList;

import static android.R.attr.data;

/**
 * Created by zjz on 2016/1/12.
 */
public class SearchHistoryAdapter2 extends BaseAdapter {


    ArrayList<String> historys=new ArrayList<>();
    Context context;
    public ArrayList<String> getDatas() {
        return historys;
    }

    SearchHistoryViewHolder.ItemRootOnclick itemOnClick;

    public SearchHistoryAdapter2(Context context, ArrayList<String> historys) {
        this.context=context;
        this.historys = historys;
    }


    public void clearDatas(){
        if(historys!=null&&historys.size()>0){
            historys.clear();
            notifyDataSetChanged();
        }
    }


    @Override
    public int getCount() {
        return historys.size();
    }

    @Override
    public Object getItem(int i) {
        return historys.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder=null;
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.hwg_item_search_history, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder)view.getTag();
        }

        String strings=historys.get(i);
        viewHolder.t_history.setText(strings+"");

        return view;
    }

    public  class ViewHolder{
        public TextView t_history;
        public ViewHolder(View itemView) {
            t_history = (TextView) itemView.findViewById(R.id.t_history);
        }
    }

}
