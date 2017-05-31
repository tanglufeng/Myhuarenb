package com.abcs.haiwaigou.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.model.Goods;
import com.abcs.sociax.android.R;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/7/2 0002.
 */
public class SearchHotHisAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final int NOMAL=0;
    public static final int ITEM=1;
    private ArrayList<Goods> goodsList;
    Activity activity;
    public Handler handler = new Handler();
    Context context;


    public ArrayList<Goods> getDates(){
        return goodsList;
    }

    public SearchHotHisAdapter(Activity activity,LinearRootOnclick linearRootOnclick) {
        this.linearRootOnclick=linearRootOnclick;
        this.activity=activity;
        this.goodsList=new ArrayList<Goods>();
    }



    @Override
    public int getItemViewType(int position) {
        int type=NOMAL;
        if(goodsList.get(position).getLayoutType()==0){
            type=NOMAL;
        }else if(goodsList.get(position).getLayoutType()==1){
            type=ITEM;
        }
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==NOMAL){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_search_nomal, parent, false);
            SearchNormalViewHolder searchNormalViewHolder = new SearchNormalViewHolder(view);
            return searchNormalViewHolder;
        }else if(viewType==ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_search_hot, parent, false);
            SearchHotViewHolder searchHotViewHolder = new SearchHotViewHolder(view);
            return searchHotViewHolder;
        }

        return null;
    }

    //    String[] strings;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Goods item = goodsList.get(position);
        if(holder instanceof SearchNormalViewHolder){
            SearchNormalViewHolder searchNormalViewHolder= (SearchNormalViewHolder) holder;
            searchNormalViewHolder.t_text.setText(item.getTitle());
        }else if(holder instanceof SearchHotViewHolder){
            SearchHotViewHolder searchHotViewHolder = (SearchHotViewHolder) holder;
            searchHotViewHolder.t_text.setText(item.getTitle());
        }


    }

    @Override
    public int getItemCount() {
        return goodsList.size();
    }

    public  class SearchNormalViewHolder extends RecyclerView.ViewHolder{

        public TextView t_text;
        public SearchNormalViewHolder(View itemView) {
            super(itemView);
            t_text= (TextView) itemView.findViewById(R.id.t_text);
        }
    }

    public  class SearchHotViewHolder extends RecyclerView.ViewHolder{

        public TextView t_text;
        public LinearLayout linear_root;
        public SearchHotViewHolder(View itemView) {
            super(itemView);
            t_text= (TextView) itemView.findViewById(R.id.t_text);
            linear_root= (LinearLayout) itemView.findViewById(R.id.linear_root);
            linear_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    linearRootOnclick.LinearRootOnClick(getAdapterPosition());
                }
            });
        }
    }
    LinearRootOnclick linearRootOnclick;
    public interface LinearRootOnclick{
        void LinearRootOnClick(int position);
    }
}
