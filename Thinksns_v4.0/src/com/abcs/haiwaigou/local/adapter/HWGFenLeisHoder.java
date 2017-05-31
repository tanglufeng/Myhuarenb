package com.abcs.haiwaigou.local.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.abcs.haiwaigou.activity.HotActivity3;
import com.abcs.haiwaigou.local.beans.PinLeiRight;
import com.abcs.hqbtravel.wedgt.MyGridView;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.yixia.camera.util.Log;

/**
 * Created by Administrator on 2016/9/8.
 */
public class HWGFenLeisHoder extends BaseViewHolder<PinLeiRight.DatasBean>{
    private ImageView img_logo;
    private MyGridView item_mgv;
    private LinearLayout liner;


    public HWGFenLeisHoder(ViewGroup parent) {
        super(parent, R.layout.item_hwg_fenleis_g);

        liner=(LinearLayout) itemView.findViewById(R.id.liner);
        img_logo=(ImageView) itemView.findViewById(R.id.img_logo);
        item_mgv=(MyGridView) itemView.findViewById(R.id.item_mgv);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((Util.WIDTH-188), (Util.WIDTH-188)*270/565);
        liner.setLayoutParams(layoutParams);


    }

    @Override
    public void setData(final PinLeiRight.DatasBean data) {

        if(data!=null){

            if(!TextUtils.isEmpty(data.img)){
//                http://www.huaqiaobang.com/data/upload/shop/store/goods/1/11_05375327290881075.jpg
                MyApplication.imageLoader.displayImage(data.img,img_logo,MyApplication.options);
            }


            img_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("zds","imglogo");
                    Intent intent = new Intent(getContext(), HotActivity3.class);
                    intent.putExtra("position",0);
                    intent.putExtra("class_2",data.class2);
                    intent.putExtra("title","");
                    getContext().startActivity(intent);
                }
            });

            if(data.class3!=null&&data.class3.size()>0){

                MyApplication.setTabList(data.class3);
                PinLeiGridViewAdapter mGridViewAdapter=new PinLeiGridViewAdapter(getContext(),data.class3);
                item_mgv.setAdapter(mGridViewAdapter);

                item_mgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        PinLeiRight.DatasBean.Class3Bean citysBean=(PinLeiRight.DatasBean.Class3Bean)adapterView.getItemAtPosition(i);

                        Intent intent = new Intent(getContext(), HotActivity3.class);
                        intent.putExtra("position",i+1);
                        intent.putExtra("class_2",data.class2);
                        intent.putExtra("title",citysBean.className);
                        getContext().startActivity(intent);

                    }
                });
            }
        }
    }
}
