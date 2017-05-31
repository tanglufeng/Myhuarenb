
package com.abcs.haiwaigou.adapter.viewholder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.model.HuoDong;
import com.abcs.haiwaigou.model.find.SmallEntry;
import com.abcs.hqbtravel.wedgt.SpaceItemDecoration;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 */
public class HuoDongHoder extends BaseViewHolder<HuoDong.DatasEntry.ChoicenessActivityEntry> {

    private ImageView img_logo;
    private LinearLayout liner_choicenewss;
    private RecyclerView hotrecyclerView;


    public HuoDongHoder(ViewGroup parent) {
        super(parent, R.layout.item_hwg_pic_hd);

        img_logo=(ImageView) itemView.findViewById(R.id.img_logo);
        liner_choicenewss=(LinearLayout) itemView.findViewById(R.id.liner_choicenewss);
        hotrecyclerView=(RecyclerView) itemView.findViewById(R.id.hotrecyclerView);

      /*  hotrecyclerView.addItemDecoration(itemDecoration);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        hotrecyclerView.setLayoutManager(layoutManager);*/
    }

    SpaceItemDecoration itemDecoration=new SpaceItemDecoration(20);
    @Override
    public void setData(final HuoDong.DatasEntry.ChoicenessActivityEntry data) {

        if(data!=null){

            if(data.big!=null&& !TextUtils.isEmpty(data.big.img)){
                MyApplication.imageLoader.displayImage(data.big.img, img_logo, MyApplication.getListOptions());
                img_logo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent iu=new Intent(getContext(),HdongListActivity.class);
//                        getContext().startActivity(iu);
                    }
                });
            }

            if(data.small!=null){

                if(data.small.size()>0){
                   /* HdongSmallAdapter galleryAdapter = new HdongSmallAdapter(getContext(), data.small);
                    hotrecyclerView.setAdapter(galleryAdapter);
                    galleryAdapter.setOnItemClickListner(new HdongSmallAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(RecyclerView.ViewHolder holder, int position) {
                            Intent it = new Intent(getContext(), GoodsDetailActivity.class);
                            it.putExtra("sid", data.small.get(position).id);
                            it.putExtra("pic", data.small.get(position).img);
                            it.putExtra("store_id", "");
                            it.putExtra("isYun", false);
                            getContext().startActivity(it);
                        }
                    });*/

                    /////////////////////
                    liner_choicenewss.setVisibility(View.VISIBLE);
                    liner_choicenewss.removeAllViews();

                    for(int i=0;i<data.small.size();i++){
                     final SmallEntry entity=data.small.get(i);
                        View itemView=View.inflate(getContext(), R.layout.item_hwg_hdong,null);
                        itemView.setLayoutParams(new ViewGroup.LayoutParams(MyApplication.getWidth()/3, ViewGroup.LayoutParams.WRAP_CONTENT));
                        ViewGroup parent = (ViewGroup) itemView.getParent();
                        if (parent != null) {
                            parent.removeAllViews();
                        }

                        ImageView img=(ImageView) itemView.findViewById(R.id.img);
                        TextView tv=(TextView) itemView.findViewById(R.id.tv);

                        if(entity!=null){
                            if(!TextUtils.isEmpty(entity.img)){
                                MyApplication.imageLoader.displayImage(entity.img, img, MyApplication.getListOptions());
                            }else {

                            }
                            if(!TextUtils.isEmpty(entity.title)){
                                tv.setText(entity.title);
                                tv.setVisibility(View.VISIBLE);
                            }else {
                                tv.setVisibility(View.GONE);
                            }
                        }

                        liner_choicenewss.addView(itemView);

                        liner_choicenewss.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent it = new Intent(getContext(), GoodsDetailActivity.class);
                                it.putExtra("sid", entity.id);
                                it.putExtra("pic", entity.img);
                                it.putExtra("store_id", "");
                                it.putExtra("isYun", false);
                                getContext().startActivity(it);
                            }
                        });
                    }
                }else {
                    liner_choicenewss.setVisibility(View.GONE);
                }
            }else {
                liner_choicenewss.setVisibility(View.GONE);
            }
        }
    }
}
