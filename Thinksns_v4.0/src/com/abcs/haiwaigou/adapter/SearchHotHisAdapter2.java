package com.abcs.haiwaigou.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.model.Goods;
import com.abcs.sociax.android.R;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/7/2 0002.
 */
public class SearchHotHisAdapter2 extends BaseAdapter{

    private ArrayList<Goods> goodsList=new ArrayList<Goods>();;
    public Handler handler = new Handler();
    Context context;


    public ArrayList<Goods> getDates(){
        return goodsList;
    }

    public SearchHotHisAdapter2( Context context,ArrayList<Goods> goodsList) {
        this.context=context;
        this.goodsList=goodsList;
    }

    @Override
    public int getCount() {
        return goodsList.size();
    }

    @Override
    public Object getItem(int i) {
        return goodsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        SearchHotViewHolder hotViewHolder=null;
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.hwg_item_search_hot, null);
            hotViewHolder = new SearchHotViewHolder(view);
            view.setTag(hotViewHolder);
        }else {
            hotViewHolder=(SearchHotViewHolder)view.getTag();
        }

        final Goods item = goodsList.get(i);
        hotViewHolder.t_text.setText(item.getTitle());


        return view;
    }

    public  class SearchHotViewHolder {

        public TextView t_text;
        public LinearLayout linear_root;

        public SearchHotViewHolder(View itemView) {
            t_text= (TextView) itemView.findViewById(R.id.t_text);
            linear_root= (LinearLayout) itemView.findViewById(R.id.linear_root);

        }
    }
}
